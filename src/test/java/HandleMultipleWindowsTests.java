import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HandleMultipleWindowsTests {
    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;
    private ChromeDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    public static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
    }

    @Test
    public void singleWindowPopUp() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/window-popup-modal-demo");
        WebElement followOnTwitter = driver.findElement(By.xpath("//a[text()='  Follow On Twitter ']"));
        followOnTwitter.click();

        webDriverWait.until(ExpectedConditions.numberOfWindowsToBe(2));

        String mainWindow = driver.getWindowHandle();
        System.out.println("Main window handle is " + mainWindow);

        // To handle child window
        List<String> allWindows = driver.getWindowHandles().stream().toList();
        for (var currentWindow:allWindows) {
            if (!mainWindow.equalsIgnoreCase(currentWindow)) {
                var childWindow = driver.switchTo().window(currentWindow);

                //webDriverWait.until(ExpectedConditions.titleContains("yourTitle"));
                childWindow.manage().window().maximize();
                // cannot find - //span[text() = 'Follow']
                // how to wait for title on window?
                // timeouts
                //var turnOnNotificationsNotNowButton = driver.findElement(By.xpath("//span[text()='Not now']"));
                var turnOnNotificationsNotNowButton = webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Not now']")));
                turnOnNotificationsNotNowButton.click();
                var acceptAllCookiesButton = driver.findElement(By.xpath("//span[text()='Accept all cookies']"));
                acceptAllCookiesButton.click();
                var followButton = driver.findElement(By.xpath("//div[@aria-label = 'Follow @lambdatesting']"));
               followButton.click();
            }
        }

        // driver.quit(); closes all tabs and windows
        // Opens a new tab and switches to new tab Selenium 4
        driver.switchTo().newWindow(WindowType.TAB);

        // Opens a new window and switches to new window Selenium 4
        driver.switchTo().newWindow(WindowType.WINDOW);

        //Switch back to the old tab or window
        driver.switchTo().window("oldWindowHandle");

        // Move the window to the top left of the primary monitor
        driver.manage().window().setPosition(new Point(0, 0));

    }

    @Test
    public void multipleWindowPopUp() {
        driver.navigate().to("https://www.lambdatest.com/selenium-playground/window-popup-modal-demo");
        WebElement followOnTwitterAndFacebook = driver.findElement(By.xpath("//a[text()='Follow Twitter & Facebook']"));
        followOnTwitterAndFacebook.click();

        String mainWindow = driver.getWindowHandle();
        List<String> allWindows = driver.getWindowHandles().stream().toList();
        for (var currentWindow:allWindows) {
            if (!mainWindow.equalsIgnoreCase(currentWindow)) {
                driver.switchTo().window(currentWindow);

                if(driver.getTitle().contains("Facebook")){
                    WebElement acceptCookies = driver.findElement(By.xpath("//span[text()='Allow essential and optional cookies']"));
                    acceptCookies.click();
                    WebElement signUpForFB = driver.findElement(By.xpath("//span[text()='Create new account']"));
                    signUpForFB.click();
                } else if(driver.getTitle().contains("Twitter")){
                    WebElement signUpForTwitter = driver.findElement(By.xpath("//span[text()='Sign up for Twitter']"));
                    signUpForTwitter.click();
                    System.out.println("Clicked on Follow Option For Twitter");
                }
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}