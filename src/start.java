import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class start {
    private JButton button1;
    private JTextField textField1;
    private JTable table1;
    private JScrollPane FileResult;
    private JPanel start;

    private Object[][] webAppsStr;
    private TableModel tm;
    private String[] Names = {"类名", "版本"};

    private List<File> fileList;
    private List<classCheck> filecheck;

    private String systempath = System.getProperty("java.io.tmpdir");

    private String showtxt;

    public start() {


        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                webAppsStr = null;
                tm = new DefaultTableModel(webAppsStr, Names);
                table1.setModel(tm);

                if (systempath == null || "".equals(systempath)) {
                    showtxt = "system temp path = null";
                    textField1.setText(showtxt);

                    textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());
                    return;
                }

                //    showtxt="start..";
                //   refreshText();
                JFileChooser jf = new JFileChooser();
                //   jf.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
                jf.showDialog(null, null);
                File fi = jf.getSelectedFile();

                File distDir = new File(systempath + File.separator + "tempzipfile");

                if (!distDir.exists()) distDir.mkdirs();
                File dist = new File(distDir.getAbsolutePath() + File.separator + fi.getName());
                if (dist.exists()) dist.delete();


                try {


                    showtxt = "get file:" + fi.getName();
                    textField1.setText(showtxt);

                    textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());
                    copyFileUsingFileStreams(fi, dist);


                    if (dist.exists()) {


                        File unzipDir = new File(dist.getAbsoluteFile() + "_unzip");

                        if (unzipDir.exists()) {
                            unzipDir.delete();
                        } else {
                            unzipDir.mkdirs();
                        }


                        try {
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dist));
                            ZipInputStream zis = new ZipInputStream(bis);

                            BufferedOutputStream bos = null;

                            //byte[] b = new byte[1024];
                            ZipEntry entry = null;
                            while ((entry = zis.getNextEntry()) != null) {

                                String entryName = entry.getName();
                                //  textField1.setText();
                                showtxt = "unzip file:" + entryName;
                                textField1.setText(showtxt);

                                textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());


                                System.out.println(entry.getName());
                                File temp = new File(unzipDir.getAbsoluteFile() + File.separator + entryName);

                                if (entry.isDirectory()||(!entryName.matches(".*\\.((jar)|(class))"))) {


                                } else {

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
                        } catch (IOException ee) {
                            ee.printStackTrace();

                        }

                        //    if(unzip(dist)){

                        fileList = showFiles(new File(dist.getAbsoluteFile() + "_unzip"));


                        filecheck = new ArrayList<classCheck>();

                        for (File temp : fileList) {
                            classCheck t = new classCheck();
                            t.setFilename(temp.getName());
                            int ver = classCheckTools.getCompileVersion(temp);


                            String v = "UNKOWN";
                            if (ver == 45) {
                                v = "JDK_1.1";
                            } else if (ver == 46) {
                                v = "JDK_1.2";
                            } else if (ver == 47) {
                                v = "JDK_1.3";

                            } else if (ver == 48) {
                                v = "JDK_1.4";

                            } else if (ver == 49) {
                                v = "JDK_5";

                            } else if (ver == 50) {
                                v = "JDK_6";

                            } else if (ver == 51) {
                                v = "JDK_7";

                            } else if (ver == 52) {
                                v = "JDK_8";

                            }
                            //    textField1.setText();
                            showtxt = "check file:" + temp.getName() + ":version=" + v;
                            textField1.setText(showtxt);

                            textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());
                            t.setFileClass(v);
                            t.setCode(ver);
                            filecheck.add(t);

                        }


                        Map<String, List<String>> textshow = new HashMap<String, List<String>>();
                        Collections.sort(filecheck);
                        for (classCheck c : filecheck) {

                            List<String> tc = textshow.get(c.getFileClass());
                            if (tc != null) {
                                tc.add(c.getFilename());
                            } else {
                                List<String> newtc = new ArrayList<String>();
                                newtc.add(c.getFilename());
                                textshow.put(c.getFileClass(), newtc);
                            }


                        }

                        StringBuffer sb = new StringBuffer();
                        for (String sbtemp : textshow.keySet()) {
                            sb.append(sbtemp);
                            sb.append("=");
                            sb.append(textshow.get(sbtemp).size());
                            sb.append("  ");
                        }
                        System.out.println(sb.toString());

                        //textField1.setText(sb.toString());
                        showtxt = sb.toString();
                        if("".equals(sb.toString()))showtxt="No java class!";
                        textField1.setText(showtxt);

                        textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());

                        webAppsStr = new String[filecheck.size()][2];
                        //遍历List
                        for (int i = 0; i < filecheck.size(); i++) {
                            webAppsStr[i][0] = filecheck.get(i).getFilename();
                            webAppsStr[i][1] = filecheck.get(i).getFileClass();

                        }

                        tm = new DefaultTableModel(webAppsStr, Names);
                        table1.setModel(tm);

                    }

                } catch (Exception es) {
                    es.printStackTrace();
                    textField1.setText("No java class!");

                    textField1.paintImmediately(0, 0, textField1.getWidth(), textField1.getHeight());
                }


            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Java-version by wzw");
        frame.setContentPane(new start().start);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.pack();
        frame.setVisible(true);
    }


    public void copyFileUsingFileStreams(File source, File dest)
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


    public List<File> showFiles(File f) {

        List<File> result = new ArrayList<File>();
        File[] flist = f.listFiles();
        for (File temp : flist) {
            if (temp.isDirectory()) {
                result.addAll(showFiles(temp.getAbsoluteFile()));
            } else {
                if (temp.getName().matches(".*\\.((jar)|(class))"))
                    result.add(temp);
            }
        }

        return result;


    }


}
