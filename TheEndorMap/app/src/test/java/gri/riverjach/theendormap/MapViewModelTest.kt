package gri.riverjach.theendormap

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gri.riverjach.theendormap.map.MapUiState
import gri.riverjach.theendormap.map.MapViewModel
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class MapViewModelTest {
    // Pour tester le LiveData en synchrone
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadPoiTriggersLoading() {
        val viewModel = MapViewModel()
        val observer: TestObserver<MapUiState> = viewModel.getUiState().testObserver()
        viewModel.loadPois(0.0, 0.0)
        Assert.assertEquals(MapUiState.Loading, observer.observeValues[0])
    }

}