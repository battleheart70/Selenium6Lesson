package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class WebFormTests {
  private WebDriver driver;
  private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
  private final String testText = "Lorem ipsum";
  private final Path TXT_FILE_PATH = Paths.get("src/test/resources/long_text.txt");

  WebFormTests() throws IOException {}

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
  void textInputTest() {
    WebElement textInputField = driver.findElement(By.id("my-text-id"));

    textInputField.sendKeys(testText);
    Assertions.assertEquals(testText, textInputField.getAttribute("value"), "Текст неправильный!");
  }

  @Test
  void textInputClearTest() {
    WebElement textInputField = driver.findElement(By.id("my-text-id"));

    textInputField.sendKeys(testText);
    textInputField.clear();
    Assertions.assertEquals("", textInputField.getAttribute("value"), "Поле должно быть очищено!");
  }

  @Test
  void passwordInputTest() {
    WebElement passwordInputField = driver.findElement(By.cssSelector("input[name='my-password']"));

    passwordInputField.sendKeys(testText);
    Assertions.assertEquals(
        testText, passwordInputField.getAttribute("value"), "Текст неправильный!");
  }

  @Test
  void passwordInputClearTest() {
    WebElement passwordInputField = driver.findElement(By.cssSelector("input[name='my-password']"));

    passwordInputField.sendKeys(testText);
    passwordInputField.clear();
    Assertions.assertEquals(
        "", passwordInputField.getAttribute("value"), "Поле должно быть очищено");
  }

  @Test
  void longTextAreaTest() throws IOException {
    WebElement textArea = driver.findElement(By.cssSelector("textarea.form-control"));
    String longText = Files.readString(TXT_FILE_PATH);

    textArea.sendKeys(longText);
    Assertions.assertEquals(longText, textArea.getAttribute("value"), "Текст должен быть из файла");
  }

  @Test
  void clearLongTextAreaTest() throws IOException {
    WebElement textArea = driver.findElement(By.cssSelector("textarea.form-control"));
    String longText = Files.readString(TXT_FILE_PATH);

    textArea.sendKeys(longText);
    textArea.clear();
    Assertions.assertEquals("", textArea.getAttribute("value"), "Поле должно быть очищено");
  }

  @Test
  void disabledInputTest() {
    WebElement disabledElement = driver.findElement(By.cssSelector("input[name='my-disabled']"));

    Assertions.assertFalse(disabledElement.isEnabled(), "Поле должно быть отключено");
    Assertions.assertEquals(
        "Disabled input", disabledElement.getAttribute("placeholder"), "Неправильный placeholder");
    Assertions.assertThrows(
        ElementNotInteractableException.class, () -> disabledElement.sendKeys("test"));
  }

  @Test
  void readOnlyInputTest() {
    WebElement readonlyElement = driver.findElement(By.cssSelector("input[name='my-readonly']"));

    Assertions.assertTrue(
        readonlyElement.isEnabled(), "Поле не должно быть отключено - оно ридонли");
    Assertions.assertTrue(readonlyElement.getAttribute("readonly") != null);
    readonlyElement.sendKeys("test");
    Assertions.assertEquals(
        "Readonly input",
        readonlyElement.getAttribute("value"),
        "Readonly поле не должно изменяться");
  }

  @Test
  void selectTest() {}

  @Test
  void fileUploadTest() throws InterruptedException {
    URL url = WebFormTests.class.getClassLoader().getResource("long_text.txt");
    String absolutePath = null;
    if (url != null) {
      absolutePath = new File(url.getPath()).getAbsolutePath();
      System.out.println("Абсолютный путь к файлу: " + absolutePath);
    } else {
      System.out.println("Ресурс не найден.");
    }
    WebElement fileUpload = driver.findElement(By.name("my-file"));
    fileUpload.sendKeys(absolutePath);
    driver.findElement(By.xpath("//button[text()='Submit']")).click();
    Thread.sleep(5000);
    Assertions.assertTrue(driver.getCurrentUrl().contains("long_text.txt"), "Файла нет в URL!");
  }
}
