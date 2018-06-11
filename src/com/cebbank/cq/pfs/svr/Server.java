package com.cebbank.cq.pfs.svr;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;


import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;


public class Server {
    private static final int PORT = 9000;
    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) throws Exception {

        PropertyConfigurator.configure("./log4j.properties");
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        // Prepare the configuration
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("GBK"))));//StandardCharsets.UTF_8

        // Bind
        acceptor.setHandler(new MinaServerHanlder());
        acceptor.bind(new InetSocketAddress(PORT));


        logger.info("Listening on port:" + PORT);
        logger.info("Server Started.");
    }
}
