import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import javax.swing.border.EmptyBorder;


public class MySocketServer extends JFrame {


    private JPanel panel;

    //端口号
    private JTextField port;

    //消息栏
    private JTextField message;

    //启动按钮
    private  JButton start;

    //消息框
    private JTextPane text;

    //发送
    private JButton send;

    private  ServerSocket serverSocket;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MySocketServer frame = new MySocketServer();
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
    public MySocketServer() throws IOException {
        serverSocket = new ServerSocket();
        serverSocket.bind(InetSocketAddress.createUnresolved(InetAddress.getLocalHost().getHostAddress(),33));

        setTitle("服务器");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 392, 300);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);

        JLabel portLabel = new JLabel("端口号:");

        port = new JTextField();
        port.setColumns(10);

        start = new JButton("启动");

         text = new JTextPane();

        message = new JTextField();
        message.setColumns(10);

        send = new JButton("发送");
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGap(19)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_panel.createSequentialGroup()
                                                .addComponent(message, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(send)
                                                .addGap(1))
                                        .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addComponent(text, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
                                                        .addContainerGap())
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addComponent(portLabel)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(port, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                                        .addComponent(start)
                                                        .addGap(31)))))
        );
        gl_panel.setVerticalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(portLabel)
                                        .addComponent(port, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(start))
                                .addGap(20)
                                .addComponent(text, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(send))
                                .addContainerGap())
        );
        panel.setLayout(gl_panel);
    }
}
