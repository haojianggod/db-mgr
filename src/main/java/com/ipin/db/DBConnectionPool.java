package com.ipin.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * Created by apple on 15/9/22.
 */
public class DBConnectionPool{

    private Connection conn=null;
    private int inUsed=0;
    private ArrayList freeConnections = new ArrayList();
    private int minConn;
    private int maxConn;
    private String name;
    private String password;
    private String url;  // connection addredd
    private String driver; // connection driver
    private String user;
    public Timer timer;




    public DBConnectionPool(){

        // TODO Auto-generated constructor
    }


    public DBConnectionPool(String name, String driver, String url, String user, String password, int maxConn){

        this.name = name;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
        this.maxConn = maxConn;
    }

    public synchronized void freeConnection(Connection conn){

        this.freeConnections.add(conn);
        this.inUsed--;
    }


    public synchronized Connection getConnection(long timeout){

        Connection conn = null;
        if(this.freeConnections.size() > 0){

            conn = (Connection) this.freeConnections.get(0);
        }else{

            conn = newConnection();
        }

        if(this.maxConn==0 || this.maxConn<this.inUsed){

            conn = null; // 达到最大连接数，暂时不能获得连接
        }

        if(conn!=null){

            this.inUsed++;
        }

        return conn;

    }


    public synchronized Connection getConnection(){

        Connection conn = null;
        if(this.freeConnections.size()>0){

            conn = (Connection)this.freeConnections.get(0);
            this.freeConnections.remove(0);
            if(conn == null)
                conn = getConnection();
        }
        else{

            conn = newConnection();
        }

        if(this.maxConn == 0 || this.maxConn<this.inUsed){

            conn = null;
        }

        if (conn!=null){

            this.inUsed++;
        }

        return conn;
    }

    public synchronized void release(){

        Iterator allConns = this.freeConnections.iterator();
        while(allConns.hasNext()){

            Connection conn=(Connection)allConns.next();

            try{
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        this.freeConnections.clear();
    }


    private Connection newConnection(){

        try{
            Class.forName(driver);
            conn= DriverManager.getConnection(url,user,password);
        }catch (ClassNotFoundException e){

            e.printStackTrace();
            System.out.println("can not find db driver");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("can not create Connection");
        }

        return conn;
    }

    public int getMinConn() {
        return minConn;
    }

    public void setMinConn(int minConn) {
        this.minConn = minConn;
    }

    public int getMaxConn() {
        return maxConn;
    }

    public void setMaxConn(int maxConn) {
        this.maxConn = maxConn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
