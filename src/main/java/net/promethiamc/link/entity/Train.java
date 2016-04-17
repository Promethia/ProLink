package net.promethiamc.link.entity;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;

public class Train {

  public static final byte DIRECTION_N = 0x00;
  public static final byte DIRECTION_E = 0x01;
  public static final byte DIRECTION_S = 0x02;
  public static final byte DIRECTION_W = 0x03;

  private int ID;
  private short direction;
  private String name;

  private HashMap<Integer, TrainCart> carts = new HashMap<Integer, TrainCart>();
  private TrainLocomotive locomotive;

  public Train(int ID, String name, int carts, Location location, World world, byte direction) {
    this.ID = ID;
    this.direction = direction;
    this.name = name;

    // The locomotive will pull the rest of the train (obviously...). To simulate something with power, use a furnace cart for now
    this.locomotive = new TrainLocomotive(this, (Minecart) world.spawnEntity(location, EntityType.MINECART_FURNACE), direction);

    // Create the rest of our train
    for (int i = 0; i < carts; i++)
      addCart();
  }

  public void tick() {
    direction = locomotive.tick();
  }

  public boolean containsCard(Minecart cart) {
    if (carts.containsKey(cart.getEntityId()))
      return true;

    if (locomotive.getVehicle().getEntityId() == cart.getEntityId())
      return true;

    return false;
  }

  public void addCart() {
    Location location = locomotive.getVehicle().getLocation();
    // Calculate world position offset relative to locomotive based on direction
    int xOffset = (direction == DIRECTION_W ? 2 : (direction == DIRECTION_E ? -2 : 0)) * (carts.size() + 1);
    int zOffset = (direction == DIRECTION_N ? 2 : (direction == DIRECTION_S ? -2 : 0)) * (carts.size() + 1);

    // Only allow the cart to be spawned on a straight line behind the locomotive (requires a straight rail line which is long enough)
    Location blockLocation = new Location(location.getWorld(), location.getBlockX() + xOffset, location.getBlockY(), location.getBlockZ() + zOffset);
    Block block = location.getWorld().getBlockAt(location.getBlockX() + xOffset, location.getBlockY(), location.getBlockZ() + zOffset);

    if (block.getType().equals(Material.RAILS) || block.getType().equals(Material.POWERED_RAIL) || block.getType().equals(Material.DETECTOR_RAIL)) {
      TrainCart cart = new TrainCart(this, (Minecart) locomotive.getVehicle().getWorld().spawnEntity(blockLocation, EntityType.MINECART), carts.size() + 1, direction);
      carts.put(cart.getVehicle().getEntityId(), cart);

      return;
    }
  }

  public void removeCarts() {
    for (Entry<Integer, TrainCart> cart : carts.entrySet())
      cart.getValue().getVehicle().remove();

    locomotive.getVehicle().remove();
  }

  public HashMap<Integer, TrainCart> getCarts() {
    return carts;
  }

  public int getID() {
    return ID;
  }

  public short getDirection() {
    return direction;
  }

  public void setDirection(byte direction) {
    this.direction = direction;
  }

  public String getName() {
    return name;
  }

  public TrainLocomotive getLocomotive() {
    return locomotive;
  }

  public int getSize() {
    return carts.size() + 1;
  }

}
