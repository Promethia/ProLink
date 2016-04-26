package net.promethiamc.link.sign;

import java.util.HashMap;

import net.promethiamc.link.PromethiaLink;
import net.promethiamc.link.command.Command;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public abstract class ControlSign {

  protected Sign sign;

  private static HashMap<String, Class<?>> types = new HashMap<String, Class<?>>();

  // Empty constructor required for the reflection method .newInstance()
  public ControlSign() {}

  /**
   * Validates the syntax for the given command. By default uses the syntax
   * defined in the corresponding Command class. Override this method for custom
   * or additional validation.
   */
  public boolean validateCommandSyntax() {
    return Command.getCommand(getCommandName(), sign.getLocation(), getCommandArgs()).validateCommandSyntax();
  }

  /**
   * @return Command syntax string. Override this method for custom or
   *         additional syntax.
   */
  public String getCommandSyntax() {
    return Command.getCommand(getCommandName()).getCommandSyntax();
  }

  /**
   * Handles the action that should be performed when the sign receives a
   * signal. Defaults to the defined Command action. Override this method for
   * custom or additional actions.
   */
  public void handleRedstoneAction() {
    if (sign.getBlock().getBlockPower() == 0)
      Command.handleCommand(sign.getLocation(), getCommandArgs());
  }

  private String getCommandName() {
    return sign.getLine(1).split(" ")[0];
  }

  private String[] getCommandArgs() {
    return sign.getLine(1).split(" ");
  }

  public Sign getSign() {
    return sign;
  }

  public void setSign(Sign sign) {
    this.sign = sign;
  }

  public static boolean isControlSign(Block block) {
    if (!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN))
      return false;

    return isControlSign(((Sign) block.getState()).getLines());
  }

  public static boolean isControlSign(String[] lines) {
    if (!lines[0].equals("[prolink]"))
      return false;

    return true;
  }

  public static void registerType(String typeName, Class<?> typeClass) {
    types.put(typeName, typeClass);
  }

  public static ControlSign create(Sign sign, Player player) {
    return create(sign.getLines(), sign, player);
  }

  public static ControlSign create(String[] lines, Sign block, Player player) {
    String[] commandType = lines[1].split(" ");

    // If no command is given, do not bother
    if (commandType.length == 0) {
      System.out.println("A control sign requires a command, nothing given");
      return null;
    }

    if (!types.containsKey(commandType[0])) {
      System.out.println("Unrecognized command given for this control sign");
      return null;
    }

    ControlSign controlSign = null;

    // Apparently mc doesn't set these lines when using BlockStates
    for (int i = 0; i < lines.length; i++)
      block.setLine(i, lines[i]);

    try {
      controlSign = (ControlSign) types.get(commandType[0]).newInstance();
      controlSign.setSign(block);

      if (!controlSign.validateCommandSyntax()) {
        System.out.println("Invalid syntax for this control sign, usage: " + controlSign.getCommandSyntax());
        return null;
      }

    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }

    PromethiaLink.addControlSign(controlSign);

    return controlSign;
  }

  public static void handleSignDestruction(Sign sign) {
    PromethiaLink.deleteControlSign(sign.getLocation());
  }

}
