package otus.homework;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverTest {

    static WebDriver driver;
    private WebDriverWait wait;

    private String baseUrl = ""; // Укажите начальный URL через
                                 // -DbaseUrl=https://otus.home.kartushin.su/training.html
    private String browser = ""; // Укажите какой браузер использовать через -Dbrowser=edge

    // Конфигурация ожиданий
    private final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    private final Duration EXPLICIT_WAIT = Duration.ofSeconds(15);
    private final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    @BeforeAll
    public static void driver_setup() {
        if (System.getProperty("browser") == null) {
            System.out.println("Укажите браузер для запуска в -Dbrowser");
        } else {
            try {
                if (System.getProperty("browser").equals("edge")) {
                    WebDriverManager.edgedriver().setup();
                }
                if (System.getProperty("browser").equals("chrome")) {
                    WebDriverManager.chromedriver().setup();
                }
            } catch (Exception e) {
                System.out.println(e);
                // Указываем путь к драйверу по дефолту вручную
                System.setProperty("webdriver.edge.driver","C:\\Users\\Omnic\\Documents\\Курсы\\ОТУС\\edgedriver_win64\\msedgedriver.exe");
            }
        }
    }

    @BeforeEach
    public void driver_start() {
        if (System.getProperty("browser").equals("edge")) {
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--start-maximized");
            driver = new EdgeDriver(options);
        }
        if (System.getProperty("browser").equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--start-maximized");
            driver = new ChromeDriver(options);
        }
        // НАСТРОЙКА ОЖИДАНИЙ
        configureWaits();
        // ПОЛУЧЕНИЕ СТАРТОВОЙ СТРАНИЦЫ
        this.baseUrl = System.getProperty("baseUrl", "https://otus.home.kartushin.su/training.html");
        driver.get(this.baseUrl);
    }

    /** * Настройка явных и неявных ожиданий */
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
            // driver.quit();
        }
    }

    @Test
    public void testCheckingTextInInput() {
        WebElement testInput = driver.findElement(By.id("textInput"));
        testInput.sendKeys("ОТУС");
        assertEquals("ОТУС", testInput.getAttribute("value"));
    }

    @Test
    public void testCheckingAttributeInElement() {
        WebElement openModalBtn = driver.findElement(By.id("openModalBtn"));
        openModalBtn.click();
        WebElement modal = driver.findElement(By.id("myModal"));
        String modal_style = modal.getAttribute("style");
        assertEquals("display: block;", modal_style);
    }

    @Test
    public void testFillingAndSubmitForm() {
        WebElement form = driver.findElement(By.id("sampleForm"));
        WebElement formName = driver.findElement(By.id("name"));
        WebElement formEmail = driver.findElement(By.id("email"));

        formName.sendKeys("Otus");
        formEmail.sendKeys("Otus@otus.ru");
        WebElement buttonSummit = form.findElement(By.tagName("button"));
        buttonSummit.click();

        WebElement messageBox = driver.findElement(By.id("messageBox"));
        String message = String.format("Форма отправлена с именем: %s и email: %s", "Otus", "Otus@otus.ru");
        assertEquals(message, messageBox.getText());
    }

}
