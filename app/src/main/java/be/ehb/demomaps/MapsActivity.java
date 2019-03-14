package be.ehb.demomaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import be.ehb.demomaps.model.HoofdStadDAO;
import be.ehb.demomaps.model.Hoofdstad;

import static be.ehb.demomaps.model.Hoofdstad.Continent.AFRIKA;
import static be.ehb.demomaps.model.Hoofdstad.Continent.EUROPA;
import static be.ehb.demomaps.model.Hoofdstad.Continent.OCEANIE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    // Magic numbers are bad mkay
    private final int REQUEST_LOCATION = 42;
    private GoogleMap mMap;
    private final LatLng BRUSSEL = new LatLng(50.8450755, 4.32237);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPolygonClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        setupCamera();
        addMarkers();
        startLocationUpdates();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View myContentView = getLayoutInflater().inflate(R.layout.info_window, null,false);
                Hoofdstad current =(Hoofdstad)marker.getTag();
                TextView tvTitle = myContentView.findViewById(R.id.tv_info_window);
                tvTitle.setText(current.getCityName());

                ImageView iv = myContentView.findViewById(R.id.iv_info_window);
                iv.setImageResource(current.getDrawableId());

                return  myContentView;
            }
        });
        // Add a marker in Sydney and move the camera
     /*   LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }


    private void setupCamera() {
        //update 1,show map
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(BRUSSEL, 14);
        mMap.animateCamera(update);
        //update 2,show map, zoom in te see 3D buildings, change angle
      /* CameraPosition.Builder positionBuilder = new CameraPosition.Builder();
       CameraPosition position = positionBuilder.target(BRUSSEL).zoom(18).tilt(60).build();
       CameraUpdate secondUpdate = CameraUpdateFactory.newCameraPosition(position);
       mMap.animateCamera(secondUpdate);*/

    }

    private void addMarkers() {
        mMap.addMarker(new MarkerOptions()
                .position(BRUSSEL)
                .title("Brussel")
                .snippet("Hoofdstad van Belgie")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(50.8416763, 4.3212644))
                .title("Student resto")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_restaurant)));
//extra
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(50.810094, 4.934319), new LatLng(50.993622, 4.834812), new LatLng(50.986376, 5.050556))
                .strokeColor(0xff000000)
                .fillColor(0xff75315A)
                .clickable(true));
        //mMapPolyline voor een traject te maken
        //vanuit  datasource
        for (Hoofdstad stad : HoofdStadDAO.getInstance().getHoofdsteden()) {
            float hue = 0;
            switch (stad.getContinent()) {
                case EUROPA:
                    hue = BitmapDescriptorFactory.HUE_YELLOW;
                    break;
                case AFRIKA:
                    hue = BitmapDescriptorFactory.HUE_GREEN;
                    break;
                case OCEANIE:
                    hue = 200;
                    break;

            }
           Marker m= mMap.addMarker(
                    new MarkerOptions()
                            .title(stad.getCityName())
                            .icon(BitmapDescriptorFactory.defaultMarker(hue))
                            .position(stad.getCoordinate())
            );
            m.setTag(stad);

        }
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_LOCATION);

            } else {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            for (int result : grantResults)
                if (result == PackageManager.PERMISSION_GRANTED)
                    mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Toast.makeText(getApplicationContext(), "Marginaal", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent detailsIntent = new Intent(getApplicationContext(),DetailsHoofdstadActivity.class);
        detailsIntent.putExtra("stad",(Hoofdstad)marker.getTag());
        startActivity(detailsIntent);
    }
}
