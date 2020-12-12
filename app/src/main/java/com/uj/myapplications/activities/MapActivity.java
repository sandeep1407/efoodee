package com.uj.myapplications.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.uj.myapplications.R;
import com.uj.myapplications.utility.AbstractMapActivity;
import com.uj.myapplications.utility.GpsUtils;
import com.uj.myapplications.utility.PopupAdapter;
import com.uj.myapplications.utility.RestTags;
import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AbstractMapActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener, OnLocationUpdatedListener, OnActivityUpdatedListener {
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationGooglePlayServicesProvider provider;

    private double wayLatitude = 0.0, wayLongitude = 0.0;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private android.widget.Button btnLocation;
    // private TextView txtLocation;
    // private android.widget.Button btnContinueLocation;
    //  private TextView txtContinueLocation;
    private StringBuilder stringBuilder;

    private boolean isContinue = false;
    private boolean isGPS = false;
    private boolean needsInit = false;
    private GoogleMap map = null;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.btnLocation = (Button) findViewById(R.id.button2);
        isGPS = SmartLocation.with(MapActivity.this).location().state().isGpsAvailable();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds
        getLocationOnStart();
        final MapFragment mapFrag =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (savedInstanceState == null) {
            needsInit = true;
        }
        mapFrag.setRetainInstance(true);
        mapFrag.getMapAsync(this);

        //Gps Here
        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                isGPS = isGPSEnable;
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        if (!isContinue) {
                            Log.e(TAG, "onLocationResult() called with: location = [" + wayLatitude + " ," + wayLongitude);

                            // txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                        } else {
                            stringBuilder.append(wayLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(wayLongitude);
                            stringBuilder.append("\n\n");
                            // txtContinueLocation.setText(stringBuilder.toString());
                            Log.e(TAG, "onLocationResult() called with: location = [" + wayLatitude + " ," + wayLongitude);
                            if (map != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
                                map.clear();
                                // add markers from database to the map
                            }
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPS) {
                    Toast.makeText(MapActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                stringBuilder = new StringBuilder();
                isContinue = true;
                MapActivity.this.getLocation();
            }
        });

      /*  btnContinueLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGPS) {
                    Toast.makeText(MapActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
                    return;
                }
                isContinue = true;

                MapActivity.this.getLocation();
            }
        });*/
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    RestTags.LOCATION_REQUEST);

        } else {
            if (isContinue) {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(MapActivity.this, new OnSuccessListener<Location>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Log.d(TAG, "onSuccess() called with: location = [" + wayLatitude + " ," + wayLongitude);
                            //  txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                            //Init Map Here When u get Lat long

                        } else {
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }

                    }
                });
            }
        }
    }

    private static final String TAG = "MapActivity";

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isContinue) {
                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    } else {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(MapActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    wayLatitude = location.getLatitude();
                                    wayLongitude = location.getLongitude();
                                    Log.d(TAG, "onSuccess() called with: location = [" + wayLatitude + " ," + wayLongitude);
                                    // txtLocation.setText(String.format(Locale.US, "%s - %s", wayLatitude, wayLongitude));
                                    // Init Map here Again
                                } else {
                                    mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RestTags.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
            }
        }
    }

    public void getLocationOnStart() {
        if (!isGPS) {
            Toast.makeText(MapActivity.this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        isContinue = false;
        MapActivity.this.getLocation();
        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);
        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();
        smartLocation.location(provider).start(MapActivity.this);
        smartLocation.activity().start(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        String address = "";
        CameraUpdate center = null;
        Location lastLocation = SmartLocation.with(this).location().getLastLocation();
        if (lastLocation != null) {
            center = CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude(),
                    lastLocation.getLongitude()));
            address = getAddress(lastLocation.getLatitude(), lastLocation.getLongitude());

            addMarker(map, lastLocation.getLatitude(), lastLocation.getLongitude(),
                    address,
                    "Please confirm your address by long press the location icon and drag.");
        } else {
            if (wayLatitude == 0.0) {
                address = "N/A";
            } else {
                address = getAddress(wayLatitude, wayLongitude);
            }
            center =
                    CameraUpdateFactory.newLatLng(new LatLng(wayLatitude,
                            wayLongitude));
            addMarker(map, wayLatitude, wayLongitude,
                    address,
                    "");
        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        map.moveCamera(center);
        map.animateCamera(zoom);

      /*addMarker(map, 12.9615, 77.6157,
                R.string.rc, R.string.race_course);*/


        map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position = marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Drag from %f:%f",
                position.latitude,
                position.longitude));

        /*Toast.makeText(MapActivity.this, String.format("Drag from %f:%f",
                position.latitude,
                position.longitude), Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f", position.latitude,
                        position.longitude));

    /*Toast.makeText(MapActivity.this,String.format("Dragging to %f:%f",
            position.latitude,
            position.longitude),Toast.LENGTH_LONG).show();*/

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng position = marker.getPosition();

        Log.d(getClass().getSimpleName(), String.format("Dragged to %f:%f",
                position.latitude,
                position.longitude));
        //Save These Values
        EditProfileActivity.finalLat = position.latitude;
        EditProfileActivity.finalLng = position.longitude;

    }

    private void addMarker(GoogleMap map, double lat, double lon,
                           String title, String snippet) {
        map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(title).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .snippet(snippet)
                .draggable(true));

    }


    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    private void showLocation(Location location) {
        if (location != null) {
            final String text = String.format("Latitude %.6f, Longitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            address = text;
            Log.e(TAG, "showLocation: " + address);
            // We are going to get the address for the current position
            SmartLocation.with(this).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder(text);
                        builder.append("\n[Reverse Geocoding] ");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        address = builder.toString();
                        Log.e(TAG, "showLocation: " + address);
                    }
                }
            });
        } else {
            address = "Null";
            Log.e(TAG, "showLocation: " + address);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isContinue && mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
        // Remove for Location Updates
        SmartLocation.with(this).location().stop();
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();

            return address + " , " + city;
           /* Toast.makeText(MapActivity.this, "Address: " +
                    address + " " + city, Toast.LENGTH_LONG).show();*/

            //addMarker();

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }
}
