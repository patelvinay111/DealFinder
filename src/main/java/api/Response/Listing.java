package api.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Vinay Patel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Listing {
    private String property_id;
    private boolean is_new_construction; //
    private String listing_id;
    private String prop_type;
    private String last_update; //
    private String rdc_web_url;
    private boolean is_turbo;
    private String address;
    private String prop_status; //
    private float price_raw;
    private float sqft_raw;
    private String list_date; //
    private float advertiser_id;
    private String office_name;
    private boolean is_showcase;
    private String price;
    private float beds;
    private float baths;
    private String sqft;
    private String lot_size;
    private String photo; //
    private boolean is_cobroker;
    private String short_price;
    private float baths_half; //
    private float baths_full; //
    private float photo_count; //
    private float lat; //
    private float lon; //
    private boolean is_new_listing; //
    private boolean has_leadform;
    private float page_no;
    private float rank;

    public Listing() {

    }

    @Override
    public String toString() {
        return "Listing{" +
                "property_id='" + property_id + '\'' +
                ", is_new_construction=" + is_new_construction +
                ", listing_id='" + listing_id + '\'' +
                ", prop_type='" + prop_type + '\'' +
                ", last_update='" + last_update + '\'' +
                ", rdc_web_url='" + rdc_web_url + '\'' +
                ", is_turbo=" + is_turbo +
                ", address='" + address + '\'' +
                ", prop_status='" + prop_status + '\'' +
                ", price_raw=" + price_raw +
                ", sqft_raw=" + sqft_raw +
                ", list_date='" + list_date + '\'' +
                ", advertiser_id=" + advertiser_id +
                ", office_name='" + office_name + '\'' +
                ", is_showcase=" + is_showcase +
                ", price='" + price + '\'' +
                ", beds=" + beds +
                ", baths=" + baths +
                ", sqft='" + sqft + '\'' +
                ", lot_size='" + lot_size + '\'' +
                ", photo='" + photo + '\'' +
                ", is_cobroker=" + is_cobroker +
                ", short_price='" + short_price + '\'' +
                ", baths_half=" + baths_half +
                ", baths_full=" + baths_full +
                ", photo_count=" + photo_count +
                ", lat=" + lat +
                ", lon=" + lon +
                ", is_new_listing=" + is_new_listing +
                ", has_leadform=" + has_leadform +
                ", page_no=" + page_no +
                ", rank=" + rank +
                '}';
    }

    // Getter Methods
    public String getProperty_id() {
        return property_id;
    }

    public boolean getIs_new_construction() {
        return is_new_construction;
    }

    public String getListing_id() {
        return listing_id;
    }

    public String getProp_type() {
        return prop_type;
    }

    public String getLast_update() {
        return last_update;
    }

    public String getRdc_web_url() {
        return rdc_web_url;
    }

    public boolean getIs_turbo() {
        return is_turbo;
    }

    public String getAddress() {
        return address;
    }

    public String getProp_status() {
        return prop_status;
    }

    public float getPrice_raw() {
        return price_raw;
    }

    public float getSqft_raw() {
        return sqft_raw;
    }

    public String getList_date() {
        return list_date;
    }

    public float getAdvertiser_id() {
        return advertiser_id;
    }

    public String getOffice_name() {
        return office_name;
    }

    public boolean getIs_showcase() {
        return is_showcase;
    }

    public String getPrice() {
        return price;
    }

    public float getBeds() {
        return beds;
    }

    public float getBaths() {
        return baths;
    }

    public String getSqft() {
        return sqft;
    }

    public String getLot_size() {
        return lot_size;
    }

    public String getPhoto() {
        return photo;
    }

    public boolean getIs_cobroker() {
        return is_cobroker;
    }

    public String getShort_price() {
        return short_price;
    }

    public float getBaths_half() {
        return baths_half;
    }

    public float getBaths_full() {
        return baths_full;
    }

    public float getPhoto_count() {
        return photo_count;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public boolean getIs_new_listing() {
        return is_new_listing;
    }

    public boolean getHas_leadform() {
        return has_leadform;
    }

    public float getPage_no() {
        return page_no;
    }

    public float getRank() {
        return rank;
    }

    // Setter Methods
    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public void setIs_new_construction(boolean is_new_construction) {
        this.is_new_construction = is_new_construction;
    }

    public void setListing_id(String listing_id) {
        this.listing_id = listing_id;
    }

    public void setProp_type(String prop_type) {
        this.prop_type = prop_type;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public void setRdc_web_url(String rdc_web_url) {
        this.rdc_web_url = rdc_web_url;
    }

    public void setIs_turbo(boolean is_turbo) {
        this.is_turbo = is_turbo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setProp_status(String prop_status) {
        this.prop_status = prop_status;
    }

    public void setPrice_raw(float price_raw) {
        this.price_raw = price_raw;
    }

    public void setSqft_raw(float sqft_raw) {
        this.sqft_raw = sqft_raw;
    }

    public void setList_date(String list_date) {
        this.list_date = list_date;
    }

    public void setAdvertiser_id(float advertiser_id) {
        this.advertiser_id = advertiser_id;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public void setIs_showcase(boolean is_showcase) {
        this.is_showcase = is_showcase;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setBeds(float beds) {
        this.beds = beds;
    }

    public void setBaths(float baths) {
        this.baths = baths;
    }

    public void setSqft(String sqft) {
        this.sqft = sqft;
    }

    public void setLot_size(String lot_size) {
        this.lot_size = lot_size;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setIs_cobroker(boolean is_cobroker) {
        this.is_cobroker = is_cobroker;
    }

    public void setShort_price(String short_price) {
        this.short_price = short_price;
    }

    public void setBaths_half(float baths_half) {
        this.baths_half = baths_half;
    }

    public void setBaths_full(float baths_full) {
        this.baths_full = baths_full;
    }

    public void setPhoto_count(float photo_count) {
        this.photo_count = photo_count;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public void setIs_new_listing(boolean is_new_listing) {
        this.is_new_listing = is_new_listing;
    }

    public void setHas_leadform(boolean has_leadform) {
        this.has_leadform = has_leadform;
    }

    public void setPage_no(float page_no) {
        this.page_no = page_no;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }
}
