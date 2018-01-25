import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Tools {

    List<File> fileList;
    List<classCheck> filecheck;




    public Tools(){
        JFileChooser jf = new JFileChooser();
        //   jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
        jf.showDialog(null,null);
        File fi = jf.getSelectedFile();

        File distDir= new File(System.getProperty("java.io.tmpdir")+File.separator+"tempzipfile");
        System.out.println(distDir.getAbsolutePath());
        if(!distDir.exists())distDir.mkdirs();
        File dist = new File(distDir.getAbsolutePath()+File.separator+fi.getName());
        if(dist.exists())dist.delete();

        System.out.println("save: "+dist.getAbsolutePath());

               try{

                   copyFileUsingFileStreams(fi, dist);

                    if(dist.exists()){

                        if(unzip(dist)){

                            this.fileList= showFiles(new File(dist.getAbsoluteFile()+"_unzip"));


                            this.filecheck= new ArrayList<classCheck>();

                            for(File temp:this.fileList){
                                classCheck t= new classCheck();
                                t.setFilename(temp.getName());
                                int ver=classCheckTools.getCompileVersion(temp);
                                String v="UNKOWN";
                                if(ver==46){
                                    v="JDK_1_2";
                                }else if(ver==47){
                                    v="JDK_1_3";

                                }else if(ver==48){
                                    v="JDK_1_4";

                                }else if(ver==49){
                                    v="JDK_5";

                                }else if(ver==50){
                                    v="JDK_6";

                                }else if(ver==51){
                                    v="JDK_7";

                                }else if(ver==52){
                                    v="JDK_8";

                                }

                                t.setFileClass(v);
                                this.filecheck.add(t);

                            }

                            System.out.println(this.fileList.size()+"ddddddd");

                            System.out.println("unzip successful");
                        }

                    }

                   System.out.println("end");

                }
                catch(Exception es){
                    es.printStackTrace();
                }




    }

    public static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    public static boolean unzip(File srcZipFile) {



        File unzipDir=new File(srcZipFile.getAbsoluteFile()+"_unzip");
        System.out.println(unzipDir.getAbsolutePath());
        if(unzipDir.exists()){
            unzipDir.delete();
        }else{
            unzipDir.mkdirs();
        }

        boolean isSuccessful = true;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcZipFile));
            ZipInputStream zis = new ZipInputStream(bis);

            BufferedOutputStream bos = null;

            //byte[] b = new byte[1024];
            ZipEntry entry = null;
            while ((entry=zis.getNextEntry()) != null) {

                String entryName = entry.getName();
                System.out.println(entry.getName());
                File temp = new File(unzipDir.getAbsoluteFile() + File.separator + entryName);

                if(entry.isDirectory()){


                }else {

                    if (temp.exists()) {
                        temp.delete();
                    } else {
                        temp.getParentFile().mkdirs();
                    }

                    bos = new BufferedOutputStream(new FileOutputStream(temp));
                    int b = 0;
                    while ((b = zis.read()) != -1) {
                        bos.write(b);
                    }
                    bos.flush();
                    bos.close();

                }
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            isSuccessful = false;
        }
        return isSuccessful;
    }

    public List<File> showFiles(File f){

        List<File> result=new ArrayList<File>();
        File[] flist=f.listFiles();
        for(File temp:flist){
            if(temp.isDirectory()){
                result.addAll(showFiles(temp.getAbsoluteFile()));
            }else{
               if(temp.getName().matches(".*\\.((jar)|(class))"))
                result.add(temp);
            }
        }

        return result;


    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public List<classCheck> getFilecheck() {
        return filecheck;
    }

    public void setFilecheck(List<classCheck> filecheck) {
        this.filecheck = filecheck;
    }
}
