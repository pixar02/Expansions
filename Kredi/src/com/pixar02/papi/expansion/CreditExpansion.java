package com.pixar02.papi.expansion;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.java9.kredi.Kredi;
import me.java9.kredi.KrediAPI;

public class CreditExpansion extends PlaceholderExpansion {
    private Kredi plugin;

    /**
     * Since this expansion requires api access to the plugin "SomePlugin" we must
     * check if "SomePlugin" is on the server in this method
     */
    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getPlugin()) != null;
    }

    /**
     * We can optionally override this method if we need to initialize variables
     * within this class if we need to or even if we have to do other checks to
     * ensure the hook is properly setup.
     */
    @Override
    public boolean register() {
        /*
         * Make sure "SomePlugin" is on the server
         */
        if (!canRegister()) {
            return false;
        }

        /*
         * "SomePlugin" does not have static methods to access its api so we must create
         * set a variable to obtain access to it
         */
        plugin = (Kredi) Bukkit.getPluginManager().getPlugin(getPlugin());

        /*
         * if for some reason we can not get our variable, we should return false
         */
        if (plugin == null) {
            return false;
        }
        /*
         * Since we override the register method, we need to manually register this hook
         */
        return PlaceholderAPI.registerPlaceholderHook(getIdentifier(), this);
    }

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "pixar02";
    }

    /**
     * The placeholder identifier should go here This is what tells PlaceholderAPI
     * to call our onPlaceholderRequest method to obtain a value if a placeholder
     * starts with our identifier. This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "kredit";
    }

    /**
     * if an expansion requires another plugin as a dependency, the proper name of
     * the dependency should go here. Set this to null if your placeholders do not
     * require another plugin be installed on the server for them to work. This is
     * extremely important to set if you do have a dependency because if your
     * dependency is not loaded when this hook is registered, it will be added to a
     * cache to be registered when plugin: "getPlugin()" is enabled on the server.
     */
    @Override
    public String getPlugin() {
        return "Kredi";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and
     * needs a value We specify the value identifier in this method
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {

        if (p == null) {
            return "";
        }

        UUID uuid = p.getUniqueId();
        String name = p.getName();

        // %kredi_amount%
        if (identifier.equals("amount")) {
            return String.valueOf(KrediAPI.getKredi(String.valueOf(uuid)));
        }
        // %kredi_XXX%
        if (identifier.equals("XXX")) {
            return "XXX";

        }
        return null;
    }

}