package gri.riverjach.theendormap.geofence

import android.app.IntentService
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import timber.log.Timber

class GeofenceIntentService : IntentService("EndorGeofenceIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)
        if (geofencingEvent?.hasError() != false) {
            val messageError = geofencingEvent?.errorCode ?: "null"
            Timber.e("Error in Geofence Intent $messageError")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            Timber.e("Unhandled geofencing transition: $geofenceTransition")
        }

        if (geofencingEvent.triggeringGeofences == null) {
            Timber.w("Empty triggering geofences, nothing to do")
            return
        }

        for (triggeringGeofence in geofencingEvent.triggeringGeofences!!) {
            if (triggeringGeofence.requestId == GEOFENCE_ID_MORDOR) {
                Timber.w("ENTERING MORDOR")
            }
        }
    }
}