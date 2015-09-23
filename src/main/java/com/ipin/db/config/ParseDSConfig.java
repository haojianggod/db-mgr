package com.ipin.db.config;

import com.ipin.db.beans.DSConfigBean;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by apple on 15/9/22.
 */
public class ParseDSConfig {

    public ParseDSConfig(){

    }


    public Vector readConfigInfo(String path){

//        String rpath=ParseDSConfig.class.getResource("../../../../").getPath().substring(1) + path;
        Vector dsConfig=null;
        FileInputStream fi = null;

        try{

            fi = new FileInputStream("src/main/resources/ds.config.xml");
            dsConfig=new Vector();
            SAXBuilder sb=new SAXBuilder();
            Document doc=sb.build(fi);
            Element root=doc.getRootElement();
            List pools=root.getChildren();
            Element pool=null;
            Iterator allPool=pools.iterator();
            while(allPool.hasNext())
            {
                pool=(Element)allPool.next();
                DSConfigBean dscBean=new DSConfigBean();
                dscBean.setType(pool.getChild("type").getText());
                dscBean.setName(pool.getChild("name").getText());
                System.out.println(dscBean.getName());
                dscBean.setDriver(pool.getChild("driver").getText());
                dscBean.setUrl(pool.getChild("url").getText());
                dscBean.setUsername(pool.getChild("username").getText());
                dscBean.setPassword(pool.getChild("password").getText());
                dscBean.setMaxconn(Integer.parseInt(pool.getChild("maxconn").getText()));
                dsConfig.add(dscBean);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (JDOMException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {

            try {
                fi.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return dsConfig;

    }


    public static void main(String[] args){

        ParseDSConfig config = new ParseDSConfig();
        Vector rs = config.readConfigInfo("ds.config.xml");
        System.out.println("finish");
    }
}
