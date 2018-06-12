package com.fellowcar.android

import android.app.Application
import com.fellowcar.android.util.AppConstants
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApplicationKotlin : Application() {

    override fun onCreate() {
        super.onCreate()

        // Realm setup
        Realm.init(this)

        val realmConfig: RealmConfiguration = RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)

        //Retrofit setup
        val geoCoderAPI: Retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.GEOCODING_NOMINATIM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val routerAPI: Retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.ROUTING_OSRM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }
}