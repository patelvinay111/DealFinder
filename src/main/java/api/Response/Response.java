package api.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * @author vinay.patel@amobee.com
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private float returned_rows;
    private float matching_rows;
    private ArrayList<Listing> listings = new ArrayList <Listing> ();


    public Response() {
    }

    @Override
    public String toString() {
        return "Response{" +
                "returned_rows=" + returned_rows +
                ", matching_rows=" + matching_rows +
                ", listings=" + listings.toString() +
                '}';
    }

    // Getter Methods
    public float getReturned_rows() {
        return returned_rows;
    }

    public float getMatching_rows() {
        return matching_rows;
    }

    public ArrayList<Listing> getListings() {
        return listings;
    }

    // Setter Methods
    public void setReturned_rows(float returned_rows) {
        this.returned_rows = returned_rows;
    }

    public void setMatching_rows(float matching_rows) {
        this.matching_rows = matching_rows;
    }

    public void setListings(ArrayList<Listing> listings) {
        this.listings = listings;
    }
}