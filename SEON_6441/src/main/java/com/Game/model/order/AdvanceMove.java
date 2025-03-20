package com.Game.model.order;

import com.Game.model.Player;
import com.Game.model.Territory;

public class AdvanceMove extends AdvanceOrder {
	public AdvanceMove() {
		super();
	}
	
	public AdvanceMove(Player p_issuer, Territory p_territoryFrom, Territory p_territoryTo, int p_numberOfArmies) {
		super(p_issuer, p_territoryFrom, p_territoryTo, p_numberOfArmies);
	}
	
	public AdvanceMove(AdvanceAttack advAttack) {
		super(advAttack.getIssuer(), advAttack.getD_territoryFrom(), advAttack.getD_territoryTo(), advAttack.getD_numberOfArmies());
	}
	
	@Override
	public void execute() {
		//getD_territoryFrom().setNumOfArmies(getD_territoryFrom().getNumOfArmies() - getD_numberOfArmies());
		getD_territoryTo().setNumOfArmies(getD_territoryTo().getNumOfArmies() + getD_numberOfArmies());
		System.out.println(this.d_numberOfArmies + " armie(s) were moved from " + getD_territoryFrom().getName() + " to " + getD_territoryTo().getName());
		
	}
}
