package gri.riverjach.theendormap.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.android.gms.common.api.ResolvableApiException
import gri.riverjach.theendormap.R
import gri.riverjach.theendormap.location.LocationData
import gri.riverjach.theendormap.location.LocationLiveData
import gri.riverjach.theendormap.poi.Poi
import gri.riverjach.theendormap.poi.generateUserPoi
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.TilesOverlay
import timber.log.Timber


private const val REQUEST_PERMISSION_LOCATION_START_UPDATE = 2
private const val REQUEST_CHECK_SETTING = 1

class MapActivity : AppCompatActivity() {

    private lateinit var viewModel: MapViewModel
    private lateinit var locationLiveData: LocationLiveData
    private lateinit var myOpenMapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var progressBar: ContentLoadingProgressBar

    private var firstLocation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationLiveData = LocationLiveData(this)
        locationLiveData.observe(this, Observer { handleLocationData(it!!) })

        viewModel = ViewModelProvider(this).get(MapViewModel::class.java)
        viewModel.getUiState().observe(this, Observer { updateUiState(it!!) })
        progressBar = findViewById(R.id.loadingProgressBar)

        //load/initialize the osmdroid configuration
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        myOpenMapView = findViewById(R.id.mapView)
        myOpenMapView.getOverlayManager().getTilesOverlay()
            .setColorFilter(TilesOverlay.INVERT_COLORS)
        myOpenMapView.setTileSource(TileSourceFactory.MAPNIK) // render
        myOpenMapView.setBuiltInZoomControls(true)
        myOpenMapView.setMultiTouchControls(true)
        myOpenMapView.setClickable(true)

        mapController = myOpenMapView.controller

    }

    private fun addPoiToMapMarker(poi: Poi): Marker {
        val tec = Marker(myOpenMapView)
        val userPoi = generateUserPoi(poi.latitude, poi.longitude)
        tec.position = GeoPoint(userPoi.latitude, userPoi.longitude)
        tec.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        tec.icon = resources.getDrawable(userPoi.iconId)
        tec.title = userPoi.title
        tec.image = resources.getDrawable(userPoi.imageId)
        return tec
    }

    private fun updateUiState(state: MapUiState) {
        Timber.i("$state")
        return when (state) {
            is MapUiState.Error -> {
                progressBar.hide()
                Toast.makeText(this, "Error: ${state.erroMessage}", Toast.LENGTH_SHORT).show()
            }

            MapUiState.Loading -> {
                progressBar.show()
            }

            is MapUiState.PoiReady -> {
                progressBar.hide()

                state.userPoi?.let {
                    myOpenMapView.overlays.add(addPoiToMapMarker(it))
                    myOpenMapView.invalidate()
                }
                state.pois?.let {

                }
                return
            }
        }
    }

    private fun handleLocationData(locationData: LocationData) {
        if (handleLocationException(locationData.exception)) {
            return
        }
        //Timber.i("Last location from LIVEDATA $locationData.location")
        locationData.location?.let {
            if (firstLocation && ::mapController.isInitialized) {
                Timber.d("location handle ${it.latitude}, ${it.longitude}")
                mapController.setCenter(GeoPoint(it.latitude, it.longitude))
                mapController.setZoom(8.0)
                firstLocation = false
                viewModel.loadPois(it.latitude, it.longitude)
            }
        }
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