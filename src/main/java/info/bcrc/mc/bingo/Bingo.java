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
    private BingoCommandExecutor bingoCommandExecutor;
    private BingoListener bingoListener;

    public BingoConfig getBingoConfig()
    {
        return bingoConfig;
    }

    public Bingo() {
    }

    public ItemDisplayer getItemDisplayer() {
        return itemDisplayer;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        bingoConfig = new BingoConfig(this.getConfig());
        
        saveConfig();

        itemDisplayer = new ItemDisplayer(this);
        bingoCommandExecutor = new BingoCommandExecutor(this);
        bingoListener = new BingoListener(this);
        this.getServer().getPluginManager().registerEvents(bingoListener, this);

        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);
    }

    @Override
    public void onDisable() {

    }
}
