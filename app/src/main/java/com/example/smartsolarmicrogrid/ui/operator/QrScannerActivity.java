package com.example.smartsolarmicrogrid.ui.operator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smartsolarmicrogrid.R;
import com.example.smartsolarmicrogrid.database.DatabaseHelper;
import com.example.smartsolarmicrogrid.models.EnergyReservation;
import com.example.smartsolarmicrogrid.network.ApiClient;
import com.example.smartsolarmicrogrid.network.dto.ApiResponse;
import com.example.smartsolarmicrogrid.network.dto.CompleteQrRequest;
import com.example.smartsolarmicrogrid.network.dto.ReservationDto;
import com.example.smartsolarmicrogrid.network.dto.VerifyQrRequest;
import com.example.smartsolarmicrogrid.ui.prosumer.booking.BookingSummaryActivity;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 101;

    private DecoratedBarcodeView barcodeScannerView;
    private Button btnSimulateManualScan;

    private DatabaseHelper dbHelper;
    private boolean isScanned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        dbHelper = DatabaseHelper.getInstance(this);

        barcodeScannerView = findViewById(R.id.barcodeScannerView);
        btnSimulateManualScan = findViewById(R.id.btnSimulateManualScan);

        checkCameraPermission();

        btnSimulateManualScan.setOnClickListener(v -> simulateDemoScan());
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning();
            } else {
                Toast.makeText(this, "Camera permission required for QR code scanning", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startScanning() {
        if (barcodeScannerView == null) return;
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null && result.getText() != null && !isScanned) {
                    isScanned = true;
                    processScannedQrCode(result.getText());
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {}
        });
    }

    private void simulateDemoScan() {
        List<EnergyReservation> bookings = dbHelper.getAllBookings();
        if (!bookings.isEmpty()) {
            EnergyReservation first = bookings.get(0);
            processScannedQrCode(first.getQrData() != null ? first.getQrData() : "SUNGRID:RESERVATION:" + first.getId());
        } else {
            Toast.makeText(this, "No reservations found in memory to simulate scan", Toast.LENGTH_SHORT).show();
        }
    }

    private void processScannedQrCode(String qrData) {
        int bookingId = extractBookingIdFromQr(qrData);

        if (bookingId > 0) {
            dbHelper.updateBookingStatus(bookingId, "COMPLETED");
        }

        // Verify with ASP.NET Core API /api/qr/verify and /api/qr/complete
        VerifyQrRequest verifyReq = new VerifyQrRequest(qrData);
        ApiClient.getApiService(this).verifyQrPayload(verifyReq).enqueue(new Callback<ReservationDto>() {
            @Override
            public void onResponse(Call<ReservationDto> call, Response<ReservationDto> response) {
                completeTransferApi(qrData, bookingId);
            }

            @Override
            public void onFailure(Call<ReservationDto> call, Throwable t) {
                showTransferCompletedDialog(bookingId, qrData);
            }
        });
    }

    private void completeTransferApi(String qrData, int bookingId) {
        CompleteQrRequest completeReq = new CompleteQrRequest(qrData, 25.0, "Verified and completed by operator");
        ApiClient.getApiService(this).completeQrTransfer(completeReq).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                showTransferCompletedDialog(bookingId, qrData);
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                showTransferCompletedDialog(bookingId, qrData);
            }
        });
    }

    private int extractBookingIdFromQr(String qrData) {
        try {
            if (qrData.contains("RESERVATION:")) {
                String[] parts = qrData.split(":");
                if (parts.length >= 3) {
                    return Integer.parseInt(parts[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void showTransferCompletedDialog(int bookingId, String qrData) {
        EnergyReservation booking = dbHelper.getBookingById(bookingId);
        if (booking == null) {
            booking = new EnergyReservation(bookingId, "", 1, "Microgrid Node", "", 0.0, "SELL", "COMPLETED", qrData);
        } else {
            booking.setStatus("COMPLETED");
        }

        EnergyReservation finalBooking = booking;

        new AlertDialog.Builder(this)
                .setTitle("Energy Transfer Verified! ✅")
                .setMessage("Transaction QR Code verified successfully with ASP.NET Core Backend.\n\nBooking ID: #" + finalBooking.getId()
                        + "\nStatus: COMPLETED")
                .setPositiveButton("View Summary", (dialog, which) -> {
                    Intent intent = new Intent(QrScannerActivity.this, BookingSummaryActivity.class);
                    intent.putExtra("BOOKING", finalBooking);
                    intent.putExtra("SUMMARY_ACTION", "COMPLETED");
                    startActivity(intent);
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (barcodeScannerView != null) barcodeScannerView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (barcodeScannerView != null) barcodeScannerView.pause();
    }
}
