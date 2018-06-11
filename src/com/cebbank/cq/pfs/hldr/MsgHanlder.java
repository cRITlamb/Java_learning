package com.cebbank.cq.pfs.hldr;

import com.cebbank.cq.pfs.crypt.RSAEncrypt;
import com.cebbank.cq.pfs.tools.HttpClient;
import com.cebbank.cq.pfs.tools.Msg;
import com.cebbank.cq.pfs.tools.Tools;
import org.apache.log4j.Logger;

import java.io.IOException;

import static java.lang.System.out;

public class MsgHanlder {
    private static Logger logger = Logger.getLogger(MsgHanlder.class);

    public static String t2001(String msg){
        //1.1	认购缴款查询（FTS1）
        //3.6.1	查询认购信息接口2001
        String received2001="FTS1^SubscribeNo^Reserved1^Reserved2^Reserved3***";
        String raw_send2FGJ_2001="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><TranCode>2001</TranCode><body>" +
                "<SubscribeNo>#SubscribeNo#</SubscribeNo></body></request>";
        String resp2001="FTS1^#认购业务编码SubscribeNo#^#预售许可证ReadyPayNo#^#房屋编号HouseId#^#认购金额SubscribeAmt#^" +
                "#剩余待缴金额ResAmount#^#缴款通知书生成时间NoticePaymentDt#^#监管总账号SuperviseAcct#^" +
                "#监管子账号SuperviseSubacct#^#楼盘房屋描述HouseDesc#^#购房者姓名BuyerName#^#房屋类型HouseTyp#^#认购状态SubscribeStat#^" +
                "#锁定状态LockStat#^#缴款状态PayInfoStat#^#交易结果代码MsgCode#^#结果说明MsgDesc#^Reserved1^Reserved2^Reserved3***";

        if(msg.split("\\^").length!=5)
        {
            return "ERR^光大分前报文解析错误***";
        }

        String SubscribeNo=msg.split("\\^")[1];
        String send2FGJ = Msg.getMsg("2001",raw_send2FGJ_2001.replace("#SubscribeNo#",SubscribeNo));


        HttpClient hc = new HttpClient(send2FGJ);
        String received4FGJ=null;
        try{
            received4FGJ=hc.getResult();
        }
        catch(IOException e){
            e.printStackTrace();
            return "ERR^国土房管局报文解析错误***";
        }

        if(received4FGJ==null)
            return resp2001.replace("#交易结果代码MsgCode#","00");

        String encrypt=Tools.getLabel(received4FGJ,"encrypt");
        String backmsg= RSAEncrypt.deCode(encrypt);

        logger.info(backmsg);

//        String SubscribeNo=Tools.getLabel(backmsg,"SubscribeNo");                       // 认购业务编码SubscribeNo
        String ReadyPayNo=Tools.getLabel(backmsg,"ReadyPayNo");                   //预售许可证ReadyPayNo
        String HouseId=Tools.getLabel(backmsg,"HouseId");                         //房屋编号HouseId
        String SubscribeAmt=Tools.getLabel(backmsg,"SubscribeAmt");               //认购金额SubscribeAmt
        String ResAmount=Tools.getLabel(backmsg,"ResAmount");                     //剩余待缴金额ResAmount
        String NoticePaymentDt=Tools.getLabel(backmsg,"NoticePaymentDt");        //缴款通知书生成时间NoticePaymentDt
        String SuperviseAcct=Tools.getLabel(backmsg,"SuperviseAcct");            //监管总账号SuperviseAcct
        String SuperviseSubacct=Tools.getLabel(backmsg,"SuperviseSubacct");     //监管子账号SuperviseSubacct
        String HouseDesc=Tools.getLabel(backmsg,"HouseDesc");                    //楼盘房屋描述HouseDesc
        String BuyerName=Tools.getLabel(backmsg,"BuyerName");                    //购房者姓名BuyerName
        String HouseTyp=Tools.getLabel(backmsg,"HouseTyp");                      //房屋类型HouseTyp
        String SubscribeStat=Tools.getLabel(backmsg,"SubscribeStat");           //认购状态SubscribeStat
        String LockStat=Tools.getLabel(backmsg,"LockStat");                     //锁定状态LockStat
        String PayInfoStat=Tools.getLabel(backmsg,"PayInfoStat");               //缴款状态PayInfoStat
        String MsgCode=Tools.getLabel(backmsg,"MsgCode");                       //交易结果代码MsgCode
        String MsgDesc=Tools.getLabel(backmsg,"MsgDesc");                       //结果说明MsgDesc

        String respMsg=resp2001.replace("#认购业务编码SubscribeNo#",SubscribeNo)
                .replace("#预售许可证ReadyPayNo#",ReadyPayNo)
                .replace("#房屋编号HouseId#",HouseId)
                .replace("#认购金额SubscribeAmt#",SubscribeAmt)
                .replace("#剩余待缴金额ResAmount#",ResAmount)
                .replace("#缴款通知书生成时间NoticePaymentDt#",NoticePaymentDt)
                .replace("#监管总账号SuperviseAcct#",SuperviseAcct)
                .replace("#监管子账号SuperviseSubacct#",SuperviseSubacct)
                .replace("#楼盘房屋描述HouseDesc#",HouseDesc)
                .replace("#购房者姓名BuyerName#",BuyerName)
                .replace("#房屋类型HouseTyp#",HouseTyp)
                .replace("#认购状态SubscribeStat#",SubscribeStat)
                .replace("#锁定状态LockStat#",LockStat)
                .replace("#缴款状态PayInfoStat#",PayInfoStat)
                .replace("#交易结果代码MsgCode#",MsgCode)
                .replace("#结果说明MsgDesc#",MsgDesc);
;
        logger.info(respMsg);

        return respMsg;
    }

