package info.bcrc.mc.bingo;

import java.util.*;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Bingo extends JavaPlugin {
    public boolean setupBingo = false;
    public boolean startBingo = false;
    public World bingoWorld;
    private ItemDisplayer itemDisplayer;

    private BingoConfig bingoConfig;
    public BingoCommandExecutor bingoCommandExecutor;
    public BingoListener bingoListener;

    public BingoConfig getBingoConfig()
    {
        return bingoConfig;
    }

    public Bingo() {
        bingoConfig = new BingoConfig(this.getConfig());
    }

    public ItemDisplayer getItemDisplayer() {
        return itemDisplayer;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        itemDisplayer = new ItemDisplayer(this);
        bingoCommandExecutor = new BingoCommandExecutor(this);
        bingoListener = new BingoListener(this);
        this.getServer().getPluginManager().registerEvents(bingoListener, this);

        saveConfig();
        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);
    }

    @Override
    public void onDisable() {

    }
}
