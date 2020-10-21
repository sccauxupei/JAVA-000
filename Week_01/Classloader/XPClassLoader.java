//package *;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import com.jayway.jsonpath.internal.function.text.Length;

import junit.framework.Assert;
import sun.misc.VM;
import sun.reflect.Reflection;
		
public class XPClassLoader extends ClassLoader {
	boolean needRefactory = false;
    private static native void registerNatives();
    
//    static {
//    	System.loadLibrary("registerNatives()");
//    }	
	
	public static void main(String[] args) throws Exception {
		Class clz = new XPClassLoader()
					.findClass("com.ruoyi.system.Hello");
		Object o = clz.newInstance();
		Method m = clz.getMethod("hello", null);
		m.invoke(o);
	}

	/**
	*
	*不太了解本地方法的调用，这个方法没实验出让它跑起来的方法，很想知道该咋做。
	**/
	@SuppressWarnings("restriction")
	@Override
	protected Class<?> findClass(String qualifiedName) throws ClassNotFoundException {
		String resolvePath = resolvePath(qualifiedName);
		
		if(!needRefactory) {
//			参考ClassLoader#findSystemClass(String name)写法
			if(null == resolvePath && System.getProperty("XPCld").equals("cld")) {			
				ClassLoader classLoader = getParent();
		        if (classLoader == null) {
		            if (!checkName(resolvePath))
		                throw new ClassNotFoundException(resolvePath);
		            Class<?> cls = findBootstrapClass(resolvePath);
		            if (cls == null) {
		                throw new ClassNotFoundException(resolvePath);
		            }
		            return cls;
		        }
		        return classLoader.loadClass(qualifiedName);
			}
			
			//参考Class文件写法
			if(null == resolvePath && System.getProperty("XPCld").equals("cls")) {
		        Class<?> caller = Reflection.getCallerClass();
		        return forName0(resolvePath, true, this, caller);
			}
		}		
		
		
		/**
		*真正work的代码
		*
		**/
		InputStream inputStream;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] code = null;
		
		try {
			String s1 = resolvePath + ".xlass";
			
			inputStream = new FileInputStream(new File(s1));
			
			code = deSerilizeXlass(inputStream,bos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		defineClass("**packageName**.Hello",code, 0, code.length) ,有包头要带上包的全限定名，否则加载报NoClassDefFoundError
		return defineClass("Hello",code, 0, code.length);
	}	
	
	private byte[] deSerilizeXlass(final InputStream ips,final ByteArrayOutputStream bos) throws IOException {
		byte[] bytes = new byte[1024];
		byte[] result = null;
		int length;
		while((length = ips.read(bytes)) != -1) {
			for(int i = 0 ; i < length ; i++) {
				bytes[i] = (byte) (255 - bytes[i]);
			}
			bos.write(bytes,0,length);
		}
		result = bos.toByteArray();
		return result;
	}


	/**
	 * 解析传入路径
	 * @param name
	 * @return fullPath
	 * @throws ClassNotFoundException 
	 */
	protected String resolvePath(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		if(null == name)
			throw new ClassNotFoundException();
		
		String fullPath = null;		
		fullPath = /**/getClassPath() + name.replace(".", "/");
		if(name.contains("Hello")) {
			needRefactory = true;
		}
		return fullPath;
	}
	
	/**
	 * 返回当前类路径
	 * @return ClassPath
	 */
    private String getClassPath() {
		return XPClassLoader.class.getResource("/").toString()
				.split("file:")[1];
	}


	// true if the name is null or has the potential to be a valid binary name
    private boolean checkName(String name) {
        if ((name == null) || (name.length() == 0))
            return true;
        if ((name.indexOf('/') != -1)
            || (!VM.allowArraySyntax() && (name.charAt(0) == '[')))
            return false;
        return true;
    }
    
    
    /** Called after security check for system loader access checks have been made. */
    private static native Class<?> forName0(String name, boolean initialize,
                                            ClassLoader loader,
                                            Class<?> caller)
        throws ClassNotFoundException;
    
    // return null if not found
    private native Class<?> findBootstrapClass(String name);
    

}
