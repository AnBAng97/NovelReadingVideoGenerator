package com.ai

import com.selenium.Selenium
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.PageLoadStrategy
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.interactions.Actions
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent


class SpeechToText(
    val selenium: Selenium
) {
    val filePath = "C:\\Users\\Bang-PC\\Downloads\\y2mate.is - Learn English with Audio Story The Adventures of Tom Sawyers-d4PDohlr0Jw-192k-1692030124.mp3"
    val CONVERTER_URL = "https://converter.app/mp3-to-text/"

//    private lateinit var selenium: Selenium

    fun uploadAudio(audioLocation: String) {
        if (openUploadDialog()) {
            val rb = Robot()
            val str = StringSelection(filePath)
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null)

            Thread.sleep(1000);

            // Nhấn Control+V để dán
            rb.keyPress(KeyEvent.VK_CONTROL);
            rb.keyPress(KeyEvent.VK_V);

            // Xác nhận Control V trên
            rb.keyRelease(KeyEvent.VK_CONTROL);
            rb.keyRelease(KeyEvent.VK_V);

            Thread.sleep(1000);

            // Nhấn Enter
            rb.keyPress(KeyEvent.VK_ENTER);
            rb.keyRelease(KeyEvent.VK_ENTER);

//            Thread.sleep(4000);
        }
    }

    fun openUploadDialog(): Boolean {
        val uploadLabel: WebElement = selenium.findElement(
            selenium.driver,
            By.xpath("//*[@id=\"form-element\"]/div[2]/label/strong")
        )
        return try {
            uploadLabel.click()
//            Thread.sleep(5000)
            true
        } catch (e: Exception) {
            false
        }
    }

}

fun main(args: Array<String>) {

    val options = ChromeOptions()
    options.addArguments("enable-automation")
//                options.addArguments("--headless");
//        options.addArguments("--window-size=100,100")
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
//        selenium.driver.quit()
}
