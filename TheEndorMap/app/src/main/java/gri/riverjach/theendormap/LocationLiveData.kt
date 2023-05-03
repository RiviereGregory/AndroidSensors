package gri.riverjach.theendormap

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import timber.log.Timber

data class LocationData(
    val location: Location? = null,
    val exception: Exception? = null
)

class LocationLiveData(context: Context) : LiveData<LocationData>() {
    private val appContext = context.applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                value = LocationData(location = location)
                Timber.d("location update $location")
            }
        }
    }

    private val locationRequest = LocationRequest
        .Builder(1000)
        .setMinUpdateIntervalMillis(5000)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    // used to track th first LiveData subscriber
    // to send the last known location immediately
    private var firstSubscriber = true

    override fun onActive() {
        super.onActive()
        if (firstSubscriber) {
            requestLastLocation()
            requestLocation()
            firstSubscriber = false
        }
    }

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        firstSubscriber = true
    }

    fun startRequestLocation() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(appContext)

        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { _ ->
            Timber.i("location settings satisfied. Init location request here")
            requestLocation()
        }

        task.addOnFailureListener { exception ->
            Timber.e(exception, "Failed to modify Location settings.")
            value = LocationData(exception = exception)
        }
    }

    private fun requestLocation() {
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (exception: SecurityException) {
            value = LocationData(exception = exception)
        }
    }

    private fun requestLastLocation() {
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                value = LocationData(location = location)
            }
            fusedLocationClient.lastLocation.addOnFailureListener { exception ->
                value = LocationData(exception = exception)
            }
        } catch (exception: SecurityException) {
            value = LocationData(exception = exception)
        }
    }
}