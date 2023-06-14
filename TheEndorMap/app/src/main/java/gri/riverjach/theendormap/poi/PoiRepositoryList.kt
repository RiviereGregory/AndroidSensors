package gri.riverjach.theendormap.poi

class PoiRepositoryList : PoiRepository {
    override fun getUserPoi(latitude: Double, longitude: Double) =
        generateUserPoi(latitude, longitude)

    override fun getPois(latitude: Double, longitude: Double) = generatePois(latitude, longitude)
}