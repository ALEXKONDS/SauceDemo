package saucedemo_pages.github;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;


public class Page_Main {
	
	//Finding Elements
	@FindBy(tagName = "select") WebElement dropProductSort;
	@FindBy(xpath = "//*[contains(text(), 'Add to cart')]") WebElement btnAddToCart;
	@FindBy(xpath = "//body/div[@id='root']/div[@id='page_wrapper']/div[@id='contents_wrapper']/div[@id='header_container']/div[1]/div[3]/a[1]") WebElement btnCart;
	
	//Constructor
	public Page_Main(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	
	//Actions
	public void sort_dropProductSort(String sort) {
		Select dropDown = new Select(dropProductSort);
		dropDown.selectByValue(sort);
	}
	
	public void click_AddToCart() {
		btnAddToCart.click();
	}

	public void click_CartIcon() {
		btnCart.click();
	}
	

}