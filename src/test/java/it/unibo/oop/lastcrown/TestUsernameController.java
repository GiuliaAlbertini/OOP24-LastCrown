package it.unibo.oop.lastcrown;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.controller.user.api.UsernameController;
import it.unibo.oop.lastcrown.controller.user.impl.UsernameControllerImpl;

final class TestUsernameController { 
    private UsernameController controller;
    @BeforeEach
    void setUp() {
        controller = new UsernameControllerImpl();
    }

    @Test
    void testCheckValidityDelegation() {
        assertTrue(controller.checkValidity("User_1"));
        assertFalse(controller.checkValidity("User 1"));
    }

    @Test
    void testCheckExistenceNewUsername() {
        assertFalse(controller.checkExistance("unlikelyUsername123"));
    }
}
