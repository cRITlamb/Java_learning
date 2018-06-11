package com.cebbank.cq.pfs.tools;

import com.cebbank.cq.pfs.crypt.RSAEncrypt;

public class Msg {
    private static String raw_msg="<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><msgno>0009#报文编号msgno#</msgno><version>1.0</version>" +
            "<trancode>#交易码trancode#</trancode><sndnode>0009</sndnode><rcvnode>0999</rcvnode><datetime>#时间戳yyyyMMddHHmmss#</datetime>" +
            "<encrypt>#密文encrypt#</encrypt><signature>#签名signature#</signature></root>";

    public static String getMsg(String trancode,String xml){
        return raw_msg.replace("#报文编号msgno#",Tools.getCurrentTime("YYYYMMddHHmmss")+Seq.Next())
                .replace("#交易码trancode#",trancode)
                .replace("#时间戳yyyyMMddHHmmss#",Tools.getCurrentTime("YYYYMMddHHmmss"))
                .replace("#密文encrypt#", RSAEncrypt.enCode(xml))
                .replace("#签名signature#",RSAEncrypt.sign(xml));
    }
}
