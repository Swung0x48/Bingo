package info.bcrc.mc.bingo;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import info.bcrc.mc.bingo.util.BingoItemManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import info.bcrc.mc.bingo.base.service.BingoGame;
import info.bcrc.mc.bingo.base.view.BingoScoreboard;
import info.bcrc.mc.bingo.controller.BingoCommandExecutor;
import info.bcrc.mc.bingo.controller.BingoListener;
import info.bcrc.mc.bingo.util.BingoConfig;
import info.bcrc.mc.bingo.util.BingoRandomGenerator;
import info.bcrc.mc.bingo.util.MessageSender;

public class Bingo extends JavaPlugin {
    public World bingoWorld;

    public BingoGame getBingoGame() {
        return bingoGame;
    }

    public BingoGame bingoGame;
    private BingoConfig bingoConfig;
    private BingoCommandExecutor bingoCommandExecutor;
    private BingoListener bingoListener;
    private MessageSender messageSender;
    private BingoRandomGenerator bingoRandomGenerator;
    private BingoItemManager bingoItemManager;
    private BingoScoreboard bingoScoreboard;

    public BingoItemManager getBingoItemManager() {
        return bingoItemManager;
    }


    public MessageSender getMessageSender() {
        return messageSender;
    }

    public BingoRandomGenerator getBingoRandomGenerator() {
        return bingoRandomGenerator;
    }


    public BingoConfig getBingoConfig() {
        return bingoConfig;
    }

    public BingoScoreboard getBingoScoreboard() {
        return bingoScoreboard;
    }

    public Bingo() {
    }

    @Override
    public void onEnable() {
        messageSender = new MessageSender(this);

//        saveDefaultConfig();
        reloadConfig();
        bingoConfig = new BingoConfig(this, this.getConfig());
        saveConfig();

        File itemFile = new File(this.getDataFolder() + "/item.csv");
        InputStream stream = null;

        try
        {
            if (itemFile.exists()) {
                this.getLogger().info("Loading items from " + itemFile.getPath() + "...");
            } else {
                this.getLogger().warning("item.csv not found.");
                this.getLogger().warning("Loading defaults...");
                stream = this.getResource("item.csv");

                itemFile.getParentFile().mkdirs();
                FileOutputStream outputStream = new FileOutputStream(itemFile, false);
                if (stream != null) {
                    byte[] bytes = stream.readAllBytes();
                    outputStream.write(bytes);
                    outputStream.close();
                }
            }
            stream = new FileInputStream(itemFile);

        } catch (IOException e)
        {
            this.getLogger().warning("An IO exception occurred when reading item list.");
            e.printStackTrace();
        }
        finally
        {
            if (stream == null) {
                stream = this.getResource("item.csv");
            }
        }

        bingoItemManager = new BingoItemManager(stream, this.getLogger());
        bingoCommandExecutor = new BingoCommandExecutor(this);
        bingoListener = new BingoListener(this);
        bingoScoreboard = new BingoScoreboard(this);
        bingoRandomGenerator = new BingoRandomGenerator(getBingoConfig(), getBingoItemManager());
        this.getServer().getPluginManager().registerEvents(bingoListener, this);

        Objects.requireNonNull(getCommand("bingo")).setExecutor(bingoCommandExecutor);

    }

    @Override
    public void onDisable() {

    }
}
