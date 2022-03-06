package constants;

/**
 * @author Vinay Patel
 */
public class DealConstants {
    //API Constant
    //public static String API_KEY = "X1-ZWz17p92hlu9l7_9y702";

    //Generic Constants
    public static String DELIM = "|";

    //Cash Flow Variables
    public static double VACANCY_FACTOR = 0.075; //7.5% Vacancy
    public static double DEFAULT_PROPERTY_TAX = 0.0116; //1.16% of the Property Price
    public static double PROPERTY_MANAGEMENT_COST = 0.08; //8% of Rent
    public static double LEASING_FEE = 0.025; //2.5% of Rent
    public static double MAINTENANCE_COST = 0.04; //4% of Rent
    public static double CAPITAL_RESERVE = 0.05; //5% of Rent

    //Mortgage Variables
    public static double DOWNPAYMENT = 0.20; //20% Down Payment
    public static double INTEREST_RATE = 0.05; //5% Interest

    //Crawl Constants
    public static String BASE_URL = "https://www.zillow.com/rental-manager/price-my-rental/results/";
    public static int MIN_WAIT = 6000;
    public static int MAX_WAIT = 15000;
    public static String RENT_XPATH = "//*[@id=\"app-root\"]/div/div[3]/main/div/div[2]/div/h2";
    public static String MIN_RENT_XPATH = "//*[@id=\"app-root\"]/div/div[3]/main/div/div[2]/h4/strong";
}
