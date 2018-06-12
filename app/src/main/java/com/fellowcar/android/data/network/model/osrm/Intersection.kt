package com.fellowcar.android.data.network.model.osrm

data class Intersection(val out: Int,
                        val entry: List<Boolean>,
                        val bearings: List<Int>,
                        val location: List<Double>,
                        val ind: Int)
