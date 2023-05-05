package saucedemo_pages.github;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.github.javafaker.Faker;


public class Page_Login {
	
	//Finding Elements
	@FindBy(id = "user-name") WebElement txtUsername;
	@FindBy(id = "password") WebElement txtPassword;
	@FindBy(id = "login-button") WebElement btnLogin;
	Faker faker = new Faker();
	
	
	//Constructor
	public Page_Login(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	
	//Actions
	public void login_Username(String username) {
		txtUsername.sendKeys(username);
	}
	
	public void login_Password(String password) {
		txtPassword.sendKeys(password);
	}
	
	public void login_btnLogin_click() {
		btnLogin.click();
	}
	
	
}


