package com.cebbank.cq.pfs.svr;

import com.cebbank.cq.pfs.hldr.MsgHanlder;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class MinaServerHanlder  extends IoHandlerAdapter {
    private Map<IoSession, String> sessionMap = new HashMap<IoSession,String>();
    private static Logger logger = Logger.getLogger(MinaServerHanlder.class);

    // 由底层决定是否创建一个session
    public void sessionCreated(IoSession session) {
        logger.info("###NEW:"+session);
    }

    // 创建了session 后会回调sessionOpened
    public void sessionOpened(IoSession session) throws Exception {
    }

    // 当收到了客户端发送的消息后会回调这个函数
    public void messageReceived(IoSession session, Object message) {
        logger.info(message);
        String msg=new String((String)message);
        if(msg.indexOf("***")==-1)
        {
            String oldmsg=sessionMap.get(session);
            if(oldmsg!=null)
            {
                msg=oldmsg+msg;
                sessionMap.replace(session,msg);
            }
            else{
                sessionMap.put(session,msg);
            }
            if(msg.indexOf("FTS")==-1)
            {
                session.write("ERR^报文解析错误***");
                session.closeOnFlush();
            }
            return;
        }
        else{
            String oldmsg=sessionMap.get(session);
            if(oldmsg!=null)
            {
                sessionMap.replace(session,null);
                msg=oldmsg+msg;
            }
        }
        logger.info("Received"+session+":\n\t"+msg);

        String trancode = (msg.split("\\^"))[0];

        logger.info("Trancode:"+trancode);
        String backMsg=null;
        if(trancode.equals("FTS1"))
            backMsg = MsgHanlder.t2001(msg);
        else if(trancode.equals("FTS3"))
            backMsg = MsgHanlder.t2002(msg);
        else if(trancode.equals("FTS2"))
            backMsg = MsgHanlder.t2003(msg);
        else if(trancode.equals("FTS4"))
            backMsg = MsgHanlder.t2004(msg);
        else
            backMsg="ERR^报文解析错误***";

        session.write(backMsg);
        session.closeOnFlush();
    }

    public void messageSent(IoSession session, Object message) {
        logger.info("Sent"+session+":\n\t"+message);
    }

    // session 关闭调用
    public void sessionClosed(IoSession session) {
        logger.info("###DISCONNECT"+session+"\n\n");
        session.closeNow();
    }

    // session 空闲的时候调用
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("###IDLE"+session);
        session.closeNow();
    }

    // 异常捕捉
    public void exceptionCaught(IoSession session, Throwable cause) {
        logger.info("###EXCEPTION"+session);
        logger.error(cause);
        cause.printStackTrace();

        session.write("ERR^报文解析错误***");
        session.closeOnFlush();
    }
}
