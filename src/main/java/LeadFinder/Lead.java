package LeadFinder;

/**
 * @author Vinay Patel
 */
public class Lead {

    private String address;
    private String street;
    private String city;
    private String state;
    private int zip;
    private String area;
    private double asking_price;
    private String property_type;
    private double bed;
    private double bath;
    private double construction;
    private double lot;
    private String url;

    public Lead(String address, String street, String city, String state, int zip, String area,
                double asking_price, String property_type, double bed, double bath,
                double construction, double lot, String url) {
        this.address = address;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.area = area;
        this.asking_price = asking_price;
        this.property_type = property_type;
        this.bed = bed;
        this.bath = bath;
        this.construction = construction;
        this.lot = lot;
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getAsking_price() {
        return asking_price;
    }

    public void setAsking_price(double asking_price) {
        this.asking_price = asking_price;
    }

    public String getProperty_type() {
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public double getBed() {
        return bed;
    }

    public void setBed(double bed) {
        this.bed = bed;
    }

    public double getBath() {
        return bath;
    }

    public void setBath(double bath) {
        this.bath = bath;
    }

    public double getConstruction() {
        return construction;
    }

    public void setConstruction(double construction) {
        this.construction = construction;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
