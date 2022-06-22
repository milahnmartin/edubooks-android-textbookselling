package edubooks.main.Activity;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisterActivityTest {

    @Test
    public void isValidPasswordTrue() {
        RegisterActivity registerActivityObj = new RegisterActivity();
        assertTrue(registerActivityObj.isValidPassword("#@Jake6543"));
    }
    @Test
    public void isValidPasswordFalse() {
        RegisterActivity registerActivityObj = new RegisterActivity();
        assertTrue(registerActivityObj.isValidPassword("12345"));
    }

    @Test
    public void isEmailValidTrue() {
        RegisterActivity registerActivityObj = new RegisterActivity();
        assertTrue(registerActivityObj.isEmailValid("edubooks@gmail.com"));
    }
    @Test
    public void isEmailValidFalse() {
        RegisterActivity registerActivityObj = new RegisterActivity();
        assertTrue(registerActivityObj.isEmailValid("edubooks.com"));
    }
}