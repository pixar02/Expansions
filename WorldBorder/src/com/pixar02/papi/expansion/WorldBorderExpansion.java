package com.pixar02.papi.expansion;

import com.sun.istack.internal.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class WorldBorderExpansion extends PlaceholderExpansion {
    /**
     * Match _ that aren't inside {}.
     * Example: fromWorld_{world_nether}_size => fromWorld, {world_nether}, size
     */
    protected static Pattern splitPattern = Pattern.compile("_(?![^{]*})");

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
        return "1.2.0";
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return Arrays.asList(
            "%worldborder_size%",
            "%worldborder_center%",
            "%worldborder_center_x%",
            "%worldborder_center_z%",
            "%worldborder_damage_amount%",
            "%worldborder_damage_buffer%",
            "%worldborder_warning_distance%",
            "%worldborder_warning_time%",
            "%worldborder_fromWorld_{worldName}_(anyArgument)%"
        );
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        String[] parts = splitPattern.split(identifier);
        if (parts.length < 1) return "Insufficient arguments.";
        String argument;
        WorldBorder worldBorder;

        if (parts[0].equals("fromWorld")) {
            if (parts.length < 3) return "Insufficient arguments.";
            // {world}
            String worldNameWithBrackets = parts[1];
            // world
            String worldName = worldNameWithBrackets.substring(1, worldNameWithBrackets.length() - 1);
            worldBorder = getBorderByWorldName(worldName);
            if (worldBorder == null) return "World not found";

            String[] argumentsArray = Arrays.copyOfRange(parts, 2, parts.length);
            argument = String.join("_", argumentsArray);
        } else {
            if (player == null) return "Player not found.";
            if (!player.isOnline()) {
                return "Player is offline";
            }

            Player p = player.getPlayer();

            if (p == null) {
                return "Player not found.";
            }

            worldBorder = p.getWorld().getWorldBorder();
            argument = identifier;
        }

        // %worldborder_size%
        if (argument.equals("size")) {
            return String.valueOf(worldBorder.getSize());
        }

        // %worldborder_center%
        if (argument.startsWith("center")) {
            String[] centerParts = argument.split("_");
            Location center = worldBorder.getCenter();

            if (centerParts.length == 1) return center.getX() + ", " + center.getZ();
            String requestedCoordinate = centerParts[1];
            if (requestedCoordinate.equals("x")) return String.valueOf(center.getX());
            if (requestedCoordinate.equals("z")) return String.valueOf(center.getZ());

            return "Invalid requested coordinate. Must be x or z.";
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