package LeadFinder;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author vinay.patel@amobee.com
 */
public class LeadFinder {

    public static void main(String[] args){
        //System.setProperty("webdriver.chrome.driver","/Users/vinaypatel/deal-finder/chromedriver");
        //System.setProperty("webdriver.gecko.driver", "/Users/vinaypatel/deal-finder/Firefox.app");
        WebDriver driver = new FirefoxDriver();

        String[] areas = new String[]{
                "Dallas, TX"
                /*"Houston, TX",
                "San Antonio, TX",
                "Dallas, TX",
                "Fort Worth, TX",
                "El Paso, TX",
                "Memphis, TN",
                "Oklahoma City, OK",
                "Atlanta, GA",
                "Arlington, TX",
                "Corpus Christi, TX",
                "Lubbock, TX",
                "Irving, TX",
                "Birmingham, AL",
                "Amarillo, TX",
                "Montgomery, AL",
                "Huntsville, AL"*/
        };

        Connection conn;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dealfinder", "postgres", "l");
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }

        for(String area : areas) {
            System.out.println("Analyzing... " + area);
            String searchUrl = LeadConstants.BASE_URL + LeadConstants.urlifyCity(area) + LeadConstants.SEARCH_CRITERIA;
            System.out.println("Search URL: " + searchUrl);
            driver.get(searchUrl);
            waitForLoad(driver);
            try {
                int waitTime = getRandomNumberInRange();
                System.out.println("Waiting for..." + waitTime/1000.0 + " seconds");
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(driver.getPageSource());
            List<WebElement> allLeads = driver.findElements(By.xpath(LeadConstants.LEAD_DETAIL_CARD_XPATH));
            System.out.println("ItemList: " + allLeads.size());
            List<Lead> validLeads = getValidLeads(allLeads, area);
            System.out.println("ValidList: " + validLeads.size());

            for(Lead l : validLeads){
                String insertStmt = "INSERT INTO " +
                        "leads(address, street, city, state, zip, area, asking_price, property_type, bed, bath, construction, lot, url) " +
                        "VALUES " +
                        String.format("('%s' , '%s' , '%s' , '%s' , %d , '%s' , %f , '%s', %f , %f , %f , %f , '%s') ", l.getAddress(), l.getStreet(),
                                l.getCity(), l.getState(), l.getZip(), l.getArea(), l.getAsking_price(), l.getProperty_type(), l.getBed(), l.getBath(),
                                l.getConstruction(), l.getLot(), l.getUrl()) +
                        "ON conflict (address) DO " +
                        "UPDATE " +
                        "SET street = excluded.street, " +
                        "city = excluded.city, " +
                        "state = excluded.state, " +
                        "zip = excluded.zip, " +
                        "area = excluded.area, " +
                        "asking_price = excluded.asking_price, " +
                        "property_type = excluded.property_type, " +
                        "bed = excluded.bed, " +
                        "bath = excluded.bath, " +
                        "construction = excluded.construction, " +
                        "lot = excluded.lot, " +
                        "url = excluded.url;";
                try {
                    stmt.executeUpdate(insertStmt);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        driver.quit();
    }

    private static void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    private static int getRandomNumberInRange() {
        Random r = new Random();
        return r.nextInt((LeadConstants.MAX_WAIT - LeadConstants.MIN_WAIT) + 1) + LeadConstants.MIN_WAIT;
        //return 10000000;
    }

    private static List<Lead> getValidLeads(List<WebElement> allLeads, String area) {
        if(allLeads.size() < 10) {
            return new ArrayList<Lead>();
        }

        List<Lead> validLeads = new ArrayList<Lead>();

        for(WebElement card: allLeads){
            try{
                //Get Elements
                WebElement addressEle = card.findElement(By.xpath(".//div[@class='address ellipsis ']"));
                WebElement streetEle = card.findElement(By.xpath(".//span[@class='listing-street-address']"));
                WebElement cityEle = card.findElement(By.xpath(".//span[@class='listing-city']"));
                WebElement stateEle = card.findElement(By.xpath(".//span[@class='listing-region']"));
                WebElement zipEle = card.findElement(By.xpath(".//span[@class='listing-postal']"));
                WebElement priceEle = card.findElement(By.xpath(".//span[@class='data-price']"));
                WebElement propertyTypeEle = card.findElement(By.xpath(".//div[@class='property-type']"));
                WebElement bedEle = card.findElement(By.xpath(".//li[@data-label='property-meta-beds']"));
                WebElement bathEle = card.findElement(By.xpath(".//li[@data-label='property-meta-baths']"));
                WebElement constructionEle = card.findElement(By.xpath(".//li[@data-label='property-meta-sqft']"));
                WebElement lotEle = card.findElement(By.xpath(".//li[@data-label='property-meta-lotsize']"));
                String url = "https://www.realtor.com" + card.getAttribute("data-url");
                WebElement foreclosureEle = card.findElement(By.className("pre-card-wrap"));

                //Validate Elements
                Pattern p = Pattern.compile(LeadConstants.ADDRESS_REGEX);
                String address = addressEle.getText();
                boolean validAddress = p.matcher(address).find();
                boolean validZip = isParsableInt(cleanseNumericString(zipEle.getText()));
                boolean validPrice = isParsableDouble(cleanseNumericString(priceEle.getText()));
                boolean validBed = isParsableDouble(cleanseNumericString(bedEle.getText()));
                boolean validBath = isParsableDouble(cleanseNumericString(bathEle.getText()));
                boolean validConstruction = isParsableDouble(cleanseNumericString(constructionEle.getText()));
                boolean validLot = isParsableDouble(cleanseNumericString(lotEle.getText()));
                boolean isForeclosure = foreclosureEle.getText().contains("Foreclosure");

                //Process Data and build Lead
                if(validAddress && validZip && validPrice && validBed && validBath && validConstruction && validLot && !isForeclosure) {
                    String street = streetEle.getText();
                    String city = cityEle.getText();
                    String state = stateEle.getText();
                    int zip = Integer.parseInt(cleanseNumericString(zipEle.getText()));
                    double price = Double.parseDouble(cleanseNumericString(priceEle.getText()));
                    String propertyType = propertyTypeEle.getText();
                    double bed = Double.parseDouble(cleanseNumericString(bedEle.getText()));
                    double bath = Double.parseDouble(cleanseNumericString(bathEle.getText()));
                    double construction = Double.parseDouble(cleanseNumericString(constructionEle.getText()));
                    double lot = getLotSize(lotEle);

                    validLeads.add(new Lead(address, street, city, state, zip, area, price, propertyType, bed, bath, construction, lot, url));
                }
            }catch (Exception e) {
                continue;
            }
        }

        if(validLeads.size() < 10) {
            return new ArrayList<Lead>();
        } else {
            return validLeads;
        }
    }

    private static double getLotSize(WebElement lotInfo) {
        WebElement lotDataEle = lotInfo.findElement(By.className("data-value"));
        double lotData = Double.parseDouble(cleanseNumericString(lotDataEle.getText()));
        String lotLabel = lotInfo.findElement(By.className("lot-label")).getText();
        if (lotLabel.contains("acre")) {
            return lotData * 43560;
        } else {
            return lotData;
        }
    }

    private static boolean isParsableInt(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private static boolean isParsableDouble(String s) {
        try {
            double d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private static String cleanseNumericString(String s){
        return s.replaceAll("[^\\d.]", "");
    }
}
