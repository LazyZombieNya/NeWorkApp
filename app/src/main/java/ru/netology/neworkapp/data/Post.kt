package ru.netology.neworkapp.data

data class Post(
    val id: Int = 0,
    val authorId: Int = 0,
    val author: String,
    val authorJob: String? = null,
    val authorAvatar: String? = null,
    val content: String,
    val published: String,
    val coords: Coordinates? = null,
    val link: String? = "string",
    val mentionIds: List<Int> = emptyList(),
    val mentionedMe: Boolean = false,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val attachment: Attachment? = null,
    val users: Map<String, User> = emptyMap()
)

data class Coordinates(
    val lat: Double,
    val long: Double
)

data class Attachment(
    val url: String,
    val type: String
)

