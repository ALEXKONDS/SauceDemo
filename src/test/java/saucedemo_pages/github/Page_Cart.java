package saucedemo_pages.github;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Page_Cart {
	
	//Finding Elements
		@FindBy(id = "continue-shopping") WebElement btnContinueShopping;
		@FindBy(xpath = "//*[contains(text(), 'Remove')]") WebElement btnRemoveItem;
		
		
		//Constructor
		public Page_Cart(WebDriver driver) {
			PageFactory.initElements(driver, this);
		}
		
		
		//Actions
		public void click_ContinueShopping() {
			btnContinueShopping.click();
		}
		
		public void click_RemoveItem() {
			btnRemoveItem.click();
		}

}
