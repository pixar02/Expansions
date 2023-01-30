package com.pixar02.papi.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class WorldBorderExpansion extends PlaceholderExpansion {

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "pixar02";
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "worldborder";
    }

    /**
     * if an expansion requires another plugin as a dependency, the proper name of the dependency should
     * go here. Set this to null if your placeholders do not require another plugin be installed on the server
     * for them to work
     */
    @Override
    public String getRequiredPlugin() {
        return null;
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.1.0";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String[] parts = identifier.split("_");
        if (parts.length < 1) return "Insufficient arguments.";
        String firstPart = parts[0];

        String argument;
        WorldBorder worldBorder;

        if (firstPart.equals("fromWorld")) {
            if (parts.length < 3) return "Insufficient arguments.";
            String worldName = parts[1];
            String[] argumentsArray = Arrays.copyOfRange(parts, 2, parts.length);
            argument = String.join("_", argumentsArray);

            worldBorder = getBorderByWorldName(worldName);
            if (worldBorder == null) return "World not found";
        } else {
            if (!player.isOnline()) {
                return "Player is offline";
            }

            Player p = player.getPlayer();

            if (p == null) {
                return "";
            }

            worldBorder = p.getWorld().getWorldBorder();
            String[] argumentsArray = Arrays.copyOfRange(parts, 1, parts.length);
            argument = String.join("_", argumentsArray);
        }

        // %worldborder_size%
        if (argument.equals("size")) {
            return String.valueOf(worldBorder.getSize());
        }

        // %worldborder_center%
        if (argument.equals("center")) {
            return String.valueOf(worldBorder.getCenter());
        }

        // %worldborder_damage_amount%
        if (argument.equals("damage_amount")) {
            return String.valueOf(worldBorder.getDamageAmount());
        }

        // %worldborder_damage_buffer%
        if (argument.equals("damage_buffer")) {
            return String.valueOf(worldBorder.getDamageBuffer());
        }

        // %worldborder_warning_distance%
        if (argument.equals("warning_distance")) {
            return String.valueOf(worldBorder.getWarningDistance());
        }

        // %worldborder_warning_time%
        if (argument.equals("warning_time")) {
            return String.valueOf(worldBorder.getWarningTime());
        }

        return null;
    }

    protected @Nullable WorldBorder getBorderByWorldName(@Nonnull String name) {
        World world = Bukkit.getWorld(name);
        if (world == null) return null;
        return world.getWorldBorder();
    }
}