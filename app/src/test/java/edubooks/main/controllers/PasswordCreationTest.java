package edubooks.main.controllers;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordCreationTest {
    @Test
    public void passwordHashJake(){
        assertEquals("b61a65a38fb3aa7752291cd4f1f0dbfd",PasswordCreation.encryptPassword("#@Jake1234"));
    }

    @Test
    public void passwordHashJames(){
        assertEquals("419e4df15edd79256deac18054000a4b",PasswordCreation.encryptPassword("#@James1234"));
    }
}