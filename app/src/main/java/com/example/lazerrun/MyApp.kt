package com.example.lazerrun

import android.app.Application
import android.os.SystemClock

class MyApp : Application() {
    // Utilisation de var pour permettre la modification de chronoTotal si nécessaire
    var chronoTotal: Long = SystemClock.elapsedRealtime()

    var tireValide: Short = 0

    var lapCount: Short = 0
}