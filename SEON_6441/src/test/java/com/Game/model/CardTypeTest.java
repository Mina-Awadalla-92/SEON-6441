package com.Game.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTypeTest {

    @Test
    void testCardTypeValues() {
        // Verify that all enum values are present
        CardType[] cardTypes = CardType.values();
        assertNotNull(cardTypes, "CardType values should not be null.");
        assertEquals(4, cardTypes.length, "CardType should have exactly 4 values.");
        assertArrayEquals(new CardType[]{CardType.BOMB, CardType.BLOCKADE, CardType.AIRLIFT, CardType.NEGOTIATE}, cardTypes, "CardType values should match the expected values.");
    }

    @Test
    void testCardTypeValueOf() {
        // Verify that valueOf correctly retrieves enum constants
        assertEquals(CardType.BOMB, CardType.valueOf("BOMB"), "CardType.valueOf should return the BOMB constant.");
        assertEquals(CardType.BLOCKADE, CardType.valueOf("BLOCKADE"), "CardType.valueOf should return the BLOCKADE constant.");
        assertEquals(CardType.AIRLIFT, CardType.valueOf("AIRLIFT"), "CardType.valueOf should return the AIRLIFT constant.");
        assertEquals(CardType.NEGOTIATE, CardType.valueOf("NEGOTIATE"), "CardType.valueOf should return the NEGOTIATE constant.");
    }

    @Test
    void testCardTypeName() {
        // Verify the name of each enum constant
        assertEquals("BOMB", CardType.BOMB.name(), "CardType.BOMB name should be 'BOMB'.");
        assertEquals("BLOCKADE", CardType.BLOCKADE.name(), "CardType.BLOCKADE name should be 'BLOCKADE'.");
        assertEquals("AIRLIFT", CardType.AIRLIFT.name(), "CardType.AIRLIFT name should be 'AIRLIFT'.");
        assertEquals("NEGOTIATE", CardType.NEGOTIATE.name(), "CardType.NEGOTIATE name should be 'NEGOTIATE'.");
    }
}