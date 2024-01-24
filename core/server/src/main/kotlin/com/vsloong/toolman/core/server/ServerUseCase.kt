package com.vsloong.toolman.core.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.stream.ChunkedWriteHandler
import java.net.InetSocketAddress
import java.nio.file.Path

class ServerUseCase {

    fun start(serverConfig: ServerConfig) {
        start(port = serverConfig.port, localDirPath = serverConfig.localServerDirPath)
    }

    fun start(port: Int, localDirPath: Path) {

        val bossGroup = NioEventLoopGroup()
        val workGroup = NioEventLoopGroup()

        try {
            val bootStrap = ServerBootstrap()
            bootStrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel::class.java)
                .localAddress(InetSocketAddress(port))
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(socketChannel: SocketChannel) {
                        //http消息解码器
                        socketChannel.pipeline().addLast("http-decoder", HttpRequestDecoder())
                        //将消息转为单一的FullHttpRequest或者FullHttpResponse，因为http解码器在每个http消息中会生成多个消息对象
                        socketChannel.pipeline().addLast("http-aggregator", HttpObjectAggregator(65535))
                        //对响应消息进行编码
                        socketChannel.pipeline().addLast("http-encoder", HttpResponseEncoder())
                        //支持异步发送大大码流，但不占用过多但内存，防止发生Java内存溢出
                        socketChannel.pipeline().addLast("http-chunked", ChunkedWriteHandler())

                        // 自定义文件处理器
                        socketChannel.pipeline().addLast("fileServerHandler", HttpFileServerHandler(localDirPath))
                    }

                })
                .childOption(ChannelOption.SO_KEEPALIVE, true)

            val future = bootStrap.bind().sync()
            future.channel().closeFuture().sync()
        } catch (e: Throwable) {
            bossGroup.shutdownGracefully()
            workGroup.shutdownGracefully()
        }
    }
}