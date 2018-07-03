package com.example.mobileapp.directionapipb5;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mobileapp.directionapipb5.directions.DirectionCallback;
import com.example.mobileapp.directionapipb5.directions.DirectionResponse;
import com.example.mobileapp.directionapipb5.directions.Step;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnPolylineClickListener,
        DirectionCallback{
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMapOptions options;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private String origin = "23.750674,90.393434";
    private String destination = "23.805456,90.363352";
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        options = new GoogleMapOptions();
        options.zoomControlsEnabled(true);
        mapFragment = SupportMapFragment.newInstance(options);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapContainer,mapFragment);
        ft.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnPolylineClickListener(this);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.750674,90.393434),13));
        mainViewModel.getDirections(origin,destination,getString(R.string.direction_api),this);

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        String instructions = (String) polyline.getTag();
        Toast.makeText(this, instructions, Toast.LENGTH_SHORT).show();
    }

    public void showInstructions(View view) {

    }

    @Override
    public void onResponse(DirectionResponse directionResponse) {
        List<Step>steps = directionResponse.getRoutes().get(2).getLegs().get(0).getSteps();
        for(int i = 0; i < steps.size(); i++){
            String instruction = String.valueOf(Html.fromHtml(steps.get(i).getHtmlInstructions()));
            double startLat = steps.get(i).getStartLocation().getLat();
            double startLng = steps.get(i).getStartLocation().getLng();

            double endLat = steps.get(i).getEndLocation().getLat();
            double endLng = steps.get(i).getEndLocation().getLng();

            LatLng start = new LatLng(startLat,startLng);
            LatLng end = new LatLng(endLat,endLng);

            Log.e(TAG, "step "+i+" : "+instruction);

            Polyline polyline = map.addPolyline(new PolylineOptions()
                    .add(start)
                    .add(end)
                    .color(Color.BLUE));
            polyline.setTag(instruction);
        }
    }
}
