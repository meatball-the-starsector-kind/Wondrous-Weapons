package Meatball.w_w.data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class ConvMissileBay extends BaseHullMod {
	//BOOM!!!
	public static final float RADIUS_MULT = 20f;
	public static final float DAMAGE_MULT = 8f;
	//duh
	public static final float CAPACITY_PENALTY_PERCENT = 90f;
	//more meesile
	public static float AMMO_BONUS = 100f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//BOOM!!!
		stats.getDynamic().getStat(Stats.EXPLOSION_DAMAGE_MULT).modifyMult(id, DAMAGE_MULT);
		stats.getDynamic().getStat(Stats.EXPLOSION_RADIUS_MULT).modifyMult(id, RADIUS_MULT);
		//something something cargo capacity penalty
		float effect = stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		stats.getCargoMod().modifyMult(id, 1f - (CAPACITY_PENALTY_PERCENT * effect) / 100f);
		CompromisedStructure.modifyCost(hullSize, stats, id);
		//meesyle
		stats.getMissileAmmoBonus().modifyPercent(id, AMMO_BONUS);

	}
		
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		//uhhhhhhh
		if (index == 0) return "" + Math.round(AMMO_BONUS) + "%";
		if (index == 1) return "" + Math.round(CAPACITY_PENALTY_PERCENT) + "%";
		if (index == 2) return "greatly increased";
		//mysile
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains(HullMods.MISSILE_AUTOLOADER)&&!ship.getVariant().getHullMods().contains(HullMods.MISSLERACKS);
	}

	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains(HullMods.MISSILE_AUTOLOADER)) {
			return "Incompatible with Missile Autoloader";
		}
		if (ship.getVariant().getHullMods().contains(HullMods.MISSLERACKS)) {
			return "Incompatible with Expanded Missile Racks";
		}
		return null;
	}


}




