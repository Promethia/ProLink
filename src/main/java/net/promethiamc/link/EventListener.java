package net.promethiamc.link;

import net.promethiamc.link.sign.ControlSign;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class EventListener implements Listener {

  @EventHandler
  public void onVehicleCreate(VehicleCreateEvent event) {
    if (!(event.getVehicle() instanceof Minecart))
      return;

    // TODO Handle automated growing when placed near a train?
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
    if (event.getVehicle() instanceof Minecart && event.getEntity() instanceof Player)
      event.setCancelled(true);

    // TODO Handle cart with cart collisions
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onVehicleExit(VehicleExitEvent event) {
    if (!(event.getVehicle() instanceof Minecart))
      return;

    // TODO Lock the train when moving. When the train is locked, cancel this event
    System.out.println("Exited minecart");
  }

  @EventHandler
  public void onBlockRedstone(BlockRedstoneEvent event) {
    for (int x = -2; x < 2; x++) {
      for (int y = -2; y < 2; y++) {
        for (int z = -2; z < 2; z++) {
          Block block = event.getBlock().getWorld().getBlockAt(event.getBlock().getX() + x, event.getBlock().getY() + y, event.getBlock().getZ() + z);

          if (block.getType().equals(Material.SIGN) || block.getType().equals(Material.WALL_SIGN)) {
            PromethiaLink.handleSignRedstone(block);
            return;
          }
        }
      }
    }
  }

  @EventHandler
  public void onSignChange(SignChangeEvent event) {
    if (!ControlSign.isControlSign(event.getLines()))
      return;

    if (ControlSign.create(event.getLines(), (Sign) event.getBlock().getState(), event.getPlayer()) == null) {
      event.getBlock().breakNaturally();
      return;
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    if (!ControlSign.isControlSign(event.getBlock()))
      return;

    ControlSign.handleSignDestruction((Sign) event.getBlock().getState());
  }

}
