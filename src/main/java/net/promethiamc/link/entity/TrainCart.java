package net.promethiamc.link.entity;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

public class TrainCart {

  private Minecart cart;
  private Vector currentVelocity = new Vector(), lastVelocity = new Vector();
  private Location oldLocation;

  private byte direction;
  private int trainPosition;
  private boolean occupied;

  public TrainCart(Minecart cart, int trainPosition, byte direction) {
    this.cart = cart;
    this.trainPosition = trainPosition;
    this.direction = direction;

    this.cart.setInvulnerable(true);

    this.updateOldLocation();
  }

  public byte tick() {
    double velocity = 0.4D; // TODO Make this a config entry
    double deltaZ = getOldLocation().getBlockZ() - getVehicle().getLocation().getBlockZ();
    double deltaX = getOldLocation().getBlockX() - getVehicle().getLocation().getBlockX();

    // Not completely satisfied with this solution but it gets the job done for now
    if (deltaZ > 0.0D && (direction == Train.DIRECTION_E || direction == Train.DIRECTION_W))
      direction = Train.DIRECTION_S;

    else if (deltaZ < 0.0D && (direction == Train.DIRECTION_E || direction == Train.DIRECTION_W))
      direction = Train.DIRECTION_N;

    else if (deltaX > 0.0D && (direction == Train.DIRECTION_N || direction == Train.DIRECTION_S))
      direction = Train.DIRECTION_W;

    else if (deltaX < 0.0D && (direction == Train.DIRECTION_N || direction == Train.DIRECTION_S))
      direction = Train.DIRECTION_E;

    if (direction == Train.DIRECTION_W || direction == Train.DIRECTION_S)
      velocity = -velocity;

    Vector velVector = new Vector();
    if (direction == Train.DIRECTION_W || direction == Train.DIRECTION_E)
      velVector.setX(velocity);

    if (direction == Train.DIRECTION_N || direction == Train.DIRECTION_S)
      velVector.setZ(velocity);

    setVelocity(velVector);
    updateOldLocation();

    return direction;
  }

  public Minecart getVehicle() {
    return cart;
  }

  public int getTrainPosition() {
    return trainPosition;
  }

  public byte getDirection() {
    return direction;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public Vector getCurrentVelocity() {
    return currentVelocity;
  }

  public void setVelocity(Vector velocity) {
    lastVelocity = currentVelocity;
    currentVelocity = velocity;

    cart.setVelocity(currentVelocity);
  }

  public void increaseVelocity(Vector velocity) {
    lastVelocity = currentVelocity;
    currentVelocity.add(velocity);

    cart.setVelocity(currentVelocity);
  }

  public Vector getLastVelocity() {
    return lastVelocity;
  }

  public Location getOldLocation() {
    return oldLocation;
  }

  public void updateOldLocation() {
    oldLocation = cart.getLocation();
  }

}
