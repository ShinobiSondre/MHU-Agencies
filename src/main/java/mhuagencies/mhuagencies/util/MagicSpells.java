package mhuagencies.mhuagencies.util;

import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.Spellbook;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MagicSpells {

    public HashMap<Player, String> previousspells = new HashMap<>();
    public HashMap<Player, Player> players = new HashMap<>();




    public void giveSpells(Player sender, String spellgroup, String text, ChatColor c) {

        Spellbook s1 = com.nisovin.magicspells.MagicSpells.getSpellbook(sender);

        previousspells.put(sender, s1.getSpellBookGroup());
        players.put(sender,sender);

        s1.removeAllSpells();
        s1.addGrantedSpells();

        List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(spellgroup)).collect(Collectors.toList());

        if (spellsInGroup.size() != 0) {


            spellsInGroup.forEach(spell -> {


                s1.addSpell(spell);

            });
            s1.setSpellBookGroup(spellgroup);
            s1.save();


            sender.sendMessage(c + text);


        }


    }


    public void returnSpells(Player sender, String message) {

        Spellbook s2 = com.nisovin.magicspells.MagicSpells.getSpellbook((Player) sender);
        String spellgroup = previousspells.get(sender);
        players.remove(sender);

        s2.removeAllSpells();
        s2.addGrantedSpells();

        List<Spell> spellsInGroup = (List<Spell>) com.nisovin.magicspells.MagicSpells.spells().stream().filter(s -> s.getSpellGroups().contains(spellgroup)).collect(Collectors.toList());

        if (spellsInGroup.size() != 0) {


            spellsInGroup.forEach(spell -> {


                s2.addSpell(spell);

            });
            s2.setSpellBookGroup(spellgroup);
            s2.save();

                sender.sendMessage(message);


        }
    }


}
