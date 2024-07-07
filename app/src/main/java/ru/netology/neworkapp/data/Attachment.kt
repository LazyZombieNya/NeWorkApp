package ru.netology.neworkapp.data

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

enum class AttachmentType{
    IMAGE, AUDIO, VIDEO
}