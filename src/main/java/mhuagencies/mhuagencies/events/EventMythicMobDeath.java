package mhuagencies.mhuagencies.events;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import mhuagencies.mhuagencies.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EventMythicMobDeath implements Listener {

    Util util;

    public EventMythicMobDeath(Util utils) {
        this.util = utils;
    }

    @EventHandler
    public void AddExp(MythicMobDeathEvent d) throws InterruptedException {
        Player killer = (Player) d.getKiller();
        if (killer.hasPermission("expshare.lvl1")){
           double expsharescore = (300);
           expsharescore = expsharescore + util.onlinefactionmembers(expsharescore, killer) + 10;
            if (expsharescore<50){expsharescore = 50;}
        }

    }
}
