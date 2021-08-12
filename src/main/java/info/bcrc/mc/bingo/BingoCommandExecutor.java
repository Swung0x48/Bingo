package info.bcrc.mc.bingo;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BingoCommandExecutor implements CommandExecutor, TabCompleter {

    Bingo plugin;

    public BingoCommandExecutor(Bingo plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("bingo")) {
            switch (args[0])
            {
                case "setup":
                    sender.sendMessage("You have set up a new bingo map");
                    //plugin.displayer = new ItemDisplayer(plugin);
                    for (Player p : plugin.getServer().getOnlinePlayers())
                    {
                        for (PotionEffect effect : p.getActivePotionEffects())
                        {
                            p.removePotionEffect(effect.getType());
                        }
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 255, false, false));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 999999, 255, false, false));

                        p.getInventory().clear();

                        ItemStack bingoGuide = new ItemStack(Material.NETHER_STAR);
                        ItemMeta meta = bingoGuide.getItemMeta();
                        meta.setDisplayName("Bingo Card");
                        bingoGuide.setItemMeta(meta);
                        p.getInventory().setItem(8, bingoGuide);

                        p.setGameMode(GameMode.ADVENTURE);

                        p.sendMessage("Click with the nether_star to check the bingo map");
                    }
                    plugin.setupBingo = true;
                    break;
                case "start":
                    WorldCreator bingoWorldCreator = new WorldCreator("Bingo_Game");
                    bingoWorldCreator.environment(World.Environment.NORMAL);
                    bingoWorldCreator.type(WorldType.NORMAL);
                    plugin.bingoWorld = bingoWorldCreator.createWorld();

                    if (plugin.setupBingo)
                    {
                        for (Player p : plugin.getServer().getOnlinePlayers())
                        {
                            p.teleport(new Location(plugin.bingoWorld, 0, 100, 0));
                            p.setGameMode(GameMode.SPECTATOR);
                            p.sendMessage("The game will be started in 10 seconds...");
                        }

                        // 昂……不知道咋写这里，等10秒
                        try
                        {
                            wait(10);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        for (Player p : plugin.getServer().getOnlinePlayers())
                        {
                            p.sendMessage(ChatColor.GREEN + "Start game");
                            p.setGameMode(GameMode.SURVIVAL);
                        }

                    } else
                    {
                        sender.sendMessage("Error: The bingo has not been set up");
                    }
                    plugin.startBingo = true;
                    break;
                case "exitworld":
                    if (plugin.startBingo)
                    {
                        for (Player p : plugin.getServer().getWorld("Bingo_Game").getPlayers())
                        {
                            p.teleport(new Location(plugin.getServer().getWorld("over_world"), 0, 100, 0));
                        }

                    } else
                    {
                        sender.sendMessage("Error: The bingo has not been start up");
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equals("bingo")) {
            if (args.length <= 1) {
                return Arrays.asList("setup", "start", "exitworld");
            }
        }
        return null;
    }
}
