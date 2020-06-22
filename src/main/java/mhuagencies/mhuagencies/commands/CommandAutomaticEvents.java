package mhuagencies.mhuagencies.commands;

import com.comphenix.protocol.PacketType;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import it.unimi.dsi.fastutil.Hash;
import mhuagencies.mhuagencies.AutomaticEvents;
import mhuagencies.mhuagencies.events.EventMythicMobDeath;
import mhuagencies.mhuagencies.util.ExplosionEvent;
import mhuagencies.mhuagencies.util.MagicSpells;
import mhuagencies.mhuagencies.util.Util;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.server.v1_12_R1.Explosion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.*;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class CommandAutomaticEvents implements CommandExecutor, Listener {

    Util util;
    MagicSpells ms = new MagicSpells();
    BossBar start = null;
    HashMap<Player, BossBar> playerBossbar = new HashMap<>();
    HashMap<Player,Double> bossbarScore = new HashMap<>();
    HashMap<Player, Player> players = new HashMap<>();
    HashMap<Player,Timer> timer = new HashMap<>();
    Timer t = new Timer();
    boolean stop;
    AutomaticEvents autoevent;
    public Map<String, Integer> taskID = new HashMap<String, Integer>();
    public CommandAutomaticEvents(Util utils, AutomaticEvents auto) {
        this.autoevent = auto;
        this.util = utils;
    }


    //call this to schedule the task
    public void scheduleRepeatingTask(final Player sender, long ticks){

        final int tid = autoevent.getServer().getScheduler().scheduleSyncRepeatingTask(autoevent, new Runnable(){
            public void run(){
                bossbarScore.put(sender,bossbarScore.get(sender)-0.1);
                start.setProgress(bossbarScore.get(sender));
                sender.sendMessage("" + bossbarScore.get(sender));
                //END
                if (bossbarScore.get(sender) <= 0.1){
                    ms.returnSpells(sender,"Quirk Returned");
                    start.removePlayer(sender);
                    bossbarScore.remove(sender);
                    players.remove(sender,sender);
                    endTask(sender);

                }
            }
        },0, 200); //schedule task with the ticks specified in the arguments

        taskID.put(sender.getName(), tid); //put the player in a hashmap
    }

    //call this to end the task
    public void endTask(Player p){
        if(taskID.containsKey(p.getName())){
            int tid = taskID.get(p.getName()); //get the ID from the hashmap
            autoevent.getServer().getScheduler().cancelTask(tid); //cancel the task
            taskID.remove(p.getName()); //remove the player from the hashmap
        }
    }


    @EventHandler
    public void PlayerMessage(PlayerCommandPreprocessEvent p) throws Exception {


       /* if(p.getMessage().contains("groupremove")){

            Player sender = p.getPlayer();
            String[] message = p.getMessage().split(" ");

            String group = message[1];


        }*/

        if(p.getMessage().contains("groups help") || p.getMessage().contains("g help")&&!p.getMessage().contains("svs")){

            Player sender = p.getPlayer();
            sender.sendMessage("\n" + " " + ChatColor.RED + "Commands: " + "\n" + " " + "\n");

            sender.sendMessage("\n" + ChatColor.RED + "/group list");
            sender.sendMessage("Usage: /groups list");
            sender.sendMessage("Alias: /g list");
            sender.sendMessage("\n" + ChatColor.RED + "/groups add");
            sender.sendMessage("Usage: /groups add SomeQuirk");
            sender.sendMessage("Alias: /g add SomeQuirk");
            sender.sendMessage("\n" + ChatColor.RED + "/groups search");
            sender.sendMessage("Alias: /g search");
            sender.sendMessage("Usage: /groups search Ex will return ExplosionQuirk");
            sender.sendMessage("            the groups are casesensitive!");
            sender.sendMessage("\n" + ChatColor.RED + "/quirk test");
            sender.sendMessage("Usage: /quirk test ExplosionQuirk for example");
            sender.sendMessage("Alias: /qt ExplosionQuirk");

            sender.sendMessage("\n" + " " + ChatColor.RED + "   E.N.D" + "\n" + " " + "\n");
        }

       if(p.getMessage().contains("groups list")|| p.getMessage().contains("g list")&&!p.getMessage().contains("svs")){

           Player sender = p.getPlayer();

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("plugins/MagicSpells/spells-command.yml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            sender.sendMessage("\n" + " " + ChatColor.RED + "Quirk Groups: " + "\n" + " " + "\n");
            while (line != null) {
                //dataParts = line.split("-" + " "); // since your delimiter is "|"
                if (line.contains("- ") && !line.contains("- %")){

                    String[] list = line.split("-");
                    sender.sendMessage(ChatColor.WHITE + list[1]);}
                // read next line
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sender.sendMessage("\n" + " " + ChatColor.RED + "   E.N.D" + "\n" + " " + "\n");
        }


        if (p.getMessage().contains("groups add") || p.getMessage().contains("g add")){

            Player sender = p.getPlayer();
            String[] message = p.getMessage().split(" ");

            String group = message[2];

            String filename = "plugins/MagicSpells/spells-command.yml";
            FileWriter fw = new FileWriter(filename,true); //the true will append the new data
            fw.write("\n" + "        - " + group);//appends the string to the file
            fw.close();

            sender.sendMessage(ChatColor.GREEN + group + " added!");


        }


        if (p.getMessage().contains("groups search")|| p.getMessage().contains("g search")&&!p.getMessage().contains("svs")){
            Player sender = p.getPlayer();
            String[] message = p.getMessage().split(" ");

            String group = message[2];

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("plugins/MagicSpells/spells-command.yml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> quirkgroups = null;
            String[] dataParts = null;

            sender.sendMessage("\n" + " " + ChatColor.RED + "Search Result: " + "\n" + " " + "\n");
            while (line != null) {
                //dataParts = line.split("-" + " "); // since your delimiter is "|"
                if (line.contains("- ") && !line.contains("- %") && line.contains("- " + group)){

                    String[] list = line.split("-");
                    sender.sendMessage(ChatColor.WHITE + list[1]);}
                // read next line
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sender.sendMessage("\n" + " " + ChatColor.RED + "   E.N.D" + "\n" + " " + "\n");
            reader.close();

        }



        if (p.getMessage().contains("quirk test") || p.getMessage().contains("qt") &&!p.getMessage().contains("svs")) {
            Player sender = p.getPlayer();
            timer.put(sender,t);

            if (players.get(sender) !=null) {
                sender.sendMessage("Wait until your quirk has returned");

            }
            else{
                players.put(sender,sender);
            String quirktestmessage = " j";
            String[] split = p.getMessage().split(" ");

            if(p.getMessage().contains("qt")&&!p.getMessage().contains("svs")){
            quirktestmessage = split[1];}
            else{
            quirktestmessage = split[2];}
            sender.sendMessage("---------------------------------");
            ms.giveSpells(sender,quirktestmessage,"The quirk will be enabled for 1 minute" + "\n" + "           " + "\n" +  "unless you die or disconnect" + "\n" + "           " + "\n" + "whilst the timer is active.",ChatColor.RED);
            sender.sendMessage("---------------------------------");
            playerBossbar.put(sender, start);

            start = playerBossbar.get(sender);

            start = Bukkit.getServer().createBossBar(ChatColor.BOLD + "" + ChatColor.RED + "Remaining Time",BarColor.RED,BarStyle.SEGMENTED_6,BarFlag.DARKEN_SKY);
            start.setVisible(true);
            start.addPlayer(sender);
            bossbarScore.put(sender,0.9);


            //TimerTask

           //timer(sender);


                scheduleRepeatingTask(sender,200);

        }}


    }


    public void timer(Player sender){


        Timer t = new Timer();

        bossbarScore.put(sender, 0.9);
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                bossbarScore.put(sender,bossbarScore.get(sender)-0.1);
                start.setProgress(bossbarScore.get(sender));
                sender.sendMessage("" + bossbarScore.get(sender));
                //END
                if (bossbarScore.get(sender) <= 0.1){
                    ms.returnSpells(sender,"Quirk Returned");
                    start.removePlayer(sender);
                    bossbarScore.remove(sender);
                    players.remove(sender,sender);
                    t.cancel();

                }

            }
        }, 0, 6500);
        }

    @EventHandler
    public void DeathEvent(PlayerDeathEvent killer2){


        Player killer = (Player) killer2.getEntity();

        if (ms.players.get(killer) !=null){
            stop = true;
            ms.returnSpells(killer,"Quirk Returned");
            start.removePlayer(killer);
            bossbarScore.remove(killer);
            playerBossbar.remove(killer);
            players.remove(killer);
            t.cancel();


        }}

    @EventHandler
    public void LeaveEvent(PlayerQuitEvent killer1){

        Player killer = killer1.getPlayer();


        if (ms.players.get(killer) !=null){
            stop = true;
            ms.returnSpells(killer,"Quirk Returned");
            start.removePlayer(killer);
            bossbarScore.remove(killer);
            playerBossbar.remove(killer);
            players.remove(killer);
            t.cancel();

        }}



        @Override
    public boolean onCommand(CommandSender sender1, Command cmd, String cmdString, String[] args) {

        Player sender = (Player) sender1;

        if (cmd.getName().equals("explodejoin")) {
            // main logic
            util.joinEvent(sender,"ExplosionStart","ExplosionStart","ExplosionQuirk",""+ChatColor.GOLD + "Explosion Hit The Target", BarColor.RED, BarStyle.SEGMENTED_10,
                    BarFlag.CREATE_FOG,"Explosion-HTP"

                    ,"Points" ,"course too tough?", ChatColor.RED,ChatColor.GOLD,"SportsArena");
        }

        else if(cmd.getName().equals("explodequit")) {

            util.leaveEvent(sender,"ExplosionStart","ExplosionStart","spawn","? The parkour too tough?", ChatColor.GOLD);
        }

        else if (cmd.getName().equals("nomuraidquit")) {

            util.leaveEvent(sender,"NomuRaidStart","","spawn","? The Nomus too tough?", ChatColor.RED);
        }

        else if(cmd.getName().equals("nomuraid")) {

            util.joinEvent(sender,"NomuRaidStart","","",""+ChatColor.RED + "Nomu Factory Raid", BarColor.RED, BarStyle.SEGMENTED_10,
                    BarFlag.DARKEN_SKY,"Nomu-FR"

            ,"Points" ,"Nomus too tough?", ChatColor.RED,ChatColor.DARK_RED,"nomuraidstart");
        }

        else if (cmd.getName().equals("quirktest")){

            if (playerBossbar.get(sender.getPlayer())!=null){
                playerBossbar.remove(sender.getPlayer());
                bossbarScore.remove(sender.getPlayer());
               }

        }

        else if (cmd.getName().contains("groups lists")){


        }

        else if (cmd.getName().contains("groups search")){


        }

        else if (cmd.getName().contains("groups add")){


        }

            else if (cmd.getName().equals("groups help")){


            }


       /* else if (cmd.getName().equals("groupremove")){


        }*/



        else if(cmd.getName().contains("groups list")) {
        }
        return true;
    }
}
