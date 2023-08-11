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

    val directoryPath = "F:\\NovelReadingVideoGenerator\\HarryPotter"
    val directory = File(directoryPath)

    directory.listFiles()?.forEach {

        val promptFile = it.canonicalPath
        val outputName = "HarryPotter1_chapter_" + it.name.split(".")[0]
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