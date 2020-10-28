package cn.zs.mstpxu.io.server.netty;

import java.security.cert.CertificateException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class NettyServer {
    private boolean ssl;
    private int port;
    
    
	public NettyServer(boolean ssl, int port) {
		this.ssl = ssl;
		this.port = port;
	}

	
	public NettyServer() {
		this(false, 8888);
	}



	public void run() throws Exception {
        final SslContext sslCtx;
        if (ssl) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        
		EventLoopGroup bossGroup = new NioEventLoopGroup(3);
		EventLoopGroup workerGroup = new NioEventLoopGroup(100);
        try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.option(ChannelOption.SO_BACKLOG, 128)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_REUSEADDR, true)
				.option(ChannelOption.SO_RCVBUF, 2*1024)
				.option(ChannelOption.SO_SNDBUF, 2*1024)
				.option(EpollChannelOption.SO_REUSEPORT, true);
			boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new HttpInitializer(sslCtx));
			
	        Channel ch = boot.bind(port).sync().channel();
      
	        ch.closeFuture().sync();
	    } finally {
	        bossGroup.shutdownGracefully();
	        workerGroup.shutdownGracefully();
	    }
	}
}
