package com.socketclient;

import android.app.Application;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import Model.User;

/**
 * Created by erick on 2016-05-16.
 */
public class ConnectionToServer extends Application{
    Socket socket;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;
    public static int userId;
    public static HashMap<Integer, User> userMap;

    @Override
    public void onCreate(){
        super.onCreate();
    }
    public ConnectionToServer(){}
    public ConnectionToServer(Socket socket){
        try {
            this.socket = socket;
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(Object message) throws Exception {
        oos.writeUnshared(message);
        oos.flush();
    }

    public Object read() throws Exception {
        return ois.readObject();
    }

    public void close() throws Exception {
        ois.close();
        oos.close();
    }

}