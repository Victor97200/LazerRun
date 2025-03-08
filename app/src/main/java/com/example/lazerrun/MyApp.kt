package com.example.lazerrun

import android.app.Application

class MyApp : Application() {
    var chronoBase: Long = 0
    var tireValide: Int = 0
    var shootingTimes = mutableListOf<Long>()
    var runTimes = mutableListOf<Long>()
}