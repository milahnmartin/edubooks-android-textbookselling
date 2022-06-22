package edubooks.main.controllers;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordCreationTest {
    @Test
    public void passwordHash(){
        assertEquals("b61a65a38fb3aa7752291cd4f1f0dbfd",PasswordCreation.encryptPassword("#@Jake1234"));
    }
}