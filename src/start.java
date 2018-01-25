import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class start {
    private JButton button1;
    private JTextField textField1;
    private JTable table1;
    private JScrollPane FileResult;
    private JPanel start;

    private Object[][] webAppsStr;
    private TableModel tm;
    private String[] Names={"类名","版本"};

    public start() {



        tm = new DefaultTableModel(webAppsStr,Names);
        table1.setModel(tm);



        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<classCheck> r=   new Tools().getFilecheck();
                Map<String,List<String>> textshow= new HashMap<String, List<String>>();

                for(classCheck c:r){

                    List<String> tc= textshow.get(c.getFileClass());
                    if(tc!=null){
                        tc.add(c.getFilename());
                    }else{
                        List<String> newtc= new ArrayList<String>();
                        newtc.add(c.getFilename());
                        textshow.put(c.getFileClass(),newtc);
                    }


                }

                StringBuffer sb= new StringBuffer();
                for(String sbtemp:textshow.keySet()){
                    sb.append(sbtemp);
                    sb.append(":");
                    sb.append(textshow.get(sbtemp).size());
                    sb.append("|||");
                }
                System.out.println(sb.toString());

                textField1.setText(sb.toString());

                webAppsStr = new String[r.size()][2];
                //遍历List
                for (int i = 0; i < r.size(); i++) {
                    webAppsStr[i][0] = r.get(i).getFilename();
                    webAppsStr[i][1] = r.get(i).getFileClass();

                }

                tm = new DefaultTableModel(webAppsStr,Names);
                table1.setModel(tm);



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
}
