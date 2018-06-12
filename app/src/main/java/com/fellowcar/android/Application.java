package com.fellowcar.android;

import android.content.Context;

import com.fellowcar.android.data.network.APIGeocoding;
import com.fellowcar.android.util.AppConstants;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fellowcar project
 * Created by ANDREY. Y on 1/17/2018.
 * Email: inittey@gmail.com
 */

public class Application extends android.app.Application {
    private static APIGeocoding mGeocodingAPI;
    private static APIGeocoding mRoutingAPI;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        Application application = (Application) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
        refWatcher = LeakCanary.install(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);


        Retrofit geocodingAPI = new Retrofit.Builder()
                .baseUrl(AppConstants.GEOCODING_NOMINATIM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit routingAPI = new Retrofit.Builder()
                .baseUrl(AppConstants.ROUTING_OSRM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGeocodingAPI = geocodingAPI.create(APIGeocoding.class);
        mRoutingAPI = routingAPI.create(APIGeocoding.class);
    }

    public static APIGeocoding getGeocodingAPI() {
        return mGeocodingAPI;
    }

    public static APIGeocoding getRoutingAPI() {
        return mRoutingAPI;
    }


}
