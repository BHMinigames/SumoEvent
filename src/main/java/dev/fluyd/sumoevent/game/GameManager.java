package dev.fluyd.sumoevent.game;

import dev.fluyd.sumoevent.utils.CageUtil;
import dev.fluyd.sumoevent.utils.ConfigUtils;
import dev.fluyd.sumoevent.utils.LootUtils;
import dev.fluyd.sumoevent.utils.ReplayUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;

@UtilityClass
public class GameManager {
    public ArrayList<Location> placedBlocks = new ArrayList<>();
    public boolean canBuild = ConfigUtils.getConfig().getBoolean("build");
    public boolean gameStarted = false;
    public boolean gameStarting = false;
    public boolean inCage = false;
    public ArrayList<UUID> players = new ArrayList<>();
    public ArrayList<UUID> alivePlayers = new ArrayList<>();
    public Countdown countdown;
    public boolean invincibility = true;
    public boolean noFall = true;
    public boolean unbreakableItems = true;
    public ReplayUtils replayUtils = new ReplayUtils();
    public ArrayList<UUID> launchpadCooldown = new ArrayList<>();
    World world = ConfigUtils.getParsedLocation(ConfigUtils.getGameStartLocation()).getWorld();

    public void stopGame(boolean force) {
        gameStarted = false;
        gameStarting = false;
        inCage = false;
        invincibility = true;
        noFall = true;
        unbreakableItems = true;
        replayUtils.stopReplay();
        replayUtils = new ReplayUtils();
        players.clear();
        alivePlayers.clear();
        placedBlocks.forEach(block -> block.getBlock().setType(Material.AIR));
        placedBlocks.clear();
        launchpadCooldown.clear();
        try {LootUtils.chestNametagTask.cancel();} catch (Exception ignored) {}
        LootUtils.chestNametags.values().forEach(ArmorStand::remove);
        LootUtils.chestNametags.clear();
        LootUtils.removeAllChests();
        LootUtils.chestLocations.clear();
        LootUtils.openedChests.clear();
        CageUtil.setCage(false);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item || entity instanceof Arrow || entity instanceof EnderPearl || entity instanceof Snowball && entity instanceof Egg) {
                entity.remove();
            }
        }

        for (Player op : Bukkit.getOnlinePlayers()) {
            op.setGameMode(GameMode.ADVENTURE);
            op.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            op.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 255));
            op.getInventory().clear();
            op.getPlayer().getInventory().setArmorContents(null);
            op.teleport(ConfigUtils.getParsedLocation(ConfigUtils.getLobbyLocation()));
        }
    }

    public void startGame() {
        gameStarting = true;
        countdown = new Countdown(3);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item || entity instanceof Arrow || entity instanceof EnderPearl || entity instanceof Snowball && entity instanceof Egg) {
                entity.remove();
            }
        }

        for (Player op : Bukkit.getOnlinePlayers()) {
            if (!GameManager.players.contains(op.getUniqueId())) {
                GameManager.players.add(op.getUniqueId());
                GameManager.alivePlayers.add(op.getUniqueId());
            }

            op.setGameMode(GameMode.ADVENTURE);
            op.setFoodLevel(20);
            op.setHealth(20);
            op.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0));
            op.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 255));
            op.getInventory().clear();
            op.getPlayer().getInventory().setArmorContents(null);
            CageUtil.setCage(true);
            op.teleport(ConfigUtils.getParsedLocation(ConfigUtils.getGameStartLocation()));
            KitManager.giveKit(op);
        }

        inCage = true;
        countdown.countdown();
    }

    public void respawnPlayer(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setVelocity(player.getVelocity().setY(.5));
        alivePlayers.remove(player.getUniqueId());
        player.teleport(ConfigUtils.getParsedLocation(ConfigUtils.getGameStartLocation()));
        player.setGameMode(GameMode.SURVIVAL);
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
        KitManager.giveKit(player);
        alivePlayers.add(player.getUniqueId());
    }

    public void countdownEnd() {
        invincibility = false;
        gameStarted = true;
        gameStarting = false;
        replayUtils.startReplay(players);
        LootUtils.placeChests();
        LootUtils.handleChestNametags();
        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            CageUtil.setCage(false);
            p.setGameMode(GameMode.SURVIVAL);
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 1));
        }
    }

    public void handleDeath(Player victim, Player killer, DeathCause cause) {
        alivePlayers.remove(victim.getUniqueId());
        if (cause == DeathCause.FALL) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + victim.getName() + ChatColor.RED + " fell to their death!"));
        } else if (cause == DeathCause.PLAYER) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.GRAY + victim.getName() + ChatColor.RED + " was killed by " + ChatColor.GRAY + killer.getName() + ChatColor.RED + "!"));
        }

        victoryCheck();
    }

    private void victoryCheck() {
        if (alivePlayers.size() == 1) {
            Player winner = Bukkit.getPlayer(alivePlayers.get(0));
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.YELLOW + winner.getName() + ChatColor.GREEN + " has won the game!"));
        } else if (alivePlayers.size() == 0) {
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(ChatColor.RED + "Nobody won the game!"));
        } else {
            return;
        }

        alivePlayers.forEach(uuid -> {
            int xp = (int) (Math.random() * 100) + 200;
            Player player = Bukkit.getPlayer(uuid);
            giveXP(player, xp);
        });

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (alivePlayers.contains(player.getUniqueId())) return;

            int xp = (int) (Math.random() * 50) + 50;

            giveXP(player, xp);
        });

        stopGame(false);
    }

    public void giveXP(Player player, int xp) {
        player.sendMessage(ChatColor.LIGHT_PURPLE + "+" + ChatColor.LIGHT_PURPLE + xp + ChatColor.LIGHT_PURPLE + " experience!");
    }
}
