package net.promethiamc.link;

import net.promethiamc.link.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
    return Command.handleCommand((sender instanceof Player ? ((Player) sender).getLocation() : null), args);
  }

}
