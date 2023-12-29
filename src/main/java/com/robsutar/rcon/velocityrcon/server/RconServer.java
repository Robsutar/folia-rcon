package com.robsutar.rcon.velocityrcon.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.bukkit.Server;

import java.net.SocketAddress;

public class RconServer {

    private final  org.bukkit.Server server;

    private ServerBootstrap bootstrap = new ServerBootstrap();
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    public RconServer( org.bukkit.Server server, final String password) {
        this.server = server;

        bootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new RconFramingHandler())
                                .addLast(new RconHandler(RconServer.this, password));
                    }
                });
    }

    public Server getServer() {
        return server;
    }

    /**
     * Bind the server on the specified address.
     *
     * @param address The address.
     * @return Netty channel future for bind operation.
     */
    public ChannelFuture bind(final SocketAddress address) {
        return bootstrap.bind(address);
    }

    /**
     * Shut the Rcon server down.
     */
    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

}
