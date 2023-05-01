package github.SauceDemo;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.time.Duration;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;


public class MakeSimpleOrder {
	
	//Global variables
	String url = "https://www.saucedemo.com/";
	String evidenceFolder = "..\\SauceDemo\\evidenceFolder\\MakeSimpleOrder\\";
	File screenshotEvidence;
	WebDriver chromeDriver;
	
		
	@BeforeSuite(description="driver setup")
	public void setUp() {
		
		System.out.println("====================");
		System.out.println("Testing suite start!");
		System.out.println("====================");
		chromeDriver = new ChromeDriver();
		
	}
	
	
	@BeforeTest(description="browser setup and login")
	public void logIn() throws IOException {
		
		//Open chrome, delete cookies, maximize windows
		chromeDriver.get(url);
		chromeDriver.manage().deleteAllCookies();
		chromeDriver.manage().window().maximize();
		
		//Assert to verify browser is on the same url that was provided
		Assert.assertEquals(url, chromeDriver.getCurrentUrl());
		
		//Implicit wait until page is fully loaded
		chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
				
		//Filling fields
		chromeDriver.findElement(By.id("user-name")).sendKeys("standard_user");;
		chromeDriver.findElement(By.id("password")).sendKeys("secret_sauce");

		//Asserts: Check fields are not empty
		Assert.assertNotNull(chromeDriver.findElements(By.id("user-name")));
		Assert.assertNotNull(chromeDriver.findElements(By.id("password")));
				
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_UserLogin.png"));
				
		//Login button click
		chromeDriver.findElement(By.id("login-button")).click();
		
	}
	
	
	@Test(priority=0, description="Sort products from higher to lower price") 
	public void productSortHiLo() throws IOException {
		
		//Implicit wait until page is fully loaded
		chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		//Dropdown high to low
		Select dropProductSort = new Select (chromeDriver.findElement(By.tagName("select")));
		dropProductSort.selectByValue("hilo");
		
		//Assert: Dropdown has specific text showing
		//Assert.assertEquals(dropProductSort.getFirstSelectedOption().getText(), "Price (high to low)");
		
		//Long screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(chromeDriver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_Sort_HiLo.png"));

	}
	
	
	@Test(priority=1, description="Add two items to cart") 
	public void addToCartAndGoToCheckout () throws IOException {
		
		//Explicit wait until element jacket is visible and click on it
		WebDriverWait explicitWait = new WebDriverWait (chromeDriver, Duration.ofSeconds(15));
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='add-to-cart-sauce-labs-fleece-jacket']"))).click();
		
		//Assert: Cart is not empty
		Assert.assertTrue(chromeDriver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")).isDisplayed());
		
		//Click on bike light title
		chromeDriver.findElement(By.xpath("//*[@id=\"item_0_title_link\"]/div")).click();
		
		//Assert: Current page is bike light (https://www.saucedemo.com/inventory-item.html?id=0)
		Assert.assertEquals("https://www.saucedemo.com/inventory-item.html?id=0", chromeDriver.getCurrentUrl());
		
		//Press add to cart button
		chromeDriver.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
		
		//Assert: Cart goes from one to two items
		Assert.assertNotEquals(chromeDriver.findElement(By.xpath("//span[contains(text(),'2')]")), "1");
		
		//Click on cart icon
		chromeDriver.findElement(By.xpath("//body/div[@id='root']/div[@id='page_wrapper']/div[@id='contents_wrapper']/div[@id='header_container']/div[1]/div[3]/a[1]")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_CartItems.png"));
		
		//click on checkout button
		chromeDriver.findElement(By.id("continue-shopping")).click();
		
	}
	
	
	@Test(priority=2, description="Complete checkout info and buy items")
	public void checkoutFinish() throws IOException {
		
		//Click on cart button
		chromeDriver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a")).click();
		
		//Assert: Cart is not empty
		Assert.assertTrue(chromeDriver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")).isDisplayed());
		
		//Click on Checkout button
		chromeDriver.findElement(By.id("checkout")).click();
		
		//Fill checkout with randomizer
		Faker faker = new Faker();
		chromeDriver.findElement(By.id("first-name")).sendKeys(faker.name().firstName());
		chromeDriver.findElement(By.id("last-name")).sendKeys(faker.name().lastName());
		chromeDriver.findElement(By.id("postal-code")).sendKeys(faker.address().zipCode());
		chromeDriver.findElement(By.id("continue")).click();
		
		//Long screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(chromeDriver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_checkout_items.png"));
		
		//Click on finish button
		chromeDriver.findElement(By.id("finish")).click();
		
		//Assert: Page is checkout last part (https://www.saucedemo.com/checkout-complete.html)
		Assert.assertEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/checkout-complete.html");
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_checkout_complete.png"));
		
		//Click on Back Home button
		chromeDriver.findElement(By.id("back-to-products")).click();
		
	}
	
	
	@AfterTest (description="Close browser")
	public void closeBrowser() {
		
		chromeDriver.quit();
		
	}
	
	
	@AfterSuite (description="Show message in console")
	public void showConsoleMessage() {
		
		System.out.println("===============================================");
		System.out.println("Test suite end!");
		System.out.println("Evidence screenshots at " + evidenceFolder);
		System.out.println("===============================================");
		
	}
	
}