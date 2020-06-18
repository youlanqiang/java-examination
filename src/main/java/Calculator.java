import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;



/**
 * Java实操题目
 * 基本图形编程：编写一个程序，实现一个简单的计算器界面，为该计算器加上适当的事件处理，完成计算功能。
 * @author youlanqiang
 */
public class Calculator {

    JFrame frame;
    JTextField textField;

    String op;

    boolean cl;

    BigDecimal temp = BigDecimal.ZERO;

    StringBuilder builder = new StringBuilder();

    public static void main(String[] args) {
        new Calculator();
    }



    public Calculator(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);


        textField = new JTextField();
        textField.setText("0");
        textField.setHorizontalAlignment(JTextField.RIGHT);
        textField.setEditable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,4));

        addOperatorAction(panel, "EXIT");
        addOperatorAction(panel, "C");
        addShowHistory(panel);
        addOperatorAction(panel, "<-");

        addNumberAction(panel, "7");
        addNumberAction(panel, "8");
        addNumberAction(panel, "9");
        addCalAction(panel, "x");

        addNumberAction(panel, "4");
        addNumberAction(panel, "5");
        addNumberAction(panel, "6");
        addCalAction(panel, "/");

        addNumberAction(panel, "1");
        addNumberAction(panel, "2");
        addNumberAction(panel, "3");
        addCalAction(panel, "+");


        addEvalAction(panel);
        addNumberAction(panel, "0");
        addNumberAction(panel, ".");
        addCalAction(panel, "-");

        frame.add(panel);

        frame.add(textField, BorderLayout.NORTH);



        frame.setVisible(true);

    }

    public void eval(BigDecimal value){
        builder.append(temp.stripTrailingZeros().toPlainString());
        if("+".equals(op)){
            builder.append("+").append(value.stripTrailingZeros().toPlainString());
            value = temp.add(value);
        }
        if("-".equals(op)){
            builder.append("-").append(value.stripTrailingZeros().toPlainString());
            value = temp.subtract(value);
        }
        if("x".equals(op)){
            builder.append("x").append(value.stripTrailingZeros().toPlainString());
            value = temp.multiply(value);
        }
        if("/".equals(op)){
            builder.append("/").append(value.stripTrailingZeros().toPlainString());
            value = temp.divide(value, 6, RoundingMode.HALF_UP);
        }
        builder.append("=").append(value.stripTrailingZeros().toPlainString()).append("\n");
        //去掉小数点后面多余的0
        textField.setText(value.stripTrailingZeros().toPlainString());
        temp = BigDecimal.ZERO;
        op = null;
    }



    public void addShowHistory(JPanel panel){
        JButton button = new JButton("History");
        button.addActionListener((e)->{
            JDialog jDialog = new JDialog(frame , true);
            jDialog.setLocationRelativeTo(null);
            jDialog.setSize(500, 300);
            JTextArea area = new JTextArea(builder.toString());
            jDialog.add(area);
            jDialog.setVisible(true);
        });
        panel.add(button);
    }

    public void addEvalAction(JPanel panel){
        JButton button = new JButton("=");
        button.addActionListener((e)->{
            BigDecimal v1 =  BigDecimal.valueOf(Double.parseDouble(textField.getText()));
            eval(v1);
        });
        panel.add(button);
    }




    public void addCalAction(JPanel panel, String value){
        JButton button = new JButton(value);
        button.addActionListener((e)->{
            JButton item = (JButton) e.getSource();
            String v = item.getText();
            if(op!=null){
                BigDecimal v1 =  BigDecimal.valueOf(Double.parseDouble(textField.getText()));
                eval(v1);
            }
            op = v;
            temp = BigDecimal.valueOf(Double.parseDouble(textField.getText()));
            cl = true;
        });
        panel.add(button);
    }

    public void addOperatorAction(JPanel panel, String value){
        JButton button = new JButton(value);
        button.addActionListener((e)->{
            JButton item =  (JButton)e.getSource();
            String v = item.getText();
            if("EXIT".equals(v)){
                System.exit(-1);
            }
            if("C".equals(v)){
                textField.setText("0");
                op = null;
                temp = BigDecimal.ZERO;
            }
            if("<-".equals(v)){
                String v2 =  textField.getText();
                if(v2.length() == 1){
                    textField.setText("0");
                }else {
                    v2 = v2.substring(0, v2.length() - 1);
                    textField.setText(v2);
                }
            }
        });
        panel.add(button);
    }

    public void addNumberAction(JPanel panel , String value){
        JButton button = new JButton(value);
        button.addActionListener((e)->{
            JButton item =  (JButton)e.getSource();
            String t = textField.getText();
            if(cl){
                textField.setText(item.getText());
                cl = false;
            } else if( "0".equals(t) && !".".equals(item.getText()) ){
                textField.setText(item.getText());
            }else{
                textField.setText(textField.getText() + item.getText());
            }
        });
        panel.add(button);
    }


}
