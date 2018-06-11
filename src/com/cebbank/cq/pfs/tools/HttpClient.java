package com.cebbank.cq.pfs.tools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class HttpClient
{
    private String _host = "localhost";
    private int _port = 80;
    private String _path = "/cqgtfw_access/";
    private String _content;
    private String userAgent = "CEBBank/CQ";
    private String accept = "www/source; text/html; image/gif; */*";
    private String contentType = "text/html; charset=utf-8";
    private String contentLength;

    public HttpClient(String content)
    {
        this._content = content;
        this.contentLength = Integer.toString(content.getBytes().length);
    }

    private Socket connect() throws UnknownHostException, IOException
    {
        return new Socket(this._host, this._port);
    }

    private String getMessage()
    {
        StringBuffer message = new StringBuffer();
        message.append("POST " + this._path + " HTTP/1.0\r\n");
        message.append("User-Agent: " + this.userAgent + "\r\n");
        message.append("Accept: " + this.accept + "\r\n");
        message.append("Content-type: " + this.contentType + "\r\n");
        message.append("Content-length: " + this.contentLength + "\r\n\r\n");
        message.append(this._content+ "\r\n\r\n");
        message.append("\r\n\r\n");
        return message.toString();
    }

    public String getResult() throws UnknownHostException, IOException
    {
        Socket socket = connect();
        byte[] returnBytes = (byte[])null;

        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//        out.println("abc:");
//        out.println(getMessage());
        output.write(getMessage().getBytes());
        output.flush();

        InputStream input = socket.getInputStream();

        StringBuffer buffer = new StringBuffer();

        List<Byte> byteList = new ArrayList<Byte>();
        boolean isExit = false;
        while (true)
        {
            if (input.available() > 0)
            {
                while (input.available() > 0 || isExit==false)
                {
                    returnBytes = new byte[input.available()];
                    input.read(returnBytes);

                    for(Byte b:returnBytes)
                    {
                        byteList.add(b);
                    }

                    String strReceived = new String(returnBytes).trim();
                    buffer.append(strReceived);

                    if(buffer.toString().indexOf("</root>")!=-1)
                    {
                        isExit=true;
                    }
                }
                break;
            }
        }

        byte[] bytes = new byte[byteList.size()];
        for(int i=0;i<byteList.size();i++)
        {
            bytes[i] = byteList.get(i).byteValue();
        }

        if (buffer.length() == 0)
        {
        }
        else
        {
        }

        input.close();
        output.close();

        socket.close();

        if (buffer.length() > 0)
        {
            return new String(new String(bytes).trim());
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException, IOException
    {
        String msg="<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<request>" +
                "<TranCode>2001</TranCode>" +
                "<body>" +
                "<SubscribeNo>123456789012345</SubscribeNo>" +
                "</body>" +
                "</request>";

        String allmsg=Msg.getMsg("2001",msg);
        out.println(allmsg);

        HttpClient hc = new HttpClient(allmsg);
        String backmsg=hc.getResult();
        out.println("返回:"+backmsg);

    }
}
