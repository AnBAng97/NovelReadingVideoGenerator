package com.reader

import com.google.gson.Gson
import com.squareup.okhttp.*
import java.io.BufferedReader
import java.io.FileReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


class PlayHT(
    val prompt: String,
    val title: String,
    val voice: String = "Matthew"
) {
    val calendar = Calendar.getInstance()

    fun promptTTS(): String? {
        val client = OkHttpClient()

        val mediaType = MediaType.parse("application/json")
        val body =
            RequestBody.create(mediaType, "{\"content\":[\"$prompt\"],\"voice\":\"$voice\",\"title\":\"$title\"}")
        val request = Request.Builder()
            .url("https://play.ht/api/v1/convert")
            .post(body)
            .addHeader("accept", "application/json")
            .addHeader("content-type", "application/json")
            .addHeader("AUTHORIZATION", "407377c522dc4b37890b56f04a33c742")
            .addHeader("X-USER-ID", "Yt5unHL11iSJCHHk8pRnZPU1fX42")
            .build()

        val response: Response = client.newCall(request).execute()
        val responseStatus = response.code()

        if (responseStatus != HttpURLConnection.HTTP_CREATED)
            return null

        val gson = Gson()
        val ttsResponse = gson.fromJson(response.body().string(), TTSResponse::class.java)
        val transcriptionId = ttsResponse.transcriptionId

        println("${calendar.time}: $title has been uploaded")
        return transcriptionId
    }

    fun jobStatus(transcriptionId: String?): String {
        if (transcriptionId == null) {
            return ""
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://play.ht/api/v1/articleStatus?transcriptionId=$transcriptionId")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("AUTHORIZATION", "407377c522dc4b37890b56f04a33c742")
            .addHeader("X-USER-ID", "Yt5unHL11iSJCHHk8pRnZPU1fX42")
            .build()

        var response = client.newCall(request).execute()
        val responseStatus = response.code()

        if (responseStatus != HttpURLConnection.HTTP_OK)
            return ""

        val gson = Gson()
        var jobStatus = gson.fromJson(response.body().string(), JobStatus::class.java)
        var converted = jobStatus.converted

        while (converted != true) {
            response = client.newCall(request).execute()
            jobStatus = gson.fromJson(response.body().string(), JobStatus::class.java)
            converted = jobStatus.converted
            println("${calendar.time}: $title has been being converting")
            Thread.sleep(8000)
        }

        println("${calendar.time}: $title has been converted")
        return jobStatus.audioUrl
    }

}

data class TTSResponse(
    val status: String,
    val transcriptionId: String,
    val contentLength: Int,
    val wordCount: Int
)

data class JobStatus(
    var voice: String,
    var converted: Boolean,
    var audioDuration: Double,
    var audioUrl: String,
    var message: String,
    var error: Boolean,
    var errorMessage: String
)

fun main(args: Array<String>) {
    val reader =
        BufferedReader(FileReader("F:\\NovelReadingVideoGenerator\\NovelReadingVideoGenerator\\src\\main\\resources\\animalsFact.txt"))
    var line = reader.readLine()
    while (line != null) {
        val split = line.split("_")

        val title = split[0]
        val prompt = split[1]

        val promptTTS = PlayHT(prompt, title)

        val id = promptTTS.promptTTS()
        val downloadUrl = promptTTS.jobStatus(id)
        try {
            val url = URL(downloadUrl)

            val connection = url.openConnection() as HttpURLConnection
            connection.setRequestProperty("User-Agent", "Chrome/93.0.4577.82")
            val input = connection.inputStream

            val destinationPath = Paths.get("F:\\NovelReadingVideoGenerator\\AnimalFact\\$title.mp3")
            Files.copy(input, destinationPath, StandardCopyOption.REPLACE_EXISTING)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        line = reader.readLine()
    }


}