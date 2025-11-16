package otus.homework;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DriverTest {

    WebDriver driver;
    WebDriverWait wait;

    String urlTraining = "https://otus.home.kartushin.su/training.html";

    // Конфигурация ожиданий
    private final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
    private final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    @BeforeAll
    public static void driver_setup() {
        // Указываем путь к драйверу вручную
        System.setProperty("webdriver.edge.driver",
                "C:\\Users\\Omnic\\Documents\\Курсы\\ОТУС\\edgedriver_win64\\msedgedriver.exe");
    }

    @BeforeEach
    public void driver_start() {
        EdgeOptions options = new EdgeOptions();
        // Добавляем опции для избежания проблем
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        driver = new EdgeDriver(options);

        // НАСТРОЙКА ОЖИДАНИЙ
        configureWaits();
        driver.get(this.urlTraining);
    }

    /**
     * Настройка явных и неявных ожиданий
     */
    private void configureWaits() {
        // НЕЯВНОЕ ОЖИДАНИЕ - применяется ко всем findElement операциям
        driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
        // Таймаут загрузки страницы
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);
        // Таймаут для выполнения скриптов
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));
        // ЯВНОЕ ОЖИДАНИЕ - для создания wait объекта
        wait = new WebDriverWait(driver, EXPLICIT_WAIT);
    }

    @AfterEach
    public void close_and_quit() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }

    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep был прерван", e);
        }
    }

    @Test
    public void testNumber1() {
        WebElement testInput = driver.findElement(By.id("textInput"));
        testInput.sendKeys("ОТУС");
        assert testInput.getAttribute("value").equals("ОТУС");
    }

    @Test
    public void testNumber2() {
        WebElement openModalBtn = driver.findElement(By.id("openModalBtn"));
        openModalBtn.click();
        WebElement modal = driver.findElement(By.id("myModal"));
        String modal_style = modal.getAttribute("style");
        assert modal_style.equals("display: block;");
    }

    @Test
    public void testNumber3() {
        WebElement form = driver.findElement(By.id("sampleForm"));
        WebElement formName = driver.findElement(By.id("name"));
        WebElement formEmail = driver.findElement(By.id("email"));

        formName.sendKeys("Otus");
        formEmail.sendKeys("Otus@otus.ru");
        WebElement buttonSummit = form.findElement(By.tagName("button"));
        buttonSummit.click();
        
        WebElement messageBox = driver.findElement(By.id("messageBox"));
        String message = String.format("Форма отправлена с именем: %s и email: %s", "Otus", "Otus@otus.ru");
        assert messageBox.getText().equals(message);
    }

}
