package com.selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class Selenium {
    public final WebDriver driver;
    public Wait<WebDriver> wait;
    public ArrayList<String> tabList;
    public JavascriptExecutor js;
    String originalTab = "";

    public Selenium() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("enable-automation");
//        options.addArguments("--headless");
        options.addArguments("--window-size=100,100");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-extensions");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-gpu");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
//        options.setPageLoadStrategy(PageLoadStrategy.NONE);

        driver = new ChromeDriver(options);
        setLoadTimeout(15);
        js = (JavascriptExecutor) driver;
    }
    public Selenium(ChromeOptions options) {
        driver = new ChromeDriver(options);
        setLoadTimeout(15);
        js = (JavascriptExecutor) driver;
    }
    public Selenium(EdgeOptions options) {
        driver = new EdgeDriver(options);
        setLoadTimeout(15);
        js = (JavascriptExecutor) driver;
    }

    private void setLoadTimeout(int timeout) {
        driver.manage().timeouts().pageLoadTimeout(Duration
                .ofSeconds(timeout));
    }

    //    public WebDriver getChromeDriver() {
//        return chromeDriver;
//    }
    public void navigateTo(String url) {
        driver.navigate()
                .to(url);
        if (originalTab.isEmpty()) originalTab = driver.getWindowHandle();
    }

    public Wait<WebDriver> setFluentWait(long timeout, long refreshEvery, WebDriver driver) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(refreshEvery))
                .ignoring(NoSuchElementException.class);
    }


    public Wait<WebDriver> setFluentWait(long timeoutSecond, long refreshRateSecond) {
        return this.wait = new FluentWait<>(this.driver)
                .withTimeout(Duration.ofSeconds(timeoutSecond))
                .pollingEvery(Duration.ofSeconds(refreshRateSecond))
                .ignoring(NoSuchElementException.class);
    }

    public WebElement waitElementVisible(Wait<WebDriver> wait, By by) {
        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    public WebElement waitElementVisible(By by) {
        return wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(by);
            }
        });
    }

    public List<WebElement> findElements(WebDriver driver, By by) {
        return driver.findElements(by);
    }

    public WebElement findElement(WebDriver driver, By by) {
        return driver.findElement(by);
    }

    public List<WebElement> getAllLinks() {
        return this.driver.findElements(By.xpath("//a[@href]"));
    }

    public void loadTabList() {
        tabList = new ArrayList<>(driver.getWindowHandles());
    }

    public void goToTab(int index) {
        driver.switchTo().window(tabList.get(index));
    }

    public void goToTab(String handle) {
        driver.switchTo().window(handle);
    }

    public String goToOriginalTab() {
        driver.switchTo().window(originalTab);
        return driver.getWindowHandle();
    }

    public void clearTabList() {
        tabList.clear();
    }

    public void newTab() {
        driver.switchTo().newWindow(WindowType.TAB);
//        chromeDriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");

//        Actions action = new Actions(chromeDriver);
//        action.keyDown(Keys.LEFT_CONTROL).sendKeys("t").keyUp(Keys.LEFT_CONTROL).build().perform();
    }
    public void newTab(String url) {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.navigate().to(url);
//        chromeDriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL +"t");

//        Actions action = new Actions(chromeDriver);
//        action.keyDown(Keys.LEFT_CONTROL).sendKeys("t").keyUp(Keys.LEFT_CONTROL).build().perform();
    }

    public void closeCurrentTab() {
        driver.close();
    }

    public void goToNextTab() {
        Actions action = new Actions(driver);
        action.keyDown(Keys.ALT).sendKeys(Keys.TAB).keyUp(Keys.ALT).build().perform();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void closeAlLKeepOrigin() {
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(originalTab)) {
                goToTab(handle);
                closeCurrentTab();
            }
        }
        goToOriginalTab();
    }
}
