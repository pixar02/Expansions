package com.pixar02.papi.expansion;

import com.pixar02.papi.expansion.argument.ArgumentSplitType;
import me.clip.placeholderapi.expansion.Configurable;
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
import java.util.Map;
import java.util.Collections;

/**
 * This class will automatically register as a placeholder expansion
 * when a jar including this class is added to the /plugins/placeholderapi/expansions/ folder
 *
 */
public class WorldBorderExpansion extends PlaceholderExpansion implements Configurable {
    protected final static String SPLIT_TYPE_PATH = "splitType";

    /**
     * Match _ that aren't inside {}.
     * Example: fromWorld_{world_nether}_size => fromWorld, {world_nether}, size
     */
    protected @Nonnull ArgumentSplitType argumentSplitType = ArgumentSplitType.DEFAULT;

    public WorldBorderExpansion() {
    }

    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {
        String configuredIdentifier = getString(SPLIT_TYPE_PATH, "").toUpperCase();
        try {
            this.argumentSplitType = ArgumentSplitType.valueOf(configuredIdentifier);
        } catch (Exception exception) {
            this.argumentSplitType = ArgumentSplitType.DEFAULT;
            String[] argumentSplitTypeEnumKeys = Arrays
                .stream(ArgumentSplitType.class.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
            String readableSplitTypeKeys = String.join(", ", argumentSplitTypeEnumKeys);
            warning("No split type (PlaceholderAPI/config.yml > expansions.worldborder." + SPLIT_TYPE_PATH + ") was found or it's invalid.");
            warning(String.format("Should be one of: " + readableSplitTypeKeys + ". Using default: %s.", this.argumentSplitType.name()));
            warning("For example, OMIT_ANGLE_BRACKET omits underscores inside angle brackets (fromWorld_{world_nether}). NO_OMIT captures all underscores.");
            warning("If this is the first time installing the expansion, ignore this message.");
        }
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

    public String getName() {
        return "WorldBorder Expansion";
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
        return "1.2.1";
    }

    @Override
    public @Nonnull List<String> getPlaceholders() {
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

    @Override
    @Nonnull
    public Map<String, Object> getDefaults() {
        return Collections.singletonMap("splitType", ArgumentSplitType.DEFAULT.name());
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    @Nullable
    public String onRequest(OfflinePlayer player, String identifier) {
        String[] parts = this.argumentSplitType.getPattern().split(identifier);
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
            if (worldBorder == null) return String.format("World %s not found.", worldName);

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