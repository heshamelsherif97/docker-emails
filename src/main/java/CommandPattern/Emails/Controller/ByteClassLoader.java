package CommandPattern.Emails.Controller;

public class ByteClassLoader  extends ClassLoader{


    public ByteClassLoader(){super();}

    public Class<?> createClass(byte [] bytes, String name){
        System.out.print(bytes.toString());
        return defineClass(name, bytes, 0, bytes.length);
    }

}
