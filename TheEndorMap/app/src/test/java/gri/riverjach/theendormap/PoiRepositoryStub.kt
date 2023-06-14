package gri.riverjach.theendormap

import gri.riverjach.theendormap.poi.Poi
import gri.riverjach.theendormap.poi.PoiRepository

class PoiRepositoryStub(
    private val userPoi: Poi,
    private val pois: List<Poi>
) : PoiRepository {
    override fun getUserPoi(latitude: Double, longitude: Double) = userPoi

    override fun getPois(latitude: Double, longitude: Double) = pois
}