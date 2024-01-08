package com.kentreyhan.commons.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class LocationUtils {

    fun checkForFineLocationPermission(context: Context): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
            .PERMISSION_GRANTED
    }

    fun checkForCoarseLocationPermission(context: Context): Boolean{
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
            .PERMISSION_GRANTED
    }

    fun askForFineLocationPermission(activity: Activity){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MY_PERMISSIONS_REQUEST_READ_LOCATION
        )
    }

    companion object{
        private const val MY_PERMISSIONS_REQUEST_READ_LOCATION: Int = 1
    }
}