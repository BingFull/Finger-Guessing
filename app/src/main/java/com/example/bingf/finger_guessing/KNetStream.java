package com.example.bingf.finger_guessing;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by karthur on 2016/3/31.
 */
public class KNetStream {
    private String ip;
    private int port;
    private Handler handler;
    private String name;
    DatagramSocket ds;
    public KNetStream(Handler handler){
        ip="115.159.4.119";
        port=12322;
        this.handler=handler;
        try {
            ds=new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open(String myName){
        this.name = myName;
        new Thread(new Listener(handler,myName)).start();
    }

    public void  send(final String userName,final String msg){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ds.send(new DatagramPacket(("2,"+userName+","+name+","+msg).getBytes("UTF-8"),("2,"+userName+","+name+","+msg).length(),InetAddress.getByName(ip),port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getAllUserName(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ds.send(new DatagramPacket(("3," + name).getBytes("UTF-8"), ("3," + name).length(), InetAddress.getByName(ip), port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close(){//login out
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ds.send(new DatagramPacket(("1," + name).getBytes("UTF-8"),("1," + name).length(), InetAddress.getByName(ip), port));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class Listener implements Runnable{
    private Handler handler;
    private  String name;
    public Listener(Handler handler,String name){
        this.handler=handler;
        this.name=name;
    }

    public void run(){
        try {
            OutputStream os;
            InputStream is;
            Socket listener = new Socket();
            listener.connect(new InetSocketAddress(InetAddress.getByName("115.159.4.119"),12321));
            os=listener.getOutputStream();
            os.write(("0," + name).getBytes("UTF-8"));
            os.flush();
            byte[] data = new byte[128];
            int length;
            String d;
            for(;;){
                is=listener.getInputStream();
                length =  is.read(data);
                d=new String(data,"UTF-8").substring(0,length);
                if(d.compareTo("loginok")==0){
                    handler.onResult(true);
                }
                else if(d.compareTo("loginfailed")==0){
                    handler.onResult(false);
                }
                else if(d.contains("ihaveamsg")){
                    String dd = d.substring(9);
                    handler.onReceive(dd.substring(0,dd.indexOf(',')),dd.substring(dd.indexOf(',')+1));
                }
                else if(d.contains("allusername")){
                    String dd = d.substring(11);
                    List<String> users=new LinkedList<>();
                    String un="";
                    for(int i=0;i<dd.length();i++){
                        if(dd.charAt(i)==','){
                            users.add(un);
                            un="";
                        }
                        else{
                            un+=dd.charAt(i);
                        }
                    }
                    handler.onAllUserName(users);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}