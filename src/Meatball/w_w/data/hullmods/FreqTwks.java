package Meatball.w_w.data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BallisticRangefinder;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import static com.fs.starfarer.api.impl.hullmods.BallisticRangefinder.HYBRID_BONUS_MIN;


public class FreqTwks extends BaseHullMod {
	public static float ENERGY_RANGE_BONUS = 100f;
	public static float HYBRID_RANGE_BONUS = 200;
	public static float ENERGY_DAMAGE_PENALTY = -10f;
	public static float BEAM_DAMAGE_PENALTY = -10f;
	//public static float HYBRID_MULT = 2f;
	//public static float BEAM_TURN_PENALTY = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getBeamWeaponRangeBonus().modifyFlat(id, ENERGY_RANGE_BONUS);
		//stats.getBeamWeaponRangeBonus().modifyFlat(id, HYBRID_RANGE_BONUS);
		stats.getEnergyWeaponDamageMult().modifyPercent(id, ENERGY_DAMAGE_PENALTY);
		stats.getBeamWeaponDamageMult().modifyPercent(id, BEAM_DAMAGE_PENALTY);
		//stats.getBeamWeaponTurnRateBonus().modifyMult(id, 1f - BEAM_TURN_PENALTY * 0.01f);
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.addListener(new freqTweaksRangeModifier());

	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ENERGY_RANGE_BONUS;
		if (index == 1) return "" + (Math.round(ENERGY_DAMAGE_PENALTY) + 20) + "%";
		if (index == 2) return "doubled";
		//if (index == 1) return "" + (int) BEAM_TURN_PENALTY + "%";
		return null;
	}
	public static class freqTweaksRangeModifier implements WeaponBaseRangeModifier {

		@Override
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}

		@Override
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}

		@Override
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			if (weapon.getSpec().getMountType() != WeaponAPI.WeaponType.ENERGY &&
					weapon.getSpec().getMountType() != WeaponAPI.WeaponType.HYBRID) {
				return 0f;
			}
			float bonus = 0f;
			if (weapon.getSpec().getMountType() == WeaponAPI.WeaponType.HYBRID) {
				bonus = HYBRID_RANGE_BONUS; // +200f, so double range
			}
			if (weapon.getSpec().getMountType() == WeaponAPI.WeaponType.ENERGY) {
				bonus = ENERGY_RANGE_BONUS; // +100f
			}
			return bonus;
		}

	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP)&&!ship.getVariant().getHullMods().contains(HullMods.ADVANCEDOPTICS);
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP)) {
			return "Incompatible with High Scatter Amplifier";
		}
		if (ship.getVariant().getHullMods().contains(HullMods.ADVANCEDOPTICS)) {
			return "Incompatible with Advanced Optics";
		}
		return null;
	}


}
