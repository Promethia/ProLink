package net.promethiamc.link.command;

import java.util.HashMap;

import org.bukkit.Location;

public abstract class Command {

  private static HashMap<String, Class<?>> commands = new HashMap<String, Class<?>>();

  protected String[] args;

  protected Location location;

  public abstract boolean validateCommandSyntax();

  public abstract String getCommandSyntax();

  public abstract void handleCommand();

  public String[] getArgs() {
    return args;
  }

  /**
   * Sets the command arguments for later use, removes command itself keeping
   * only the parameters
   */
  public void setArgs(String[] args) {
    if (args == null) {
      this.args = null;
      return;
    }

    String[] newArgs = new String[args.length - 1];

    for (int i = 1; i < args.length; i++)
      newArgs[i - 1] = args[i];

    this.args = newArgs;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public static boolean handleCommand(Location location, String[] args) {
    if (!commands.containsKey(args[0]))
      return false;

    try {
      Command cmd = (Command) commands.get(args[0]).newInstance();
      cmd.setArgs(args);
      cmd.setLocation(location);

      if (!cmd.validateCommandSyntax()) {
        System.out.println("Invalid command syntax, usage: " + cmd.getCommandSyntax());
        return false;
      }

      cmd.handleCommand();

    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return true;
  }

  public static void registerCommand(String commandName, Class<?> commandClass) {
    commands.put(commandName, commandClass);
  }

  public static Command getCommand(String name) {
    return getCommand(name, null, null);
  }

  public static Command getCommand(String name, Location location, String[] args) {
    Command command = null;

    try {
      if (!commands.containsKey(name))
        return null;

      command = (Command) commands.get(name).newInstance();
      command.setArgs(args);
      command.setLocation(location);
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }

    return command;
  }

}
