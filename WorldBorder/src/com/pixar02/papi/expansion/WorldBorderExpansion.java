package com.pixar02.papi.expansion;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.Bukkit;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

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
    public String getPlugin() {
        return null;
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        WorldBorder worldBorder = p.getWorld().getWorldBorder();

        // %worldborder_size%
        if (identifier.equals("size")) {
            return String.valueOf(worldBorder.getSize());
        }

        // %worldborder_center%
        if (identifier.equals("center")) {
            return String.valueOf(worldBorder.getCenter());
        }

        // %worldborder_damage_amount%
        if (identifier.equals("damage_amount")) {
            return String.valueOf(worldBorder.getDamageAmount());
        }

        // %worldborder_damage_buffer%
        if (identifier.equals("damage_buffer")) {
            return String.valueOf(worldBorder.getDamageBuffer());
        }

        // %worldborder_warning_distance%
        if (identifier.equals("warning_distance")) {
            return String.valueOf(worldBorder.getWarningDistance());
        }

        // %worldborder_warning_time%
        if (identifier.equals("warning_time")) {
            return String.valueOf(worldBorder.getWarningTime());
        }



        return null;
    }
}