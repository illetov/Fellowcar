package com.fellowcar.android.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fellowcar.android.Application;
import com.fellowcar.android.R;
import com.fellowcar.android.data.realm.TripRealmObject;
import com.squareup.leakcanary.RefWatcher;

import java.util.UUID;

import io.realm.Realm;

public class HomeActivity extends AppCompatActivity {
    public final String LIST_TAG = "LIST_FRAGMT";
    public final String SEARCH_TAG = "SEARCH_FRAGMT";
    public final String ADD_TAG = "ADD_FRAGMT";
    public final String EMPTY_TAG = "EMPTY_FRAGMT";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        Toast.makeText(this, " is is: "+getTripId().toString()+" num", Toast.LENGTH_SHORT).show();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        realm = Realm.getDefaultInstance();

        if (savedInstanceState == null) {
            FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

            //if db is empty - show message
            //else add default list of trips fragment
            Fragment fragment = ListTrpFragment.newInstance(isEmptyTripList());
            mTransaction.add(R.id.fragment_container, fragment, EMPTY_TAG).commit();
        }
    }

    private void replaceFragment(Fragment newFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, newFragment, tag).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_trips:
                    replaceFragment(ListTrpFragment.newInstance(isEmptyTripList()), LIST_TAG);
                    return true;

                case R.id.navigation_search:
                    replaceFragment(SearchTripsFragment.newInstance(), SEARCH_TAG);
                    return true;

                case R.id.navigation_add_route:
                    replaceFragment(MakeTripFragment.newInstance(), ADD_TAG);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        RefWatcher refWatcher = Application.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings_item:
                return true;
            case R.id.profile_item:
                return true;
            case R.id.add_data:
                addObj();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    //this method just add temporary objects in DB
    private void addObj() {
        TripRealmObject tripRealmObject = new TripRealmObject();

        tripRealmObject.setDriverName("Andrey");
        tripRealmObject.setCarPhotoURL("https://sun1-4.userapi.com/c840621/v840621856/54d0c/tMTWc_Qg548.jpg");
        tripRealmObject.setDistanceNearby(20);
        tripRealmObject.setDistanceWay(52);
        tripRealmObject.setDriverAge(19);
        tripRealmObject.setDuration(200);
        tripRealmObject.setFrom("Новороссийск");
        tripRealmObject.setPrice(UUID.randomUUID().clockSequence());
        tripRealmObject.setTo("Краснодар");
        tripRealmObject.setStatusTrip("act");
        tripRealmObject.setDriverPhotoURL("https://sun1-4.userapi.com/c840621/v840621856/54d0c/tMTWc_Qg548.jpg");

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tripRealmObject);
        realm.commitTransaction();

        Log.d("Realm", "has been added obj");

    }


    private Object getTripId() {
        return realm.where(TripRealmObject.class).max("idsd");
    }

    private boolean isEmptyTripList() {
        return realm.where(TripRealmObject.class).findAll().isEmpty();
    }
}
