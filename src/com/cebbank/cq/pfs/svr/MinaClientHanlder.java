package com.cebbank.cq.pfs.svr;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaClientHanlder extends IoHandlerAdapter {
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("客户端登陆");
        session.write("HelloWorld");
        session.write("认购业务编码SubscribeNo#|#预售许可证ReadyPayNo#|#房屋编号HouseId#|#认购金额SubscribeAmt#|#剩余待缴金额ResAmount#|#缴款通知书生成时间NoticePaymentDt#|#银行代码BankNo#|#银行名称BankName#|#监管总账号SuperviseAcct#|#监管子账号SuperviseSubacct#|#楼盘房屋描述HouseDesc#|#购房者姓名BuyerName#|#房屋类型HouseTyp#|#认购状态SubscribeStat#|#锁定状态LockStat#|#缴款状态PayInfoStat#|#交易结果代码MsgCode#|#结果说明MsgDesc#|#");

//        messageReceived(session,"");
        for (int i = 0; i < 10; i++) {
            session.write("p 412703840,4,1,1410248991,73451566,22615771,1239,125,90,0,0,1,900\r\n"
                    + "p 412703840,4,1,1410248991,73451566,22615771,1239,125,90,0,0,1,900\r\n"
                    + "p 412703840,4,1,1410248991,73451566,22615771,1239,125,90,0,0,1,900\r\n"
                    + "p 412703840,4,1,1410248991,73451566,22615771,1239,125,90,0,0,1,900");
        }
    }

    public void sessionClosed(IoSession session)
    {
        System.out.println("client close");
    }

    public void messageReceived(IoSession session , Object message)throws Exception
    {
        System.out.println("客户端接受到了消息"+message) ;

//        session.write("Sent by Client1");
    }

    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        super.exceptionCaught(session, cause);


    }
}
