package info.bcrc.mc.bingo;

import java.util.Objects;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.controller.BingoCommandExecutor;
import info.bcrc.mc.bingo.controller.BingoListener;
import info.bcrc.mc.bingo.util.BingoConfig;
import info.bcrc.mc.bingo.util.BingoRandomGenerator;

public class Bingo extends JavaPlugin {
    public World bingoWorld;
    private ItemDisplayer itemDisplayer;

    public BingoGame getBingoGame() {
        return bingoGame;
    }

    public BingoGame bingoGame;
    private BingoConfig bingoConfig;
    private BingoCommandExecutor bingoCommandExecutor;
    private BingoListener bingoListener;

    public BingoRandomGenerator getBingoRandomGenerator() {
        return bingoRandomGenerator;
    }

    private BingoRandomGenerator bingoRandomGenerator;

    public BingoConfig getBingoConfig() {
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
        bingoRandomGenerator = new BingoRandomGenerator(getBingoConfig());
        this.getServer().getPluginManager().registerEvents(bingoListener, this);

        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);
    }

    @Override
    public void onDisable() {

    }
}
