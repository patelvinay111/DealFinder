package DealAnalyzer;

import model.deal.Deal;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import constants.DealConstants;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.DealUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Vinay Patel
 */
public class NewDealAnalyzer {

    private static final Logger LOGGER = Logger.getLogger(NewDealAnalyzer.class.getName());

    public static void main(String[] args){
        //Get the leads from the DB
        Map<String, Double> leads = new HashMap<>();

        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dealfinder", "postgres", "l");
            c.setAutoCommit(true);

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT address, asking_price " +
                    "FROM leads " +
                    "WHERE address " +
                    "NOT IN (SELECT property_name FROM deals) ");
            while (rs.next()) {
                String address = rs.getString("address");
                double price = rs.getDouble("asking_price");
                leads.put(address, price);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(1);
        }


        WebDriver driver = new FirefoxDriver();

        for (String address : leads.keySet()) {
            LOGGER.info("Analyzing... " + address);
            String searchUrl = DealConstants.BASE_URL + DealUtil.urlifyAddress(address);

            System.out.print(searchUrl);

            driver.get(searchUrl);
            waitForLoad(driver);
            try {
                int waitTime = getRandomNumberInRange();
                System.out.println("Waiting for..." + waitTime/1000.0 + " seconds");
                Thread.sleep(waitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int rent = 0;
            try{
                rent = Integer.parseInt(driver.findElement(By.xpath(DealConstants.RENT_XPATH)).getText().replaceAll("[^\\d.]", ""));
            } catch (Exception e) {
                LOGGER.info("Unable to parse the rent info for " + address);
            }

            if (rent > 0) {
                //Analyse the deal
                double askingPrice = leads.get(address);
                Deal deal = DealUtil.createDeal(address, askingPrice, rent);

                String insertStmt = "INSERT INTO " +
                        "deals(property_name, rent_to_price, cash_flow, min_equity_earn, asking_price, rent, vacancy_cost, property_tax, property_managent, leasing_fee, " +
                        "insurance, maintenance_cost, capital_reserve, mortgage_payment, noi)" +
                        "VALUES " +
                        "(" +
                        String.format("'%s', '%f', '%f', %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f", deal.getName(),
                                deal.getRentToPrice(), deal.getCashFlow(), deal.getMinEquityEarn(), deal.getPropertyPrice(),
                                deal.getRent(), deal.getVacancyCost(), deal.getPropertyTax(), deal.getPropertyManagement(),
                                deal.getLeasingFee(), deal.getInsurance(), deal.getMaintenanceCost(), deal.getCapitalReserve(),
                                deal.getMortgageCost(), deal.getNoi()) +
                        ") " +
                        "ON conflict (property_name) DO " +
                        "UPDATE " +
                        "SET rent_to_price = excluded.rent_to_price, " +
                        "min_equity_earn = excluded.min_equity_earn, " +
                        "cash_flow = excluded.cash_flow, " +
                        "asking_price = excluded.asking_price, " +
                        "rent = excluded.rent, " +
                        "vacancy_cost = excluded.vacancy_cost, " +
                        "property_tax = excluded.property_tax, " +
                        "property_managent = excluded.property_managent, " +
                        "leasing_fee = excluded.leasing_fee, " +
                        "insurance = excluded.insurance, " +
                        "maintenance_cost = excluded.maintenance_cost, " +
                        "capital_reserve = excluded.capital_reserve, " +
                        "mortgage_payment = excluded.mortgage_payment, " +
                        "noi = excluded.noi;";
                try {
                    stmt.executeUpdate(insertStmt);
                } catch (Exception e) {
                    LOGGER.info("Failed to execute: " + insertStmt);
                }
            }
        }

        driver.quit();

        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM daily_hunt;");
            while (rs.next()) {
                String address = rs.getString("property_name");
                String rent_price = new DecimalFormat("#.#").format(rs.getDouble("rent_to_price"));;
                String cashflow = new DecimalFormat("#.#").format(rs.getDouble("cash_flow"));
                String url = rs.getString("url");
                System.out.format("%32s%32s%32s%32s", address, rent_price, cashflow, url);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            stmt.close();
            c.commit();
        } catch (Exception e) {
            LOGGER.info("Failed to close connection to DB");
        }

    }

    private static void waitForLoad(WebDriver driver) {
        ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(pageLoadCondition);
    }

    private static int getRandomNumberInRange() {
        Random r = new Random();
        return r.nextInt((DealConstants.MAX_WAIT - DealConstants.MIN_WAIT) + 1) + DealConstants.MIN_WAIT;
    }

}
