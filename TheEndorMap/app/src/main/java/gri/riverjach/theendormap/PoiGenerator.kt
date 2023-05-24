package gri.riverjach.theendormap

fun generateUserPoi(latitude: Double, longitude: Double): Poi {
    return Poi(
        title = "Frodo Baggins",
        latitude = latitude,
        longitude = longitude,
        imageId = R.drawable.frodobaggins,
        iconId = R.drawable.marker_frodo,
        detailUrl = "http://lotr.wikia.com/wiki/Frodo_Baggins",
        description = """
            Frodo Baggins, son of Drogo Baggins, was a Hobbit of the Shire during the Third Age. He was, and still is, Tolkien's most renowned character for his leading role in the Quest of the Ring, in which he bore the One Ring to Mount Doom, where it was destroyed
        """.trimIndent()
    )
}