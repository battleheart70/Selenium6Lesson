package org.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

public class NavigationTest {
  private WebDriver driver;
  private static final String BASE_URL =
      "https://bonigarcia.dev/selenium-webdriver-java/navigation1.html";
  private static final String PAGE_TITLE = "Navigation example";
  private static final String FIRST_PAGE_CONTEXT =
      "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
  private static final String SECOND_PAGE_CONTEXT =
      "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.";
  private static final String THIRD_PAGE_CONTEXT =
      "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

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
  void openPageCheckTitleTest() {
    checkPreviousButtonIsDisabled();
    checkPageContent(PAGE_TITLE, FIRST_PAGE_CONTEXT);
  }

  @Test
  void nextButtonNavigation() throws InterruptedException {
    checkPreviousButtonIsDisabled();

    clickNextButton();
    Thread.sleep(1000);
    checkPageContent(PAGE_TITLE, SECOND_PAGE_CONTEXT);

    clickNextButton();
    Thread.sleep(1000);
    checkPageContent(PAGE_TITLE, THIRD_PAGE_CONTEXT);
  }

  @Test
  void previousButtonNavigation() {
    checkPreviousButtonIsDisabled();

    clickNextButton();
    clickNextButton();
    clickPreviousButton();
    checkPageContent(PAGE_TITLE, SECOND_PAGE_CONTEXT);
    clickPreviousButton();
    checkPageContent(PAGE_TITLE, FIRST_PAGE_CONTEXT);
    checkPreviousButtonIsDisabled();
  }

  @Test
  void pageNumberButtonNavigation(){
    checkPreviousButtonIsDisabled();

    clickFirstPageButton();
    checkPageContent(PAGE_TITLE, FIRST_PAGE_CONTEXT);

    clickSecondPageButton();
    checkPageContent(PAGE_TITLE, SECOND_PAGE_CONTEXT);

    clickThirdPageButton();
    checkPageContent(PAGE_TITLE, THIRD_PAGE_CONTEXT);
  }


  private void clickNextButton() {
    WebElement nextButton = driver.findElement(By.xpath("//a[text()='Next']"));
    nextButton.click();
  }

  private void clickPreviousButton() {
    WebElement previousButton = driver.findElement(By.xpath("//a[text()='Previous']"));
    previousButton.click();
  }

  private void clickFirstPageButton(){
    WebElement firstPageButton = driver.findElement(By.xpath("//a[text()='1']"));
    firstPageButton.click();
  }
  private void clickSecondPageButton() {
    WebElement secondPageButton = driver.findElement(By.xpath("//a[text()='2']"));
    secondPageButton.click();
  }

  private void clickThirdPageButton() {
    WebElement thirdPageButton = driver.findElement(By.xpath("//a[text()='3']"));
    thirdPageButton.click();
  }

  private void checkPreviousButtonIsDisabled() {
    WebElement previousButton = driver.findElement(By.xpath("//a[text()='Previous']"));
    WebElement parentLi = previousButton.findElement(By.xpath("./parent::li"));
    Assertions.assertTrue(
        parentLi.getAttribute("class").contains("disabled"), "Кнопка Previous не отключена!");
  }

  private void checkPageContent(String expectedTitle, String expectedContext) {
    WebElement pageTitle = driver.findElement(By.cssSelector("h1.display-6"));
    WebElement pageContext = driver.findElement(By.cssSelector("p.lead"));

    Assertions.assertEquals(expectedTitle, pageTitle.getText(), "Неправильный title на странице!");
    Assertions.assertEquals(
        expectedContext, pageContext.getText(), "Неправильный текст на странице!");
  }
}
