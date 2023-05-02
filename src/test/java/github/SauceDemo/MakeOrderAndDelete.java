package github.SauceDemo;

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
import org.openqa.selenium.NoSuchElementException;
import java.time.Duration;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import com.github.javafaker.Faker;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class MakeOrderAndDelete {

	//Global variables
	String url = "https://www.saucedemo.com/";
	String evidenceFolder = "..\\SauceDemo\\evidenceFolder\\MakeOrderAndDelete\\";
	File screenshotEvidence;
	WebDriver chromeDriver;
	Faker faker = new Faker();
		
			
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
		
		//Login button click
		chromeDriver.findElement(By.id("login-button")).click();
		
	}
	
	
	@Test(priority=0, description="Sort products from Z to A") 
	public void productSortZA() throws IOException {
		
		//Implicit wait until page is fully loaded
		chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		//Dropdown high to low
		Select dropProductSort = new Select (chromeDriver.findElement(By.tagName("select")));
		dropProductSort.selectByValue("za");
		
		//Assert: Dropdown has specific text showing
		//Assert.assertEquals(dropProductSort.getFirstSelectedOption().getText(), "Name (Z to A)");
		
		//Long screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(chromeDriver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_Sort_ZA.png"));
		}
	
	
	@Test(priority=1, description="Add random number of items to cart") 
	public void addToCartAndGoToCheckout () throws IOException {
		
		//Explicit wait until element jacket is visible and add it to cart
		WebDriverWait explicitWait = new WebDriverWait (chromeDriver, Duration.ofSeconds(15));
		explicitWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='add-to-cart-sauce-labs-fleece-jacket']"))).click();
		
		for (int i = 0; i < faker.number().numberBetween(1, 6); i++) {
			chromeDriver.findElement(By.xpath("//*[contains(text(), 'Add to cart')]")).click();
		}
									
		//Click on cart icon
		chromeDriver.findElement(By.xpath("//body/div[@id='root']/div[@id='page_wrapper']/div[@id='contents_wrapper']/div[@id='header_container']/div[1]/div[3]/a[1]")).click();
		
		//Screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(chromeDriver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_RandomItemsCart.png"));
		
		//click on continue shopping button
		chromeDriver.findElement(By.id("continue-shopping")).click();
		
	}
	
	
	@Test(priority=2, description="Delete items from cart")
	public void checkoutFinish() throws IOException, NoSuchElementException {
		
		//Click on cart button
		chromeDriver.findElement(By.id("shopping_cart_container")).click();
		
		//Assert: Cart is not empty
		//Assert.assertTrue(chromeDriver.findElement(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")).isDisplayed());
				
		//Delete items			
		Integer cartCounter = Integer.parseInt(chromeDriver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]/a[1]/span[1]")).getText());
		System.out.println(cartCounter);
		
		for (int i = 0; i < cartCounter; i++) {
			chromeDriver.findElement(By.xpath("//*[contains(text(), 'Remove')]")).click();
		}
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_CartItemsDeleted.png"));
		
		//click on continue shopping button
		chromeDriver.findElement(By.id("continue-shopping")).click();
					
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