/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ordermanager.messanger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class sendSMS {

    public String sendSms(String BillNo ,String MobileNo,String Name) {
        try {
            // Construct data
            String user = "username=" + "chanky.mallick@gmail.com";
            String hash = "&hash=" + "58928697161c74f2131ed8d7d67146dbc010a6a6";
            String message = "&message=" + "Thank you "+Name+" for Your Order ,  Bill No :"+BillNo+", - Mallick Dresses";
            String sender="&sender="+"";
            String numbers = "&numbers=" + "91"+MobileNo;
           // String test = "&test=true";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
            String data = user + hash + numbers + message +sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }
  
    public String sendSmsSMSLane(String BillNo ,String MobileNo,String Name) {
        try {
            // Construct data
            String user = "user=" + "chanky.mallick";
            String hash = "&password=" + "smu520759958";
            String message = "&msg=" + "Thank you "+Name+" for Your Order ,  Bill No :"+BillNo+", - Mallick Dresses-জমা হিসাব";
            String sender="&sid="+"WebSMS";
            String numbers = "&msisdn=" + "91"+MobileNo;
           // String test = "&test=true";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("/http://smslane.com/vendorsms/pushsms.aspx?").openConnection();
            String data = user + hash + numbers + message +sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS " + e);
            return "Error " + e;
        }
    }

    public static void main(String[] args) {
        System.out.println(new sendSMS().sendSmsSMSLane("1002", "7097919273", "CHANKY"));
    }
}
