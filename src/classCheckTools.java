import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class classCheckTools {

    /**
     * Java �汾����
     *
     * @author wuzhengwei
     *
     */


        private static final int JAVA_CLASS_MAGIC = 0xCAFEBABE;

        public final static int JDK_1_2 = 46;
        public final static int JDK_1_3 = 47;
        public final static int JDK_1_4 = 48;
        public final static int JDK_5 = 49;
        public final static int JDK_6 = 50;
        public final static int JDK_7 = 51;
        public final static int JDK_8 = 52;

        /**
         * ��õ�ǰ����JDK�汾
         *
         * @return
         */
        public static int getJDKVersion()
        {
            String version = System.getProperty("java.version");
            if (version != null && version.matches("1\\.\\d.*"))
            {
                int v = Integer.parseInt(version.charAt(2) + "");
                if (v >= 2)
                {
                    return 44 + v;
                }
            }
            return -1;
        }

        /**
         * ���class��jar����汾
         *
         * @param file
         * @return
         * @throws Exception
         */
        public static int getCompileVersion(File file) throws Exception
        {
            if (file == null || !file.isFile() || !file.getName().matches(".*\\.((jar)|(class))"))
            {
              return 0;
            }
            int version = -1;
            if (file.getName().endsWith("jar"))
            {
                JarFile jarFile = new JarFile(file);
                Enumeration<JarEntry> enumeration = jarFile.entries();
                while (enumeration.hasMoreElements())
                {
                    JarEntry entry = enumeration.nextElement();
                    if (entry.getName().endsWith(".class"))
                    {
                        InputStream in = jarFile.getInputStream(entry);
                        version = getVersion(in);
                        in.close();
                        break;
                    }
                }
                jarFile.close();
            }
            else
            {
                InputStream in = new FileInputStream(file);
                version = getVersion(in);
                in.close();
            }
            return version;
        }


        private static int getVersion(InputStream in) throws Exception
        {
            DataInputStream dis = new DataInputStream(in);
            // ��ǰ��8���ֽ�CA FE BA BE �ǹ̶��ģ�֮��4���ֽ��Ǵΰ汾�ţ��ΰ汾�ź����4���ֽ���jdk�İ汾��
            int magic = dis.readInt();
            if (magic == JAVA_CLASS_MAGIC)
            {
                // int minorVersion =
                dis.readUnsignedShort();
                int majorVersion = dis.readUnsignedShort();
                // Java 1.2 >> 46
                // Java 1.3 >> 47
                // Java 1.4 >> 48
                // Java 5 >> 49
                // Java 6 >> 50
                // Java 7 >> 51
                // Java 8 >> 52
                return majorVersion;
            }
            return -1;
        }

}
