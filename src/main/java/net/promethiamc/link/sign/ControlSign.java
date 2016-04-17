package net.promethiamc.link.sign;

import net.promethiamc.link.PromethiaLink;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class ControlSign {

  public static final byte ACTION_NONE = 0x00;
  public static final byte ACTION_UNKNOWN = 0x01;
  public static final byte ACTION_INVALID = 0x02;
  public static final byte ACTION_SPAWN = 0x03;

  public static byte validateSign(Block block) {
    if (!block.getType().equals(Material.SIGN_POST) && !block.getType().equals(Material.WALL_SIGN))
      return ACTION_NONE;

    return validateSign((Sign) block.getState());
  }

  public static byte validateSign(Sign sign) {
    String lineFirst = sign.getLine(1);
    if (lineFirst.startsWith("spawn")) {
      String[] line = lineFirst.split(" ");

      if (line.length < 3)
        return ACTION_INVALID;

      if (Integer.parseInt(line[2]) > 12)
        return ACTION_INVALID;

      return ACTION_SPAWN;
    }

    return ACTION_UNKNOWN;
  }

  public static void handleRedstoneAction(Block block) {
    byte action = validateSign(block);

    if (action == ACTION_NONE || action == ACTION_UNKNOWN || action == ACTION_INVALID)
      return;

    switch (action) {
      case ACTION_NONE:
        System.out.println("Action not found");
        break;
      case ACTION_SPAWN:
        handleActionSpawn((Sign) block.getState());
        break;
    }
  }

  public static void handleActionSpawn(Sign sign) {
    if (sign.getBlock().getBlockPower() > 0)
      PromethiaLink.createTrain(sign.getLocation(), Integer.parseInt(sign.getLine(1).split(" ")[2]), PromethiaLink.getDirection(sign.getLine(1).split(" ")[1]));
  }

}
