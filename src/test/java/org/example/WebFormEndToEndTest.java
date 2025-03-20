package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class WebFormEndToEndTest {
    private WebDriver driver;
    private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
    private final String testText = "Loremipsum";
    private final Path TXT_FILE_PATH = Paths.get("src/test/resources/long_text.txt");
    URL url = WebFormEndToEndTest.class.getClassLoader().getResource("long_text.txt");
    Color customColor = new Color(61, 124, 61, 1); // #3d7c3d
    String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));";

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        driver.findElement(By.xpath("//a[@href = 'web-form.html']")).click();
    }

    @AfterEach
    void cleanUp() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void endToEndTest() throws IOException, InterruptedException {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String longText = Files.readString(TXT_FILE_PATH);

        fillTextInput(testText);
        fillPassword(testText);
        fillTextArea(longText);
        selectDropdown("One");
        selectDatalist("Los Angeles");
        uploadFile();
        pickColor(customColor.asHex());
        setDate("07/11/1997");

        int steps = 2;
        int expectedRange = 5 + steps;
        setRange(steps);
        submitForm();

        Thread.sleep(2000);

        assertAll(
                () -> assertTrue(driver.getCurrentUrl().contains("my-text=" + testText), "Поле Text Input заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-password=" + testText), "Поле Password заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-textarea=The+wind+whispered+of+distant+lands+where"), "Поле Text Area заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-readonly=Readonly+input"), "Поле Readonly поменяло текст"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-select=1"), "Поле Select заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-datalist=Los+Angeles"), "Поле Datalist заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-date=07%2F11%2F1997"), "Поле Datepicker заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-file=long_text.txt"), "Файла нет в URL!"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-colors=%233d7c3d"), "Поле Color Picker заполнено неверно"),
                () -> assertTrue(driver.getCurrentUrl().contains("my-range=" + expectedRange), "Поле Range заполнено неверно"),
                () -> assertEquals("Form submitted", getPageTitle(), "Title неверный"),
                () -> assertEquals("Received!", getPageConfirmation(), "Confirmation неверный")
        );
    }


    private void fillTextInput(String text) {
        WebElement textInputField = driver.findElement(By.id("my-text-id"));
        textInputField.sendKeys(text);
    }

    private void fillPassword(String password) {
        WebElement passwordInputField = driver.findElement(By.cssSelector("input[name='my-password']"));
        passwordInputField.sendKeys(password);
    }

    private void fillTextArea(String text) {
        WebElement textArea = driver.findElement(By.cssSelector("textarea.form-control"));
        textArea.sendKeys(text);
    }

    private void selectDropdown(String option) {
        WebElement dropdown = driver.findElement(By.name("my-select"));
        new Select(dropdown).selectByVisibleText(option);
    }

    private void selectDatalist(String text) {
        WebElement dynamicDropdown = driver.findElement(By.name("my-datalist"));
        dynamicDropdown.sendKeys(text);
        ((JavascriptExecutor) driver).executeScript(script, dynamicDropdown, text);
    }

    private void uploadFile() {
        WebElement fileUpload = driver.findElement(By.name("my-file"));
        String absolutePath = new File(url.getPath()).getAbsolutePath();
        fileUpload.sendKeys(absolutePath);
    }

    private void pickColor(String hexColor) {
        WebElement colorPicker = driver.findElement(By.cssSelector("input[type = 'color']"));
        ((JavascriptExecutor) driver).executeScript(script, colorPicker, hexColor);
    }

    private void setDate(String date) {
        WebElement dateInput = driver.findElement(By.name("my-date"));
        dateInput.sendKeys(date);
        dateInput.sendKeys(Keys.ENTER);
    }

    private void setRange(int steps) {
        WebElement rangePicker = driver.findElement(By.name("my-range"));
        for (int i = 0; i < steps; i++) {
            rangePicker.sendKeys(Keys.ARROW_RIGHT);
        }
    }

    private void submitForm() {
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
        submitButton.click();
    }

    // 🔹 Методы для проверки результата
    private String getPageTitle() {
        return driver.findElement(By.cssSelector("h1[class ='display-6']")).getText();
    }

    private String getPageConfirmation() {
        return driver.findElement(By.cssSelector("p[class='lead']")).getText();
    }
}
