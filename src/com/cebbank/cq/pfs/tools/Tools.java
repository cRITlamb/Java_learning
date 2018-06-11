package com.cebbank.cq.pfs.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static java.lang.System.out;

public class Tools {

    public static String getDayBefore(String dateFormat) {
        String specifiedDay = getCurrentTime(dateFormat);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat(dateFormat).format(c.getTime());
        return dayBefore;
    }

    public static String getCurrentTime(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        Calendar toDay = Calendar.getInstance();
        toDay.setTime(date);
        return sdf.format(toDay.getTime());
    }

    public static String getDayAfter(String dateFormat) {
        String specifiedDay = getCurrentTime(dateFormat);
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        String dayBefore = new SimpleDateFormat(dateFormat).format(c.getTime());
        return dayBefore;
    }

    public static String show(byte[] b) {
        char[] ch = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F' };

        java.io.StringWriter stream = new java.io.StringWriter();

        stream.write("[");
        int len = b.length;
        for (int i = 0; i < len; i++) {
            byte c = (byte) ((b[i] & 0xf0) >> 4);
            stream.write(ch[c]);
            c = (byte) (b[i] & 0x0f);
            stream.write(ch[c]);
            stream.write(" ");
        }
        stream.write("]");
        return stream.toString();
    }

    public static String uuid()
    {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getLabel(String theXml,String label)
    {
        String xml=theXml.replace("< ","<").replace(" >",">")
                .replace(" /","/").replace("/ ","/");

        String startlabel = "<"+label+">";
        String endlabel = "</"+label+">";

        int start = xml.indexOf(startlabel)+startlabel.length();
        int end = xml.indexOf(endlabel);

        return xml.substring(start, end);
    }

    public static void main(String[] args){
        out.println(Tools.getCurrentTime("YYYY-MM-dd HH:mm:ss"));
        out.println(Tools.getCurrentTime("YYYYMMddHHmmss"));
    }
}
