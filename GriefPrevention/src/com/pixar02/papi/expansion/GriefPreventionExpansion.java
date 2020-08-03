package com.pixar02.papi.expansion;

import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.DataStore;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GriefPreventionExpansion extends PlaceholderExpansion implements Configurable {

    private GriefPrevention plugin;

    /**
     * Since this expansion requires api access to the plugin "SomePlugin" we must
     * check if "SomePlugin" is on the server in this method
     */
    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null;
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
        plugin = (GriefPrevention) Bukkit.getPluginManager().getPlugin(getRequiredPlugin());

        /*
         * if for some reason we can not get our variable, we should return false
         */
        if (plugin == null) {
            return false;
        }
        /*
         * Since we override the register method, we need to manually register this hook
         */
        return super.register();
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
        return "griefprevention";
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
    public String getRequiredPlugin() {
        return "GriefPrevention";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.5.1";
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<String, Object>();
        defaults.put("formatting.thousands", "k");
        defaults.put("formatting.millions", "M");
        defaults.put("formatting.billions", "B");
        defaults.put("formatting.trillions", "T");
        defaults.put("formatting.quadrillions", "Q");
        defaults.put("color.enemy", "&4");
        defaults.put("color.trusted", "&a");
        defaults.put("color.neutral", "&7");
        return defaults;
    }

    /**
     * This is the method called when a placeholder with our identifier is found and
     * needs a value We specify the value identifier in this method
     */
    @Override
    public String onRequest(OfflinePlayer p, String identifier) {
        if (!p.isOnline() || p == null) {
            return "";
        }

        Player player = p.getPlayer();

        DataStore DataS = plugin.dataStore;
        PlayerData pd = DataS.getPlayerData(player.getUniqueId());

        /*
         %griefprevention_claims%
         %griefprevention_claims_formatted%
        */
        if (identifier.equals("claims")) {
            return String.valueOf(pd.getClaims().size());
        } else if (identifier.equals("claims_formatted")) {
            return fixMoney(pd.getClaims().size());
        }

        // %griefprevention_bonusclaims%
        if (identifier.equals("bonusclaims")) {
            return String.valueOf(pd.getBonusClaimBlocks());
        }

        /*
         %griefprevention_accruedclaims%
         %griefprevention_accruedclaims_formatted%
        */
        if (identifier.equals("accruedclaims")) {
            return String.valueOf(pd.getAccruedClaimBlocks());
        } else if (identifier.equals("accruedclaims_formatted")) {
            return fixMoney(pd.getAccruedClaimBlocks());
        }

        // %griefprevention_accruedclaims_limit%
        if (identifier.equals("accruedclaims_limit")) {
            return String.valueOf(pd.getAccruedClaimBlocksLimit());
        }

        /*
         %griefprevention_remainingclaims%
         %griefprevention_remainingclaims_formatted%
        */
        if (identifier.equals("remainingclaims")) {
            return String.valueOf(pd.getRemainingClaimBlocks());
        } else if (identifier.equals("remainingclaims_formatted")) {
            return fixMoney(pd.getRemainingClaimBlocks());
        }

        // %griefprevention_currentclaim_ownername_color%
        // %griefprevention_currentclaim_ownername%
        if (identifier.equals("currentclaim_ownername")) {
            Claim claim = DataS.getClaimAt(player.getLocation(), true, null);
            if (claim == null) {
                return "Unclaimed";
            } else {
                return String.valueOf(claim.getOwnerName());
            }
        } else if (identifier.equals("currentclaim_ownername_color")) {
            Claim claim = DataS.getClaimAt(player.getLocation(), true, null);
            if (claim == null) {
                return ChatColor.translateAlternateColorCodes('&',
                        getString("color.neutral", "") + "Unclaimed");
            } else {
                if (claim.allowAccess(player) == null){
                    //Trusted
                    return ChatColor.translateAlternateColorCodes('&',
                            getString("color.trusted", "") + String.valueOf(claim.getOwnerName()));
                }else{
                    // not trusted
                    return ChatColor.translateAlternateColorCodes('&',
                            getString("color.enemy", "") + String.valueOf(claim.getOwnerName()));

                }
            }
        }
        return null;
    }

    private String fixMoney(double d) {
        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000L) + getString("formatting.thousands", "k");
        }
        if (d < 1000000000L) {
            return format(d / 1000000L) + getString("formatting.millions", "m");
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000L) + getString("formatting.billions", "b");
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000L) + getString("formatting.trillions", "t");
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000L) + getString("formatting.quadrillions", "q");
        }
        return toLong(d);
    }

    private String toLong(double amt) {
        long send = (long) amt;
        return String.valueOf(send);
    }

    private String format(double d) {
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(d);
    }

}