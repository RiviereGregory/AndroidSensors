package gri.riverjach.theendormap.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import gri.riverjach.theendormap.poi.Poi
import timber.log.Timber

const val GEOFENCE_ID_MORDOR = "Mordor"

class GeoFenceManager(context: Context) {

    private val appContext = context.applicationContext

    private val geofencingClient = LocationServices.getGeofencingClient(appContext)
    private val geofenceList = mutableListOf<Geofence>()

    @SuppressLint("MissingPermission")
    fun createGeofence(poi: Poi, radiusMeter: Float, requestId: String) {
        Timber.d("Creating geofence at coordinate ${poi.latitude}, ${poi.longitude}")

        geofenceList.add(
            Geofence.Builder()
                .setRequestId(requestId)
                .setExpirationDuration(10 * 60 * 1000)
                .setCircularRegion(
                    poi.latitude,
                    poi.longitude,
                    radiusMeter
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )
        val task = geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)
        task.addOnSuccessListener {
            Timber.i("Geofence added")
        }

        task.addOnFailureListener { exception ->
            Timber.e(exception, "Cannot add geofence")
        }
    }

    fun removeAllGeofence() {
        geofencingClient.removeGeofences(geofencePendingIntent)
        geofenceList.clear()
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(appContext, GeofenceIntentService::class.java)
        PendingIntent.getService(
            appContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}