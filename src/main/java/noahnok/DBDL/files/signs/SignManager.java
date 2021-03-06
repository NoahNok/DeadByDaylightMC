package noahnok.DBDL.files.signs;

;


import noahnok.DBDL.files.DeadByDaylight;
import noahnok.DBDL.files.game.DGame;
import noahnok.DBDL.files.utils.Config;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;


public class SignManager {


    private DeadByDaylight main;


    private List<DSign> signs = new ArrayList<DSign>();

    public SignManager(DeadByDaylight main) {
        this.main = main;
    }

    public List<DSign> getSigns() {
        return signs;
    }

    public void setSigns(List<DSign> signs) {
        this.signs = signs;
    }

    public void saveSignsToFile(){
        int count = 1;
        for (DSign sign : signs){
            String path = "signs."+count+".";
            main.getSignConfig().getConfig().set(path+"loc", sign.getSignBlock().getLocation());
            count++;
        }



        main.getSignConfig().saveConfig();
    }

    public void loadSignsFromFile() {
        try {


            for (String key : main.getSignConfig().getConfig().getConfigurationSection("signs").getKeys(false)) {
                String path = "signs." + key + ".loc";
                Location loc = (Location) main.getSignConfig().getConfig().get(path);

                DSign newSign = new DSign(loc.getBlock());
                signs.add(newSign);

            }

        } catch (NullPointerException e){
            main.getLogger().warning("No signs were found!");
        }
    }

    public DSign getSign(DGame game){
        for (DSign sign : signs){
            if (sign.getGame().equals(game)){
                return sign;
            }
        }
        return null;
    }

    public DSign getSign(Block b){
        for (DSign sign : signs){
            if (sign.getSignBlock().equals(b)){
                return sign;
            }
        }
        return null;
    }

    public void removeSign(Block b){
        for (DSign sign : signs){
            if (sign.getSignBlock().equals(b)){
                sign.setGame(null);
                signs.remove(sign);
                sign = null;
            }
        }
    }
}
