package mhuagencies.mhuagencies.util;

import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;
import io.lumine.utils.Time;
import io.lumine.utils.tasks.Scheduler;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractBossBar;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitBossBar;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.puharesource.mc.titlemanager.TitleManagerPlugin;
import io.puharesource.mc.titlemanager.api.v2.TitleManagerAPI;
import me.robin.battlelevels.api.BattleLevelsAPI;
import mhuagencies.mhuagencies.commands.CommandAutomaticEvents;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class Util {

    MagicSpells ms = new MagicSpells();


    public boolean start1 = false;
    public int i = 0;
    public ArrayList<Player> players = new ArrayList<Player>();

    public HashMap<Player, Player> playerjoined = new HashMap<Player, Player>();

    public HashMap<Player, ArrayList> leader = new HashMap<Player, ArrayList>();
    public ArrayList<Integer> leaderscore = new ArrayList<Integer>();
    public HashMap<Player, Integer> playerScore = new HashMap<Player, Integer>();
    public HashMap<Player, Double> bossbarScore = new HashMap<Player, Double>();
    public TitleManagerPlugin p = (TitleManagerPlugin) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");
    public HashMap<Player, BossBar> playerBossbar = new HashMap<>();

    public BossBar start;
    Player player;


    public TitleManagerAPI titleManagerAPI = (TitleManagerAPI) Bukkit.getServer().getPluginManager().getPlugin("TitleManager");


    public void countdown(CommandSender sender, Integer delay) {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        //Code here

                    }
                },
                delay
        );
    }


    public void looseEvent(CommandSender sender, String permission, String returnspellsperm, String warp, ChatColor c) {


        Player player = (Player) sender;
        playerjoined.remove(player);

        if (sender.hasPermission(permission)) {


            if(returnspellsperm!= ""){

                ms.returnSpells(player, "Minigame ended and you got your original quirk back :)");

            }

            playerBossbar.get(player).removePlayer(player);


            p.removeScoreboard(player);

            if (players.contains(sender)) {
                players.remove(sender);

                if (start != null) {
                    start.removePlayer(player);
                }


                p.giveDefaultScoreboard(player);
                p.titleManagerComponent.playerInfoService().showScoreboard(player, false);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), warp + " " + player.getName());
                p.titleManagerComponent.playerInfoService().showScoreboard(player, true);

                leaderscore.removeAll(leaderscore);

                leader.remove(player);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stopsound " + player.getName());
            } else {

            }
        } else {
            sender.sendMessage(c + "The event has not started yet");
        }

    }



    public void leaveEvent(CommandSender sender, String permission, String returnquirk, String warp, String quittext, ChatColor c) {



        Player player = (Player) sender;
        if (sender.hasPermission(permission)) {

            if (returnquirk != ""){
            ms.returnSpells(player,"Minigame ended and you got your original quirk back :)");}

            playerjoined.remove((Player)sender);

            playerBossbar.get(player).removePlayer(player);


            p.removeScoreboard(player);

            if (players.contains(sender)) {
                players.remove(sender);

                if (start != null) {
                    start.removePlayer(player);
                }


                p.giveDefaultScoreboard(player);
                p.titleManagerComponent.playerInfoService().showScoreboard(player, false);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), warp + " " + player.getName());
                p.titleManagerComponent.playerInfoService().showScoreboard(player, true);

                leaderscore.removeAll(leaderscore);

                    leader.remove(player);


                sender.sendMessage(c + "You quit " + sender.getName() + quittext);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"lp user "+player.getName() + " set " + permission + " false");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"stopsound "+player.getName());
            } else {
                sender.sendMessage(c + "");
            }
        }else{sender.sendMessage(c + "The event has not started yet");}

    }




    public void joinEvent(CommandSender sender, String permission, String quirkgivepermission, String quirkgroup,String title, BarColor color, BarStyle style, BarFlag flag, String ScoreBoardTitle,String ScoreBoardValue2,String quitcmdtxt,ChatColor c, ChatColor b, String warp ) {
        player = Bukkit.getPlayer(sender.getName());


        if (!players.contains(player)) {

            if (sender.hasPermission(permission)) {
                playerjoined.put((Player) sender, (Player) sender);
                p.removeScoreboard(player);

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warp " + warp + " " + player.getName());

                start1 = true;

                if (quirkgroup != "" && quirkgivepermission != "") {
                    ms.giveSpells(player, "ExplosionQuirk", "Explosion Quirk Enabled!", ChatColor.GOLD);
                }


                for (int i = 0; i < 3; i++) {


                    titleManagerAPI.setScoreboardTitle(player, b + ScoreBoardTitle);
                    titleManagerAPI.setScoreboardValue(player, 1, "Leader: ");
                    titleManagerAPI.setScoreboardValue(player, 2, c + ScoreBoardValue2 + ": 0");
                    titleManagerAPI.giveScoreboard(player);
                }

                playerScore.put(player, 1);


                        //BossBar
                        start = Bukkit.getServer().createBossBar(title, color, style, flag);
                        start.setVisible(true);
                        start.setProgress(0.0);
                        start.addPlayer(player);
                        playerBossbar.put(player, start);
                        bossbarScore.put(player, 0.1);

                        players.add(player);
                        sender.sendMessage(ChatColor.BOLD.GREEN + "Successfully joined the " + title + " Event!");

                }
            } else {
                sender.sendMessage(c + "The event has not started yet");

            if (sender instanceof Player) {
                if (players.contains(sender)) {
                    sender.sendMessage("                                       ");
                    sender.sendMessage("                                       ");
                    sender.sendMessage(c + "------------ " + b + " " + title + " " + c + " ------------");
                    sender.sendMessage("        " + c + sender.getName() + ChatColor.WHITE + " already joined");
                    sender.sendMessage("        " + ChatColor.WHITE + "Do " + c + quitcmdtxt);
                    sender.sendMessage("        " + ChatColor.WHITE + "to either be able to rejoin again or quit");
                    sender.sendMessage(c + "--------------- " + b + " E.N.D " + c + " ---------------");
                    sender.sendMessage("                                       ");
                    sender.sendMessage("                                       ");

                }
            }
        }
    }}



