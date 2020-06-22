package mhuagencies.mhuagencies;

import mhuagencies.mhuagencies.commands.CommandAutomaticEvents;
import mhuagencies.mhuagencies.events.EventMythicMobDeath;
import mhuagencies.mhuagencies.util.MagicSpells;
import mhuagencies.mhuagencies.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

public final class AutomaticEvents extends JavaPlugin {
    Logger logger;

    public Map<String, Integer> taskID = new HashMap<String, Integer>();
    MagicSpells ms = new MagicSpells();
   public  HashMap<Player, Timer> timer = new HashMap<>();
    boolean check = false;
   public BossBar start = null;
    public HashMap<Player, BossBar> playerBossbar = new HashMap<>();
    public HashMap<Player,Double> bossbarScore = new HashMap<>();
    public HashMap<Player, Player> players = new HashMap<>();
    public Plugin pl = this;


    @Override
    public void onEnable() {
        // Plugin startup logic
        Util util = new Util();
        logger = Bukkit.getLogger();
        logger.info( "\n" + "\n" + "\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" + "--------------------------------------" + "\n" + "MHU-AutomaticEvents" + "\n" + "--------------------------------------"

                + "\n" + "\n" + "\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n");
        logger.info("Registering commands!");
        commandLoader(util);
        logger.info("Registering Events");
        eventLoader(util);
    }

    public void scheduleRepeatingTask(final Player p, long ticks, BossBar start){

        bossbarScore.put(p,0.9);
        final int tid = this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
            public void run(){
                bossbarScore.put(p,bossbarScore.get(p)-0.1);
                start.setProgress(bossbarScore.get(p));
                p.sendMessage("" + bossbarScore.get(p));
                //END
                if (bossbarScore.get(p) <= 0.1){
                    ms.returnSpells(p,"Quirk Returned");
                    start.removePlayer(p);
                    bossbarScore.remove(p);
                    players.remove(p,p);
                    endTask(p);}

            }
        },0, 6500); //schedule task with the ticks specified in the arguments

        taskID.put(p.getName(), tid); //put the player in a hashmap
    }


//call this to end the task
        public void endTask(Player p){
            if(taskID.containsKey(p.getName())){
                int tid = taskID.get(p.getName()); //get the ID from the hashmap
                this.getServer().getScheduler().cancelTask(tid); //cancel the task
                taskID.remove(p.getName()); //remove the player from the hashmap
            }
        }


        public void eventLoader(Util util) {
        getServer().getPluginManager().registerEvents(new EventMythicMobDeath(util), this);
        getServer().getPluginManager().registerEvents(new CommandAutomaticEvents(util,this), this);
    }

    public void commandLoader(Util util) {
        this.getCommand("nomuraid").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("nomuraidquit").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("explodejoin").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("explodequit").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("quirk test").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("groups help").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("groups list").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("groups search").setExecutor(new CommandAutomaticEvents(util,this));
        this.getCommand("groups add").setExecutor(new CommandAutomaticEvents(util,this));
        /*this.getCommand("groupremove").setExecutor(new CommandAutomaticEvents(util));*/
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}