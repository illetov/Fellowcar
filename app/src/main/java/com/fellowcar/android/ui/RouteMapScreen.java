package com.fellowcar.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.fellowcar.android.Application;
import com.fellowcar.android.R;
import com.fellowcar.android.data.AsyncDrawerRoad;
import com.fellowcar.android.data.adapters.GeoAutoCompleteAdapter;
import com.fellowcar.android.data.network.model.osrm.GeocodingResponseItem;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.fellowcar.android.R.color.colorRoad;

public class RouteMapScreen extends AppCompatActivity implements MapEventsReceiver {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng latLng;

    private RecyclerView mRecyclerView;
    private GeoAutoCompleteAdapter mGeoItemAdapter;
    private Response response;
    private Button nextBtn;
    private MapView map;
    private AutoCompleteTextView mTextView;
    private ArrayAdapter mArrayAdapter;


    private RoadManager mRoadManager;
    private ArrayList<GeoPoint> listWithPointsResponse;
    private ArrayList<GeoPoint> waypoints;

    private Thread updateThread = null;
    List<GeoPoint> xd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            Permission.INSTANCE.requestPermission(this, MULTIPLE_REQUEST_PERMISSION);

            map = findViewById(R.id.map);
        final MapBoxTileSource tileSource = new MapBoxTileSource();
        //option 1, load your settings from the manifest
        tileSource.retrieveAccessToken(this);
        tileSource.retrieveMapBoxMapId(this);
//option 2, provide them programmatically
        tileSource.setAccessToken("pk.eyJ1IjoianN4Y2FwIiwiYSI6ImNqMHQ2ODhiMTAwM2IzM3A3ZmZ0aWhwMW8ifQ.Xy1WvjUoHKsnHlClYT7k2g");
        tileSource.setMapboxMapid("mapbox.light");
        map.setTileSource(tileSource);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(44.550535, 38.073661);
        GeoPoint finalPoint = new GeoPoint(44.687467, 37.790970);
        GeoPoint depPoint = new GeoPoint(54.610464, 56.080166);
        final IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(depPoint);

        Marker startMarker = new Marker(map);
        Marker finalMarker = new Marker(map);
        Marker deplMarker = new Marker(map);

        startMarker.setPosition(startPoint);
        finalMarker.setPosition(finalPoint);
        deplMarker.setPosition(depPoint);
        map.getOverlays().add(startMarker);
        map.getOverlays().add(deplMarker);
        map.getOverlays().add(finalMarker);
        map.invalidate();

        mRoadManager = new OSRMRoadManager(this);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

        map.getOverlays().add(0, mapEventsOverlay);


        waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        waypoints.add(depPoint);
        waypoints.add(finalPoint);

        AsyncTask<ArrayList<GeoPoint>, Void, Polyline> execute =
                new AsyncDrawerRoad(this, map, getResources()
                        .getColor(colorRoad), 5.0f)
                        .execute(waypoints);
        Log.d("DRAW LAOD OBJ", execute.getStatus().toString());
        try {
            xd = execute.get().getPoints();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        map.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                drawRoute(RouteMapScreen.this, map, getResources().getColor(colorRoad));
                return false;

            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                drawRoute(RouteMapScreen.this, map, getResources().getColor(colorRoad));
                return false;
            }
        });





        /*API REQUEST FOR OSRM*/
//        Application.getRoutingAPI().getRoutingBetweenPoints(
//                44.550535,
//                38.073661,
//                44.687467,
//                37.790970,
//                "geojson",
//                false,
//                false,
//                false)
//                .enqueue(new Callback<RouterContainer>() {
//                    @Override
//                    public void onResponse(@NonNull Call<RouterContainer> call, @NonNull Response<RouterContainer> response) {
//                        if (response.body() != null) {
//                            RouterContainer body = response.body();
//
////                            for (int i = 0; i < body.getRoutes().size(); i++) {
////                                List<List<Double>> coordinatesBox = body.getRoutes().get(i).getGeometry().getCoordinates();
////                                for (int j = 0; j < coordinatesBox.size(); j++) {
////                                    List<Double> coordinatesItem = coordinatesBox.get(j);
////                                    for (int k = 0; k < coordinatesItem.size(); k++) {
////                                        pts.add(new GeoPoint(coordinatesItem.get(0), coordinatesItem.get(1)));
////                                    }
////
////                                }
////                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<RouterContainer> call, Throwable t) {
//                        Log.d("routeX", "GAVNO?! OPYAT>>>");
//                    }
//                });
        /*END REQUEST FOR API OSRM*/


        /*  fragment code of autotextcomplete\search adress
        *
        * this fragment id work
        *
        mTextView = findViewById(R.id.autoField);
        mGeoItemAdapter = new GeoAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line);
        mTextView.setAdapter(mGeoItemAdapter);
        *
         */

        //mRecyclerView = findViewById(R.id.routeRecyclerView);
//        nextBtn = findViewById(R.id.searchLocation);
//
//        mGeoItemAdapter = new GeoItemAdapter(routesList);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mGeoItemAdapter);


