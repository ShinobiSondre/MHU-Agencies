package mhuagencies.mhuagencies;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.robin.battlelevels.api.BattleLevelsAPI;
import me.robin.battlelevels.core.BattleLevels;
import me.robin.battlelevels.objects.BattlePlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MHUAgencies extends JavaPlugin implements Listener {

    double expsharescore;
    double previousscore;
    Player killer;
    Player player;

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.print("--------------------------------------" + "\n" + "MHU-Agencies" + "\n" + "--------------------------------------");

        this.getServer().getPluginManager().registerEvents(this, this);
    }



    @EventHandler
    public void AddExp(MythicMobDeathEvent d) throws InterruptedException {


        killer = (Player) d.getKiller();
        if (killer.hasPermission("expshare.lvl1")){
            expsharescore = (300)+onlinefactionmembers() + 10;
            if (expsharescore<50){expsharescore = 50;}
        }

    }

    public double onlinefactionmembers(){
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



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}