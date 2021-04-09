package me.lokka30.elementalmenus.commands;

import me.lokka30.elementalmenus.ElementalMenus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class ElementalMenusCommand implements TabExecutor {

    private final ElementalMenus main;
    public ElementalMenusCommand(final ElementalMenus main) { this.main = main; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        return suggestions;
    }
}
