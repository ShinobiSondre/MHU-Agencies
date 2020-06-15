package mhuagencies.mhuagencies.util;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import me.robin.battlelevels.api.BattleLevelsAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Util {

    public ActiveMob spawnDefenders(String MobName, String killer) {
        MobManager mm = MythicMobs.inst().getMobManager();
        Location loc1 = new Location(Bukkit.getPlayer(killer).getWorld(), Bukkit.getPlayer(killer).getLocation().getX() + 4, Bukkit.getPlayer(killer).getLocation().getY(), Bukkit.getPlayer(killer).getLocation().getZ() + 4);
        return mm.spawnMob(MobName, loc1);
    }

    public double onlinefactionmembers(double expsharescore, Player killer){
        int i = 0;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.hasPermission("expshare.lvl1") && !(player.getName().equals(killer.getName()))){
                if (expsharescore == 0.0){expsharescore = (300);}
                player.sendMessage("You gained" + ChatColor.GREEN + " "+ expsharescore + " points from " + killer.getDisplayName());
                BattleLevelsAPI.addScore(player.getUniqueId(),expsharescore,true);
            }
            i++;}
        return i;
    }

}
