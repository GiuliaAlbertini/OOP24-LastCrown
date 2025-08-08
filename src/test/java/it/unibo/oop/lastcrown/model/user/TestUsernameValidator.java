package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.user.api.UsernameValidator;
import it.unibo.oop.lastcrown.model.user.impl.UsernameValidatorImpl;

final class TestUsernameValidator { 
    private UsernameValidator validator;
    @BeforeEach
    void setUp() {
        validator = new UsernameValidatorImpl();
    }

    @Test
    void testValidNames() {
        assertTrue(validator.isNameValid("Alice123"));
        assertTrue(validator.isNameValid("bob_smith"));
    }

    @Test
    void testInvalidNames() {
        assertFalse(validator.isNameValid("bad name"));
        assertFalse(validator.isNameValid("!nv@l!d"));
        assertFalse(validator.isNameValid("   "));
    }
}