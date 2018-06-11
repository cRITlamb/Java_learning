package com.cebbank.cq.pfs.web;

import com.cebbank.cq.pfs.crypt.RSAEncrypt;
import com.cebbank.cq.pfs.tools.Msg;
import com.cebbank.cq.pfs.tools.Tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.System.out;

public class Test extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doPost(request,response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String msgxml=getContent(request);
        String msgno=Tools.getLabel(msgxml,"msgno");
        String trancode= Tools.getLabel(msgxml,"trancode");
        String encrypt= Tools.getLabel(msgxml,"encrypt");

        out.println(msgno);
        out.println(trancode);

        String msg=RSAEncrypt.deCode(encrypt);

        out.println("Received:\n"+msg);

        PrintWriter respout = response.getWriter();
        String backmsg=processMsg(trancode,msg);
        out.println(backmsg);
        out.println("Sent:\n"+backmsg);
        respout.flush();
        respout.close();
    }

    private String getContent(HttpServletRequest request) throws IOException {
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
        return wholeStr;
    }

    private String processMsg(String trancode,String msg){
        String backMsg=null;
        String back2001="<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>" +
                "<TranCode>2001</TranCode>" +
                "<body>" +
                    "<SubscribeNo>认购业务编码SubscribeNo</SubscribeNo>" + //认购业务编码
                    "<ReadyPayNo>预售许可证ReadyPayNo</ReadyPayNo>" + //预售许可证
                    "<HouseId>房屋编号HouseId</HouseId>" + //房屋编号
                    "<SubscribeAmt>5000000</SubscribeAmt>" + //认购金额
                    "<ResAmount>4000000</ResAmount>" + //剩余待缴金额
                    "<NoticePaymentDt>20180101120000</NoticePaymentDt>" + //缴款通知书生成时间yyyyMMddHHmmss
                    "<BankNo>0009</BankNo>" + //银行代码
                    "<BankName>光大银行</BankName>" + //银行名称
                    "<SuperviseAcct>7848018800005487</SuperviseAcct>" + //监管总账号
                    "<SuperviseSubacct>784801880000548700001</SuperviseSubacct>" + //监管子账号
                    "<HouseDesc>楼盘房屋描述</HouseDesc>" + //楼盘房屋描述:房屋项目+楼栋+楼层+房号
                    "<BuyerName>购房者姓名</BuyerName>" + //购房者姓名
                    "<HouseTyp>0</HouseTyp>" + //房屋类型:0期房1现房
                    "<SubscribeStat>1</SubscribeStat>" +//认购状态:1-正常2-解除
                    "<LockStat>0</LockStat>" + //锁定状态:0-正常 1-锁定
                    "<PayInfoStat>1</PayInfoStat>" +//缴款状态：1-未交 2-部分缴清 3-已缴清 4-已退款
                    "<MsgCode>01</MsgCode>" + //交易结果代码:01-成功、00-失败
                    "<MsgDesc>结果说明</MsgDesc>" +
                "</body></response>";

        String back2002="<?xml version=\"1.0\" encoding=\"UTF-8\"?><response>" +
                "<TranCode>2002</TranCode>" +
                "<body>" +
                "<ContractNo>#合同业务编号ContractNo#</ContractNo>" + //合同业务编号
                "<SubscribeNo>#认购业务编码SubscribeNo#</SubscribeNo>" + //认购业务编码
                "<ReadyPayNo>#预售许可证ReadyPayNo#</ReadyPayNo>" + //预售许可证
                "<NetConNo>网签合同编号NetConNo#</NetConNo>" + //网签合同编号
                "<NetsignStat>1</NetsignStat>" + //网签状态:1正常2解除
                "<PayInfoStat>缴款状态</PayInfoStat>" + //缴款状态：1未交2部分缴清3已缴清
                "<HouseId>房屋编号</HouseId>" + //房屋编号
                "<HouseDesc>楼盘房屋描述</HouseDesc>" + //楼盘房屋描述:房屋项目+楼栋+楼层+房号
                "<NetTradeAmt>5000000</NetTradeAmt>" +//网签交易总金额
                "<NetDownpayAmt>1500000</NetDownpayAmt>" + //网签首付款金额
                "<ResAmount>3500000</ResAmount>" +//剩余待缴金额
                "<PayType>3</PayType>" + //付款方式:1一次性;2分期;3按揭
                "<PayCount></PayCount>" + //分期总期数
                "<PayNum></PayNum>" + //本次付款期数
                "<PayAmount></PayAmount>" + //本期应付金额
                "<PayDate></PayDate>" + //本期应付日期
                "<PayStagesStat></PayStagesStat>" + //分期缴款状态:0-未缴清；1-缴清
                "<NoticePaymentDt>20180101120000</NoticePaymentDt>" + //缴款通知书生成时间
                "<SuperviseAcct>7848018800005487</SuperviseAcct>" + //监管总账号
                "<SuperviseSubacct>784801880000548700001</SuperviseSubacct>" + //监管子账号
                "<BuyerName>购房者姓名</BuyerName>" + //购房者姓名
                "<HouseTyp>0</HouseTyp>" + //房屋类型 0期房1现房
                "<MsgCode>01</MsgCode>" + //交易结果代码 01-成功、00-失败
                "<MsgDesc>结果说明</MsgDesc>" + //结果说明
                "</body></response>";

        String back2003="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<response>" +
                "<TranCode>2003</TranCode>" +
                "<body>" +
                "<SubscribeNo>认购业务编码SubscribeNo</SubscribeNo>" +
                "<DoneTranNo>接入平台确认流水号DoneTranNo</DoneTranNo>" +
                "<DoneDt>接入平台确认日期DoneDt</DoneDt>" +
                "<MsgCode>01</MsgCode>" + //交易结果代码 01-成功、00-失败
                "<MsgDesc>结果说明</MsgDesc>" + //结果说明
                "</body>" +
                "</response>";

        String back2004="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<response>" +
                "<TranCode>2004</TranCode>" +
                "<body>" +
                "<ContractNo>合同业务编号ContractNo</ContractNo>" +
                "<PayNum>付款期数PayNum</PayNum>" +
                "<DoneTranNo>接入平台确认流水号DoneTranNo</DoneTranNo>" +
                "<DoneDt>接入平台确认日期DoneDt</DoneDt>" +
                "<MsgCode>01</MsgCode>" + //交易结果代码 01-成功、00-失败
                "<MsgDesc>结果说明</MsgDesc>" +
                "</body>" +
                "</response>";

        if(trancode.equals("2001"))
            return Msg.getMsg("2001",back2001);
        else if(trancode.equals("2002"))
            return Msg.getMsg("2002",back2002);
        else if(trancode.equals("2003"))
        {
            return Msg.getMsg("2003",back2003);
        }
        else if(trancode.equals("2004"))
        {
            return Msg.getMsg("2004",back2004);
        }
        return null;
    }
}