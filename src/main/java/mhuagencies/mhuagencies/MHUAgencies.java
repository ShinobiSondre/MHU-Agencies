package mhuagencies.mhuagencies;

import mhuagencies.mhuagencies.commands.CommandAgencyDefenders;
import mhuagencies.mhuagencies.events.EventMythicMobDeath;
import mhuagencies.mhuagencies.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MHUAgencies extends JavaPlugin {

    Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Util util = new Util();
        logger = Bukkit.getLogger();
        logger.info("--------------------------------------" + "\n" + "MHU-Agencies" + "\n" + "--------------------------------------");
        logger.info("Registering commands!");
        commandLoader(util);
        logger.info("Registering Events");
        eventLoader(util);
    }

    public void eventLoader(Util util) {
        getServer().getPluginManager().registerEvents(new EventMythicMobDeath(util), this);
    }

    public void commandLoader(Util util) {
        this.getCommand("agencydefenders").setExecutor(new CommandAgencyDefenders(util));
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}