package github.SauceDemo;

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
import com.github.javafaker.Faker;


public class Login_Chrome {
	
	String url = "https://www.saucedemo.com/";;
	String evidenceFolder = "..\\SauceDemo\\evidenceFolder\\LogIn\\Chrome\\";
	File screenshotEvidence;
	Faker faker = new Faker();
	WebDriver chromeDriver;

	
	@BeforeSuite(description="driver setup")
	public void driver_Setup() {
		
		System.out.println("====================");
		System.out.println("Testing suite start!");
		System.out.println("====================");
		chromeDriver = new ChromeDriver();
			
	}
	
	
	@BeforeTest(description="browser setup") 
	public void browser_Setup() {
		
		chromeDriver.get(url);
		chromeDriver.manage().deleteAllCookies();
		chromeDriver.manage().window().maximize();
		chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
				
	}
	
	
	@Test(description="Login with correct credentials") 
	public void login_Firefox_Credentials_Ok() throws IOException {
				
		//Switch for randomly picking one of the four available usernames
		switch (faker.number().numberBetween(0, 2)) {
			case 0: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("standard_user");;
				break;
			}
			case 1: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("problem_user");;
				break;
			}
			case 2: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("performance_glitch_user");;
				break;
			}
		}
		
		//Assert: Username field is not empty
		Assert.assertNotNull(chromeDriver.findElement(By.id("user-name")).getText());
		
		//Password field
		chromeDriver.findElement(By.id("password")).sendKeys("secret_sauce");

		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_Ok_1.png"));
		
		//Click on login button
		chromeDriver.findElement(By.id("login-button")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_Ok_2.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(description="Login with lockout user")
	public void login_Firefox_Credentials_Lockout() throws IOException {
		
		chromeDriver.findElement(By.id("user-name")).sendKeys("locked_out_user");
		chromeDriver.findElement(By.id("password")).sendKeys("secret_sauce");
		
		//Click on login button
		chromeDriver.findElement(By.id("login-button")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_Lockout.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(description="Login with wrong credentials: Username")
	public void login_Firefox_Credentials_Username_Wrong() throws IOException {
		
		//Random username
		chromeDriver.findElement(By.id("user-name")).sendKeys(faker.name().username());
		//Correct password
		chromeDriver.findElement(By.id("password")).sendKeys("secret_sauce");
		
		//Click on login button
		chromeDriver.findElement(By.id("login-button")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_Wrong_Username.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}
	
	
	@Test(description="Login with wrong credentials: Password")
	public void login_Firefox_Credentials_Password_Wrong() throws IOException {

		//Switch for randomly picking one of the four available usernames
		switch (faker.number().numberBetween(1, 4)) {
			case 1: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("standard_user");
				break;
			}
			case 2: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("locked_out_user");
				break;
			}
			case 3: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("problem_user");
				break;
			}
			case 4: {
				chromeDriver.findElement(By.id("user-name")).sendKeys("performance_glitch_user");
				break;
			}
		}
			
		//Random password
		chromeDriver.findElement(By.id("password")).sendKeys(faker.internet().password());
		
		//Click on login button
		chromeDriver.findElement(By.id("login-button")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_Wrong_Password.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}

	
	@Test(description="Login without credentials")
	public void login_Firefox_Credentials_None() throws IOException {
		
		//Click on login button
		chromeDriver.findElement(By.id("login-button")).click();
		
		//Screenshot evidence
		screenshotEvidence = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshotEvidence, new File(evidenceFolder + "evidence_Login_Firefox_Credentials_None.png"));
		
		//Assert: Page is different from the login one
		Assert.assertNotEquals(chromeDriver.getCurrentUrl(), "https://www.saucedemo.com/");
		
	}

	
	@AfterMethod
	public void cleanBrowser() {
		chromeDriver.get(url);
	}
	
	
	@AfterTest (alwaysRun=true, description="Close browser")
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
