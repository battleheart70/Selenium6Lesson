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

class WebFormTests {
  private WebDriver driver;
  private static final String BASE_URL = "https://bonigarcia.dev/selenium-webdriver-java/";
  private final String testText = "Loremipsum";
  private final Path TXT_FILE_PATH = Paths.get("src/test/resources/long_text.txt");


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
  void selectDefaultTest() {
    WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));
    Select select = new Select(dropdownSelectMenu);
    Assertions.assertEquals("Open this select menu", select.getFirstSelectedOption().getText());
  }

  @Test
  void selectOptionsTest() {
    WebElement dropdownSelectMenu = driver.findElement(By.name("my-select"));
    Select select = new Select(dropdownSelectMenu);
    select.selectByVisibleText("One");
    Assertions.assertEquals("One", select.getFirstSelectedOption().getText());
    select.selectByValue("2");
    Assertions.assertEquals("Two", select.getFirstSelectedOption().getText());
    select.selectByIndex(3);
    Assertions.assertEquals("Three", select.getFirstSelectedOption().getText());
  }
  @Test
  void typingDropdownTest(){
    WebElement dynamicDropdown = driver.findElement(By.name("my-datalist"));
    dynamicDropdown.sendKeys("New Y");
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js.executeScript("arguments[0].value = 'New York'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);

    Assertions.assertEquals("New York", dynamicDropdown.getAttribute("value"), "Город выбран неверно!");
  }

  @Test
  void selectAnotherValueDynamicDropdownTest(){
    WebElement dynamicDropdown = driver.findElement(By.name("my-datalist"));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    dynamicDropdown.sendKeys("new yo");
    js.executeScript("arguments[0].value = 'New York'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);
    while (!dynamicDropdown.getAttribute("value").equals("")) {
      dynamicDropdown.sendKeys(Keys.BACK_SPACE);
    }
    dynamicDropdown.sendKeys("sea");
    js.executeScript("arguments[0].value = 'Seattle'; arguments[0].dispatchEvent(new Event('change'));", dynamicDropdown);
    Assertions.assertEquals("Seattle", dynamicDropdown.getAttribute("value"), "Город выбран неверно!");

  }

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
    Thread.sleep(3000);
    Assertions.assertTrue(driver.getCurrentUrl().contains("long_text.txt"), "Файла нет в URL!");
  }

  @Test
  void checkboxPageLoadTest(){
    Assertions.assertTrue(driver.findElement(By.id("my-check-1")).isSelected());
    Assertions.assertFalse(driver.findElement(By.id("my-check-2")).isSelected());
  }

  @Test
  void checkboxSelectOtherValuesTest(){
    WebElement firstCheckbox = driver.findElement(By.id("my-check-1"));
    WebElement secondCheckbox = driver.findElement(By.id("my-check-2"));
    firstCheckbox.click();
    secondCheckbox.click();
    Assertions.assertFalse(firstCheckbox.isSelected());
    Assertions.assertTrue(secondCheckbox.isSelected());
  }

  @Test
  void radioButtonPageLoadTest(){
    Assertions.assertTrue(driver.findElement(By.id("my-radio-1")).isSelected());
    Assertions.assertFalse(driver.findElement(By.id("my-radio-2")).isSelected());
  }
  @Test
  void radioButtonSelectOtherValueTest(){
    WebElement firstRadio = driver.findElement(By.id("my-radio-1"));
    WebElement secondRadio = driver.findElement(By.id("my-radio-2"));
    secondRadio.click();
    Assertions.assertFalse(firstRadio.isSelected());
    Assertions.assertTrue(secondRadio.isSelected());
  }

  @Test
  void colorPickerTest(){
    WebElement colorPicker = driver.findElement(By.cssSelector("input[type = 'color']"));
    JavascriptExecutor js = (JavascriptExecutor) driver;
    Color customColor = new Color(61, 71, 124, 1);//#3d477c
    Color redColor = new Color(255, 0, 0, 1); // #ff0000
    Color blueColor = new Color(0, 0, 255, 1); // #0000ff
    Color greenColor = new Color(0, 255, 0, 1); // #00ff00
    String script = "arguments[0].value = arguments[1]; arguments[0].dispatchEvent(new Event('change'));";

    js.executeScript(script, colorPicker, customColor.asHex());
    Assertions.assertEquals("#3d477c", colorPicker.getAttribute("value"));

    js.executeScript(script, colorPicker, redColor.asHex());
    Assertions.assertEquals("#ff0000", colorPicker.getAttribute("value"));

    js.executeScript(script, colorPicker, blueColor.asHex());
    Assertions.assertEquals("#0000ff", colorPicker.getAttribute("value"));

    js.executeScript(script, colorPicker, greenColor.asHex());
    Assertions.assertEquals("#00ff00", colorPicker.getAttribute("value"));
  }

  @Test
  void datePickerTest() throws InterruptedException {
    WebElement dateInput = driver.findElement(By.name("my-date"));
    dateInput.sendKeys("07 11 1997");
    dateInput.sendKeys(Keys.ENTER);
    Thread.sleep(1000);
    Assertions.assertEquals("07/11/1997", dateInput.getAttribute("value"), "Дата не была отформатирована или неверна");
  }

  @Test
  void rangePickerTest() throws InterruptedException {
    WebElement rangePicker = driver.findElement(By.name("my-range"));
    rangePicker.sendKeys(Keys.ARROW_RIGHT);
    Assertions.assertEquals("6", rangePicker.getAttribute("value"));
  }

}
