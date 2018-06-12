package com.fellowcar.android.data.network.model.osrm

data class Maneuver(val bearing_after: Int,
                    val bearing_before: Int,
                    val location: List<Double>,
                    val type: String)
