package net.promethiamc.link;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import net.promethiamc.link.command.CommandMain;
import net.promethiamc.link.entity.Train;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PromethiaLink extends JavaPlugin {

  private static CopyOnWriteArrayList<Train> trains = new CopyOnWriteArrayList<Train>();
  private static World world;

  public static CopyOnWriteArrayList<Train> getTrains() {
    return trains;
  }

  public void tick() {
    for (Train train : trains)
      train.tick();
  }

  public static boolean createTrain(Location location, int carts, byte direction) {
    for (int x = -2; x < 2; x++) {
      for (int y = -2; y < 2; y++) {
        for (int z = -2; z < 2; z++) {
          Block block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);

          if (block.getType().equals(Material.RAILS) || block.getType().equals(Material.POWERED_RAIL) || block.getType().equals(Material.DETECTOR_RAIL)) {
            location = new Location(location.getWorld(), location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
            Train train = new Train(trains.size(), "Train " + trains.size(), carts, location, location.getWorld(), direction);

            trains.add(train);

            return true;
          }
        }
      }
    }

    System.out.println("Couldn't find nearby rails to spawn train locomotive on: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
    return false;
  }

  public static void removeTrain(Train train) {
    train.removeCarts();
    trains.remove(train);
  }

  public static void log(CommandSender sender, String message) {
    if (sender instanceof Player) {
      ((Player) sender).sendMessage(message);
    } else {
      System.out.println(message);
    }
  }

  public static byte getDirection(String direction) {
    return direction.equals("n") ? Train.DIRECTION_N : (direction.equals("e") ? Train.DIRECTION_E : (direction.equals("s") ? Train.DIRECTION_S : (direction.equals("w") ? Train.DIRECTION_W : Train.DIRECTION_N)));
  }

  public static World getWorld() {
    return world;
  }

  public void onEnable() {
    // Register event listener
    getServer().getPluginManager().registerEvents(new EventListener(), this);

    // Register commands
    this.getCommand("plink").setExecutor(new CommandMain());

    world = this.getServer().getWorld("Summer Forest");

    // Use a Bukkit Schedular to execute our train tick function on every 5 server ticks
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        tick();
      }
    }, 1, 1);
  }

  public void onDisable() {
    // Remove trains when plugin is disabled (usually on server shutdown)
    for (Iterator<Train> it = trains.iterator(); it.hasNext();)
      it.next().removeCarts();
  }

}
