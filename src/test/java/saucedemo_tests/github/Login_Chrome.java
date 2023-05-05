package saucedemo_tests.github;

import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import java.time.Duration;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.github.javafaker.Faker;

import saucedemo_pages.github.Page_Login;


public class Login_Chrome {
	
	String url = "https://www.saucedemo.com/";;
	String evidenceFolder = "..\\github\\saucedemo_tests\\login\\evidence_chrome\\";
	File screenshotEvidence;
	Faker faker = new Faker();
	WebDriver driver;

	
	@BeforeSuite(description="driver setup")
	public void driver_Setup() {
		
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
	
	
	@Test(description="Login with correct credentials") 
	public void login_Credentials_Ok() throws IOException, InvalidFormatException, InterruptedException {
		
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
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_Ok_1.png"));
		
		login.login_btnLogin_click();
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_Ok_2.png"));
		
	}
	
	
	@Test(description="Login with lockout user")
	public void login_Credentials_Lockout() throws IOException {
		
		Page_Login login = new Page_Login(driver);
				
		login.login_Username("locked_out_user");
		login.login_Password("secret_sauce");
		login.login_btnLogin_click();
				
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_Lockout.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(description="Login with wrong credentials: Username")
	public void login_Credentials_Username_Wrong() throws IOException {
		
		Page_Login login = new Page_Login(driver);
		
		//Random username
		login.login_Username(faker.name().username());
		
		login.login_Password("secret_sauce");
		login.login_btnLogin_click();
				
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_Wrong_Username.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(description="Login with wrong credentials: Password")
	public void login_Credentials_Password_Wrong() throws IOException {

		Page_Login login = new Page_Login(driver);
		
		//Switch for randomly picking one of the three available usernames
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
		
		login.login_Password(faker.internet().password());		
		login.login_btnLogin_click();
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_Wrong_Password.png"));
		
	}
	
	
	@Test(description="Login without credentials")
	public void login_Credentials_None() throws IOException {
		
		Page_Login login = new Page_Login(driver);
		
		login.login_btnLogin_click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Credentials_None.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(driver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}

	
	@AfterMethod
	public void cleanBrowser() {
		driver.get(url);
	}
	
	
	@AfterTest (alwaysRun=true, description="Close browser")
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
