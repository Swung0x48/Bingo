package info.bcrc.mc.bingo;

import java.util.Objects;

import info.bcrc.mc.bingo.util.BingoItemGenerator;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.controller.BingoCommandExecutor;
import info.bcrc.mc.bingo.controller.BingoListener;
import info.bcrc.mc.bingo.util.BingoConfig;

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

    public BingoItemGenerator getBingoItemGenerator()
    {
        return bingoItemGenerator;
    }

    private BingoItemGenerator bingoItemGenerator;

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
        bingoItemGenerator = new BingoItemGenerator(getBingoConfig());
        this.getServer().getPluginManager().registerEvents(bingoListener, this);

        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);
    }

    @Override
    public void onDisable() {

    }
}
