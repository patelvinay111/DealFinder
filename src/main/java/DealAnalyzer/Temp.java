package DealAnalyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;

public class Temp {

    public static void main(String[] args) {
        Connection c = null;
        Statement stmt = null;

        try {
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dealfinder", "postgres", "l");
            c.setAutoCommit(true);
            stmt = c.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM daily_hunt;");
            while (rs.next()) {
                String address = rs.getString("property_name");
                String rent_price = new DecimalFormat("#.#").format(rs.getDouble("rent_to_price"));;
                String cashflow = new DecimalFormat("#.#").format(rs.getDouble("cash_flow"));
                String url = rs.getString("url");
                System.out.format("%16s%4s%6s%32s", address, rent_price, cashflow, url);
                System.out.println();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
