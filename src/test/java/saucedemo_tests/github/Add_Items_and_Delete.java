package saucedemo_tests.github;

import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.javafaker.Faker;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import saucedemo_pages.github.Page_Cart;
import saucedemo_pages.github.Page_Login;
import saucedemo_pages.github.Page_Main;


public class Add_Items_and_Delete {

	//Global variables
	String url = "https://www.saucedemo.com/";
	String evidenceFolder = "..\\github\\saucedemo_tests\\makeorderanddelete\\evidence\\";
	File screenshotEvidence;
	Faker faker = new Faker();
	WebDriver driver;
			
			
	@BeforeSuite(description="driver setup")
	public void setUp() {
		
		System.out.println("====================");
		System.out.println("Testing suite start!");
		System.out.println("====================");
		driver = new ChromeDriver();
			
	}
	
	
	@BeforeTest(description="browser setup") 
	public void browser_Setup() {
		
		driver.get(url);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
						
	}	
	
	
	@BeforeTest(description="browser setup and login")
	public void logIn() throws IOException, InvalidFormatException, InterruptedException {
		
		Page_Login login = new Page_Login(driver);
				
		//Switch for randomly picking one of the four available usernames
		switch (faker.number().numberBetween(0, 2)) {
			case 0: {
				login.login_Username("standard_user");
				break;
			}
			case 1: {
				login.login_Username("problem_user");;
				break;
			}
			case 2: {
				login.login_Username("performance_glitch_user");;
				break;
			}
		}
		
		login.login_Password("secret_sauce");
		
		//Assert: Username field is not empty
		Assert.assertNotNull(driver.findElement(By.id("user-name")).getText());
		
		login.login_btnLogin_click();
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(priority=0, description="Sort products randomly") 
	public void productSortZA() throws IOException {
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		Page_Main pageMain = new Page_Main(driver);

		//Switch for randomly picking one of the four available usernames
		switch (faker.number().numberBetween(0, 3)) {
			case 0: {
				pageMain.sort_dropProductSort("az");
				break;
			}
			case 1: {
				pageMain.sort_dropProductSort("za");
				break;
			}
			case 2: {
				pageMain.sort_dropProductSort("lohi");
				break;
			}
			case 3: {
				pageMain.sort_dropProductSort("hilo");
				break;
			}
		}		
		
		//Assert: Dropdown has specific text showing
		//Assert.assertEquals(dropProductSort.getFirstSelectedOption().getText(), "Name (Z to A)");
		
		//Long screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_Sort_Random.png"));
		
		}
	
	
	@Test(priority=1, description="Add random number of items to cart") 
	public void addToCartAndGoToCheckout () throws IOException {
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		Page_Main pageMain = new Page_Main(driver);
		Page_Cart pageCart = new Page_Cart(driver);
		
		for (int i = 0; i < faker.number().numberBetween(1, 6); i++) {
			pageMain.click_AddToCart();
		}
		
		pageMain.click_CartIcon();
			
		//Screenshot evidence
		Screenshot screenshotLong = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(500)).takeScreenshot(driver);
		ImageIO.write(screenshotLong.getImage(),"PNG", new File(evidenceFolder + "evidence_Cart_Add_Items.png"));
		
		pageCart.click_ContinueShopping();
		
	}
	
	
	@Test(priority=2, description="Delete items from cart")
	public void checkoutFinish() throws IOException, NoSuchElementException {
		
		//Assert: Cart is not empty
		//Assert.assertFalse(driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]/a[1]/span[1]")).isDisplayed());
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		Page_Main pageMain = new Page_Main(driver);
		Page_Cart pageCart = new Page_Cart(driver);
		
		pageMain.click_CartIcon();
		
		//Delete items based on number on cart icon		
		Integer cartCounter = Integer.parseInt(driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]/a[1]/span[1]")).getText());
				
		for (int i = 0; i < cartCounter; i++) {
			pageCart.click_RemoveItem();
		}
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_CartItemsDeleted.png"));
		
		//click on continue shopping button
		pageCart.click_ContinueShopping();
					
	}
	
	
	@AfterTest (alwaysRun = true, description="Close browser")
	public void closeBrowser() {
	
		driver.quit();
	
	}
	
	
	@AfterSuite (description="Show message in console")
	public void showConsoleMessage() {
	
		System.out.println("===============================================");
		System.out.println("Test suite end!");
		System.out.println("Evidence screenshots at " + evidenceFolder);
		System.out.println("===============================================");
	
	}

	
}