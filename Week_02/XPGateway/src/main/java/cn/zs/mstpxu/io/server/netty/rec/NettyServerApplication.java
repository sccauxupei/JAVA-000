package cn.zs.mstpxu.io.server.netty.rec;

import java.io.IOException;

public class NettyServerApplication {

	public static void main(String[] args) {
        try {
        	NettyServer server = new NettyServer(false,8808);
            server.run();
        }catch (Exception ex){

        }
	}

}
