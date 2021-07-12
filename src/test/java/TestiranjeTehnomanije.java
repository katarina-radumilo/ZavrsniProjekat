import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

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
    public void logovanjeKorisnika() {
        ukloniPopupove();

        ulogujKorisnika();

        WebElement loginDropdown = driver.findElement(By.className("user-name"));
        String loginDropdownCaption = loginDropdown.getText().toUpperCase().trim();

        Assert.assertNotEquals("PRIJAVA", loginDropdownCaption);
    }

    @Test
    public void odaberiKategorijuMasineZaPranjeVesa(){
        postaviPocetnoStanje();

        odaberiKategorijuProizvoda("bela-tehnika", "masine-za-pranje-vesa");

        String location = driver.getCurrentUrl();

        Assert.assertEquals("https://www.tehnomanija.rs/bela-tehnika/masine-za-pranje-vesa", location);
    }

    @Test
    public void odjavljivanjeKorisnika() {
        postaviPocetnoStanje();

        odjaviKorisnika();

        WebElement loginDropdown = driver.findElement(By.className("user-name"));
        String loginDropdownCaption = loginDropdown.getText().toUpperCase().trim();

        Assert.assertEquals("PRIJAVA", loginDropdownCaption);
    }

    @Test
    public void kupiNajjeftinijiFen() {
        postaviPocetnoStanje();

        odaberiKategorijuProizvoda("lepota-i-zdravlje", "fenovi-za-kosu");

        sortirajPoCeniRastuce();

        dodajPrviProizvodUKorpu();

        pregledajKorpu();

        Double totalAmountBasket = totalKorpe();

        boolean priceInRange = (totalAmountBasket > 0.0 && totalAmountBasket < 1500.0);

        Assert.assertTrue(priceInRange);
    }

    @Test
    public void dodajNajjeftinijuPeglu() {
        postaviPocetnoStanje();

        odaberiKategorijuProizvoda("mali-kucni-aparati", "standardne-pegle");

        sortirajPoCeniRastuce();

        dodajPrviProizvodUKorpu();

        pregledajKorpu();

        Double totalAmountBasket = totalKorpe();

        boolean priceInRange = (totalAmountBasket > 0.0 && totalAmountBasket < 2000.0);

        Assert.assertTrue(priceInRange);
    }

    @Test
    public void nastavakKupovine(){
        postaviPocetnoStanje();

        odaberiKategorijuProizvoda("mali-kucni-aparati", "aparati-za-espresso");

        sortirajPoCeniRastuce();

        dodajPrviProizvodUKorpu();

        pregledajKorpu();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("optional-continue-shopping"))));
        WebElement continueShopping = driver.findElement(By.className("optional-continue-shopping"));
        actions.moveToElement(continueShopping).perform();
        continueShopping.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("category-menu"))));
        String location = driver.getCurrentUrl();

        Assert.assertEquals("https://www.tehnomanija.rs/", location);
    }

    private void postaviPocetnoStanje() {
        vratiNaPocetnuStranu();

        ukloniPopupove();

        ulogujKorisnika();
    }

    private void vratiNaPocetnuStranu() {
        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//[@href='https://www.tehnomanija.rs/']"))));
            WebElement continueShopping = driver.findElement(By.xpath("//[@href='https://www.tehnomanija.rs/']"));
            actions.moveToElement(continueShopping).perform();
            continueShopping.click();
        }
        catch (Exception e) {

        }
    }

    private void odaberiKategorijuProizvoda(String kategorija, String podKategorija) {
        WebElement categoryMenu = driver.findElement(By.className("category-menu"));
        actions.moveToElement(categoryMenu).perform();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@href='https://www.tehnomanija.rs/" + kategorija + "']"))));
        WebElement productCategory = driver.findElement(By.xpath("//a[@href='https://www.tehnomanija.rs/" + kategorija + "']"));
        actions.moveToElement(productCategory).perform();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@href='https://www.tehnomanija.rs/" + kategorija + "/" + podKategorija + "']"))));
        WebElement productSubCategory = driver.findElement(By.xpath("//a[@href='https://www.tehnomanija.rs/" + kategorija + "/" + podKategorija + "']"));
        productSubCategory.click();
    }

    private void sortirajPoCeniRastuce() {
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("sort-products"))));
        WebElement sortProducts = driver.findElement(By.className("sort-products"));
        actions.moveToElement(sortProducts).perform();
        sortProducts.click();

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@data-value='price_asc']"))));
        WebElement sortCategory = driver.findElement(By.xpath("//div[@data-value='price_asc']"));
        sortCategory.click();
    }

    private void dodajPrviProizvodUKorpu() {
        List<WebElement> productList = (List<WebElement>) driver.findElements(By.xpath("//div[@class='product-wrap-grid js-product-ga-wrap']//div[@class='no-display js-ga-product-data']"));
        WebElement firstProductItem = productList.stream().findFirst().get();
        String firstProductID = firstProductItem.getAttribute("data-product-id");

        WebElement addToBasket = driver.findElement(By.xpath("//a[@data-blue-product-product-id='" + firstProductID + "']"));
        actions.moveToElement(addToBasket).perform();
        addToBasket.click();
    }

    private void pregledajKorpu() {
        WebElement goToBasket = driver.findElement(By.className("basket-header"));
        goToBasket.click();
    }

    private void ulogujKorisnika() {
        WebElement loginDropdown = driver.findElement(By.className("user-name"));
        String loginDropdownCaption = loginDropdown.getText().toUpperCase().trim();
        if (!loginDropdownCaption.equals("PRIJAVA")) {
            odjaviKorisnika();
        }

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
            System.out.println(e.getMessage());
        }

        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("seg-popup-push-bttn-later-new"))));
            WebElement pushNotification = driver.findElement(By.className("seg-popup-push-bttn-later-new"));
            pushNotification.click();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void odjaviKorisnika() {
        try {
            WebElement loginDropdown = driver.findElement(By.className("user-name"));
            actions.moveToElement(loginDropdown).perform();

            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='dropdown-login logged-in']//a[@id='js-logout']"))));
            WebElement logoutOption = driver.findElement(By.xpath("//div[@class='dropdown-login logged-in']//a[@id='js-logout']"));
            logoutOption.click();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Double totalKorpe() {
        Double basketTotalAmount = 0.0;

        WebElement basketTotal = driver.findElement(By.xpath("//div[@class='basket-header js-basket-header']//span[@class='js-header-basket-total header-basket-total']"));
        String basketTotalText = basketTotal.getText().replace("RSD", "").trim();

        DecimalFormat decimalFormat = new DecimalFormat("###,##0.00");

        try {
            basketTotalAmount = decimalFormat.parse(basketTotalText).doubleValue();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return basketTotalAmount;
    }
}
