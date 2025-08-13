package Meatball.w_w.data.campaign;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.impl.campaign.ids.ShipRoles;
import com.fs.starfarer.api.impl.campaign.missions.HandMeDownFreighter;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import java.util.HashSet;

import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.FREIGHTER_LARGE;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.FREIGHTER_MEDIUM;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.FREIGHTER_SMALL;

import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.TANKER_LARGE;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.TANKER_MEDIUM;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.TANKER_SMALL;

import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.LINER_LARGE;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.LINER_MEDIUM;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.LINER_SMALL;

import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.COMBAT_FREIGHTER_LARGE;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.COMBAT_FREIGHTER_MEDIUM;
import static com.fs.starfarer.api.impl.campaign.ids.ShipRoles.COMBAT_FREIGHTER_SMALL;


//public static class WeightedRandomPicker<String> FREIGHTERS = new WeightedRandomPicker<>();

public class HandDownListener extends BaseCampaignEventListener{

    private static final WeightedRandomPicker<String> FREIGHTERS = new WeightedRandomPicker<>();
    //role picker
    static {
        FREIGHTERS.add(ShipRoles.FREIGHTER_LARGE, 2F);
        FREIGHTERS.add(ShipRoles.FREIGHTER_MEDIUM, 4F);
        FREIGHTERS.add(ShipRoles.FREIGHTER_SMALL, 4F);
        FREIGHTERS.add(ShipRoles.COMBAT_FREIGHTER_LARGE, 2F);
        FREIGHTERS.add(ShipRoles.COMBAT_FREIGHTER_MEDIUM, 4F);
        FREIGHTERS.add(ShipRoles.COMBAT_FREIGHTER_SMALL, 4F);
        FREIGHTERS.add(ShipRoles.LINER_LARGE, 2F);
        FREIGHTERS.add(ShipRoles.LINER_MEDIUM, 4F);
        FREIGHTERS.add(ShipRoles.LINER_SMALL, 4F);
        FREIGHTERS.add(ShipRoles.TANKER_LARGE, 2F);
        FREIGHTERS.add(ShipRoles.TANKER_MEDIUM, 4F);
        FREIGHTERS.add(ShipRoles.TANKER_SMALL, 4F);
    }

    public HandDownListener(boolean permaRegister) {
        super(permaRegister);
    }

    @Override
    public void reportShownInteractionDialog(InteractionDialogAPI dialog) {
        if (dialog.getInteractionTarget() == null || dialog.getInteractionTarget().getMarket() == null || dialog.getInteractionTarget().getMarket().getFaction() == null) return;

        WeightedRandomPicker<String> newHullPicker = new WeightedRandomPicker<>();
        //hull picker within picked role
        WeightedRandomPicker<String> oldHullPicker = HandMeDownFreighter.HULLS.clone();
        //old selection fallback
        //WeightedRandomPicker<String> FREIGHTERS = new WeightedRandomPicker<>();

        HashSet<String> done = new HashSet<String>();
        //chosen ships within the selected role

        for (String variantId : dialog.getInteractionTarget().getMarket().getFaction().getVariantsForRole(FREIGHTERS.pick())) {
            String hullId = Global.getSettings().getVariant(variantId).getHullSpec().getHullId();
            newHullPicker.add(hullId, 1f);
            if (done.contains(hullId)) {
                continue;
            }
            done.add(hullId);
        }



        HandMeDownFreighter.HULLS = newHullPicker;

        Global.getSector().addTransientScript(new EveryFrameScript() {
            private boolean isDone = false;

            @Override
            public void advance(float arg0) {
                if (Global.getSector().getCampaignUI().getCurrentInteractionDialog() == null) {
                    HandMeDownFreighter.HULLS = oldHullPicker;
                //if no logistics ships for current faction who owns market, fallback option is used
                    isDone = true;
                    Global.getSector().removeTransientScript(this);
                }
            }

            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public boolean runWhilePaused() {
                return true;
            }
            
        });
    }
}
