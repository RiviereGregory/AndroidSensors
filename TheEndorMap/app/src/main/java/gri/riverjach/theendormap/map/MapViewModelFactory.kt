package gri.riverjach.theendormap.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gri.riverjach.theendormap.poi.PoiRepository

@Suppress("UNCHECKED_CAST")
class MapViewModelFactory(private val poiRepository: PoiRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MapViewModel(poiRepository) as T
    }
}