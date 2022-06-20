package edubooks.main.Activity;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreateListingTest {

    @Test
    public void ISBNValidTrue() {
        CreateListing createListingObj = new CreateListing();
        assertTrue(createListingObj.ISBNValid("978-0-979-859-0-32"));
    }
    @Test
    public void ISBNValidFalse() {
        CreateListing createListingObj = new CreateListing();
        assertFalse(createListingObj.ISBNValid("978-0-979-859-0-0"));
    }

    @Test
    public void priceValidTrue() {
        CreateListing createListingObj = new CreateListing();
        assertTrue(createListingObj.priceValid("100"));
    }

    @Test
    public void priceValidFalse() {
        CreateListing createListingObj = new CreateListing();
        assertFalse(createListingObj.priceValid("500.345"));
    }
}