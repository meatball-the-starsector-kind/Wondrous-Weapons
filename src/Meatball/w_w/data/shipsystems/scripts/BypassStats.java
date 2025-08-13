package Meatball.w_w.data.shipsystems.scripts;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.impl.combat.BaseShipSystemScript;
import com.fs.starfarer.api.plugins.ShipSystemStatsScript;
public class BypassStats extends BaseShipSystemScript {

	public static final float ROF_BONUS = 1f;
	public static final float FLUX_REDUCTION = 75f;
	public static final float DAMAGE_BONUS_PERCENT = 50f;

	private static final float FLUX_CAPACITY_MULT = 1.5f;

	//public static final float FLUX_REDUCTION_ENERGY = 75f;
	
	public void apply(MutableShipStatsAPI stats, String id, State state, float effectLevel) {
		
		float mult = 1f + ROF_BONUS * effectLevel;
		stats.getBallisticRoFMult().modifyMult(id, mult);
		stats.getBallisticWeaponFluxCostMod().modifyMult(id, 1f - (FLUX_REDUCTION * 0.01f));

		float bonusPercent = DAMAGE_BONUS_PERCENT * effectLevel;
		stats.getEnergyWeaponDamageMult().modifyPercent(id, bonusPercent);

		stats.getZeroFluxMinimumFluxLevel().modifyFlat(id, 2f);

		stats.getFluxCapacity().modifyMult(id, FLUX_CAPACITY_MULT);

		if (state == ShipSystemStatsScript.State.OUT) {
			stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
			stats.getMaxTurnRate().unmodify(id);
		} else {
			stats.getMaxSpeed().modifyFlat(id, 50f * effectLevel);
//            stats.getAcceleration().modifyFlat(id, 50f * effectLevel);

//            stats.getMaxTurnRate().modifyMult(id, 1+ (1 * effectLevel));
//            stats.getTurnAcceleration().modifyMult(id, 1+ (0.5f * effectLevel));

			//using percentage modifier for proper additive boost
			stats.getMaxTurnRate().modifyPercent(id, 50 * effectLevel);
//            stats.getTurnAcceleration().modifyPercent(id, 50 * effectLevel);
		}
//		ShipAPI ship = (ShipAPI)stats.getEntity();
//		ship.blockCommandForOneFrame(ShipCommand.FIRE);
//		ship.setHoldFireOneFrame(true);
	}
	public void unapply(MutableShipStatsAPI stats, String id) {
		stats.getBallisticRoFMult().unmodify(id);
		stats.getBallisticWeaponFluxCostMod().unmodify(id);
		stats.getEnergyWeaponDamageMult().unmodify(id);
		stats.getZeroFluxMinimumFluxLevel().unmodifyFlat(id);
		stats.getFluxCapacity().unmodifyMult(id);
		//stats.getEnergyWeaponFluxCostMod().unmodify(id);
	}
	
	public StatusData getStatusData(int index, State state, float effectLevel) {
		float mult = 1f + ROF_BONUS * effectLevel;
		float bonusPercent = (int) ((mult - 1f) * 100f);
		float bonusPercent2 = DAMAGE_BONUS_PERCENT * effectLevel;
		float mult2 = 50;
		if (index == 0) {
			return new StatusData("ballistic rate of fire +" + (int) bonusPercent + "%", false);
		}
		if (index == 1) {
			return new StatusData("ballistic flux use -" + (int) FLUX_REDUCTION + "%", false);
		}
		if (index == 2) {
			return new StatusData("+" + (int) bonusPercent2 + "% energy weapon damage" , false);
		}
		if (index == 3) {
			return new StatusData("flux capacity increased by " + (int) mult2 + "%" , false);
		}
		return null;
	}
}
