package Meatball.w_w;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import Meatball.w_w.data.campaign.HandDownListener;

public class MeatballPlugin extends BaseModPlugin {


    @Override
    public void onNewGame() {
        super.onNewGame();


        // The code below requires that Nexerelin is added as a library (not a dependency, it's only needed to compile the mod).
//        boolean isNexerelinEnabled = Global.getSettings().getModManager().isModEnabled("nexerelin");

//        if (!isNexerelinEnabled || SectorManager.getManager().isCorvusMode()) {
//                    new MySectorGen().generate(Global.getSector());
            // Add code that creates a new star system (will only run if Nexerelin's Random (corvus) mode is disabled).
//        }
    }


    public void onGameLoad() {
        Global.getSector().addTransientListener(new HandDownListener(false));
    }
}

