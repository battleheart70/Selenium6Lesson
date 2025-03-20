package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.util.List;

public class ActionsApiDropdownTest {
  private WebDriver driver;
  private static final String BASE_URL =
      "https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html";
  List<String> expectedItems =
      List.of("Action", "Another action", "Something else here", "Separated link");

  @BeforeEach
  void setup() {
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.get(BASE_URL);
  }

  @AfterEach
  void cleanUp() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Test
  void baseTestOpenPageCheckContent() {
    WebElement pageTitle = driver.findElement(By.cssSelector("h1.display-6"));
    Assertions.assertEquals(
        "Dropdown menu", pageTitle.getText(), "Неправильный title на странице!");
  }

  @Test
  void checkDropdownItemsOfLeftClick() throws InterruptedException {
    WebElement firstDropdownButton = driver.findElement(By.id("my-dropdown-1"));
    new Actions(driver).click(firstDropdownButton).perform();
    Thread.sleep(1000);

    checkColor(firstDropdownButton, "#0d6efd");
    List<String> actualItems = getDropdownItems(firstDropdownButton);

    Assertions.assertEquals(expectedItems, actualItems, "Элементы выпадающего меню не совпадают!");
  }

  @Test
  void checkDropdownItemsOfRightClick() throws InterruptedException {
    WebElement secondDropdownButton = driver.findElement(By.id("my-dropdown-2"));
    new Actions(driver).contextClick(secondDropdownButton).perform();
    Thread.sleep(1000);

    checkColor(secondDropdownButton, "#198754");
    List<String> actualItems = getDropdownItems(secondDropdownButton);

    Assertions.assertEquals(expectedItems, actualItems, "Элементы выпадающего меню не совпадают!");
  }

  @Test
  void checkDropdownItemsOfDoubleClick() throws InterruptedException {
    WebElement thirdDropdownButton = driver.findElement(By.id("my-dropdown-3"));
    new Actions(driver).doubleClick(thirdDropdownButton).perform();
    Thread.sleep(1000);

    checkColor(thirdDropdownButton, "#dc3545");
    List<String> actualItems = getDropdownItems(thirdDropdownButton);

    Assertions.assertEquals(expectedItems, actualItems, "Элементы выпадающего меню не совпадают!");
  }

  private void checkColor(WebElement element, String expectedColor) {
    String backgroundColorHex = Color.fromString(element.getCssValue("background-color")).asHex();
    String borderColorHex = Color.fromString(element.getCssValue("border-top-color")).asHex();
    Assertions.assertEquals(
        expectedColor,
        backgroundColorHex,
        "Цвет фона кнопки неверный! Ожидалось: "
            + expectedColor
            + ", получено: "
            + backgroundColorHex);
    Assertions.assertEquals(
        expectedColor,
        borderColorHex,
        "Цвет рамки кнопки неверный! Ожидалось: "
            + expectedColor
            + ", получено: "
            + borderColorHex);
  }

  private List<String> getDropdownItems(WebElement element) {
    List<WebElement> dropdownItems =
        element.findElements(
            By.xpath(
                "./following-sibling::ul[contains(@class, 'dropdown-menu')]//a[@class='dropdown-item']"));
    return dropdownItems.stream().map(WebElement::getText).toList();
  }
}
