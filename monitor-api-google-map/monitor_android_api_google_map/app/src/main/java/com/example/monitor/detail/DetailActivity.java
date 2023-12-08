package com.example.monitor.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.example.monitor.Earthquake;
import com.example.monitor.R;
import com.example.monitor.databinding.ActivityDetailBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String TERREMOTO_KEY = "terromoto";

    private ActivityDetailBinding binding;
    private MapView mMapView;
    private GoogleMap mMap;
    Earthquake ter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        ter = extras.getParcelable(TERREMOTO_KEY);

        Log.d("SDI","time: "+ter.getTime());
        Log.d("SDI","magnitude: "+ter.getMagnitude());
        Log.d("SDI","Longitude: "+ter.getLongitude());
        Log.d("SDI","Latitude: "+ter.getLatitude());


        Date date = new Date(ter.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(date);


        binding.setTer(ter);
        binding.setMag(String.format(String.valueOf(R.string.magnitude_format),ter.getMagnitude()));
        binding.setTime(formattedDate);

        mMapView = binding.mapView;
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            LatLng loc = new LatLng(ter.getLongitude(), ter.getLatitude());
            MarkerOptions mko = new MarkerOptions()
                    .position(loc)
                    .title(ter.getPlace());

            mMap.addMarker(mko);
            // Mover la cámara al marcador
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 3));
        }
    }
}