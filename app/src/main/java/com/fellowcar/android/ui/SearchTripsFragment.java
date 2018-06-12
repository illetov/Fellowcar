package com.fellowcar.android.ui;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fellowcar.android.R;
import com.fellowcar.android.data.AsyncDrawerRoad;
import com.fellowcar.android.data.adapters.GeoAutoCompleteAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.MapBoxTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Response;

import static com.fellowcar.android.R.color.colorRoad;

//import com.fellowcar.android.data.RoadManager;


public class SearchTripsFragment extends Fragment implements MapEventsReceiver {
    private RoadManager mRoadManager;
    private ArrayList<GeoPoint> listWithPointsResponse;
    private ArrayList<GeoPoint> waypoints;
    EditText filed1;

    private GeoAutoCompleteAdapter mGeoItemAdapter;
    private Response response;
    private Button nextBtn;
    private MapView map;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LatLng latLng;
    private Thread updateThread = null;
    private List<GeoPoint> computedRoadWay;

    private RecyclerView recyclerListCoordinates;

    GeoPoint startPoint = new GeoPoint(44.550535, 38.073661);
    GeoPoint finalPoint = new GeoPoint(44.687467, 37.790970);
    GeoPoint depPoint = new GeoPoint(54.610464, 56.080166);

    public SearchTripsFragment() {
    }

    public static SearchTripsFragment newInstance() {
        return new SearchTripsFragment();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getContext(), "editetxt", Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_trips, container, false);
        Context mContext = getContext();

        Configuration.getInstance().setOsmdroidBasePath
                (new File(Environment.getExternalStorageDirectory(), "osmdroid"));
        Configuration.getInstance().setOsmdroidTileCache
                (new File(Environment.getExternalStorageDirectory(), "osmdroid/tiles"));

        recyclerListCoordinates = view.findViewById(R.id.coordinates_recycler);

        map = view.findViewById(R.id.searchMapPolygon);
        final MapBoxTileSource tileSource = initTile(mContext);
        setTile(tileSource);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);
        map.setMultiTouchControls(true);

        //pre init coordinates UFA/NOVOROSSYISK


        final IMapController mapController = map.getController();
        mapController.setZoom(2);
        mapController.setCenter(finalPoint);

        setUpMarkers(mContext, startPoint, finalPoint, depPoint);

        mRoadManager = new OSRMRoadManager(mContext);
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);

        map.getOverlays().add(0, mapEventsOverlay);


        waypoints = new ArrayList<>();
        waypoints.add(startPoint);
        waypoints.add(depPoint);
        waypoints.add(finalPoint);


        AsyncTask<ArrayList<GeoPoint>, Void, Polyline> execute = new AsyncDrawerRoad
                (mContext, map, getResources().getColor(colorRoad), 3.8f).execute(waypoints);

        Log.d("DrawerRoadStatus", execute.getStatus().toString());

        try {
            computedRoadWay = execute.get().getPoints();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        updateRoute(mContext, map, getResources().getColor(colorRoad));

        map.setMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
//                drawRoute(getContext(), map, getResources().getColor(colorRoad));
                return false;

            }

            @Override
            public boolean onZoom(ZoomEvent event) {
//                drawRoute(getContext(), map, getResources().getColor(colorRoad));
                return false;
            }
        });
        return view;
        //        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//
//        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpMarkers(Context mContext, GeoPoint startPoint, GeoPoint finalPoint, GeoPoint depPoint) {
        Marker startMarker = new Marker(map);
        Marker finalMarker = new Marker(map);
        Marker deplMarker = new Marker(map);

        startMarker.setPosition(startPoint);
        finalMarker.setPosition(finalPoint);
        deplMarker.setPosition(depPoint);

        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_radio_button_checked_black_24dp));
        finalMarker.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_place_red_12dp));
        deplMarker.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_radio_button_checked_black_24dp));
        //draw marker on the map
        drawMarkerOnTheMap(startMarker, finalMarker, deplMarker);
    }


    private void drawMarkerOnTheMap(final Marker startMarker, final Marker finalMarker, final Marker deplMarker) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                map.getOverlays().add(startMarker);
                map.getOverlays().add(deplMarker);
                map.getOverlays().add(finalMarker);
            }
        }).start();
    }

    private void setTile(final MapBoxTileSource tileSource) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                map.setTileSource(tileSource);
            }
        }).start();
    }

    @NonNull
    private MapBoxTileSource initTile(final Context mContext) {
        final MapBoxTileSource tileSource = new MapBoxTileSource();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //option 1, load your settings from the manifest
                tileSource.retrieveAccessToken(mContext);
                tileSource.retrieveMapBoxMapId(mContext);
                tileSource.setAccessToken("pk.eyJ1IjoianN4Y2FwIiwiYSI6ImNqMHQ2ODhiMTAwM2IzM3A3ZmZ0aWhwMW8ifQ.Xy1WvjUoHKsnHlClYT7k2g");
                tileSource.setMapboxMapid("mapbox.light");
            }
        }).start();
        return tileSource;
    }

    //    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(ModelTripsFragment.newInstance(), "Активные");
//        adapter.addFragment(ModelTripsFragment.newInstance(), "Завершеные");
//        viewPager.setAdapter(adapter);
//    }


//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFragment(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }

    public void drawRoute(final Context context, final MapView osmMap, final int color) {
        if (updateThread == null || !updateThread.isAlive()) {
            updateRoute(context, osmMap, color);
        }
    }

    Polyline pathOverlay = null;

    private void updateRoute(final Context context, final MapView osmMap, final int color) {
        updateThread = new Thread(new Runnable() {
            public void run() {


                //Remove any points that are offscreen
//                removeHiddenPoints(osmMap, zoomPoints);

                //If there's still too many then thin the array
                if (computedRoadWay.size() > 5000) {
                    int stepSize = computedRoadWay.size() / 5000;
                    int count = 1;
                    for (Iterator<GeoPoint> iterator = computedRoadWay.iterator(); iterator.hasNext(); ) {
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
                        pathOverlay.setPoints(computedRoadWay);
                        pathOverlay.setColor(color);
                        osmMap.getOverlays().add(pathOverlay);
                        setUpMarkers(getContext(), startPoint, finalPoint, depPoint);
                        osmMap.invalidate();

                    }
                });
            }
        });
        updateThread.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        map.destroyDrawingCache();

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
}
