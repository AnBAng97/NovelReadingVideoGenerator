package com.ai

import com.selenium.Selenium
import org.openqa.selenium.By
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.edge.EdgeOptions

class SpeechToText(
    val selenium: Selenium
) {
    val speechLocation = ""
    val CONVERTER_URL = "https://converter.app/mp3-to-text/"

//    private lateinit var selenium: Selenium

    fun uploadAudio(audioLocation: String) {
        val uploadLabel = selenium.findElement(
            selenium.driver,
            By.xpath("//*[@id=\"form-element\"]/div[2]/label/strong")
        )
        uploadLabel.click()
    }

}
    fun main(args: Array<String>) {

        val options = EdgeOptions()
        options.addArguments("enable-automation")
        //        options.addArguments("--headless");
        options.addArguments("--window-size=100,100")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-extensions")
        options.addArguments("--dns-prefetch-disable")
        options.addArguments("--disable-popup-blocking")
        options.addArguments("--disable-gpu")
        options.addArguments("--remote-allow-origins=*")
        options.setPageLoadStrategy(PageLoadStrategy.EAGER)
        val selenium = Selenium(options)
        val speechToText = SpeechToText(selenium)
        selenium.navigateTo(speechToText.CONVERTER_URL)
        speechToText.uploadAudio("")
    }
