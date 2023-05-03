package gri.riverjach.theendormap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import timber.log.Timber

private const val REQUEST_PERMISSION_LOCATION_LAST_LOCATION = 1
private const val REQUEST_PERMISSION_LOCATION_START_UPDATE = 2
private const val REQUEST_CHECK_SETTING = 1

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Timber.d("location update $location")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // récupération de la position a aprtir du provider disponible GPS, WIFI ,...
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        updateLastLocation()
        createLocationRequest()

    }

    private fun updateLastLocation() {
        Timber.d("updateLocation")
        if (!checkLocationPermission(REQUEST_PERMISSION_LOCATION_LAST_LOCATION)) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Timber.i("Last location $location")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTING -> startLocationUpdate()
        }
    }


    private fun createLocationRequest() {
        locationRequest = LocationRequest
            .Builder(1000)
            .setMinUpdateIntervalMillis(5000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)

        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { _ ->
            Timber.i("location settings satisfied. Init location request here")
            startLocationUpdate()
        }

        task.addOnFailureListener { exception ->
            Timber.e(exception, "Failed to modify Location settings.")
            if (exception is ResolvableApiException) {
                exception.startResolutionForResult(this, REQUEST_CHECK_SETTING)
            }
        }
    }

    private fun startLocationUpdate() {
        Timber.i("StartLocationUpdate")

        if (!checkLocationPermission(REQUEST_PERMISSION_LOCATION_START_UPDATE)) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun checkLocationPermission(requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestCode
            )
            return false
        }
        return true
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            return
        }

        when (requestCode) {
            REQUEST_PERMISSION_LOCATION_LAST_LOCATION -> updateLastLocation()
            REQUEST_PERMISSION_LOCATION_START_UPDATE -> startLocationUpdate()
        }
    }
}