package com.config

import java.io.FileInputStream
import java.util.*

class ConfigLoader {

    companion object {
        lateinit var commandLineLocation: String
        lateinit var callCommand: String
        lateinit var promptFile: String
        lateinit var outputFileName: String
        lateinit var outputFileDir: String
        lateinit var historyPrompt: String
        lateinit var novelName: String
    }
    fun loadProperties (cofigLocation : String){
        val properties = Properties()

        try {
            FileInputStream(cofigLocation).use { inputStream ->
                properties.load(inputStream)
            }
            commandLineLocation = properties.getProperty("commandLineLocation");
            callCommand = properties.getProperty("callCommand");
            promptFile = properties.getProperty("promptFile");
            outputFileName = properties.getProperty("outputFileName");
            outputFileDir = properties.getProperty("outputFileDir");
            historyPrompt = properties.getProperty("historyPrompt");
            novelName = properties.getProperty("novelName");

            println("commandLineLocation: $commandLineLocation")
            println("callCommand: $callCommand")
            println("promptFile: $promptFile")
            println("outputFileName: $outputFileName")
            println("outputFileDir: $outputFileDir")
            println("historyPrompt: $historyPrompt")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}