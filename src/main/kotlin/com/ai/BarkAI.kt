package com.ai

import java.io.BufferedReader
import java.io.InputStreamReader

class BarkAI(
    private val commandLineLocation: String = "F:\\bark-installer_june28_2023\\COMMAND_LINE_BARK_INFINITY.bat"
) {
    fun startPrompting(prompt: Prompt) {

        val batchFilePath = commandLineLocation
        val callCommand = prompt.callCommand
        val promptFile = prompt.promptFile
        val outputFileName = prompt.outputFileName
        val outputFileDir = prompt.outputFileDir
        val historyPrompt = prompt.historyPrompt
        val batchFileArguments = listOf(callCommand, promptFile, outputFileName, outputFileDir, historyPrompt)

        val command = mutableListOf<String>()
        command.add("\"$batchFilePath\"")
        command.addAll(batchFileArguments)

        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()

        val reader = BufferedReader(InputStreamReader(process.inputStream))

        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }

        // Wait for the process to complete
        val exitCode = process.waitFor()
        println("Process exited with code: $exitCode")
    }

    class Prompt(
        val callCommand: String = "python bark_perform.py",
        val promptFile: String,
        val outputFileName: String,
        val outputFileDir: String,
        val historyPrompt: String
    ){

    }
}