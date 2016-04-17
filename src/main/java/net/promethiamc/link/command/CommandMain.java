package net.promethiamc.link.command;

import net.promethiamc.link.PromethiaLink;
import net.promethiamc.link.entity.Train;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {

  // Command 'plink'
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "spawn":
          handleSpawnCommand(sender, args);
          break;
        case "removeall":
          handleRemoveAllCommand(sender);
          break;
      }
    }

    return true;
  }

  private void handleSpawnCommand(CommandSender sender, String[] args) {
    if (args.length < 3) {
      PromethiaLink.log(sender, "Invalid use of spawn command, usage: /plink spawn <carts> <direction>");
      return;

    } else if (args.length == 3 && !(sender instanceof Player)) {
      PromethiaLink.log(sender, "Command not sent by player, when send without player you should include a location, usage: /plink spawn <carts> <direction> <x> <y> <z>");
      return;

    } else if (args.length > 3 && args.length < 6) {
      PromethiaLink.log(sender, "Location should be an xyz world location, usage: /plink spawn <carts> <direction> <x> <y> <z>");
      return;
    }

    // We don't care about NumberFormatExceptions when parsing Integers here, just use integers when using these commands...
    if (args.length == 3) {
      PromethiaLink.createTrain(((Player) sender).getLocation(), Integer.parseInt(args[2]), PromethiaLink.getDirection(args[1]));

    } else if (args.length == 6) {
      Location location = new Location(PromethiaLink.getWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
      PromethiaLink.createTrain(location, Integer.parseInt(args[4]), PromethiaLink.getDirection(args[1]));
    }
  }

  private void handleRemoveAllCommand(CommandSender sender) {
    int removedTrains = 0, removedCarts = 0;

    for (Train train : PromethiaLink.getTrains()) {
      removedTrains++;
      removedCarts += train.getSize();

      PromethiaLink.removeTrain(train);
    }

    PromethiaLink.log(sender, "Removed " + removedTrains + " trains, " + removedCarts + " carts");
  }

}
