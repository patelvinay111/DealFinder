import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Vinay Patel
 */
public class DealUtil {

    public static double calculateMortgagePayment(double propertyPrice){
        double financeAmount = propertyPrice * (1 - DealConstants.DOWNPAYMENT);
        double monthlyInterest = DealConstants.INTEREST_RATE / 12;
        double discountFactor = Math.pow((1+monthlyInterest), 360);
        double monthlyDF = (discountFactor - 1) / (monthlyInterest * discountFactor);
        return (financeAmount/monthlyDF);
    }

    public static double calculatePropertyTax(int zip, double propertyPrice){
        try {
            Connection conn = null;
            Statement stmt = null;
            Class.forName("org.postgresql.Driver");
            double taxRate = DealConstants.DEFAULT_PROPERTY_TAX;

            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dealfinder", "postgres", "l");
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT tax FROM ziptax WHERE zip=" + zip + ";" );
            while (rs.next()){
                taxRate = rs.getDouble("tax");
            }
            return (propertyPrice * (taxRate/100)) / 12;
        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage());
        }
        return (propertyPrice * DealConstants.DEFAULT_PROPERTY_TAX) / 12;
    }

    public static double calculateInsurance(double propertyPrice){
        if(propertyPrice <= 150000){
            return 600/12;
        } else if(propertyPrice > 150000 && propertyPrice <= 300000) {
            return 680/12;
        } else if(propertyPrice > 300000 && propertyPrice <= 450000){
            return 830/12;
        } else {
            return 930/12;
        }
    }

    public static double calculateMinEquityEarn(double propertyPrice, double mortgagePayment) {
        double initBalance =  (1-DealConstants.DOWNPAYMENT)*propertyPrice;
        double firstMonthInterest = (initBalance * DealConstants.INTEREST_RATE) / 12;
        return mortgagePayment - firstMonthInterest;
    }

    public static String printSchema() {
        return "Name" + DealConstants.DELIM +
                "DetailURL" + DealConstants.DELIM +
                "Comps" + DealConstants.DELIM +
                "Price" + DealConstants.DELIM +
                "Rent" + DealConstants.DELIM +
                "Vacancy Cost("+DealConstants.VACANCY_FACTOR*100+"%)" + DealConstants.DELIM +
                "Property Tax("+DealConstants.DEFAULT_PROPERTY_TAX*100+"%)" + DealConstants.DELIM +
                "Property Management("+DealConstants.PROPERTY_MANAGEMENT_COST*100+"%)" + DealConstants.DELIM +
                "Leasing Fee("+DealConstants.LEASING_FEE*100+"%)" + DealConstants.DELIM +
                "Insurance" + DealConstants.DELIM +
                "Maintenance Cost("+DealConstants.MAINTENANCE_COST*100+"%)" + DealConstants.DELIM +
                "Mortgage Payments("+DealConstants.INTEREST_RATE*100+"%)" + DealConstants.DELIM +
                "Minimum Monthly Equity Paydown" + DealConstants.DELIM +
                "Monthly Cashflow";
    }

}
