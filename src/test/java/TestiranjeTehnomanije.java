import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

public class TestiranjeTehnomanije {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    public TestiranjeTehnomanije() {
        System.setProperty("webdriver.edge.driver","C:\\Users\\katar\\OneDrive\\Radna površina\\QA Kurs\\Zavrsni Projekat\\Biblioteke\\Driver\\msedgedriver.exe");

        this.driver = new EdgeDriver();
        this.actions = new Actions(this.driver);
        this.wait = new WebDriverWait(this.driver, 5);

        this.driver.get("https://www.tehnomanija.rs/");
        this.driver.manage().window().maximize();

    }

    @Test
    public void kupiNajjeftinijiFen() {
        ukloniPopupove();

        ulogujKorisnika();


    }

    private void ulogujKorisnika() {
        WebElement loginDropdown = driver.findElement(By.className("user-name"));

        actions.moveToElement(loginDropdown).perform();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("email"))));
        WebElement emailField = driver.findElement(By.name("email"));
        emailField.sendKeys("katarina.radumilo@hotmail.com");

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.name("password"))));
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("Ab!12345");

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("login-button"))));
        WebElement loginButton = driver.findElement(By.className("login-button"));
        loginButton.click();
    }

    private void ukloniPopupove() {
        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("cookie-agree"))));
            WebElement cookieConsent = driver.findElement(By.className("cookie-agree"));
            cookieConsent.click();
        }
        catch (Exception e) {
            System.out.println("Problem with Cookie Consent popup");
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("seg-popup-push-bttn-later-new"))));
            WebElement pushNotification = driver.findElement(By.className("seg-popup-push-bttn-later-new"));
            pushNotification.click();
        }
        catch (Exception e) {
            System.out.println("Problem with Push Notification popup");
        }
    }
}
