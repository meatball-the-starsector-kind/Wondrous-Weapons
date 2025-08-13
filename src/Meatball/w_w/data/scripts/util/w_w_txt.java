/*
By Tartiflette
 */
package Meatball.w_w.data.scripts.util;

import com.fs.starfarer.api.Global;

public class w_w_txt {
    private static final String ML="tada";    
    
    public static String txt(String id){
        return Global.getSettings().getString(ML, id);
    }       
}