package gri.riverjach.theendormap

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gri.riverjach.theendormap.map.MapUiState
import gri.riverjach.theendormap.map.MapViewModel
import gri.riverjach.theendormap.poi.GONDOR
import gri.riverjach.theendormap.poi.GREY_HAVENS
import gri.riverjach.theendormap.poi.HOBBITON
import gri.riverjach.theendormap.poi.ISENGARD
import gri.riverjach.theendormap.poi.LORIEN
import gri.riverjach.theendormap.poi.MORIA_GATES
import gri.riverjach.theendormap.poi.MOUNT_DOOM
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

    @Test
    fun loadPoiTriggersLoading() {
        val viewModel = MapViewModel()
        val observer: TestObserver<MapUiState> = viewModel.getUiState().testObserver()
        viewModel.loadPois(0.0, 0.0)

        assertEquals(
            listOf(
                MapUiState.Loading,
                MapUiState.PoiReady(
                    userPoi = Poi(
                        title = "Frodo Baggins",
                        latitude = 0.0,
                        longitude = 0.0,
                        imageId = R.drawable.frodobaggins,
                        iconId = R.drawable.marker_frodo,
                        detailUrl = "http://lotr.wikia.com/wiki/Frodo_Baggins",
                        description = """
            Frodo Baggins, son of Drogo Baggins, was a Hobbit of the Shire during the Third Age. He was, and still is, Tolkien's most renowned character for his leading role in the Quest of the Ring, in which he bore the One Ring to Mount Doom, where it was destroyed
        """.trimIndent()
                    ),
                    pois = listOf(
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
                        ),
                        Poi(
                            title = ISENGARD,
                            latitude = 0.135135135135135,
                            longitude = -0.5225225225225221,
                            imageId = R.drawable.isengard,
                            iconColor = Color.RED,
                            detailUrl = "http://lotr.wikia.com/wiki/Isengard",
                            description = """
                Isengard, also known as Angrenost ('Iron Fortress') in Sindarin, was one of the three Fortresses of Gondor, and held within it one of the realm's Palantiri.
            """.trimIndent()
                        ),
                        Poi(
                            title = MORIA_GATES,
                            latitude = 0.40540540540540504,
                            longitude = -0.459459459459459,
                            imageId = R.drawable.moriagates,
                            iconColor = Color.YELLOW,
                            detailUrl = "http://lotr.wikia.com/wiki/Khazad-d%C3%BBm",
                            description = """
                Khazad-dûm, also commonly known as Moria or the Dwarrowdelf, was an underground kingdom beneath the Misty Mountains.
            """.trimIndent()
                        ),
                        Poi(
                            title = LORIEN,
                            latitude = 0.36036036036036,
                            longitude = -0.324324324324324,
                            imageId = R.drawable.lorien,
                            iconColor = Color.GREEN,
                            detailUrl = "http://lotr.wikia.com/wiki/Lothl%C3%B3rien",
                            description = """
                Lothlórien was both a forest and elven realm located next to the lower Misty Mountains. It was first settled by Nandorin elves, but later enriched by Ñoldor and Sindar, under Celeborn of Doriath and Galadriel, daughter of Finarfin
            """.trimIndent()
                        ),
                        Poi(
                            title = GONDOR,
                            latitude = -0.162162162162162,
                            longitude = -0.045045045045045,
                            imageId = R.drawable.gondor,
                            iconColor = Color.BLUE,
                            detailUrl = "http://lotr.wikia.com/wiki/Gondor",
                            description = """
                Gondor was the prominent kingdom of Men in Middle-earth, bordered by Rohan to the north, Harad to the south, the cape of Andrast and the Sea to the west, and Mordor to the east.
            """.trimIndent()
                        ),
                        Poi(
                            title = MOUNT_DOOM,
                            latitude = -0.09009009009009,
                            longitude = 0.135135135135135,
                            imageId = R.drawable.mountdoom,
                            iconColor = Color.RED,
                            detailUrl = "http://lotr.wikia.com/wiki/Mount_Doom",
                            description = """
                Mount Doom, also known as Orodruin and Amon Amarth, was a volcano in Mordor where the One Ring was forged and finally destroyed. It was the ultimate destination for Frodo's Quest of the Ring.
            """.trimIndent()
                        )
                    )
                )
            ), observer.observeValues
        )
    }

    @Test
    fun loadPoisWithInvalidCoordinatesError() {
        val viewModel = MapViewModel()
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