//        pepareData();

    }

    public void drawRoute(final Context context, final MapView osmMap, final int color) {
        if (updateThread == null || !updateThread.isAlive()) {
            updateRoute(context, osmMap, color);
        }
    }

    Polyline pathOverlay = null;

    private void updateRoute(final Context context, final MapView osmMap, final int color) {
        updateThread = new Thread(new Runnable() {
            public void run() {
                final ArrayList<GeoPoint> zoomPoints = new ArrayList<GeoPoint>(xd);

                //Remove any points that are offscreen
//                removeHiddenPoints(osmMap, zoomPoints);

                //If there's still too many then thin the array
                if (zoomPoints.size() > 5000) {
                    int stepSize = zoomPoints.size() / 5000;
                    int count = 1;
                    for (Iterator<GeoPoint> iterator = zoomPoints.iterator(); iterator.hasNext(); ) {
                        iterator.next();

                        if (count != stepSize) {
                            iterator.remove();
                        } else {
                            count = 0;
                        }

                        count++;
                    }
                }

                //Update the map on the event thread
                osmMap.post(new Runnable() {
                    public void run() {
                        //ideally the Polyline construction would happen in the thread but that causes glitches while the event thread
                        //waits for redraw:
                        osmMap.getOverlays().remove(pathOverlay);
                        pathOverlay = new Polyline();
                        pathOverlay.setPoints(zoomPoints);
                        pathOverlay.setColor(color);
                        osmMap.getOverlays().add(pathOverlay);
                        osmMap.invalidate();

                    }
                });
            }
        });
        updateThread.start();
    }

    private void removeHiddenPoints(MapView osmMap, ArrayList<GeoPoint> zoomPoints) {
        BoundingBox bounds = osmMap.getBoundingBox();

        for (Iterator<GeoPoint> iterator = zoomPoints.iterator(); iterator.hasNext(); ) {
            GeoPoint point = iterator.next();

            boolean inLongitude = point.getLatitude() < bounds.getLatNorth() && point.getLatitude() > bounds.getLatSouth();
            boolean inLatitude = point.getLongitude() > bounds.getLonWest() && point.getLongitude() < bounds.getLonEast();
            if (!inLongitude || !inLatitude) {
                iterator.remove();
            }
        }
    }


    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        return false;
    }


    @Override
    public boolean longPressHelper(GeoPoint p) {
        Polygon circle = new Polygon();
        circle.setPoints(Polygon.pointsAsCircle(p, 2000.0));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(Color.RED);
        circle.setStrokeWidth(2);

        circle.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        circle.setTitle("Centered on " + p.getLatitude() + "," + p.getLongitude());

        map.getOverlays().add(circle);
        map.invalidate();
        return false;
    }

    public List<GeocodingResponseItem> makeRequestGeocodingAPI(String query, String osm_type, Integer polygon) {

        final List<GeocodingResponseItem> list = new ArrayList<>();

        Application.getGeocodingAPI().getGeocoder(query, 5, "json", polygon, "ru", 1, "ru").enqueue(new Callback<List<GeocodingResponseItem>>() {
            @Override
            public void onResponse(Call<List<GeocodingResponseItem>> call, Response<List<GeocodingResponseItem>> response) {
                Log.d("InetResp", response.body().toString());

                if (response.body() != null && response.body().size() > 0) {
                    Log.d("InetRespX", "has been added item into array");
                    list.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<GeocodingResponseItem>> call, Throwable t) {
                Log.d("InetResp", "error");
            }
        });

        return list;
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED) {
//
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//
//        }
//    }

    public void onResume() {
        super.onResume();
//        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }


//    public void pepareData() {
//        GeoRouteItem item = new GeoRouteItem(23.222344, 45.433322, "", 3, 1);
//        GeoRouteItem item2 = new GeoRouteItem(23.222344, 45.433322, "", 3, 2);
//        GeoRouteItem item3 = new GeoRouteItem(23.222344, 45.433322, "", 3, 0);
//        routesList.add(item);
//        routesList.add(item2);
//        routesList.add(item3);
//        mGeoItemAdapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
//
//        //Place current location marker
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        Log.d("RouteMapScreen", latLng.toString());
//
//
//        Application.getGeocodingAPI().getGeocoder("json", latLng.latitude, latLng.longitude).enqueue(new Callback<GeoRouteItem>() {
//
//            @Override
//            public void onResponse(Call<GeoRouteItem> call, Response<GeoRouteItem> response) {
//                Log.d("RetrofitApp", response.body().toString());
//            }
//
//            @Override
//            public void onFailure(Call<GeoRouteItem> call, Throwable t) {
//                Toast.makeText(RouteMapScreen.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
//            }
//        });
//        //move map camera
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//
//        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        googleMap.getUiSettings().setMapToolbarEnabled(false);
//
//        //Initialize Google Play Services
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                buildGoogleApiClient();
////                mMap.setMyLocationEnabled(true);
//            }
//        } else {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
//        }
//    }
//
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }

}
