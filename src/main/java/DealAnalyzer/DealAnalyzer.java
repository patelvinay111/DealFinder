//package DealAnalyzer;
//
//import constants.DealConstants;
//import model.deal.Deal;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import util.DealUtil;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.*;
//import java.util.logging.Logger;
//
///**
// * @author Vinay Patel
// */
//public class DealAnalyzer {
//
//    private static final Logger LOGGER = Logger.getLogger(DealAnalyzer.class.getName());
//
//    public static void main(String args[]){
//
//        Map<String, Double> leads = new HashMap<String, Double>();
//
//        Connection c = null;
//        Statement stmt = null;
//        try {
//            Class.forName("org.postgresql.Driver");
//            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dealfinder", "postgres", "l");
//            c.setAutoCommit(true);
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT street, zipcode, asking_price " +
//                    "FROM leads " +
//                    "WHERE street " +
//                    "NOT IN (SELECT SPLIT_PART(property_name, ',', 1) FROM deals) " +
//                    "AND zipcode " +
//                    "NOT IN (SELECT SPLIT_PART(property_name, ',', 2) FROM deals) " +
//                    "ORDER BY random()" +
//                    "LIMIT 1;" );
//            while (rs.next()) {
//                String address = rs.getString("street") + ", " + rs.getString("zipcode");
//                double price = rs.getDouble("asking_price");
//                leads.put(address, price);
//            }
//            rs.close();
//        } catch (Exception e) {
//            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(1);
//        }
//
//        for(String propertyName : leads.keySet()){
//            LOGGER.info("Analyzing... " + propertyName);
//            Document doc = getAPIDocument(propertyName);
//
//            int returnCode = Integer.parseInt(doc.getElementsByTagName("code").item(0).getTextContent());
//            if(returnCode == 0){
//                NodeList zestimate = doc.getElementsByTagName("zestimate");
//                NodeList rentZestimate = doc.getElementsByTagName("rentzestimate");
//                String zillowPage = doc.getElementsByTagName("mapthishome").item(0).getTextContent() == null ? "<N/A>" :
//                        doc.getElementsByTagName("mapthishome").item(0).getTextContent();
//                String comparables = doc.getElementsByTagName("comparables").item(0).getTextContent() == null ? "<N/A>" :
//                        doc.getElementsByTagName("comparables").item(0).getTextContent();
//
//                if (rentZestimate.getLength() > 0) {
//                    Element zEle = (Element)zestimate.item(0);
//                    String strZestimate = zEle.getElementsByTagName("amount").item(0).getTextContent();
//                    Element rzEle = (Element)rentZestimate.item(0);
//                    String strRentZestimate = rzEle.getElementsByTagName("amount").item(0).getTextContent();
//                    if(strRentZestimate != null && strRentZestimate.trim() != ""){
//                        double propertyPrice = leads.get(propertyName);
//                        double rent = Double.parseDouble(strRentZestimate);
//                        Double propertyValue;
//                        Double instantEquity;
//                        try {
//                            propertyValue = Double.parseDouble(strZestimate);
//                            instantEquity = propertyValue - propertyPrice;
//                        } catch (NumberFormatException nfe) {
//                            instantEquity = null;
//                        }
//                        Deal deal = createDeal(
//                                propertyName,
//                                propertyPrice,
//                                rent,
//                                instantEquity,
//                                zillowPage,
//                                comparables);
//                        String insertStmt = "INSERT INTO " +
//                                "deals(property_name, details_page, comps, asking_price, rent, vacancy_cost, property_tax, property_managent, leasing_fee, " +
//                                "insurance, maintenance_cost, capital_reserve, mortgage_payment, min_equity_earn, cash_flow, instant_equity_earn, noi)" +
//                                "VALUES " +
//                                "(" +
//                                String.format("'%s', '%s', '%s', %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f, %f", deal.getName(), deal.getDetailsPage(),
//                                        deal.getComps(), deal.getPropertyPrice(), deal.getRent(), deal.getVacancyCost(), deal.getPropertyTax(),
//                                        deal.getPropertyManagement(), deal.getLeasingFee(), deal.getInsurance(), deal.getMaintenanceCost(),
//                                        deal.getCapitalReserve(), deal.getMortgageCost(), deal.getMinEquityEarn(), deal.getCashFlow(),
//                                        deal.getInstantEquityEarn(), deal.getNoi()) +
//                                ") " +
//                                "ON conflict (property_name) DO " +
//                                "UPDATE " +
//                                "SET details_page = excluded.details_page, " +
//                                "comps = excluded.comps, " +
//                                "asking_price = excluded.asking_price, " +
//                                "rent = excluded.rent, " +
//                                "vacancy_cost = excluded.vacancy_cost, " +
//                                "property_tax = excluded.property_tax, " +
//                                "property_managent = excluded.property_managent, " +
//                                "leasing_fee = excluded.leasing_fee, " +
//                                "insurance = excluded.insurance, " +
//                                "maintenance_cost = excluded.maintenance_cost, " +
//                                "capital_reserve = excluded.capital_reserve, " +
//                                "mortgage_payment = excluded.mortgage_payment, " +
//                                "min_equity_earn = excluded.min_equity_earn, " +
//                                "cash_flow = excluded.cash_flow, " +
//                                "instant_equity_earn = excluded.instant_equity_earn, " +
//                                "noi = excluded.noi;";
//                        try {
//                            stmt.executeUpdate(insertStmt);
//                        } catch (Exception e) {
//                            LOGGER.info("Failed to execute: " + insertStmt);
//                        }
//                    }
//                }
//            }
//        }
//        try {
//            stmt.close();
//            c.commit();
//        } catch (Exception e) {
//            LOGGER.info("Failed to close connection to DB");
//        }
//
//    }
//
//    public static Document getAPIDocument(String address) {
//        try {
//            String street = address.substring(0, address.indexOf(", "));
//            String zip = address.substring(address.indexOf(", ")+2);
//            String encodedStreet = URLEncoder.encode(street, "UTF-8");
//            String encodedZip = URLEncoder.encode(zip, "UTF-8");
//
//            String url =
//                    "http://www.zillow.com/webservice/GetSearchResults.htm?zws-id=" //+ DealConstants.API_KEY
//                            +"&address="+encodedStreet
//                            +"&citystatezip="+encodedZip
//                            +"&rentzestimate=true";
//            LOGGER.info("API URL: " + url);
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(response.toString())));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    private static Deal createDeal(String propertyName, double propertyPrice, double rent,  Double instantEquity, String zillowPage, String comparables) {
//        int zip = Integer.parseInt(propertyName.substring(propertyName.lastIndexOf(" ") + 1));
//        Deal deal = new Deal(propertyName, propertyPrice, rent);
//        deal.setDetailsPage(zillowPage);
//        deal.setComps(comparables);
//        deal.setVacancyCost(rent*DealConstants.VACANCY_FACTOR);
//        deal.setPropertyTax(DealUtil.calculatePropertyTax(zip, propertyPrice));
//        deal.setPropertyManagement(rent*DealConstants.PROPERTY_MANAGEMENT_COST);
//        deal.setLeasingFee(rent*DealConstants.LEASING_FEE);
//        deal.setInsurance(DealUtil.calculateInsurance(propertyPrice));
//        deal.setMaintenanceCost(rent*DealConstants.MAINTENANCE_COST);
//        deal.setNoi(
//                deal.getRent()
//                        - (
//                        deal.getVacancyCost()
//                        + deal.getPropertyTax()
//                        + deal.getPropertyManagement()
//                        + deal.getLeasingFee()
//                        + deal.getInsurance()
//                        + deal.getMaintenanceCost()
//                )
//        );
//        deal.setCapitalReserve(rent*DealConstants.CAPITAL_RESERVE);
//        deal.setMortgageCost(DealUtil.calculateMortgagePayment(propertyPrice));
//        deal.setMinEquityEarn(DealUtil.calculateMinEquityEarn(propertyPrice, deal.getMortgageCost()));
//        deal.setInstantEquityEarn(instantEquity);
//        deal.setCashFlow(
//                deal.getNoi()
//                - (
//                    deal.getCapitalReserve()
//                    + deal.getMortgageCost()
//                )
//        );
//        return deal;
//    }
//}
