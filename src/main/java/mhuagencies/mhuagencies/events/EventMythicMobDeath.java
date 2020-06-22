package mhuagencies.mhuagencies.events;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.puharesource.mc.titlemanager.api.v2.animation.Animation;
import io.puharesource.mc.titlemanager.api.v2.animation.AnimationFrame;
import io.puharesource.mc.titlemanager.internal.model.animation.AnimationSendablePart;
import mhuagencies.mhuagencies.util.MagicSpells;
import mhuagencies.mhuagencies.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class EventMythicMobDeath implements Listener {

    Util util;
    MagicSpells ms;


    public EventMythicMobDeath(Util utils) {
        this.util = utils;
    }




    @EventHandler
    public void LoginEvent(PlayerLoginEvent killer3){

        Player killer = killer3.getPlayer();

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set NomuRaidStart false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set ExplosionStart false");

    }


    @EventHandler
    public void DeathEvent(PlayerDeathEvent killer2){


        Player killer = (Player) killer2.getEntity();

        if (ms.players.get(killer) !=null){

            ms.returnSpells(killer,"Quirk Returned");

        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set NomuRaidStart false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set ExplosionStart false");


    }


    @EventHandler
    public void LeaveEvent(PlayerQuitEvent killer1){

        Player killer = killer1.getPlayer();

        if (ms.players.get(killer)!=null){

            ms.returnSpells(killer,"Quirk Returned");

        }



        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set NomuRaidStart false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+killer.getName() + " set ExplosionStart false");

    }


    @EventHandler
    public void MobCounter(MythicMobDeathEvent d) throws InterruptedException {

        Player killer = (Player) d.getKiller();
        int score = util.playerScore.get(killer);
        util.leader.put(killer,util.leaderscore);
        util.leaderscore.add(score);


        if (util.start1 == true) {
            if (Collections.max(util.leaderscore).equals(score) && score >0) {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if (player.hasPermission("NomuRaidStart") || player.hasPermission("ExplosionStart")) {
                        util.titleManagerAPI.setProcessedScoreboardValue(player, 1, "Leader: " + killer.getName());


                    }
                }
            }

        }



        //END



        if (killer.hasPermission("NomuRaidStart")|| killer.hasPermission("ExplosionStart")) {

            double barscore = util.bossbarScore.get(killer);
            util.playerBossbar.get(killer).setProgress(barscore);

            if (killer.hasPermission("ExplosionStart"))
            util.titleManagerAPI.setProcessedScoreboardValue(killer, 2, ChatColor.GOLD + "Targets hit: " + score);
            if (killer.hasPermission("NomuRaidStart"))
                util.titleManagerAPI.setProcessedScoreboardValue(killer, 2, ChatColor.RED + "Nomus killed: " + score);
            util.playerScore.put(killer, score + 1);
            util.bossbarScore.put(killer, barscore + 0.1);


            if (barscore >= 0.9) {

                util.leaderscore.removeAll(util.leaderscore);

                for (Player player : Bukkit.getServer().getOnlinePlayers()) {

                    util.leader.remove(player);

                    if (player.hasPermission("NomuRaidStart"))
                    player.sendMessage(ChatColor.BOLD + killer.getDisplayName() + ChatColor.GREEN + " Won the NomuRaid event!");
                    if (player.hasPermission("ExplosionStart"))
                        player.sendMessage(ChatColor.BOLD + killer.getDisplayName() + ChatColor.GREEN + " Won the Explosion-HTP event!");

                    if (player.hasPermission("NomuRaidStart")|| player.hasPermission("ExplosionStart")) {

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"stopsound "+player.getName());

                        if (player == killer){util.titleManagerAPI.sendTitle(killer, ChatColor.GREEN + "Winner");}
                        else{
                        util.titleManagerAPI.sendTitle(player, ChatColor.DARK_RED + "Game Over");}

                            if (player.hasPermission("NomuRaidStart"))
                            util.looseEvent(player,"NomuRaidStart","", "spawn", ChatColor.RED);
                            if (player.hasPermission("ExplosionStart"))
                                util.looseEvent(killer,"ExplosionStart","ExplosionStart","spawn",ChatColor.RED);

                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+player.getName() + " set NomuRaidStart false");
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+player.getName() + " set ExplosionStart false");


                            }
                        }
                    }

        /*if (!Arrays.asList(util.scoremap).contains(killer))
            util.i = 1;

    }*/
                }
            }

        }