    public static String t2002(String msg){
        //1.3	网签缴款查询（FTS03）
        //3.6.2	查询网签信息接口2002
        String received="FTS3^ContractNo合同业务编号^Reserved1保留1^Reserved2保留2^Reserved3保留3^***";
        String resp2002="FTS3^#合同业务编号ContractNo#^#认购业务编码SubscribeNo#^#预售许可证ReadyPayNo#^#网签合同编号NetConNo#^#网签状态NetsignStat#^" +
                "#缴款状态PayInfoStat#^#房屋编号HouseId#^#楼盘房屋描述HouseDesc#^#网签交易总金额NetTradeAmt#^#网签首付款金额NetDownpayAmt#^" +
                "#剩余待缴金额ResAmount#^#付款方式PayType#^#分期总期数PayCount#^#本次付款期数PayNum#^#本期应付金额PayAmount#^#本期应付日期PayDate#^" +
                "#分期缴款状态PayStagesStat#^#缴款通知书生成时间NoticePaymentDt#^#监管总账号SuperviseAcct#^#监管子账号SuperviseSubacct#^" +
                "#购房者姓名BuyerName#^#房屋类型HouseTyp#^#交易结果代码MsgCode#^#结果说明MsgDesc#^Reserved1^Reserved2^Reserved3***";

        String raw_send2FGJ_2002="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><TranCode>2002</TranCode><body>" +
                "<ContractNo>#ContractNo#</ContractNo></body></request>";

        if(msg.split("\\^").length!=5)
        {
            return "ERR^光大分前报文解析错误***";
        }

        String ContractNo=msg.split("\\^")[1];
        String send2FGJ = Msg.getMsg("2002",raw_send2FGJ_2002.replace("#ContractNo#",ContractNo));

        HttpClient hc = new HttpClient(send2FGJ);
        String received4FGJ=null;
        try{
            received4FGJ=hc.getResult();
        }
        catch(IOException e){
            e.printStackTrace();
            return "ERR^国土房管局报文解析错误***";
        }

        if(received4FGJ==null)
            return resp2002.replace("#交易结果代码MsgCode#","00");

        String encrypt=Tools.getLabel(received4FGJ,"encrypt");
        String backmsg= RSAEncrypt.deCode(encrypt);

        out.println(backmsg);

        //String ContractNo=Tools.getLabel(backmsg,"ContractNo");//#合同业务编号ContractNo#
        String SubscribeNo=Tools.getLabel(backmsg,"SubscribeNo");//#认购业务编码SubscribeNo#
        String ReadyPayNo=Tools.getLabel(backmsg,"ReadyPayNo");//#预售许可证ReadyPayNo#
        String NetConNo=Tools.getLabel(backmsg,"NetConNo");//#网签合同编号NetConNo#
        String NetsignStat=Tools.getLabel(backmsg,"NetsignStat");//#网签状态NetsignStat#
        String PayInfoStat=Tools.getLabel(backmsg,"PayInfoStat");//#缴款状态PayInfoStat#
        String HouseId=Tools.getLabel(backmsg,"HouseId");//#房屋编号HouseId#
        String HouseDesc=Tools.getLabel(backmsg,"HouseDesc");//#楼盘房屋描述HouseDesc#
        String NetTradeAmt=Tools.getLabel(backmsg,"NetTradeAmt");//#网签交易总金额NetTradeAmt#
        String NetDownpayAmt=Tools.getLabel(backmsg,"NetDownpayAmt");//#网签首付款金额NetDownpayAmt#
        String ResAmount=Tools.getLabel(backmsg,"ResAmount");//#剩余待缴金额ResAmount#
        String PayType=Tools.getLabel(backmsg,"PayType");//#付款方式PayType#
        String PayCount=Tools.getLabel(backmsg,"PayCount");//#分期总期数PayCount#
        String PayNum=Tools.getLabel(backmsg,"PayNum");//#本次付款期数PayNum#
        String PayAmount=Tools.getLabel(backmsg,"PayAmount");//#本期应付金额PayAmount#
        String PayDate=Tools.getLabel(backmsg,"PayDate");//#本期应付日期PayDate#
        String PayStagesStat=Tools.getLabel(backmsg,"PayStagesStat");//#分期缴款状态PayStagesStat#
        String NoticePaymentDt=Tools.getLabel(backmsg,"NoticePaymentDt");//#缴款通知书生成时间NoticePaymentDt#
        String SuperviseAcct=Tools.getLabel(backmsg,"SuperviseAcct");//#监管总账号SuperviseAcct#
        String SuperviseSubacct=Tools.getLabel(backmsg,"SuperviseSubacct");//#监管子账号SuperviseSubacct#
        String BuyerName=Tools.getLabel(backmsg,"BuyerName");//#购房者姓名BuyerName#
        String HouseTyp=Tools.getLabel(backmsg,"HouseTyp");//#房屋类型HouseTyp#
        String MsgCode=Tools.getLabel(backmsg,"MsgCode");//#交易结果代码MsgCode#
        String MsgDesc=Tools.getLabel(backmsg,"MsgDesc");//#结果说明MsgDesc#

        String respMsg=resp2002.replace("#合同业务编号ContractNo#",ContractNo)
                .replace("#认购业务编码SubscribeNo#",SubscribeNo)
                .replace("#预售许可证ReadyPayNo#",ReadyPayNo)
                .replace("#网签合同编号NetConNo#",NetConNo)
                .replace("#网签状态NetsignStat#",NetsignStat)
                .replace("#缴款状态PayInfoStat#",PayInfoStat)
                .replace("#房屋编号HouseId#",HouseId)
                .replace("#楼盘房屋描述HouseDesc#",HouseDesc)
                .replace("#网签交易总金额NetTradeAmt#",NetTradeAmt)
                .replace("#网签首付款金额NetDownpayAmt#",NetDownpayAmt)
                .replace("#剩余待缴金额ResAmount#",ResAmount)
                .replace("#付款方式PayType#",PayType)
                .replace("#分期总期数PayCount#",PayCount)
                .replace("#本次付款期数PayNum#",PayNum)
                .replace("#本期应付金额PayAmount#",PayAmount)
                .replace("#本期应付日期PayDate#",PayDate)
                .replace("#分期缴款状态PayStagesStat#",PayStagesStat)
                .replace("#缴款通知书生成时间NoticePaymentDt#",NoticePaymentDt)
                .replace("#监管总账号SuperviseAcct#",SuperviseAcct)
                .replace("#监管子账号SuperviseSubacct#",SuperviseSubacct)
                .replace("#购房者姓名BuyerName#",BuyerName)
                .replace("#房屋类型HouseTyp#",HouseTyp)
                .replace("#交易结果代码MsgCode#",MsgCode)
                .replace("#结果说明MsgDesc#",MsgDesc);

        return respMsg;
    }

