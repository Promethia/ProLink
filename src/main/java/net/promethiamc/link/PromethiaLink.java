package net.promethiamc.link;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import net.promethiamc.link.command.Command;
import net.promethiamc.link.command.CommandDestroy;
import net.promethiamc.link.command.CommandSpawn;
import net.promethiamc.link.data.JSON;
import net.promethiamc.link.data.Station;
import net.promethiamc.link.entity.Train;
import net.promethiamc.link.sign.ControlSign;
import net.promethiamc.link.sign.SignCommand;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PromethiaLink extends JavaPlugin {

  private static CopyOnWriteArrayList<Train> trains = new CopyOnWriteArrayList<Train>();
  private static HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
  private static HashMap<Location, ControlSign> signs = new HashMap<Location, ControlSign>();

  private static HashMap<String, World> worlds = new HashMap<String, World>();

  private File configf, stationConfig, signConfig;
  private FileConfiguration config;

  public void tick() {
    for (Train train : trains)
      train.tick();
  }

  public static boolean createTrain(Location location, int carts, short direction) {
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

  public static void handleSignRedstone(Block block) {
    if (!signs.containsKey(block.getLocation()))
      return;

    signs.get(block.getLocation()).handleRedstoneAction();
  }

  public static void log(CommandSender sender, String message) {
    if (sender instanceof Player) {
      ((Player) sender).sendMessage(message);
    } else {
      System.out.println(message);
    }
  }

  public static byte getDirection(String direction) {
    return direction.equals("n") ? Train.DIRECTION_N : (direction.equals("e") ? Train.DIRECTION_E : (direction.equals("s") ? Train.DIRECTION_S : (direction.equals("w") ? Train.DIRECTION_W : Train.DIRECTION_UNKNOWN)));
  }

  public static World getWorld(String name) {
    return worlds.get(name);
  }

  public static CopyOnWriteArrayList<Train> getTrains() {
    return trains;
  }

  public static HashMap<Integer, Station> getStations() {
    return stations;
  }

  public static Station getStation(int ID) {
    return stations.get(ID);
  }

  public static HashMap<Location, ControlSign> getControlSigns() {
    return signs;
  }

  public static void addControlSign(ControlSign sign) {
    signs.put(sign.getSign().getLocation(), sign);
  }

  public static void deleteControlSign(Location location) {
    signs.remove(location);
  }

  private void createConfigs() {
    configf = new File(getDataFolder(), "config.yml");
    stationConfig = new File(getDataFolder(), "stations.json");
    signConfig = new File(getDataFolder(), "signs.json");

    // Make sure the config files actually exists
    if (!configf.exists()) {
      try {
        configf.getParentFile().mkdirs();
        configf.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (!stationConfig.exists()) {
      try {
        stationConfig.getParentFile().mkdirs();
        stationConfig.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (!signConfig.exists()) {
      try {
        signConfig.getParentFile().mkdirs();
        signConfig.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    config = new YamlConfiguration();

    try {
      config.load(configf);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void onEnable() {
    createConfigs();

    for (World w : getServer().getWorlds())
      worlds.put(w.getName(), w);

    // Register event listener
    getServer().getPluginManager().registerEvents(new EventListener(), this);

    // Register commands
    getCommand("plink").setExecutor(new CommandListener());
    Command.registerCommand("spawn", CommandSpawn.class);
    Command.registerCommand("destroy", CommandDestroy.class);

    // Register our control signs
    ControlSign.registerType("spawn", SignCommand.class);
    ControlSign.registerType("destroy", SignCommand.class);

    stations = JSON.readStationFile(getDataFolder() + File.separator + "stations.json");
    JSON.readSignFile(getDataFolder() + File.separator + "signs.json");

    // Use a Bukkit Schedular to execute our train tick function on every server tick
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

    // Update our sign config file
    JSON.writeSignsFile(getDataFolder() + File.separator + "signs.json");
  }

}
