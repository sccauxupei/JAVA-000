package io.kimmking.rpcfx.demo.provider.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ListModel;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午8:27:03
 * @project_name 项目名 rpcfx-demo-provider
 * @type_name 类名 FindAllSubClassUtil
 * @function 功能 TODO
 */
public class FindAllSubClassUtil<T> {
	
	private static Map<String, List<Class<?>>> mapCache = new ConcurrentHashMap<String, List<Class<?>>>();
    /** 
     * 获取同一路径下所有子类或接口实现类 
     * 由于基类与实现类不在同一路径下，找实现类需要从当前类的路径下找，所以传了一个now为this的Class<?>。 
     * @param intf 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */  
    public static List<Class<?>> getAllAssignedClass(Class<?> clsInf,Class<?> now) throws IOException,  
            ClassNotFoundException, InstantiationException, IllegalAccessException {  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        for (Class<?> c : getClasses(clsInf,now)) {  
            if (clsInf.isAssignableFrom(c) && !clsInf.equals(c)) {  
                classes.add(c);  
            }  
        }
        return classes;  
    } 
    
    /** 
     * 取得当前类路径下的所有类 
     *  
     * @param cls 
     * @return 
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */  
    public static List<Class<?>> getClasses(Class<?> cls,Class<?> now) throws IOException,  
            ClassNotFoundException, InstantiationException, IllegalAccessException {  
        if(mapCache.containsKey(cls.getName())) {
        	if(mapCache.get(cls.getName()).size() != 0) {
        		return mapCache.get(cls.getName());
        	}
        }
    	String pk = now.getPackage().getName();  
        String path = pk.replace('.', '/');  
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();  
        URL url = classloader.getResource(path);
        List<Class<?>> resultSet = getClasses(new File(url.getFile()), pk,cls);
        mapCache.put(cls.getName(), resultSet);
        return resultSet;  
    }  
  
    /** 
     * 迭代查找类 
     *  
     * @param dir 
     * @param pk 
     * @return 
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */  
    private static List<Class<?>> getClasses(File dir, String pk,Class<?> targetInterface) throws ClassNotFoundException, InstantiationException, IllegalAccessException {  
        List<Class<?>> classes = new ArrayList<Class<?>>();  
        if (!dir.exists()) {  
            return classes;  
        }  
        for (File f : dir.listFiles()) {  
            if (f.isDirectory()) {  
                classes.addAll(getClasses(f, pk + "." + f.getName(),targetInterface));  
            }  
            String name = f.getName();  
            if (name.endsWith(".class")) {
            	Class<?> candidate = Class.forName(pk + "." + name.substring(0, name.length() - 6));
            	if(targetInterface.isAssignableFrom(candidate)) {
            		classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));              		
            	}
            }  
        }  
        return classes;  
    }  
  
}  
