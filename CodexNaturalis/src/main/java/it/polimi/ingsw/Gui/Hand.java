package it.polimi.ingsw.Gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.player.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


public class Hand {
    public static void main(String[] args) {
        Map<Integer,String> paths_gold = new HashMap<>();
        Map<Integer,String> paths_resource = new HashMap<>();

        // GOLD ------------------------------------

        for(int i=41; i<81; i++){
            paths_gold.put(i,"C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\GoldCards\\GoldBack\\0" + i + ".png");
        }

        for(int i=41; i<81; i++){
            paths_gold.put(i+40,"C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\GoldCards\\GoldFront\\" + "0" + i + ".png");
        }


        // RESOURCE ---------------------------------------


        for (int i=1; i<41; i++){
            paths_resource.put(i,"C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceBack\\0" + i + ".png");
        }

        for (int i=1; i<41; i++){
            paths_resource.put(i+40,"C:\\Users\\vital\\Desktop\\ing-sw-2024-Fregna-Introzzi-LoPiccolo-Mandara\\CodexNaturalis\\src\\main\\resources\\ResourceCards\\ResourceFront\\0" + i + ".png");
        }


    }
}
