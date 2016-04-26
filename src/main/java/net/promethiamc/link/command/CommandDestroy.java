package net.promethiamc.link.command;

import java.util.Iterator;

import net.promethiamc.link.PromethiaLink;
import net.promethiamc.link.entity.Train;

public class CommandDestroy extends Command {

  public CommandDestroy() {}

  @Override
  public boolean validateCommandSyntax() {
    if (args.length != 1)
      return false;

    if (!args[0].equals("all") && !args[0].equals("train"))
      return false;

    return true;
  }

  @Override
  public String getCommandSyntax() {
    return "destroy <all|train>";
  }

  @Override
  public void handleCommand() {
    if (args.length != 1) {
      System.out.println("The destroy command requires one additional parameter");
      return;

    } else if (!args[0].equals("all")) {
      System.out.println("When using as a command, only the 'all' type is allowed");
      return;
    }

    int removedTrains = 0, removedCarts = 0;

    for (Iterator<Train> it = PromethiaLink.getTrains().iterator(); it.hasNext();) {
      Train train = it.next();
      removedTrains++;
      removedCarts += train.getSize();

      PromethiaLink.removeTrain(train);
    }

    System.out.println("Removed " + removedTrains + " trains, " + removedCarts + " carts");
  }

}
