package com.generate.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * User: dorid
 * Date: 12-11-2
 * Time: 11:04
 */
public class Config {

    private Map<String, String> prop = null;

    private static Config instance = null;
    protected Log logger = LogFactory.getLog(getClass());

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
            instance.init();
        }
        return instance;
    }

    public static Config getInstance(String file) {
        if (instance == null) {
            instance = new Config();
            instance.init(file);
        }
        return instance;
    }

    public String get(String key) {
        if (instance.getValue(key) == null) {
            logger.warn("The [" + key  +"] value is null!!!");
        }
        return instance.getValue(key);
    }

    public void reload() {
    	clean();
    }

    private String getValue(String key){
        if (prop == null) {
            init();
        }

        return prop.get(key);
    }
    
    public void setValue(String key, String value){
        if (prop == null) {
            init();
        }
        prop.put(key, value);
        logger.warn("Put key:"+key+" value:"+value);
    }
    
    public void save(String file) throws IOException{
    	 if (prop == null || prop.isEmpty()) {
    		 logger.warn("Because prop is empty, didn't save to file.");
    		 throw new IOException("Because prop is empty, didn't save to file.");
         }
    	 
    	 OutputStream out = null;    	 
    	 try {    	 
	    	 out = getOutputStream(file);
	    	 if(null == out){
	    		 logger.warn("Couldn't get the OutputStream.");
	    		 throw new IOException("Couldn't get the OutputStream.");
	    	 }
	    	 Properties p = new Properties();
	         p.putAll(prop);
	         p.store(out, "Last modify: " + new Date());         
    	 } catch (IOException e1) {
             e1.printStackTrace();
             throw e1;
         }finally {
             try {
            	 out.close();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
    }

    public Map<String, String> getPropertyMap() {
    	return prop;
    }

    private void init(String file) {

    	InputStream inputStream = getInputStream(file);
    	
        Properties p = new Properties();
        try {
            p.load(inputStream);
            Set<Object> set = p.keySet();
            prop = new HashMap<String, String>();
            Iterator<Object> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = (String) p.get(key);

                prop.put(key, value);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
        
    private InputStream getInputStream(String file) {
    	InputStream inputStream = null;

    	String path = System.getProperty("app_conf_path");
    	if(null != path){
    		file = path + "/cloudSolv/"+ file;
    		try {
				inputStream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(),e);
			}
    	}else{
    		inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
    	}
		return inputStream;
	}
    
    private OutputStream getOutputStream(String file) {
    	OutputStream outputStream = null;

    	String path = System.getProperty("app_conf_path");
    	if(null != path){
    		file = path + "/cloudSolv/"+ file;
    		try {
    			outputStream = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e.getMessage(),e);
			}
    	}
		return outputStream;
	}

	public static void clean(){
    	instance = null;
    }

    private void init(){
        init("generate.properties");
    }

    public static void main(String[] args) {
        System.out.println(Config.getInstance().get("user_name"));
    }
}
