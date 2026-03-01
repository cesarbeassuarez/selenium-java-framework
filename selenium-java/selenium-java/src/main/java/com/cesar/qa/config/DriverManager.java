package com.cesar.qa.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static WebDriver driver;

    public static void initDriver() {
        System.out.println(">>> initDriver: driver es null? " + (driver == null));
        if (driver != null) return;

        String browser = ConfigReader.getProperty("browser");
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

        if (browser == null) browser = "chrome";

        switch (browser.toLowerCase()) {
            case "chrome":
                System.out.println(">>> Antes de chromedriver setup");
                WebDriverManager.chromedriver().setup();
                //System.setProperty("webdriver.chrome.driver",
                //        "C:\\Users\\Usuario\\.cache\\selenium\\chromedriver\\win64\\145.0.7632.117\\chromedriver.exe");
                System.out.println(">>> Después de chromedriver setup");

                ChromeOptions options = new ChromeOptions();

                if (headless) {
                    // "new" headless en Chrome moderno
                    options.addArguments("--headless=new");
                }
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--window-size=1920,1080");

                options.addArguments("--force-device-scale-factor=1");
                options.addArguments("--enable-features=NetworkService,NetworkServiceInProcess");

                options.addArguments("--lang=es");
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("intl.accept_languages", "es-ES,es");
                options.setExperimentalOption("prefs", prefs);

                System.out.println(">>> Antes de new ChromeDriver");
                driver = new ChromeDriver(options);
                driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
                System.out.println(">>> Chrome abierto OK");

                break;

            default:
                throw new RuntimeException("Browser no soportado: " + browser);
        }

        applyConfig(driver);
    }

    private static void applyConfig(WebDriver driver) {
        int implicit = Integer.parseInt(ConfigReader.getProperty("timeout.implicit"));
        int pageLoad = Integer.parseInt(ConfigReader.getProperty("timeout.pageLoad"));
        boolean maximize = Boolean.parseBoolean(ConfigReader.getProperty("window.maximize"));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));

        if (maximize) {
            driver.manage().window().maximize();
        }
    }

    public static WebDriver getDriver() {
        if (driver == null) initDriver();
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
