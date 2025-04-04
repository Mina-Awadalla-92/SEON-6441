package com.Game.model.order;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.Game.model.Player;
import com.Game.model.HumanPlayer;

/**
 * Unit tests for NegotiateOrder.
 *
 * These tests verify that when a NegotiateOrder is executed: - The target
 * player is added to the issuer's negotiated players list. - The issuer is
 * added to the target player's negotiated players list.
 */
public class NegotiateOrderTest {

	private Player issuer;
	private Player target;
	private NegotiateOrder negotiateOrder;

	@Before
	public void setUp() {
		// Create two players: the issuer and the target of the negotiation.
		issuer = new HumanPlayer("Issuer");
		target = new HumanPlayer("Target");

		// Ensure the negotiated lists are initially empty.
		issuer.resetNegociatedPlayersPerTurn();
		target.resetNegociatedPlayersPerTurn();

		// Create the negotiate order.
		negotiateOrder = new NegotiateOrder(issuer, target);
	}

	@Test
	public void testNegotiateOrderExecution() {
		// Pre-condition: neither player's negotiated list should contain the other.
		assertFalse("Issuer's negotiated list should not contain target before execution.",
				issuer.getNegociatedPlayersPerTurn().contains(target));
		assertFalse("Target's negotiated list should not contain issuer before execution.",
				target.getNegociatedPlayersPerTurn().contains(issuer));

		// Execute the negotiate order.
		negotiateOrder.execute();

		// Post-condition: each player's negotiated list should now contain the other.
		assertTrue("Issuer's negotiated list should contain target after execution.",
				issuer.getNegociatedPlayersPerTurn().contains(target));
		assertTrue("Target's negotiated list should contain issuer after execution.",
				target.getNegociatedPlayersPerTurn().contains(issuer));
	}
}
