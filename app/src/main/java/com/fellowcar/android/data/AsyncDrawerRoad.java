package com.fellowcar.android.data;

import android.content.Context;
import android.os.AsyncTask;

import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

/**
 * Fellowcar project
 * Created by ANDREY. Y on 1/19/2018.
 * Email: inittey@gmail.com
 */
public class AsyncDrawerRoad extends AsyncTask<ArrayList<GeoPoint>, Void, Polyline> {

    private int mColorPolyline;
    private float mWidthPolyline;
    private RoadManager mRoadManager;
    private Context mContext;
    private MapView mapView;


    public AsyncDrawerRoad(Context context, MapView view, int resColor, float widthLine) {
        mColorPolyline = resColor;
        mWidthPolyline = widthLine;
        mContext = context;
        mapView = view;
    }

    @Override
    protected final org.osmdroid.views.overlay.Polyline doInBackground(ArrayList<GeoPoint>... arrayLists) {
        mRoadManager = new OSRMRoadManager(mContext);
        mRoadManager.addRequestOption("alternatives=true");
        mRoadManager.addRequestOption("geometries=polyline");
        Road road = mRoadManager.getRoad(arrayLists[0]);

        return RoadManager.buildRoadOverlay(road);
    }



    @Override
    protected void onPostExecute(final Polyline polyline) {

    }
}