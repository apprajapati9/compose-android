package ca.apprajapati.composer

class Photo(
    val id: Int,
    val url: String,
    val highResUrl: String,
    val contentDescription: String
)

val urls = listOf(
    "https://plus.unsplash.com/premium_photo-1673643405538-de0f82933fcb",
    "https://plus.unsplash.com/premium_photo-1667311649552-2cfab63bdcfc",
    "https://images.unsplash.com/photo-1500622944204-b135684e99fd",
    "https://plus.unsplash.com/premium_photo-1675433344518-21eb72dfc7a5",
    "https://images.unsplash.com/photo-1441239372925-ac0b51c4c250",
    "https://images.unsplash.com/photo-1500828131278-8de6878641b8",
    "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1",
    "https://images.unsplash.com/photo-1611771341253-dadb347165a8",
    "https://plus.unsplash.com/premium_photo-1673240367277-e1d394465b56",
    "https://images.unsplash.com/photo-1512036849132-48508f294900")

fun getPhotos() : List<Photo> {
    val list = mutableListOf<Photo>()
    for(i in 0..9){
        val photo = Photo(id = i, url = urls[i], highResUrl = urls[i], contentDescription = "")
        list.add(photo)
    }
    return list
}
