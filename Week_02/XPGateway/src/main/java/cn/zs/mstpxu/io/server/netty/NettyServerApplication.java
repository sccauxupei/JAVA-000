package cn.zs.mstpxu.io.server.netty;

public class NettyServerApplication {

	public static void main(String[] args) {
        NettyServer server = new NettyServer(false,8808);
        try {
            server.run();
        }catch (Exception ex){
            ex.printStackTrace();
        }
	}

}
