package gri.riverjach.theendormap.geofence

import android.Manifest
import android.app.IntentService
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import gri.riverjach.theendormap.App
import gri.riverjach.theendormap.R
import timber.log.Timber

private const val NOTIFICATION_ID_MORDOR = 0

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
                sendMordorNotification(geofenceTransition)
            }
        }
    }

    private fun sendMordorNotification(transitionType: Int) {
        val title: String
        val text: String
        val drawable: Drawable

        when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                title = "You entered the Mordor"
                text = "Be careful... Sauron is always watching.."
                drawable = ContextCompat.getDrawable(this, R.drawable.sauroneye)!!
            }

            else -> {
                title = "You left the Mordor"
                text = "You can breath now.. But where is the One Ring?"
                drawable = ContextCompat.getDrawable(this, R.drawable.mordorgate)!!
            }
        }
        val bitmap = (drawable as BitmapDrawable).bitmap
        val bitmapNull: Bitmap? = null

        var builder = NotificationCompat.Builder(this, App.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(text)
            .setLargeIcon(bitmap)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(bitmapNull)
            )
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID_MORDOR, builder.build())
    }
}