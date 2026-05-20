package com.example.programmingquiz_bidaoui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.programmingquiz_bidaoui.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView tvAddress;
    private TextView tvCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tvAddress = findViewById(R.id.tvAddress);
        tvCoords  = findViewById(R.id.tvCoords);

        ImageView ivBack = findViewById(R.id.ivBackMap);
        ivBack.setOnClickListener(v -> finish());

        ImageView ivMyLocation = findViewById(R.id.ivMyLocation);
        ivMyLocation.setOnClickListener(v -> moveToMyLocation());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Apply dark style
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_dark));
            if (!success) {
                // Style parsing failed – fall back to default
            }
        } catch (Exception e) {
            // Raw resource not found – use default style
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // We use our own button

        if (hasLocationPermission()) {
            enableMyLocation();
        } else {
            requestLocationPermission();
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                             Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        mMap.setMyLocationEnabled(true);
        moveToMyLocation();
    }

    private void moveToMyLocation() {
        if (!hasLocationPermission()) {
            requestLocationPermission();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                showLocationOnMap(location);
            } else {
                Toast.makeText(this, "Position non disponible. Activez le GPS.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Erreur de localisation", Toast.LENGTH_SHORT).show());
    }

    private void showLocationOnMap(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng userLatLng = new LatLng(lat, lng);

        mMap.clear();

        // Purple accuracy circle
        mMap.addCircle(new CircleOptions()
                .center(userLatLng)
                .radius(location.getAccuracy())
                .strokeColor(0x557C6AF7)
                .fillColor(0x227C6AF7)
                .strokeWidth(2f));

        // Custom marker
        mMap.addMarker(new MarkerOptions()
                .position(userLatLng)
                .title("Ma position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16f));

        // Update coords text
        tvCoords.setText(String.format(Locale.getDefault(),
                "%.5f, %.5f", lat, lng));

        // Reverse geocoding
        reverseGeocode(lat, lng);
    }

    private void reverseGeocode(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(address.getAddressLine(i));
                }
                tvAddress.setText(sb.toString());
            } else {
                tvAddress.setText("Adresse introuvable");
            }
        } catch (IOException e) {
            tvAddress.setText("Impossible de récupérer l'adresse");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this,
                        "Permission de localisation refusée", Toast.LENGTH_LONG).show();
            }
        }
    }
}
