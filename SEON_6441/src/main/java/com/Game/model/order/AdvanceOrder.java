package com.Game.model.order;
import com.Game.model.*;
public abstract class AdvanceOrder extends Order{
	protected Territory d_territoryFrom;
	protected Territory d_territoryTo;
	protected int d_numberOfArmies;
	
	public AdvanceOrder() {
		super();
	}
	
	public AdvanceOrder(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer);
		this.d_territoryFrom = p_territoryFrom;
		this.d_territoryTo = p_territoryTo;
		this.d_numberOfArmies = p_numberOfArmies;
	}
	
	public AdvanceOrder(AdvanceOrder p_advOrder) {
		super(p_advOrder.getIssuer());
		this.d_territoryFrom = p_advOrder.getD_territoryFrom();
		this.d_territoryTo = p_advOrder.getD_territoryTo();
		this.d_numberOfArmies = p_advOrder.getD_numberOfArmies();
	}
	
	public abstract void execute();

	/**
	 * @return the d_territoryFrom
	 */
	public Territory getD_territoryFrom() {
		return d_territoryFrom;
	}

	/**
	 * @param d_territoryFrom the d_territoryFrom to set
	 */
	public void setD_territoryFrom(Territory d_territoryFrom) {
		this.d_territoryFrom = d_territoryFrom;
	}

	/**
	 * @return the d_territoryTo
	 */
	public Territory getD_territoryTo() {
		return d_territoryTo;
	}

	/**
	 * @param d_territoryTo the d_territoryTo to set
	 */
	public void setD_territoryTo(Territory d_territoryTo) {
		this.d_territoryTo = d_territoryTo;
	}

	/**
	 * @return the d_numberOfArmies
	 */
	public int getD_numberOfArmies() {
		return d_numberOfArmies;
	}

	/**
	 * @param d_numberOfArmies the d_numberOfArmies to set
	 */
	public void setD_numberOfArmies(int d_numberOfArmies) {
		this.d_numberOfArmies = d_numberOfArmies;
	}
	
	
}
