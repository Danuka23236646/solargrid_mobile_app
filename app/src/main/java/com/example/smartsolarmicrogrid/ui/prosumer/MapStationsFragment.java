package com.example.smartsolarmicrogrid.ui.prosumer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.smartsolarmicrogrid.R;
import com.example.smartsolarmicrogrid.database.DatabaseHelper;
import com.example.smartsolarmicrogrid.models.SolarStation;
import com.example.smartsolarmicrogrid.ui.prosumer.booking.CreateBookingActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MapStationsFragment extends Fragment {

    // 100% Free OpenStreetMap Humanitarian tile source (clean, high resolution, zero watermarks, zero API keys)
    public static final OnlineTileSourceBase OSM_HOT = new XYTileSource(
            "OpenStreetMap_HOT",
            0, 19, 256, ".png",
            new String[]{
                    "https://a.tile.openstreetmap.fr/hot/",
                    "https://b.tile.openstreetmap.fr/hot/",
                    "https://c.tile.openstreetmap.fr/hot/"
            }
    );

    private MapView osmMapView;
    private DatabaseHelper dbHelper;

    private CardView cardStationDetails;
    private TextView tvMapStationName, tvMapStationAddress, tvMapStationCapacity, tvMapStationSlots;
    private Button btnMapBookNow;

    private SolarStation selectedStation = null;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));
        Configuration.getInstance().setUserAgentValue("SmartSolarMicrogridApp/1.0 (Android; contact@smartsolarmicrogrid.com)");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() != null) {
            Configuration.getInstance().setUserAgentValue("SmartSolarMicrogridApp/1.0 (Android; contact@smartsolarmicrogrid.com)");
            dbHelper = DatabaseHelper.getInstance(getContext());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_stations, container, false);

        osmMapView = view.findViewById(R.id.osmMapView);
        cardStationDetails = view.findViewById(R.id.cardStationDetails);
        tvMapStationName = view.findViewById(R.id.tvMapStationName);
        tvMapStationAddress = view.findViewById(R.id.tvMapStationAddress);
        tvMapStationCapacity = view.findViewById(R.id.tvMapStationCapacity);
        tvMapStationSlots = view.findViewById(R.id.tvMapStationSlots);
        btnMapBookNow = view.findViewById(R.id.btnMapBookNow);

        // Hide station card by default for clean full-screen map view
        cardStationDetails.setVisibility(View.GONE);

        btnMapBookNow.setOnClickListener(v -> {
            if (selectedStation != null) {
                Intent intent = new Intent(getActivity(), CreateBookingActivity.class);
                intent.putExtra("STATION_ID", selectedStation.getId());
                intent.putExtra("STATION_NAME", selectedStation.getName());
                startActivity(intent);
            }
        });

        setupOpenStreetMap();
        loadStationsData();

        return view;
    }

    private void setupOpenStreetMap() {
        if (osmMapView == null) return;

        // Use OSM_HOT tile source (clean, high resolution, no watermarks)
        osmMapView.setTileSource(OSM_HOT);
        osmMapView.setMultiTouchControls(true);

        GeoPoint colomboCenter = new GeoPoint(6.9271, 79.8612);
        osmMapView.getController().setZoom(8.5);
        osmMapView.getController().setCenter(colomboCenter);
    }

    private void loadStationsData() {
        if (dbHelper == null) return;
        List<SolarStation> stations = dbHelper.getAllStations();

        // Plot markers on OpenStreetMap
        if (osmMapView != null) {
            osmMapView.getOverlays().clear();

            for (SolarStation station : stations) {
                GeoPoint point = new GeoPoint(station.getLatitude(), station.getLongitude());
                Marker marker = new Marker(osmMapView);
                marker.setPosition(point);
                marker.setTitle(station.getName());
                marker.setSnippet(station.getAddress());
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                marker.setOnMarkerClickListener((m, mapView) -> {
                    displayStationDetails(station);
                    return true;
                });

                osmMapView.getOverlays().add(marker);
            }
            osmMapView.invalidate();
        }
    }

    private void displayStationDetails(SolarStation station) {
        if (station == null) return;
        selectedStation = station;
        tvMapStationName.setText(station.getName());
        tvMapStationAddress.setText("📍 " + station.getAddress());
        tvMapStationCapacity.setText("Capacity: " + station.getCapacityKw() + " kW");
        tvMapStationSlots.setText("Slots: " + station.getAvailableSlots() + " Available");
        cardStationDetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (osmMapView != null) osmMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (osmMapView != null) osmMapView.onPause();
    }
}
