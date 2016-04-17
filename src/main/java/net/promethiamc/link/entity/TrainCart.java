package net.promethiamc.link.entity;

import org.bukkit.Location;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

public class TrainCart {

  protected Minecart cart;
  protected Train train;
  protected Vector currentVelocity = new Vector(), lastVelocity = new Vector();
  protected double velocity;
  protected Location oldLocation;

  protected short direction;
  private int trainPosition;
  private boolean occupied;

  public TrainCart(Train train, Minecart cart, int trainPosition, short direction) {
    this.train = train;
    this.cart = cart;
    this.trainPosition = trainPosition;
    this.direction = direction;

    this.cart.setInvulnerable(true);
    this.cart.setMaxSpeed(2.0D);
    this.cart.setSlowWhenEmpty(false);

    this.updateOldLocation();
  }

  protected void updateDirection(double velocity) {
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
  }

  public Minecart getVehicle() {
    return cart;
  }

  public int getTrainPosition() {
    return trainPosition;
  }

  public short getDirection() {
    return direction;
  }

  public void setDirection(byte direction) {
    this.direction = direction;
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

  public Train getTrain() {
    return train;
  }

}
