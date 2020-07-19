package LeadFinder;

/**
 * @author vinay.patel@amobee.com
 */
public class LeadConstants {

    //Leads Constants
    public static String LEAD_DETAIL_CARD_XPATH = "//li[@class=\"component_property-card js-component_property-card js-quick-view \"]";
    public static String BASE_URL = "https://www.realtor.com/realestateandhomes-search/";
    public static String SEARCH_CRITERIA = "/beds-1/baths-1/type-single-family-home/price-80000-120000/sqft-500/lot-sqft-2000/pnd-hide/dom-30//radius-10/sby-8";
    public static String ADDRESS_REGEX = "^\\d{1,6}\\s([0-9A-z]+\\s?\\b){2,3},\\s([A-z]+\\s?\\b){1,5},\\s[A-Z]{2}\\s\\d{5}";
    public static String STREET_REGEX = "^\\d{1,6}\\s([0-9A-z]+\\s?\\b){2,3}";

    //Crawl Constants
    public static int MIN_WAIT = 21000;
    public static int MAX_WAIT = 31000;

    public static String urlifyCity(String city) {
        return city.replace(", ", "_").replace(" ", "-");
    }

}
