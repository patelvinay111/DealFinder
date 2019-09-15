/**
 * @author vinay.patel@amobee.com
 */
public class Deal implements Comparable<Deal>{

    private String name;
    private String detailsPage;
    private String comps;
    private double propertyPrice;
    private double rent;
    private double vacancyCost;
    private double propertyTax;
    private double propertyManagement;
    private double leasingFee;
    private double insurance;
    private double maintenanceCost;
    private double capitalReserve;
    private double mortgageCost;
    private double avgEquityEarn;
    private double cashFlow;

    public Deal(String name, double propertyPrice, double rent) {
        this.name = name;
        this.propertyPrice = propertyPrice;
        this.rent = rent;
    }

    public int compareTo(Deal otherDeal) {
        if(cashFlow >= otherDeal.cashFlow) {
            return 1;
        } else if(cashFlow < otherDeal.cashFlow) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetailsPage() {
        return detailsPage;
    }

    public void setDetailsPage(String detailsPage) {
        this.detailsPage = detailsPage;
    }

    public String getComps() {
        return comps;
    }

    public void setComps(String comps) {
        this.comps = comps;
    }

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getVacancyCost() {
        return vacancyCost;
    }

    public void setVacancyCost(double vacancyCost) {
        this.vacancyCost = vacancyCost;
    }

    public double getPropertyTax() {
        return propertyTax;
    }

    public void setPropertyTax(double propertyTax) {
        this.propertyTax = propertyTax;
    }

    public double getPropertyManagement() {
        return propertyManagement;
    }

    public void setPropertyManagement(double propertyManagement) {
        this.propertyManagement = propertyManagement;
    }

    public double getLeasingFee() {
        return leasingFee;
    }

    public void setLeasingFee(double leasingFee) {
        this.leasingFee = leasingFee;
    }

    public double getInsurance() {
        return insurance;
    }

    public void setInsurance(double insurance) {
        this.insurance = insurance;
    }

    public double getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public double getCapitalReserve() { return capitalReserve; }

    public void setCapitalReserve(double capitalReserve) { this.capitalReserve = capitalReserve; }

    public double getMortgageCost() {
        return mortgageCost;
    }

    public void setMortgageCost(double mortgageCost) {
        this.mortgageCost = mortgageCost;
    }

    public double getAvgEquityEarn() {
        return avgEquityEarn;
    }

    public void setAvgEquityEarn(double avgEquityEarn) {
        this.avgEquityEarn = avgEquityEarn;
    }

    public double getCashFlow() {
        return cashFlow;
    }

    public void setCashFlow(double cashFlow) {
        this.cashFlow = cashFlow;
    }

    @Override
    public String toString() {
        return name + DealConstants.DELIM +
                detailsPage + DealConstants.DELIM +
                comps + DealConstants.DELIM +
                propertyPrice + DealConstants.DELIM +
                rent + DealConstants.DELIM +
                vacancyCost + DealConstants.DELIM +
                propertyTax + DealConstants.DELIM +
                propertyManagement + DealConstants.DELIM +
                leasingFee + DealConstants.DELIM +
                insurance + DealConstants.DELIM +
                maintenanceCost + DealConstants.DELIM +
                capitalReserve + DealConstants.DELIM +
                mortgageCost + DealConstants.DELIM +
                avgEquityEarn + DealConstants.DELIM +
                cashFlow;
    }

    public String prettyPrint() {
        return name + "\n" +
                "===================================\n" +
                "Details: " + detailsPage + "\n" +
                "Comps: " + comps + "\n" +
                "Property Price: " + propertyPrice + "\n" +
                "Rent: " + rent + "\n" +
                "Vacancy Factor: " + vacancyCost + "\n" +
                "Property Tax: " + propertyTax + "\n" +
                "Property Management: " + propertyManagement + "\n" +
                "Leasing Fee: " + leasingFee + "\n" +
                "Insurance: " + insurance + "\n" +
                "Maintenance Reserve: " + maintenanceCost + "\n" +
                "Capital Reserve: " + capitalReserve + "\n" +
                "Mortgage: " + mortgageCost + "\n" +
                "Average Equity Paydown: " + avgEquityEarn + "\n" +
                "Cash Flow: " + cashFlow;
    }
}
