import com.ai.BarkAI
import com.config.ConfigLoader
import java.io.File

fun main(args: Array<String>) {
    val configLoc = args[0]
    val configLoader = ConfigLoader()
    configLoader.loadProperties(configLoc)

    val barkAI: BarkAI = if (ConfigLoader.commandLineLocation.isNotBlank()) {
        BarkAI(commandLineLocation = ConfigLoader.commandLineLocation)
    } else {
        BarkAI()
    }

//    val directoryPath = "F:\\NovelReadingVideoGenerator\\HarryPotter"
    val directory = File(ConfigLoader.chapterDir)
    val files = directory.listFiles()
    files?.forEach {
        val promptFile = it.absolutePath
        val outputName = "${ConfigLoader.novelName}_chapter_" + it.name.split(".")[0]
        println(promptFile)
        val promptRequest = BarkAI.Prompt(
            callCommand = ConfigLoader.callCommand,
            promptFile = promptFile,
            outputFileName = outputName,
            outputFileDir = ConfigLoader.outputFileDir,
            historyPrompt = ConfigLoader.historyPrompt,
        )
        barkAI.startPrompting(promptRequest);
    }
}