import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File;
import java.io.FileInputStream;

/**
 * 自定义类加载器
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) {
        try{
            Object hello = new HelloClassLoader().findClass("Hello").newInstance();
            Method method = hello.getClass().getMethod("hello");
            method.invoke(hello);
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (InstantiationException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File f = new File(this.getClass().getResource("./Hello.xlass").getPath());
        int length = (int)f.length();
        byte[] bytes = new byte[length];

        try {
            new FileInputStream(f).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return super.findClass(name);
        }

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte)(255 - bytes[i]);
        }

        return defineClass(name, bytes, 0, bytes.length);
    }
}