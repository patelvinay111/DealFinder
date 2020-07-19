package api.main;

import LeadFinder.LeadConstants;
import api.Response.Listing;
import api.Response.Response;
import javafx.util.Pair;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author vinay.patel@amobee.com
 */
public class Main {

    public static void main(String[] args){
        ArrayList<Pair> areas = new ArrayList<Pair>();
        areas.add(new Pair("Dallas", "TX"));
        areas.add(new Pair("Fort Worth", "TX"));
        areas.add(new Pair("Phoenix", "AZ"));
        areas.add(new Pair("Plano", "TX"));
        areas.add(new Pair("Atlanta", "GA"));
        areas.add(new Pair("St. Louis", "MO"));
        getListings(areas);
    }

    private static void getListings(ArrayList<Pair> areas) {
        for(Pair p : areas) {
            String url = "https://realtor.p.rapidapi.com/properties/list-for-sale?" +
                    "price_min=135000" +
                    "&price_max=225000" +
                    "&beds_min=3" +
                    "&baths_min=2" +
                    "&sqft_min=1000" +
                    "&lot_sqft_min=3000" +
                    "&radius=20" +
                    "&prop_type=single_family" +
                    "&sort=relevance" +
                    "&is_pending=false" +
                    "&is_foreclosure=false" +
                    "&is_contingent=false" +
                    "&city=" + p.getKey() +
                    "&state_code=" + p.getValue() +
                    "&offset=0" +
                    "&limit=200";

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-rapidapi-host", "realtor.p.rapidapi.com");
            headers.set("x-rapidapi-key", "8bae55c801msh9b4481d2bfb4c36p1f69f9jsn98f191d0e3f6");
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity entity = new HttpEntity(headers);

            RestTemplate restTemplate = new RestTemplate();
            Response response = restTemplate.exchange(url, HttpMethod.GET, entity, Response.class).getBody();
            saveToDb(response, p);
        }
    }

    private static void saveToDb(Response response, Pair p) {
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

        for(int i = 0; i < response.getListings().size();  i++) {
            Listing l  = response.getListings().get(i);
            //Remove Apostrophe s in property rawAddress
            l.setAddress(l.getAddress().replaceAll("'", ""));
            String rawAddress = l.getAddress();
            StringBuilder address = new StringBuilder();
            String city = rawAddress.substring(rawAddress.indexOf(", ")+2, rawAddress.lastIndexOf(", ")) + ", " + p.getValue();
            if (rawAddress.contains(" in ")) {
                //Address has neighbourhood info
                address.append(rawAddress, 0, rawAddress.indexOf(" in ")); //Street
                address.append(", ");
                address.append(rawAddress, rawAddress.indexOf(", ")+1, rawAddress.lastIndexOf(", ")); //City
                address.append(", ");
                address.append(p.getValue()); //State
                address.append(rawAddress, rawAddress.lastIndexOf(", ")+1, rawAddress.length()); //Zip
            } else {
                address.append(rawAddress, 0, rawAddress.lastIndexOf(", ")+2);
                address.append(p.getValue() + " ");
                address.append(rawAddress, rawAddress.lastIndexOf(", ")+1, rawAddress.length());
            }
            Pattern pattern = Pattern.compile(LeadConstants.STREET_REGEX);
            boolean validStreet = pattern.matcher(address.toString()).find();
            if (!validStreet) {
                continue;
            }

            String insertStmt = "";
            try {
                insertStmt = "INSERT INTO " +
                        "leads(property_id, address, city, area, asking_price, property_type, bed, bath, construction, lot, url, " +
                        "is_new_construction, last_update, prop_status, list_date, photo, baths_half, baths_full, photo_count, lat, lon, is_new_listing) " +
                        "VALUES " +
                        String.format("('%s', '%s' , '%s', '%s' , %f , '%s', %f , %f , %f , %f , '%s', %b, '%s', '%s', '%s', '%s', %f, %f, %f, %f, %f, " +
                                        "%b) ",
                                l.getProperty_id(),
                                address,
                                city,
                                p.getKey() + ", " + p.getValue(),
                                l.getPrice_raw(),
                                l.getProp_type(),
                                l.getBeds(),
                                l.getBaths(),
                                l.getSqft_raw(),
                                Double.parseDouble(cleanseNumericString(l.getLot_size())),
                                l.getRdc_web_url(),
                                l.getIs_new_construction(),
                                l.getLast_update(),
                                l.getProp_status(),
                                l.getList_date(),
                                l.getPhoto(),
                                l.getBaths_half(),
                                l.getBaths_full(),
                                l.getPhoto_count(),
                                l.getLat(),
                                l.getLon(),
                                l.getIs_new_listing()) +
                        "ON conflict (property_id) DO " +
                        "UPDATE " +
                        "SET address = excluded.address, " +
                        "city = excluded.city, " +
                        "area = excluded.area, " +
                        "asking_price = excluded.asking_price, " +
                        "property_type = excluded.property_type, " +
                        "bed = excluded.bed, " +
                        "bath = excluded.bath, " +
                        "construction = excluded.construction, " +
                        "lot = excluded.lot, " +
                        "url = excluded.url," +
                        "is_new_construction = excluded.is_new_construction," +
                        "last_update = excluded.last_update," +
                        "prop_status = excluded.prop_status," +
                        "list_date = excluded.list_date," +
                        "photo = excluded.photo," +
                        "baths_half = excluded.baths_half," +
                        "baths_full = excluded.baths_full," +
                        "photo_count = excluded.photo_count," +
                        "lat = excluded.lat," +
                        "lon = excluded.lon," +
                        "is_new_listing = excluded.is_new_listing;";
                stmt.executeUpdate(insertStmt);
            } catch (Exception e) {
                System.out.println("Error Processing the below entries:\n" + l.toString());
                System.out.println("SQL statement: " + insertStmt);
                e.printStackTrace();
            }
        }
    }

    private static String cleanseNumericString(String s){
        return s.replaceAll("[^\\d.]", "");
    }
}
