package gri.riverjach.theendormap.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import gri.riverjach.theendormap.poi.Poi
import gri.riverjach.theendormap.poi.PoiRepository
import timber.log.Timber

sealed class MapUiState {
    object Loading : MapUiState()
    data class Error(val erroMessage: String) : MapUiState()
    data class PoiReady(
        val userPoi: Poi? = null,
        val pois: List<Poi>? = null
    ) : MapUiState()
}

class MapViewModel(private val poiRepository: PoiRepository) : ViewModel() {
    private val uiState = MutableLiveData<MapUiState>()
    fun getUiState(): LiveData<MapUiState> = uiState

    fun loadPois(latitude: Double, longitude: Double) {
        Timber.i("loadPois")
        if (!(latitude in -90.0..90.0 && longitude in -180.0..180.0)) {
            uiState.value = MapUiState.Error("Invalid coordinate: lat=$latitude, long=$longitude")
            return
        }

        uiState.value = MapUiState.Loading

        uiState.value = MapUiState.PoiReady(
            userPoi = poiRepository.getUserPoi(latitude, longitude),
            pois = poiRepository.getPois(latitude, longitude)
        )
    }

}