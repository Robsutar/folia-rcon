package com.robsutar.rcon;

import com.robsutar.rcon.velocityrcon.server.RconServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.logging.Logger;

public class VelocityRcon extends JavaPlugin {
    private static VelocityRcon instance;

    private  org.bukkit.Server server;
    
    private Logger logger;

    
    private String rconHost = "127.0.0.1";
    
    private int rconPort = 1337;
    
    private String rconPassword = "8Qnvb563haX26DDF";
    
    private boolean rconColored = true;

    public static VelocityRcon getInstance() {
        return instance;
    }

    private RconServer rconServer;


    @Override
    public void onEnable() {
        this.server = Bukkit.getServer();
        this.logger = getLogger();
        instance = this;

        saveDefaultConfig();

        // Paper uses YML format for configuration
        rconHost = getConfig().getString("rcon-host", "127.0.0.1");
        rconPort = getConfig().getInt("rcon-port", 1337);
        rconPassword = getConfig().getString("rcon-password", "8Qnvb563haX26DDF");
        rconColored = getConfig().getBoolean("rcon-colored", true);

        startListener();
    }

    @Override
    public void onDisable() {
        stopListener();
    }

    private void startListener() {
        InetSocketAddress address = new InetSocketAddress(rconHost, rconPort);
        rconServer = new RconServer(server, rconPassword);
        logger.info("Binding rcon to address: /" + address.getHostName() + ":" + address.getPort());

        ChannelFuture future = rconServer.bind(address);
        Channel channel = future.awaitUninterruptibly().channel();

        if (!channel.isActive()) {
            logger.warning("Failed to bind rcon port. Address already in use?");
        }
    }

    private void stopListener() {
        if (rconServer != null) {
            logger.info("Trying to stop RCON listener");

            rconServer.shutdown();
        }
    }
}
