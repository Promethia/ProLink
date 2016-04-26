package net.promethiamc.link.command;

import net.promethiamc.link.PromethiaLink;
import net.promethiamc.link.entity.Train;

import org.bukkit.Location;

public class CommandSpawn extends Command {

  public CommandSpawn() {}

  @Override
  public boolean validateCommandSyntax() {    
    if (args.length < 2 || args.length > 5) {
      System.out.println("Invalid amount of arguments");
      return false;
    }

    if (args.length > 2 && args.length < 5) {
      System.out.println("Position is all or nothing");
      return false;
    }

    if (PromethiaLink.getDirection(args[0]) == Train.DIRECTION_UNKNOWN) {
      System.out.println("Unknown direction " + args[0]);
      return false;
    }

    try {
      Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public String getCommandSyntax() {
    return "spawn <direction> <carts> {<x> <y> <z>}";
  }

  @Override
  public void handleCommand() {
    short direction = PromethiaLink.getDirection(args[0]);
    int carts = Integer.parseInt(args[1]);

    if (args.length == 2 && location != null) {
      PromethiaLink.createTrain(location, carts, direction);

    } else if (args.length == 5) {
      Location location = new Location(PromethiaLink.getWorld("Summer Forest"), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
      PromethiaLink.createTrain(location, carts, direction);
    }
  }

}
