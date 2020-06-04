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

public final class MHUAgencies extends JavaPlugin implements Listener,CommandExecutor {

    double expsharescore;
    double previousscore;
    Player killer;
    Player player;

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.print("--------------------------------------" + "\n" + "MHU-Agencies" + "\n" + "--------------------------------------");

        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("agencydefenders").setExecutor(new MHUAgencies());
    }



    public ActiveMob spawnDefenders(String MobName, String killer) {

        MobManager mm = MythicMobs.inst().getMobManager();

        Location loc1 = new Location(Bukkit.getPlayer(killer).getWorld(), Bukkit.getPlayer(killer).getLocation().getX() + 4, Bukkit.getPlayer(killer).getLocation().getY(), Bukkit.getPlayer(killer).getLocation().getZ() + 4);

        return mm.spawnMob(MobName, loc1);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (command.getName().equalsignorecase("agencydefenders")) {

            String[] mobs = {"D1,D2,D3,D4"};

            if(mobs.equals("")){

            }

            for (int i = 0; i<5; i++) {
                spawnDefenders(mobs[i],player.getName());
            }
            player.sendMessage("The Defenders have been placed");

        }

        return false;}


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