    public static String t2003(String msg){
        //1.2	认购缴款（FTS01）
        //3.6.3	认购缴款确认接口2003
        String received="FTS2^SubscribeNo认购业务编码^PayAcctNo付款人账户^PayAcctName付款人名称^PayAmt缴款金额^FeeAmt手续费金额^PayChannel缴款渠道" +
                "^BankTranNo银行流水号^PayDt缴款日期^PayDateTime记账日期时间^Reserved1保留1^Reserved2保留2^Reserved3保留3***";

        String resp2003="FTS2^#认购业务编码SubscribeNo#^#确认流水号DoneTranNo#^#确认日期DoneDt#^#交易结果代码MsgCode#^#结果说明MsgDesc#^Reserved1^Reserved2^Reserved3***";

        String raw_send2FGJ_2003="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><TranCode>2003</TranCode>" +
                "<body><SubscribeNo>#SubscribeNo#</SubscribeNo>" +
                "<PayAcctNo>#PayAcctNo#</PayAcctNo>" +
                "<PayAcctName>#PayAcctName#</PayAcctName>" +
                "<PayAmt>#PayAmt#</PayAmt>" +
                "<FeeAmt>#FeeAmt#</FreeAmt>" +
                "<PayChannel>#PayChannel#</PayChannel>" +
                "<BankTranNo>#BankTranNo#</BankTranNo>" +
                "<PayDt>#PayDt#</PayDt>" +
                "<PayDateTime>#PayDateTime#</PayDateTime>" +
                "</body></request>";

        if(msg.split("\\^").length!=13)
        {
            return "ERR^光大分前报文解析错误***";
        }

        String SubscribeNo=msg.split("\\^")[1];
        String PayAcctNo=msg.split("\\^")[2];
        String PayAcctName=msg.split("\\^")[3];
        String PayAmt=msg.split("\\^")[4];
        String FeeAmt=msg.split("\\^")[5];
        String PayChannel=msg.split("\\^")[6];
        String BankTranNo=msg.split("\\^")[7];
        String PayDt=msg.split("\\^")[8];
        String PayDateTime=msg.split("\\^")[9];

        String send2FGJ = Msg.getMsg("2002",raw_send2FGJ_2003.replace("#SubscribeNo#",SubscribeNo)
                .replace("#PayAcctNo#",PayAcctNo)
                .replace("#PayAcctName#",PayAcctName)
                .replace("#PayAmt#",PayAmt)
                .replace("#FeeAmt#",FeeAmt)
                .replace("#PayChannel#",PayChannel)
                .replace("#BankTranNo#",BankTranNo)
                .replace("#PayDt#",PayDt)
                .replace("#PayDateTime#",PayDateTime));

        HttpClient hc = new HttpClient(send2FGJ);
        String received4FGJ=null;
        try{
            received4FGJ=hc.getResult();
        }
        catch(IOException e){
            e.printStackTrace();
            return "ERR^国土房管局报文解析错误***";
        }

        if(received4FGJ==null)
            return resp2003.replace("#交易结果代码MsgCode#","00");

        String encrypt=Tools.getLabel(received4FGJ,"encrypt");
        String backmsg= RSAEncrypt.deCode(encrypt);

        logger.info(backmsg);

        //String SubscribeNo=Tools.getLabel(backmsg,"SubscribeNo");//认购业务编码SubscribeNo
        String DoneTranNo=Tools.getLabel(backmsg,"DoneTranNo");//确认流水号DoneTranNo
        String DoneDt=Tools.getLabel(backmsg,"DoneDt");//确认日期DoneDt
        String MsgCode=Tools.getLabel(backmsg,"MsgCode");//交易结果代码MsgCode
        String MsgDesc=Tools.getLabel(backmsg,"MsgDesc");//结果说明MsgDesc

        String respMsg=resp2003.replace("#认购业务编码SubscribeNo#",SubscribeNo)
                .replace("#确认流水号DoneTranNo#",DoneTranNo)
                .replace("#确认日期DoneDt#",DoneDt)
                .replace("#交易结果代码MsgCode#",MsgCode)
                .replace("#结果说明MsgDesc#",MsgDesc);

        return respMsg;
    }

