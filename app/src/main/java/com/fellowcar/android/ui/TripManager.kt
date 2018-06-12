package com.fellowcar.android.ui

import com.fellowcar.android.data.realm.TripRealmObject

interface TripManager {
    fun removeItem(item: TripRealmObject)
    fun addNewItem(item: TripRealmObject)
    fun updateItem(item: TripRealmObject)
    fun cleanAllItem()
}