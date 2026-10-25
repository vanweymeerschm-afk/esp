package com.marthoxsmp.marthoxguard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class MarthoxGuard extends JavaPlugin implements Listener {
    private double maxBlockDistance;
    private boolean cancelSuspiciousActions;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        maxBlockDistance = getConfig().getDouble("checks.max-block-distance", 6.0);
        cancelSuspiciousActions = getConfig().getBoolean("checks.cancel-suspicious-actions", true);
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("MarthoxGuard enabled. Paper Anti-Xray must be configured separately.");
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        checkBlockAction(event.getPlayer(), event.getBlock(), "breaking", event);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        checkBlockAction(event.getPlayer(), event.getClickedBlock(), "interacting", event);
    }

    private void checkBlockAction(Player player, Block block, String action, Object event) {
        if (player.hasPermission("marthoxguard.bypass")) return;

        Location eye = player.getEyeLocation();
        Location target = block.getLocation().add(0.5, 0.5, 0.5);
        double distanceSquared = eye.distanceSquared(target);
        if (distanceSquared <= maxBlockDistance * maxBlockDistance) return;

        String message = "§c[MarthoxGuard] §f" + player.getName()
                + " attempted block " + action + " at " + String.format(java.util.Locale.ROOT, "%.2f", Math.sqrt(distanceSquared))
                + " blocks away.";
        getLogger().warning(message.replaceAll("§[0-9A-FK-ORa-fk-or]", ""));
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (staff.hasPermission("marthoxguard.alerts")) staff.sendMessage(message);
        }

        if (cancelSuspiciousActions) {
            if (event instanceof BlockBreakEvent breakEvent) breakEvent.setCancelled(true);
            if (event instanceof PlayerInteractEvent interactEvent) interactEvent.setCancelled(true);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("marthoxguard")) return false;
        if (!sender.hasPermission("marthoxguard.alerts")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }
        sender.sendMessage("§bMarthoxGuard §7v" + getDescription().getVersion());
        sender.sendMessage("§7Block reach threshold: §f" + maxBlockDistance + " blocks");
        sender.sendMessage("§7Cancel suspicious actions: §f" + cancelSuspiciousActions);
        sender.sendMessage("§eAnti-Xray is configured in Paper's world config.");
        sender.sendMessage("§eFreecam/ESP cannot be reliably detected from server-side events alone.");
        return true;
    }
}
