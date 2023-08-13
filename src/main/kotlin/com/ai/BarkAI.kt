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
//        command.add("\"$callCommand\"")
//        command.add("\"$promptFile\"")
//        command.add("\"$outputFileName\"")
//        command.add("\"$outputFileDir\"")
//        command.add("\"$historyPrompt\"")

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
//        val command_to_playwith1 = " python bark_perform.py --prompt_file F:\\NovelReadingVideoGenerator\\HarryPotter\\9.txt --output_dir F:\\bark-installer_june28_2023\\Test_Output --output_filename HarryPotter1_chapter_9 --history_prompt en_narrator_deep --hoarder_mode true --hoarder_mode true --text_temp 0.15 --waveform_temp 0.35 --stable_mode_interval 0";
//        val command_to_playwith = " F:\\bark-installer_june28_2023\\COMMAND_LINE_BARK_INFINITY_backup.bat";
//        System.out.println("Opening cmd window");
//        val command1 = "cmd /k" + " start" + command_to_playwith + command_to_playwith1;
//        val process = Runtime.getRuntime().exec(command1)
    }

    class Prompt(
        val callCommand: String = "python bark_perform.py",
        val promptFile: String,
        val outputFileName: String,
        val outputFileDir: String,
        val historyPrompt: String
    ) {

    }
}