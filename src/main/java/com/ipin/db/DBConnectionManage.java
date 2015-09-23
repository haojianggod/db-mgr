package com.ipin.db;

import com.ipin.db.beans.DSConfigBean;
import com.ipin.db.config.ParseDSConfig;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * Created by apple on 15/9/22.
 */
public class DBConnectionManage {

    private static DBConnectionManage instance;
    private static int clients;
    private static Vector drivers = new Vector();
    private Hashtable pools = new Hashtable();

    public DBConnectionManage(){
        this.init();
    }

    static synchronized public DBConnectionManage getInstance(){

        if (instance==null){
            instance = new DBConnectionManage();
        }
        return instance;
    }

    public void freeConnection(String name, Connection conn){

        DBConnectionPool pool = (DBConnectionPool) pools.get(name);
        if(pool!=null){

            pool.freeConnection(conn);
        }
    }

    public Connection getConnection(String name){

        DBConnectionPool pool=null;
        Connection conn = null;

        pool = (DBConnectionPool) pools.get(name);
        conn = pool.getConnection();
        if(conn!=null){
            System.out.println("得到连接");
        }
        return  conn;
    }

    public Connection getConnection(String name, long timeout){

        DBConnectionPool pool = null;
        Connection conn = null;
        pool = (DBConnectionPool) pools.get(name);
        conn = pool.getConnection(timeout);

        return conn;
    }

    public synchronized void release(){

        Enumeration allpools = pools.elements();

        while(allpools.hasMoreElements()){

            DBConnectionPool pool = (DBConnectionPool) allpools.nextElement();
            if(pool!=null)
                pool.release();
        }

        pools.clear();
    }


    private void createPools(DSConfigBean dsb){

        DBConnectionPool dbpool = new DBConnectionPool();
        dbpool.setName(dsb.getName());
        dbpool.setDriver(dsb.getDriver());
        dbpool.setUrl(dsb.getUrl());
        dbpool.setUser(dsb.getUsername());
        dbpool.setPassword(dsb.getPassword());
        dbpool.setMaxConn(dsb.getMaxconn());
        System.out.println("ioio:"+dsb.getMaxconn());
        pools.put(dsb.getName(), dbpool);
    }

    private void init(){

        this.loadDrivers();
        //创建连接池
        Iterator alldriver=drivers.iterator();
        while(alldriver.hasNext())
        {
            this.createPools((DSConfigBean)alldriver.next());
            System.out.println("创建连接池。。。");

        }
        System.out.println("创建连接池完毕。。。");
    }

    private void loadDrivers()
    {
        ParseDSConfig pd=new ParseDSConfig();
//读取数据库配置文件
        drivers=pd.readConfigInfo("ds.config.xml");
        System.out.println("加载驱动程序。。。");
    }


    public static void main(String[] args) {
        // TODO Auto-generated method stub
    }
}
