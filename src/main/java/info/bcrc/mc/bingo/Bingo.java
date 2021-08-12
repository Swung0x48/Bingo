package info.bcrc.mc.bingo;

import java.util.logging.Logger;
import java.util.*;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import info.bcrc.mc.bingo.BingoCreator;

public class Bingo extends JavaPlugin {

    public ItemDisplayer display;
    public boolean setupBingo = false;
    public boolean startBingo = false;
    public World bingoWorld;

    //public Logger logger;
    //public Server server;

    public Commander commander = new Commander(this);
    public BingoListener listener = new BingoListener(this);
    private BingoCreator creator = new BingoCreator(this);

    @Override
    public void onEnable() {
        // getLogger().log(Level.INFO, "{0}.onEnable()", this.getClass().getName());

        //logger = this.getLogger();
        //server = this.getServer();

        saveDefaultConfig();
        List<String> items = Arrays.asList("ACACIA_BOAT", "ACACIA_BUTTON", "ACACIA_DOOR");
        this.getConfig().set("Items", items);

        this.getServer().getPluginManager().registerEvents(listener, this);

        saveConfig();
        getCommand("bingo").setExecutor(commander);
    }

    @Override
    public void onDisable() {
        // getLogger().log(Level.INFO, "{0}.onDisable()", this.getClass().getName());
    }

}
