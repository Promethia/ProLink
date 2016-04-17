package net.promethiamc.link.entity;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;

public class TrainLocomotive extends TrainCart {

  public TrainLocomotive(Train train, Minecart cart, byte direction) {
    super(train, cart, 0, direction);
  }

  public short tick() {
    updateDirection(0.4D);

    for (Entry<Integer, TrainCart> entry : train.getCarts().entrySet()) {
      TrainCart cart = entry.getValue();

      if (cart.getDirection() != direction) {
        cart.updateDirection(!cart.getVehicle().isEmpty() ? 0.4D / 3.0D * 2.25D : 0.2D);

        if (cart.getDirection() != direction)
          continue;
      }

      double x = (cart.getDirection() == Train.DIRECTION_W ? 1.55D : (cart.getDirection() == Train.DIRECTION_E ? -1.55D : 0.0D)) * cart.getTrainPosition();
      double z = (cart.getDirection() == Train.DIRECTION_S ? 1.55D : (cart.getDirection() == Train.DIRECTION_N ? -1.55D : 0.0D)) * cart.getTrainPosition();
      double y = cart.getVehicle().getLocation().getY();

      x += getVehicle().getLocation().getX();
      z += getVehicle().getLocation().getZ();

      Location location = new Location(getVehicle().getWorld(), x, y, z);

      // TODO Properly handle carts with players
      if (!cart.getVehicle().teleport(location)) {
        cart.updateDirection(0.4D / 3.0D * 2.0D); // Minecarts cannot teleport when a player is riding them (Urgh...)
      }
    }

    return direction;
  }
}
