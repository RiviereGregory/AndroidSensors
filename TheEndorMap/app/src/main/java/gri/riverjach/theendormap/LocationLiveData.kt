package gri.riverjach.theendormap

import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationServices

data class LocationData(
    val location: Location? = null,
    val exception: Exception? = null
)

class LocationLiveData(context: Context) : LiveData<LocationData>() {
    private val appContext = context.applicationContext
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(appContext)

    // used to track th first LiveData subscriber
    // to send the last known location immediately
    private var firstSubscriber = true

    override fun onActive() {
        super.onActive()
        if (firstSubscriber) {
            requestLastLocation()
            firstSubscriber = false
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