package com.fellowcar.android.data.network.model.main

/**
 * The data class for server API
 *
 * @since 1.0.0
 * @property [driverName] is name of a driver
 *
 * @since 1.0.0
 * @property [driverAge] is age of a driver
 *
 * @since 1.0.0
 * @property [driverPhotoURL] is URL string a picture of the driver
 *
 * @since 1.0.0
 * @property [carPhotoURL] is URL string a picture of the car
 *
 * @since 1.0.0
 * @property [from] is location where is now a driver
 *
 * @since 1.0.0
 * @property [to] is the final place where will be a driver
 *
 * @since 1.0.0
 * @property [price] is amount of a trip (R/int)
 *
 * @since 1.0.0
 * @property [duration] is the duration time of the trip (sec/int)
 *
 * @since 1.0.0
 * @property [distanceNearby] is the distance between user and driver (meter/int)
 *
 * @since 1.0.0
 * @property [distanceWay] is the distance of all trip (meter/int)
 *
 * @since 1.0.0
 * @property [statusTrip] is a status of the trip (wait/active/decline/expired)
 *
 */

 data class TripElement(val driverName: String,
                       val driverAge: Int,
                       val driverPhotoURL: String,
                       val carPhotoURL: String,
                       val from: String,
                       val to: String,
                       val price: Int,
                       val duration: Int,
                       val distanceNearby: Int,
                       val distanceWay: Int,
                       val statusTrip: String)