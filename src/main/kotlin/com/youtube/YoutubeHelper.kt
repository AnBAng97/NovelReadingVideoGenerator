package com.youtube

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.googleapis.media.MediaHttpUploader.UploadState
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener
import com.google.api.client.http.InputStreamContent
import com.google.api.services.samples.youtube.cmdline.Auth
import com.google.api.services.samples.youtube.cmdline.Auth.authorize
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.Video
import com.google.api.services.youtube.model.VideoSnippet
import com.google.api.services.youtube.model.VideoStatus
import java.io.IOException
import java.security.GeneralSecurityException
import java.util.*


class YoutubeHelper {
    private lateinit var youtube: YouTube

    private val VIDEO_FILE_FORMAT = "video/*"

    private val SAMPLE_VIDEO_FILENAME = "sample-video.mp4"

    fun upload(videoData: VideoData) {
        val scopes: List<String?> = listOf("https://www.googleapis.com/auth/youtube.upload")

        try {
            // Authorize the request.
            val credential = authorize(scopes, "uploadvideo")

            // This object is used to make YouTube Data API requests.
            youtube =
                YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName("NovelReader")
                    .build()
            println("Uploading: $SAMPLE_VIDEO_FILENAME")

            val videoObjectDefiningMetadata = videoData.metadata
            val mediaContent = videoData.content

            val videoInsert: YouTube.Videos.Insert = youtube.videos()
                .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent)

            // Set the upload type and add an event listener.
            val uploader = videoInsert.mediaHttpUploader

            uploader.setDirectUploadEnabled(false)
            val progressListener =
                MediaHttpUploaderProgressListener { uploader ->
                    when (uploader.uploadState) {
                        UploadState.INITIATION_STARTED -> println("Initiation Started")
                        UploadState.INITIATION_COMPLETE -> println("Initiation Completed")
                        UploadState.MEDIA_IN_PROGRESS -> {
                            println("Upload in progress")
                            println("Upload percentage: " + uploader.numBytesUploaded)
                        }

                        UploadState.MEDIA_COMPLETE -> println("Upload Completed!")
                        UploadState.NOT_STARTED -> println("Upload Not Started!")
                    }
                }
            uploader.setProgressListener(progressListener)

            // Call the API and upload the video.
            val returnedVideo = videoInsert.execute()

            // Print data about the newly inserted video from the API response.
            println("\n================== Returned Video ==================\n")
            println("  - Id: " + returnedVideo.id)
            println("  - Title: " + returnedVideo.snippet.title)
            println("  - Tags: " + returnedVideo.snippet.tags)
            println("  - Privacy Status: " + returnedVideo.status.privacyStatus)
            println("  - Video Count: " + returnedVideo.statistics.viewCount)
        } catch (e: GoogleJsonResponseException) {
            System.err.println(
                "GoogleJsonResponseException code: " + e.details.code + " : "
                        + e.details.message
            )
            e.printStackTrace()
        } catch (e: IOException) {
            System.err.println("IOException: " + e.message)
            e.printStackTrace()
        } catch (t: Throwable) {
            System.err.println("Throwable: " + t.message)
            t.printStackTrace()
        }
    }
}

/**
 * Call function to create API service object. Define and
 * execute API request. Print API response.
 *
 * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
 */
@Throws(GeneralSecurityException::class, IOException::class, GoogleJsonResponseException::class)
fun main(args: Array<String>) {
    val yt = YoutubeHelper()
    var videoData = VideoData(videoLocation = "C:\\Users\\Bang-PC\\Downloads\\Rec 0100.mp4")
    val tags = mutableListOf("This", "is", "Bang", "Test")
    videoData.setTags(tags)
    videoData.setTitle("Bang Tests This API Title")
    videoData.setDescription("Bang Tests This API Description")
    yt.upload(videoData)
}
