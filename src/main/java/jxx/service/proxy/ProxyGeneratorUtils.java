package jxx.service.proxy;

import jxx.service.impl.IndexServiceImpl;
import sun.misc.ProxyGenerator;

@SuppressWarnings("restriction")
public class ProxyGeneratorUtils {
	/** 
     * 把代理类的字节码写到硬盘上 
     * @param path 保存路径 
     */  
    public static void writeProxyClassToHardDisk(String path) {
    	System.out.println(11);
        // 第一种方法，这种方式在刚才分析ProxyGenerator时已经知道了  
         System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
     	System.out.println(22);
          
        // 第二种方法  
          
        // 获取代理类的字节码  
        byte[] classFile = ProxyGenerator.generateProxyClass("$Proxy15", IndexServiceImpl.class.getInterfaces());  
    	System.out.println(33);
    	System.out.println(System.getProperties().get("sun.misc.ProxyGenerator.saveGeneratedFiles"));
          
//        FileOutputStream out = null;  
//          
//        try {  
//            out = new FileOutputStream(path);  
//            out.write(classFile);  
//            out.flush();  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        } finally {  
//            try {  
//                out.close();  
//            } catch (IOException e) {  
//                e.printStackTrace();  
//            }  
//        }  
    } 
    
    public static void main(String[] args) {

		ProxyGeneratorUtils.writeProxyClassToHardDisk("D:/$Proxy15.class");
	}
}
