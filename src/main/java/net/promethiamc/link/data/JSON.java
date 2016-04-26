package net.promethiamc.link.data;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.promethiamc.link.PromethiaLink;
import net.promethiamc.link.sign.ControlSign;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSON {

  public static HashMap<Integer, Station> readStationFile(String file) {
    HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
    JSONParser parser = new JSONParser();

    try {
      FileReader reader = new FileReader(file);
      JSONObject json = (JSONObject) parser.parse(reader);
      JSONArray arr = (JSONArray) json.get("stations");

      // This is a pain...
      for (int i = 0; i < arr.size(); i++) {
        JSONObject station = (JSONObject) arr.get(i);

        String name = station.get("name").toString();

        int ID = Integer.parseInt(station.get("id").toString());
        int duration = Integer.parseInt(station.get("duration").toString());

        short launchDirection = PromethiaLink.getDirection(station.get("launchDirection").toString());
        short platformDirection = PromethiaLink.getDirection(station.get("platformDirection").toString());

        boolean enabled = Boolean.parseBoolean(station.get("enabled").toString());
        boolean terminus = Boolean.parseBoolean(station.get("terminus").toString());

        Location location = getLocation((JSONObject) station.get("location"));

        Station s = new Station(ID, name, duration, launchDirection, platformDirection, location);
        s.setEnabled(enabled);
        s.setTerminus(terminus);

        stations.put(ID, s);
      }

      reader.close();

    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    return stations;
  }

  public static void readSignFile(String file) {
    JSONParser parser = new JSONParser();

    try {
      FileReader reader = new FileReader(file);
      JSONObject json = (JSONObject) parser.parse(reader);
      JSONArray arr = (JSONArray) json.get("signs");

      if (arr == null)
        return;

      if (arr.isEmpty())
        return;

      for (int i = 0; i < arr.size(); i++) {
        JSONObject sign = (JSONObject) arr.get(i);

        String[] args = { "[prolink]", sign.get("command").toString() };
        Location location = getLocation((JSONObject) sign.get("location"));
        Sign block = ((Sign) location.getWorld().getBlockAt(location).getState());

        ControlSign s = ControlSign.create(args, block, null);
        PromethiaLink.addControlSign(s);
      }

      reader.close();

    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }

    System.out.println("Added " + PromethiaLink.getControlSigns().size() + " control signs from config");
  }

  private static Location getLocation(JSONObject location) {
    String world = location.get("world").toString();
    int x = Integer.parseInt(location.get("x").toString());
    int y = Integer.parseInt(location.get("y").toString());
    int z = Integer.parseInt(location.get("z").toString());

    return new Location(PromethiaLink.getWorld(world), x, y, z);
  }

  @SuppressWarnings("unchecked")
  private static JSONObject getLocation(Location location) {
    JSONObject json = new JSONObject();

    json.put("world", location.getWorld().getName());
    json.put("x", location.getBlockX());
    json.put("y", location.getBlockY());
    json.put("z", location.getBlockZ());

    return json;
  }

  @SuppressWarnings("unchecked")
  public static void writeSignsFile(String file) {
    JSONObject json = new JSONObject();
    JSONArray signs = new JSONArray();

    for (Entry<Location, ControlSign> entry : PromethiaLink.getControlSigns().entrySet()) {
      ControlSign sign = entry.getValue();
      JSONObject signObject = new JSONObject();

      signObject.put("location", getLocation(sign.getSign().getLocation()));
      signObject.put("command", sign.getSign().getLine(1));

      signs.add(signObject);
    }

    json.put("signs", signs);

    try {
      FileWriter fw = new FileWriter(file);
      fw.write(json.toJSONString());
      fw.flush();
      fw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Wrote " + PromethiaLink.getControlSigns().size() + " control sign entries to config");
  }

}
