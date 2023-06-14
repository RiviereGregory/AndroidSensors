package gri.riverjach.theendormap

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gri.riverjach.theendormap.map.MapUiState
import gri.riverjach.theendormap.map.MapViewModel
import gri.riverjach.theendormap.poi.GREY_HAVENS
import gri.riverjach.theendormap.poi.HOBBITON
import gri.riverjach.theendormap.poi.Poi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MapViewModelTest {
    // Pour tester le LiveData en synchrone
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testPoi = Poi(
        title = "Frodo Baggins",
        latitude = 0.0,
        longitude = 0.0,
        imageId = R.drawable.frodobaggins,
        iconId = R.drawable.marker_frodo,
        detailUrl = "http://lotr.wikia.com/wiki/Frodo_Baggins",
        description = """
            Frodo Baggins, son of Drogo Baggins, was a Hobbit of the Shire during the Third Age. He was, and still is, Tolkien's most renowned character for his leading role in the Quest of the Ring, in which he bore the One Ring to Mount Doom, where it was destroyed
        """.trimIndent()
    )

    private val pois = listOf(
        Poi(
            title = GREY_HAVENS,
            latitude = 0.40540540540540504,
            longitude = -1.180180180180179,
            imageId = R.drawable.greyhavens,
            iconColor = Color.BLUE,
            detailUrl = "http://lotr.wikia.com/wiki/Grey_Havens",
            description = """
               Because of its cultural and spiritual importance to the Elves, the Grey Havens in time became the primary Elven settlement west of the Misty Mountains prior to the establishment of Eregion and, later, Rivendell.
            """.trimIndent()
        ),
        Poi(
            title = HOBBITON,
            latitude = 0.40540540540540504,
            longitude = -0.972972972972972,
            imageId = R.drawable.hobbiton,
            iconColor = Color.GREEN,
            detailUrl = "http://lotr.wikia.com/wiki/Hobbiton",
            description = """
                Hobbiton was located in the center of the Shire in the far eastern part of the Westfarthing. It was the home of many illustrious Hobbits, including Bilbo Baggins, Frodo Baggins, and Samwise Gamgee.
            """.trimIndent()
        )
    )

    private val poiRepository: PoiRepositoryStub = PoiRepositoryStub(testPoi, pois)

    @Test
    fun loadPoiTriggersLoading() {
        val viewModel = MapViewModel(poiRepository)
        val observer: TestObserver<MapUiState> = viewModel.getUiState().testObserver()
        viewModel.loadPois(0.0, 0.0)

        assertEquals(
            listOf(
                MapUiState.Loading,
                MapUiState.PoiReady(
                    userPoi = testPoi,
                    pois = pois
                )
            ), observer.observeValues
        )
    }

    @Test
    fun loadPoisWithInvalidCoordinatesError() {
        val viewModel = MapViewModel(poiRepository)
        val observer: TestObserver<MapUiState> = viewModel.getUiState().testObserver()
        val latitude = -91.0
        val longitude = -181.0
        viewModel.loadPois(latitude, longitude)
        assertTrue(observer.observeValues[0] is MapUiState.Error)
        assertEquals(
            listOf(
                MapUiState.Error("Invalid coordinate: lat=$latitude, long=$longitude")
            ),
            observer.observeValues
        )
    }
}