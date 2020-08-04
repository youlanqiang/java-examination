import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.Socket;

/**
 * author youlanqiang
 */
public class MySocketClient extends JFrame {

    private JPanel panel;

    //Host地址
    private JTextField host;

    //端口号
    private JTextField port;

    //消息输入栏
    private JTextField message;

    //连接按钮
    private JButton connect;

    //消息框
    private JTextPane text;

    //发送按钮
    private JButton send;

    private Socket socket;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MySocketClient frame = new MySocketClient();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MySocketClient() {
        socket = new Socket();

        setTitle("客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 440, 300);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);

        host = new JTextField();
        host.setColumns(10);

        port = new JTextField();
        port.setColumns(10);

        JLabel hostLabel = new JLabel("Host:");

        JLabel portLabel = new JLabel("端口号:");

        connect = new JButton("连接");

        text = new JTextPane();

        message = new JTextField();
        message.setColumns(10);

        send = new JButton("发送");
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(text))
                                                .addGroup(GroupLayout.Alignment.LEADING, gl_panel.createSequentialGroup()
                                                        .addComponent(hostLabel)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(host, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(portLabel)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(port, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(connect)))
                                        .addGroup(GroupLayout.Alignment.TRAILING, gl_panel.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(message, GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                                                .addGap(18)
                                                .addComponent(send)))
                                .addContainerGap())
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGap(19)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(hostLabel)
                                        .addComponent(host, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portLabel)
                                        .addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(connect))
                                .addGap(18)
                                .addComponent(text, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(send))
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
    }
}
