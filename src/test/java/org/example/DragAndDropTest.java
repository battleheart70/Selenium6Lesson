package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class DragAndDropTest {
    private WebDriver driver;
    private static final String BASE_URL =
            "https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html";

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
    void dragAndDropTest() throws InterruptedException {
        WebElement draggable = driver.findElement(By.id("draggable"));
        WebElement target = driver.findElement(By.id("target"));

        Point initialPosition = draggable.getLocation();

        new Actions(driver)
                .dragAndDrop(draggable, target)
                .perform();

        Thread.sleep(1000);

        Point finalPosition = draggable.getLocation();
        Assertions.assertNotEquals(initialPosition, finalPosition, "Элемент не переместился!");

        Assertions.assertTrue(isIntersecting(draggable, target), "Элементы не пересекаются");
    }

    private boolean isIntersecting(WebElement element1, WebElement element2) {
        Rectangle rect1 = element1.getRect();
        Rectangle rect2 = element2.getRect();

        int left1 = rect1.getX();
        int top1 = rect1.getY();
        int right1 = left1 + rect1.getWidth();
        int bottom1 = top1 + rect1.getHeight();

        int left2 = rect2.getX();
        int top2 = rect2.getY();
        int right2 = left2 + rect2.getWidth();
        int bottom2 = top2 + rect2.getHeight();

        return left1 < right2 &&
                right1 > left2 &&
                top1 < bottom2 &&
                bottom1 > top2;
    }

}
