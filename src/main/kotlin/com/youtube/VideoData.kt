package com.youtube

import com.google.api.client.http.InputStreamContent
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import java.io.File
import java.io.FileInputStream

class VideoData(
    private val privacy: String = "private",
    private var snippet: VideoSnippet = VideoSnippet(),
    private val videoLocation: String
) {
    private val VIDEO_FILE_FORMAT = "video/*"

    var metadata: Video = Video()
        get() = field.setStatus(videoPrivacy).setSnippet(snippet)
    var content: InputStreamContent = InputStreamContent(VIDEO_FILE_FORMAT, FileInputStream(File(videoLocation)))
    private val videoPrivacy: VideoStatus = VideoStatus().setPrivacyStatus(privacy)

    fun setTitle(title: String) {
        snippet.setTitle(title)
    }

    fun setDescription(description: String) {
        snippet.setDescription(description)
    }

    fun setTags(tags: MutableList<String>) {
        snippet.setTags(tags)
    }


}