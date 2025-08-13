package Meatball.w_w.data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;


public class SparseHangar extends BaseHullMod {

	//public static final int CREW_REQ = 40;
	public static int REFIT_TIME_PLUS = 20;
	//duh
	public static final float CAPACITY_PENALTY_PERCENT = 50f;

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getFighterRefitTimeMult().modifyPercent(id, REFIT_TIME_PLUS);
		//stats.getNumFighterBays().modifyFlat(id, 0f);
		//cargo penalty?
		float effect = stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		stats.getCargoMod().modifyMult(id, 1f - (CAPACITY_PENALTY_PERCENT * effect) / 100f);
		CompromisedStructure.modifyCost(hullSize, stats, id);
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + Math.round(CAPACITY_PENALTY_PERCENT) + "%";
		if (index == 1) return CompromisedStructure.getCostDescParam(index, 1);
		if (index == 2) return "" + Math.round(REFIT_TIME_PLUS) + "%";
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains(HullMods.EXPANDED_DECK_CREW)&&!ship.getVariant().getHullMods().contains(HullMods.CONVERTED_HANGAR);
}

	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains(HullMods.EXPANDED_DECK_CREW)) {
			return "Incompatible with Missile Autoloader";
		}
		if (ship.getVariant().getHullMods().contains(HullMods.CONVERTED_HANGAR)) {
			return "Incompatible with Expanded Missile Racks";
		}
		return null;
	}


}



