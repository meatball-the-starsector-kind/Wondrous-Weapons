package Meatball.w_w.data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;

public class OvrStrCoils extends BaseHullMod {

	public static float FLUX_THRESHOLD_INCREASE_PERCENT = -20f;
	private static final float PHASE_BONUS_MULT = 2f;
	private static final float PEAK_PERFORMANCE_MULT = 0.7f;
	private static final float DEGRADE_INCREASE_PERCENT = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		float effect = stats.getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);

		stats.getDynamic().getMod(Stats.PHASE_CLOAK_FLUX_LEVEL_FOR_MIN_SPEED_MOD).modifyPercent(id, FLUX_THRESHOLD_INCREASE_PERCENT);
		stats.getDynamic().getStat(Stats.PHASE_TIME_BONUS_MULT).modifyMult(id, phaseMult);
		stats.getPeakCRDuration().modifyMult(id, peakMult);
		stats.getCRLossPerSecondPercent().modifyPercent(id, DEGRADE_INCREASE_PERCENT * effect);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		float effect = 1f;
		if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		//float phaseMult = PHASE_BONUS_MULT + (1f - PHASE_BONUS_MULT) * (1f - effect);
		float peakMult = PEAK_PERFORMANCE_MULT + (1f - PEAK_PERFORMANCE_MULT) * (1f - effect);

		if (index == 0) return "" + (int) (Math.round(FLUX_THRESHOLD_INCREASE_PERCENT) + 40) + "%";
		if (index == 1) return "50%";
		if (index == 2) return "30%";
		if (index == 3) return "" + (int) Math.round((1f - peakMult) * 100f) + "%";
		if (index == 4) return "" + (int) Math.round(DEGRADE_INCREASE_PERCENT * effect) + "%";
		if (index == 5) return "exponentially";
		if (index == 6) return "D-Mod";
		if (index == 7) return "cannot be removed";
		return null;
	}

}