    public static String t2004(String msg){
        //1.4	网签缴款（FTS04）
        //3.6.4	网签缴款确认接口2004
        String received="FTS4^ContractNo合同业务编号^PayNum付款期数^PayAcctNo付款人账户^PayAcctName付款人名称^PayAmt缴款金额^FeeAmt手续费金额" +
                "^PayChannel缴款渠道^BankTranNo银行流水号^PayDt缴款日期^PayDateTime记账日期时间^Reserved1保留1^Reserved2保留2^Reserved3保留3***";
        String resp2004="FTS4^#合同业务编号ContractNo#^#付款期数PayNum#^#确认流水号DoneTranNo#^#确认日期DoneDt#^#交易结果代码MsgCode#^#结果说明MsgDesc#^Reserved1^Reserved2^Reserved3***";

        String raw_send2FGJ_2003="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<request>" +
                "<TranCode>2004</TranCode>" +
                "<body>" +
                "<ContractNo>#ContractNo#</ContractNo>" +
                "<PayNum>#PayNum#</PayNum>" +
                "<PayAcctNo>#PayAcctNo#</PayAcctNo>" +
                "<PayAcctName>#PayAcctName#</PayAcctName>" +
                "<PayAmt>#PayAmt#</PayAmt>" +
                "<FeeAmt>#FeeAmt#</FreeAmt>" +
                "<PayChannel>#PayChannel#</PayChannel>" +
                "<BankTranNo>#BankTranNo#</BankTranNo>" +
                "<PayDt>#PayDt#</PayDt>" +
                "<PayDateTime>#PayDateTime#</PayDateTime>" +
                "</body>" +
                "</request>";

        if(msg.split("\\^").length!=14)
        {
            return "ERR^光大分前报文解析错误***";
        }

        String ContractNo=msg.split("\\^")[1];
        String PayNum=msg.split("\\^")[2];
        String PayAcctNo=msg.split("\\^")[3];
        String PayAcctName=msg.split("\\^")[4];
        String PayAmt=msg.split("\\^")[5];
        String FeeAmt=msg.split("\\^")[6];
        String PayChannel=msg.split("\\^")[7];
        String BankTranNo=msg.split("\\^")[8];
        String PayDt=msg.split("\\^")[9];
        String PayDateTime=msg.split("\\^")[10];

        String send2FGJ = Msg.getMsg("2002",raw_send2FGJ_2003.replace("#ContractNo#",ContractNo)
                .replace("#PayNum#",PayNum)
                .replace("#PayAcctNo#",PayAcctNo)
                .replace("#PayAcctName#",PayAcctName)
                .replace("#PayAmt#",PayAmt)
                .replace("#FeeAmt#",FeeAmt)
                .replace("#PayChannel#",PayChannel)
                .replace("#BankTranNo#",BankTranNo)
                .replace("#PayDt#",PayDt)
                .replace("#PayDateTime#",PayDateTime));

        HttpClient hc = new HttpClient(send2FGJ);
        String received4FGJ=null;
        try{
            received4FGJ=hc.getResult();
        }
        catch(IOException e){
            e.printStackTrace();
            return "ERR^国土房管局报文解析错误***";
        }

        if(received4FGJ==null)
            return resp2004.replace("#交易结果代码MsgCode#","00");

        String encrypt=Tools.getLabel(received4FGJ,"encrypt");
        String backmsg= RSAEncrypt.deCode(encrypt);

        logger.info(backmsg);

//        String PayNum=Tools.getLabel(backmsg,"PayNum");//付款期数	PayNum
        String DoneTranNo=Tools.getLabel(backmsg,"DoneTranNo");//确认流水号DoneTranNo
        String DoneDt=Tools.getLabel(backmsg,"DoneDt");//确认日期DoneDt
        String MsgCode=Tools.getLabel(backmsg,"MsgCode");//交易结果代码MsgCode
        String MsgDesc=Tools.getLabel(backmsg,"MsgDesc");//结果说明MsgDesc

        String respMsg=resp2004.replace("#合同业务编号ContractNo#",ContractNo)
                .replace("#付款期数PayNum#",PayNum)
                .replace("#确认流水号DoneTranNo#",DoneTranNo)
                .replace("#确认日期DoneDt#",DoneDt)
                .replace("#交易结果代码MsgCode#",MsgCode)
                .replace("#结果说明MsgDesc#",MsgDesc);

        return respMsg;
    }
}
