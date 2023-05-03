package gri.riverjach.theendormap

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import timber.log.Timber

private const val REQUEST_PERMISSION_LOCATION_START_UPDATE = 2
private const val REQUEST_CHECK_SETTING = 1

class MainActivity : AppCompatActivity() {

    private lateinit var locationLiveData: LocationLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationLiveData = LocationLiveData(this)
        locationLiveData.observe(this, Observer { handleLocationData(it!!) })

        //createLocationRequest()

    }

    private fun handleLocationData(locationData: LocationData) {
        if (handleLocationException(locationData.exception)) {
            return
        }
        Timber.i("Last location from LIVEDATA $locationData.location")
    }

    private fun handleLocationException(exception: Exception?): Boolean {
        exception ?: return false
        Timber.e(exception, "handleLocationException")
        when (exception) {
            is SecurityException -> checkLocationPermission(
                REQUEST_PERMISSION_LOCATION_START_UPDATE
            )

            is ResolvableApiException -> exception.startResolutionForResult(
                this,
                REQUEST_CHECK_SETTING
            )
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTING -> locationLiveData.startRequestLocation()
        }
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
            REQUEST_PERMISSION_LOCATION_START_UPDATE -> locationLiveData.startRequestLocation()
        }
    }
}