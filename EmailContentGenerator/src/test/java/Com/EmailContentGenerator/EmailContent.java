package Com.EmailContentGenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class EmailContent {

	
	
 WebDriver driver;
	 
	 //before execution of the test this method will call 
     @BeforeTest
     public void setup() {
    	fileNames();
		driver =launchChrome();
	 }
	
     private boolean testFailed = false;
	 private void logAndSkipTest(ExtentTest report, Exception e) {
	        report.log(Status.SKIP, "Test skipped due to" + e.getMessage());
	        throw new SkipException("Failed " + e.getMessage(), e);
	 }
     
    
	 @DataProvider(name="a")
	 public Object[][] getdata() throws EncryptedDocumentException, IOException
	  {
		Object data[][] = getExcelData1(sheetName);
		return data;	
	  }
     
     
     
     @Test(priority=1)
     public void loginPage() throws Exception {
     ExtentTest report= extent.createTest("login").assignAuthor("Ankit").assignCategory("Regression Testing").assignDevice("Chrome");
  	 try {
  	      if (driver != null ) {
  	      testFailed=login(testFailed); 
  	    if (testFailed) {
  	    	 report.log(Status.FAIL,"Login Failed");
  	    }
  	      report.addScreenCaptureFromPath(capturescreenshot(driver));
          }
    }catch(Exception b) {
    	  System.out.print("Login Failed "+b);
  		  testFailed = true;
   	      report.log(Status.FAIL,b);
		  report.addScreenCaptureFromPath(capturescreenshot(driver));
		  
	      throw b;  
    }  }
     
	
	
	
     
     @Test(dataProvider="a",priority=2)
     public void generator(String to,String cc, String name, String subject, String content,String Comments) throws InterruptedException, IOException {
    	 try { 
    	 if (testFailed) {
 		    throw new SkipException(" Log in Failed ");
          }else {  
         	// System.out.print(to+cc+name+subject+content);
         	 if(content != null && !content.trim().isEmpty())
         	 {
         	  ExtentTest report= extent.createTest("Rephrasing contents ").assignAuthor("Ankit").assignCategory("Regression Testing").assignDevice("Chrome");
             	
 	      	   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));  
 	   	       WebElement ele = driver.findElement(By.xpath("//div[@id='paraphraser-input-box']"));
 	   	  	   Actions actions = new Actions(driver);
 	   	  	   actions.moveToElement(ele).click().perform(); // Click to focus on the input element
 	   	  	   actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform(); // Select all text
 	   	  	   actions.sendKeys(Keys.BACK_SPACE).perform(); // Delete selected text
 	   	  	   ele.sendKeys(content);
 	               
 	          
 	     try {
 	    	   driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));  
 	    	   driver.findElement(By.xpath("//div[@class='MuiBox-root css-1bvc4cc']//button[contains(@class, 'MuiIconButton-root') and @type='button']")).click();
 	     }catch (org.openqa.selenium.NoSuchElementException e) {
 	           driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));  
	               WebElement rephraseButton = driver.findElement(By.xpath("//button[@data-testid='pphr/input_footer/paraphrase_button']"));
	               rephraseButton.click();   
          }

 	       
 	     try {
 	    	 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  
 	         WebElement popupCloseButton = driver.findElement(By.xpath("(//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeLarge css-30h9jf'])[1]"));
 	         Thread.sleep(1000);
 	    	 popupCloseButton.click();
 	     }catch (org.openqa.selenium.NoSuchElementException e) {	   
 	     }
 	      
 	       driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));  
	       driver.findElement(By.xpath("//button[@aria-label='Copy Full Text']")).click();   	    	
	    	
	    	   List<String> textContents = processInputData(driver,report); 
	    	   Thread.sleep(1000);
	    	  
         	 
	    	   writeTextContents(sheetName,textContents);
	    	   
         	}
         	 }
          } catch (SkipException e) {
  			logAndSkipTest(report, e); } 
       	 catch(Exception b) {
       		 testFailed = true;
    		    System.out.print(""+b);
    		    report.log(Status.FAIL,b);
    		    report.addScreenCaptureFromPath(capturescreenshot(driver));
    	        throw b;  }
           }             
    	 
     
     @Test(priority=3)
      public void replaceName() throws IOException {
      ExtentTest report= extent.createTest("Replacing names ").assignAuthor("Ankit").assignCategory("Regression Testing").assignDevice("Chrome");
    	 try {
    	  if (testFailed) {
   		    throw new SkipException(" Log in Failed ");
            }else { 
    	    replaceAndWriteContents(sheetName,report);
      } } catch (SkipException e) {
			logAndSkipTest(report, e); } 
    	 catch(Exception b) {
 		    System.out.print(""+b);
 		    report.log(Status.FAIL,b);
 		    report.addScreenCaptureFromPath(capturescreenshot(driver));
 	        throw b;  }
        }    
    
     
     
     
     
     
     
     
     
 	
     @AfterTest
   	public void close() throws InterruptedException
   	{
   		Thread.sleep(5000);
   		driver.quit();
   	}
     
	
     public WebDriver launchChrome()  {
      	  try {
      	  	    WebDriverManager.chromedriver().setup();
      	        ChromeOptions options = new ChromeOptions();
      	        options.addArguments("--headless");
      	        options.addArguments("--disable-popup-blocking");
      	        options.addArguments("--disable-gpu");
      	        options.addArguments("--window-size=1920,1080");
      	        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");

      	        options.setExperimentalOption("excludeSwitches", new String[] {"disable-popup-blocking"});      
      	        driver = new ChromeDriver(options);
      	        
                  driver.manage().window().maximize(); 
                  driver.get("https://quillbot.com/login");
      	        return driver;	    
      	      } catch (WebDriverException e) {
      	            System.err.println("An error occurred while initializing the WebDriver: " + e.getMessage());          
      	      }
      			return null;
      	    }
        
        public static ExtentReports extent = new ExtentReports();
    	public static ExtentTest report;
    	ExtentSparkReporter spark = new ExtentSparkReporter("target/Report.html");
    	
    	
    	
    	@BeforeSuite
    	public void report() {
    		spark.config().setTheme(Theme.DARK);
    		spark.config().setDocumentTitle("Test Result");
    		extent.attachReporter(spark);
    	
    	}
    	
    	public static String capturescreenshot(WebDriver driver) throws IOException {
    		TakesScreenshot screenshotTaker = (TakesScreenshot) driver;
            java.io.File screenshot = screenshotTaker.getScreenshotAs(OutputType.FILE); 
            String projectLocation = System.getProperty("user.dir");
         	System.out.print(projectLocation);
         	String screenshotPath =projectLocation+ "\\images\\"+System.currentTimeMillis()+".png";
            FileUtils.copyFile(screenshot, new java.io.File(screenshotPath));      
            return screenshotPath;
    	}
    	
    	@AfterTest
     	public void teardown()
     	{
     		extent.flush();
     	}
    	
    	 public boolean login(boolean testFailed) {
    	 		try { 
    	 			
    	 			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    	 	        WebElement username = wait.until(
    	 	            ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='username']")));
    	 	        username.click();
    	 	        username.sendKeys("ankit.k@hashstudioz.com");
    	 		    	 
    	 		    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20)); 
    	 		    WebElement password = driver.findElement(By.xpath("//input[@name='password']"));   
    	 		    password.click();
    	 		    password.sendKeys("Rephrase@12345");
    	 		 
    	 		    WebElement login = driver.findElement(By.xpath("//span[normalize-space()='Log in']"));
    	 		    login.click();
    	 		    
    	 		    	 
    	 		    WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
    	 	        wait2.until(ExpectedConditions.invisibilityOfElementLocated(By.id("overlay")));
    	 		    WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
    	 		    WebElement navigate = wait1.until(
    	 		        ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='MuiListItemButton-root MuiListItemButton-gutters MuiButtonBase-root css-1uwabd6']//a[@href='/paraphrasing-tool?referrer=side_navbar']")));
    	 		    navigate.click();
    	 		    System.out.print("logged in");
    	 		    System.out.print("\nnavigated to text screen");	
    	 		    try {
    	 		    	 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));  
    	 	             WebElement popupCloseButton = driver.findElement(By.xpath("(//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeLarge css-30h9jf'])[1]"));
    	 	             popupCloseButton.click();
    	 		    }catch (org.openqa.selenium.NoSuchElementException e) {	   
    	 		    }
    	 	    
    	 		
    	 	}catch(Exception e )  {     		    	   	  
    	 		testFailed=true;	
    	     }
    	 	return testFailed;  }
    	 
    	 
    	 
    	 
    	 	
    	 String inputFilepath=null;
    	 String sheetName=null;
    	 public void fileNames()  {
    	         Scanner scanner = new Scanner(System.in);
    	         System.out.println("\nPlease Enter file Filepath:");
    	         inputFilepath= scanner.nextLine();
    	         System.out.println("\nWhich Sheet do you want to work on?  Please Enter Sheet Name");
    	         sheetName= scanner.nextLine();

    	  }
    	 
    	 
    	
         
    	 private List<String> processInputData(WebDriver driver,ExtentTest report) throws InterruptedException {
    	 		List<String> textContents = new ArrayList<>();
    	 		try {
    	            
    	            // Locate the element by its data-testid attribute
    	        	Thread.sleep(2000);  
    	            WebElement currentSentenceCountElement = driver.findElement(By.xpath("//b[@data-testid='pphr/output_footer/current_sentence_count']"));
    	            WebElement totalSentenceCountElement = driver.findElement(By.xpath("//span[@data-testid='pphr/output_footer/total_sentence_count']"));
    	            
    	            // Retrieve the text content from the elements
    	            int currentSentenceCount = Integer.parseInt(currentSentenceCountElement.getText());
    	            int totalSentenceCount = Integer.parseInt(totalSentenceCountElement.getText());
    	            
    	            // Print the retrieved counts
    	            System.out.println("Current sentence count: " + currentSentenceCount);
    	            System.out.println("Total sentence count: " + totalSentenceCount);
    	 		 
    	     // Continue or exit based on the sentence count
    	     if (totalSentenceCount > 1) {    	                   
    	         for (int i=currentSentenceCount;i<=totalSentenceCount;i++) {	                    	
    	              WebElement next = driver.findElement(By.xpath("//button[contains(@aria-label,'Next Sentence')]"));
    	              next.click();
    	              Thread.sleep(1000);
    	              WebElement rephrase = driver.findElement(By.xpath("(//button[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root quillArticleBtn css-vsr8va'])[1]"));  
    	              rephrase.click();   
    	              driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));  
    	  	      	  driver.findElement(By.xpath("//button[@aria-label='Copy Full Text']"));
    	  	      	  Thread.sleep(500);
    	         	  WebElement sentenceContainer = driver.findElement(By.id("paraphraser-output-box"));   	                    	 
    	         	  String textContent = sentenceContainer.getText(); 
    	         	  textContents.add(textContent);
    	         	  System.out.print("\n"+textContent);
    	              report.log(Status.PASS,textContent);      }
    	     }
    	     
    	               driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));  
    		           WebElement ele = driver.findElement(By.xpath("//div[@id='paraphraser-input-box']"));
    		           Actions actions = new Actions(driver);
    		           actions.moveToElement(ele).click().perform(); // Click to focus on the input element
    		           actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).perform(); // Select all text
    		           actions.sendKeys(Keys.BACK_SPACE).perform(); 
    	     
    		           
    	   } catch (Exception e) {
    	       e.printStackTrace();
    	   }
    	     return textContents;          
    	   }
    	
    	 
    	 
    	
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 public String[][] getExcelData1(String sheetName) throws IOException {
    	        String[][] data = null;

    	        try (FileInputStream inputStream = new FileInputStream(inputFilepath);
    	             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

    	            XSSFSheet sheet = workbook.getSheet(sheetName);

    	            if (sheet != null) {
    	                int totalRows = sheet.getLastRowNum();  // Exclude header row
    	                int totalCols = sheet.getRow(0).getLastCellNum();
    	                DataFormatter formatter = new DataFormatter();
    	                data = new String[totalRows][totalCols];

    	                for (int i = 1; i <= totalRows; i++) {  // Start from 1 to skip the header
    	                    XSSFRow row = sheet.getRow(i);
    	                    if (row != null) {
    	                        for (int j = 0; j < totalCols; j++) {
    	                            XSSFCell cell = row.getCell(j);
    	                            if (cell != null) {
    	                                data[i - 1][j] = formatter.formatCellValue(cell);  // Adjust index for data array
    	                            } else {
    	                                data[i - 1][j] = "";  // Handle null cells
    	                            }
    	                        }
    	                    } else {
    	                        // Handle null rows if needed
    	                        for (int j = 0; j < totalCols; j++) {
    	                            data[i - 1][j] = "";  // Initialize null rows to empty strings
    	                        }
    	                    }
    	                }
    	            } else {
    	                System.out.println("Sheet not found in the Excel file.");
    	            }
    	        }

    	        return data;
    	    }

    	    public void writeTextContents(String sheetName, List<String> textContents) throws IOException {
    	        try (FileInputStream inputStream = new FileInputStream(inputFilepath);
    	             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    	             FileOutputStream outputStream = new FileOutputStream(inputFilepath)) {

    	            XSSFSheet sheet = workbook.getSheet(sheetName);

    	            if (sheet != null) {
    	            	 System.out.println("\n\nWriting Rephrases in Sheet");
    	                int rowNum = 2;  // Start from H3, which is the third row (index 2)
    	                int textIndex = 0;
    	                boolean stopWriting = false;

    	                while (!stopWriting) {
    	                    XSSFRow row = sheet.getRow(rowNum);
    	                    if (row == null) {
    	                        row = sheet.createRow(rowNum);
    	                    }

    	                    // Check if column A (index 0) is empty
    	                    XSSFCell cellA = row.getCell(0);
    	                    if (cellA == null || cellA.getCellType() == CellType.BLANK) {
    	                        stopWriting = true;
    	                        continue;
    	                    }

    	                    // Write data to column H (index 7)
    	                    XSSFCell cellH = row.getCell(4);
    	                    if (cellH == null) {
    	                        cellH = row.createCell(4);
    	                    }
    	                    cellH.setCellValue(textContents.get(textIndex));
    	                    textIndex = (textIndex + 1) % textContents.size();  // Cycle through the list
    	                    rowNum++;
    	                }
    	            } else {
    	                System.out.println("Sheet not found in the Excel file.");
    	            }

    	            workbook.write(outputStream);
    	        }
    	    }
    	 
    	 
    	 
    	 
    	    public void replaceAndWriteContents(String sheetName, ExtentTest report1) throws IOException {
    	        try (FileInputStream inputStream = new FileInputStream(inputFilepath);
    	             XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
    	             FileOutputStream outputStream = new FileOutputStream(inputFilepath)) {

    	            XSSFSheet sheet = workbook.getSheet(sheetName);

    	            if (sheet != null) {
    	            	System.out.print("\n\nReplacing Names in sheet");
    	            	report1.log(Status.PASS, "Replacing Names Successfully");
    	                int totalRows = sheet.getLastRowNum();

    	                for (int rowNum = 0; rowNum <= totalRows; rowNum++) {
    	                    XSSFRow row = sheet.getRow(rowNum);
    	                    if (row != null) {
    	                        XSSFCell cellC = row.getCell(2); // Column C (index 2)
    	                        XSSFCell cellE = row.getCell(4); // Column E (index 4)

    	                        if (cellC != null && cellE != null) {
    	                            String name = cellC.getStringCellValue();
    	                            String content = cellE.getStringCellValue();
    	                            String updatedContent = content.replace("(First_Name)", name).replace("(First_name)", name);

    	                            // Write updated content back to column E (index 4)
    	                            cellE.setCellValue(updatedContent);
    	                        }
    	                    }
    	                }
    	            } else {
    	                System.out.println("Sheet not found in the Excel file.");
    	            }

    	            workbook.write(outputStream);
    	        }
    	    }
    	 
    	 
    	 
    	 
}
