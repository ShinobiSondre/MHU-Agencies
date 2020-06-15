package mhuagencies.mhuagencies.commands;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import mhuagencies.mhuagencies.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandAgencyDefenders implements CommandExecutor {
    Util util;

    public CommandAgencyDefenders(Util utils) {
        this.util = utils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdString, String[] args) {
        if(cmd.getName().equalsIgnoreCase("agencyDefenders")) {
            if(sender.hasPermission("")){
                // main logic
                String[] mobs = {"D1,D2,D3,D4"};
                if (mobs.equals("")) {
                }
                for (int i = 0; i < 5; i++) {
                    util.spawnDefenders(mobs[i], sender.getName());
                }
                sender.sendMessage("The Defenders have been placed");
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command!");
            }
        }
        return false;
    }
}
