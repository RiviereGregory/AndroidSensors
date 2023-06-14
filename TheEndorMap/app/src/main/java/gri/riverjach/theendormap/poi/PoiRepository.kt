package gri.riverjach.theendormap.poi

interface PoiRepository {
    fun getUserPoi(latitude: Double, longitude: Double): Poi
    fun getPois(latitude: Double, longitude: Double): List<Poi>
}