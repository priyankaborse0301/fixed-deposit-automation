package com.test.deposit;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FixedDepositTest {

	WebDriver driver;
	public String driverPath = "D:\\tools\\chromedriver\\chromedriver.exe";
	public String testUrl = "https://kotakcherry.com/";

	@BeforeTest
	public void setup() {
		System.setProperty("webdriver.chrome.driver", driverPath);
		driver = new ChromeDriver();
		driver.get(testUrl);
	}

	@Test(dataProvider = "duration")
	public void testFixedDeposit(String years, String months, String days) {

		try {

			Thread.sleep(5000);

			WebElement depositMenuWebElement = driver.findElement(By.xpath("(//div[contains(., ' Deposits ')])[14]"));
			depositMenuWebElement.click();
			Thread.sleep(5000);

			WebElement fixedDepositMenuWebElement = driver.findElement(By.xpath("//h2[contains(., 'Fixed deposit')]"));
			fixedDepositMenuWebElement.click();

			ExcelReader excelReader = new ExcelReader();

			excelReader.openExcelFile("C:\\Users\\Bhushan\\eclipse-workspace\\fixed-deposit\\src\\test\\java\\com\\test\\deposit\\data\\deposit_value.xlsx");
			excelReader.openExcelSheet("Sheet1");

			int rowCount = excelReader.getRowCountInSheet();

			Thread.sleep(3000);

			for (int i = 0; i < rowCount; i++) {
				int depositVal = (int) excelReader.getCellData(i + 1, 0);
				System.out.println("Enter amount - " + depositVal);

				WebElement amountDiv = driver.findElement(By.xpath("//div[@class='ieco-blue-underline']"));
				amountDiv.click();
				Thread.sleep(1000);

				WebElement amountInput = driver.findElement(By.name("investedAmt"));
				amountInput.clear();
				amountInput.sendKeys("" + depositVal);
				Thread.sleep(2000);

				boolean isfailure = false;
				try {
					WebElement errorMsg = driver
							.findElement(By.xpath("//div[@class='min-max-error ng-star-inserted']"));
					errorMsg.click();
					isfailure = true;

					if (isfailure == true) {
						// screenshot
						System.out.println("Validation Error !!! ");
						TakesScreenshot ts = (TakesScreenshot) driver;
						File file = ts.getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(file, new File("./screenshot/error_" + depositVal + ".png"));
					}
				} catch (Exception e) {
					
				}

				if (isfailure == false) {
					Thread.sleep(3000);
					WebElement durationDiv = driver.findElement(By
							.xpath("(//img[@src='https://cdn.kotakcherry.com/assets/images/ic_expand_more.svg'])[1]"));
					durationDiv.click();
					Thread.sleep(1000);

					WebElement durationYear = driver.findElement(By.name("dipositDurationYear"));
					durationYear.clear();
					durationYear.sendKeys(years);
					Thread.sleep(500);

					WebElement durationMonth = driver.findElement(By.name("dipositDurationMonth"));
					durationMonth.clear();
					durationMonth.sendKeys(months);
					Thread.sleep(500);

					WebElement durationday = driver.findElement(By.name("dipositDurationDay"));
					durationday.clear();
					durationday.sendKeys(days);
					Thread.sleep(500);

					WebElement sampleElement = driver.findElement(By.xpath("(//div[@class='ieco-label-text'])[1]"));
					sampleElement.click();
					Thread.sleep(2000);

					WebElement doneButton = driver.findElement(By.xpath(
							"//button[@class='mat-focus-indicator ieco-w-100 ieco-footnext-button mat-raised-button mat-button-base mat-primary ng-star-inserted']"));
					doneButton.click();

					Thread.sleep(3000);

					WebElement interestAmt = driver.findElement(By.xpath("//div[@class='ieco-maturity-price-val']"));
					String result = interestAmt.getText();
					String ExpectedResult = getCalculatedInterest(depositVal, years, months, days);

					Assert.assertEquals(result, ExpectedResult);

				}

			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@DataProvider(name = "duration")
	public Object[][] getDataFromDataprovider() {
		return new Object[][] { { "0", "0", "7" }, 
			{ "0", "0", "15" }, 
			{ "0", "0", "7" }, 
			{ "0", "0", "15" },
				{ "0", "1", "0" }, 
				{ "0", "3", "0" }, 
				{ "0", "6", "0" }, 
				{ "1", "0", "0" }, 
				{ "2", "0", "0" },
				{ "5", "0", "0" }, 
				{ "10", "0", "0" }, 
				{ "10", "6", "0" } };

	}

	public String getCalculatedInterest(int depositAmt, String year, String month, String days) {
		// Sample calculation..

		return "1000";
	}

}
