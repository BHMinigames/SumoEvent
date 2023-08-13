package dev.fluyd.sumoevent;

import dev.fluyd.spigotcore.CoreProvider;
import dev.fluyd.sumoevent.commands.*;
import dev.fluyd.sumoevent.customitem.CustomItemManager;
import dev.fluyd.sumoevent.customitem.items.*;
import dev.fluyd.sumoevent.game.GameManager;
import dev.fluyd.sumoevent.listeners.*;
import dev.fluyd.sumoevent.scoreboard.ScoreboardHandler;
import dev.fluyd.sumoevent.utils.LeaderboardUtils;
import dev.fluyd.sumoevent.utils.LocationsUtils;
import dev.fluyd.sumoevent.utils.LootUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class SumoEvent extends JavaPlugin {
    public static SumoEvent INSTANCE;
    public CustomItemManager customItemManager = new CustomItemManager();
    public ScoreboardHandler scoreboardHandler;
    public CoreProvider coreProvider;
    public static int scoreboardRefresh = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        initConfig();
        LocationsUtils.createLocationsConfig();
        LootUtils.createLootFile(false);
        LeaderboardUtils.createLeaderboardConfig();
        registerCustomItems();
        registerListeners();
        registerCommands();
        registerTabCompleters();

        // Periodically update the scoreboard for all players
        Bukkit.getScheduler().runTaskTimer(this, this::updateScoreboard, 0L, 20L); // update every second

        // tell the proxy we are ready after fully initializing

        Bukkit.getScheduler().runTaskLater(this, () -> {
            scoreboardHandler = new ScoreboardHandler(ChatColor.YELLOW + "SUMO EVENT");
            coreProvider = new CoreProvider("SUMO_EVENT", 8, true, false, this);
        }, 20L);

        updateScoreboard();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        GameManager.stopGame(true);
    }

    private void updateScoreboard() {
        if (coreProvider == null || scoreboardHandler == null) return; // plugin not fully initialized yet (onEnable not finished)

        scoreboardHandler.getScoreboard().getEntries().forEach(scoreboardHandler.getScoreboard()::resetScores);

        for (Player player: Bukkit.getOnlinePlayers()) {
            updateScoreboardForPlayer(player);
        }
    }

    public void updateScoreboardForPlayer(Player player) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

        // Use ScoreboardHandler's methods to set lines.
        scoreboardHandler.setLine("§7" + sdf.format(new Date()) + " §8" + coreProvider.getServerName(), 8);
        scoreboardHandler.setLine("§a ", 7);
        scoreboardHandler.setLine("§fMap: §aClassic", 6);
        scoreboardHandler.setLine("§fPlayers: §a" + Bukkit.getOnlinePlayers().size() + "/" + coreProvider.getRequiredPlayers(), 5);
        scoreboardHandler.setLine("§e ", 4);
        scoreboardHandler.setLine("§fMode: §aSolo", 3);
        scoreboardHandler.setLine("§fVersion: §7v1.0", 2);
        scoreboardHandler.setLine("§f ", 1);
        scoreboardHandler.setLine("§erizon.lol", 0);

        player.setScoreboard(scoreboardHandler.getScoreboard());
    }

    private void registerCustomItems() {
        customItemManager.registerItem(new PopupTower());
        customItemManager.registerItem(new Tracer());
        customItemManager.registerItem(new BridgeEgg(SumoEvent.INSTANCE));
        customItemManager.registerItem(new MagicPlatform());
        customItemManager.registerItem(new GravityWand());
    }

    private void registerListeners() {
        WaterListener waterListener = new WaterListener();
        // Register Runnables
        getServer().getScheduler().scheduleSyncRepeatingTask(this, waterListener, 0L, (3 * 20L));

        // Register Listeners
        getServer().getPluginManager().registerEvents(waterListener, this);
        getServer().getPluginManager().registerEvents(new KillTrackerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new CustomItemListener(), this);
    }

    /**
     * Returns true if server version is newer
     * @return
     */
    public static boolean isNewApi() {
        final String version = Bukkit.getVersion().toLowerCase();
        final String[] oldAPI = { "1.7", "1.8", "1.9", "1.10", "1.11", "1.12" };
        for (final String ver : oldAPI)
            if (version.contains(ver))
                return false;

        return true;
    }

    private void registerCommands() {
        getCommand("setrank").setExecutor(new SetRankCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("togglebuild").setExecutor(new ToggleBuildCommand());
        getCommand("test").setExecutor(new TestCommand());
        getCommand("togglenofall").setExecutor(new ToggleNoFallCommand());
        getCommand("toggleinvincibility").setExecutor(new ToggleInvincibilityCommand());
        getCommand("customitem").setExecutor(new CustomItemCommand());
        getCommand("cancel").setExecutor(new CancelCommand());
        getCommand("setamount").setExecutor(new SetAmountCommand());
        getCommand("regenchests").setExecutor(new RegenChestsCommand());
        getCommand("togglecooldowns").setExecutor(new ToggleCooldownsCommand());
        getCommand("gravitymapbreak").setExecutor(new GravityMapBreakCommand());
    }

    private void registerTabCompleters() {
        getCommand("setrank").setTabCompleter(new SetRankCommand());
        getCommand("test").setTabCompleter(new TestCommand());
        getCommand("customitem").setTabCompleter(new CustomItemCommand());
    }

    private void initConfig() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveDefaultConfig();
        }
    }
}
