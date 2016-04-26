package net.promethiamc.link.data;

import org.bukkit.Location;

public class Station {

  private int ID, duration;
  private String name;
  private short launchDirection, platformDirection;
  private boolean enabled = true, terminus = false;

  private Location location;

  public Station(int ID, String name, int duration, short launchDirection, short platformDirection, Location location) {
    this.ID = ID;
    this.name = name;
    this.launchDirection = launchDirection;
    this.location = location;
  }

  public int getID() {
    return ID;
  }

  public int getDuration() {
    return duration;
  }

  public String getName() {
    return name;
  }

  public short getLaunchDirection() {
    return launchDirection;
  }

  public short getPlatformDirection() {
    return platformDirection;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isTerminus() {
    return terminus;
  }

  public void setTerminus(boolean terminus) {
    this.terminus = terminus;
  }

  public Location getLocation() {
    return location;
  }

}
