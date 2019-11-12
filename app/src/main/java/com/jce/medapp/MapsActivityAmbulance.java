package com.jce.medapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityAmbulance extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager mLocationManager;
    LatLng currentLocation, driverLocation;
    Marker source, destination;
    TextView distance;
    Boolean flag = true;
    Bundle bundle;
    PolylineOptions opts;
    Polyline polyline;
    String patient,driver;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_ambulance);
        bundle=getIntent().getExtras();
        patient = bundle.getString("patient");
        driver = bundle.getString("driver");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        distance = findViewById(R.id.tracker_text_view);
        currentLocation= new LatLng(0,0);
        driverLocation= new LatLng(0,0);
        Log.e("Message",driver);
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
        Log.e("Message", "Terminal 0");
        // Add a marker in Sydney and move the camera
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        Log.e("Message","Terminal 1");
        updateLocation();

    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
            if (source!=null)
                source.remove();
            source = mMap.addMarker(new MarkerOptions().title("ME").position(currentLocation).icon(BitmapDescriptorFactory.fromResource(R.drawable.patient)));
            pushCurrentLocation(currentLocation,patient);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    public void updateLocation()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverLocation");
        query.whereEqualTo("userId",driver);
        try {
            List<ParseObject> value = query.find();
            ParseObject val= value.get(0);
            String did = val.getObjectId();
            query.getInBackground(did, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e==null){
                        driverLocation = new LatLng(object.getParseGeoPoint("Location").getLatitude(),object.getParseGeoPoint("Location").getLongitude());
                        Log.e("Message",driverLocation.toString());
                        if (destination!=null)
                        {
                            destination.remove();
                            destination = mMap.addMarker(new MarkerOptions().position(driverLocation).title("AMBULANCE").icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)));
                        }
                        else
                            destination = mMap.addMarker(new MarkerOptions().position(driverLocation).title("AMBULANCE").icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance)));

                        object.saveInBackground();
                    }
                }
            });
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
            getDirections(currentLocation,driverLocation);
        Double distanceInKilo = distance(currentLocation.latitude,currentLocation.longitude,driverLocation.latitude,driverLocation.longitude);
        Double distanceOneDP = (double) Math.round(distanceInKilo * 10)/10;

        distance.setText("Your Ambulance is "+distanceOneDP+" Kilometres away");
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(currentLocation);
//        builder.include(driverLocation);
//        LatLngBounds bounds = builder.build();
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//        int padding = (int) (width * 0.10);

      //  CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);

          //  LatLng center = new LatLng((currentLocation.latitude+driverLocation.latitude)/2,(currentLocation.longitude+driverLocation.longitude)/2);

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, (float) 13.5));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLocation);
        builder.include(driverLocation);
        LatLngBounds bounds = builder.build();
        CameraUpdate cu=CameraUpdateFactory.newLatLngBounds(bounds,200);
        mMap.animateCamera(cu);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLocation();


            }
        },5*1000);



    }
    public void getDirections(LatLng latLng,LatLng dest) {

        if (polyline!=null)
            polyline.remove();



        flag = false;
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBP2IwLx61VBrRlAEh9lDlYt1APg3LP7LI")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, latLng.latitude+","+latLng.longitude, dest.latitude+","+dest.longitude);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e("TAG", ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
          polyline=  mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        return;
    }








    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(locationListener);
       // handler=null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(locationListener);
      //  handler=null;
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public void pushCurrentLocation(LatLng location,String username)
    {
        final ParseGeoPoint currentLoc = new ParseGeoPoint(location.latitude,location.longitude);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLocation");
        query.whereEqualTo("Username", username);
        try {
            List<ParseObject> value = query.find();
            ParseObject val = value.get(0);
            String id = val.getObjectId();
            query.getInBackground(id, new GetCallback<ParseObject>() {
                public void done(ParseObject entity, ParseException e) {
                    if (e == null) {
                        // Update the fields we want to
                        entity.put("Location", currentLoc);

                        // All other fields will remain the same
                        entity.saveInBackground();

                    }
                }
            });
        }
        catch (ParseException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
