import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * author youlanqiang
 */
public class MyClientSocketGUI extends JFrame {

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
    private JTextArea text;

    //发送按钮
    private JButton send;

    private MyClientSocket clientSocket;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MyClientSocketGUI frame = new MyClientSocketGUI();
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
    public MyClientSocketGUI() {

        setTitle("客户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 440, 300);
        setLocationRelativeTo(null);
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panel);

        host = new JTextField();
        host.setColumns(10);
        host.setText("localhost");

        port = new JTextField();
        port.setColumns(10);
        port.setText("855");

        JLabel hostLabel = new JLabel("Host:");

        JLabel portLabel = new JLabel("端口号:");

        connect = new JButton("连接");
        connect.addActionListener((e)->{
            if(connect.getText().equals("连接")){
                try {
                    clientSocket = new MyClientSocket();
                    clientSocket.initSocket(host.getText(), Integer.parseInt(port.getText()));
                    clientSocket.send("连接成功");
                    text.append("连接成功\n");
                    message.setEditable(true);
                    send.setEnabled(true);
                    host.setEditable(false);
                    port.setEditable(false);
                    connect.setText("断开");
                } catch (Exception ex) {
                    text.append("连接失败\n");
                }
            }else{
                clientSocket.close();
                text.append("关闭成功\n");
                message.setEditable(false);
                send.setEnabled(false);
                host.setEditable(true);
                port.setEditable(true);
                connect.setText("连接");
            }
        });

        text = new JTextArea();
        JScrollPane textPane = new JScrollPane(text);

        message = new JTextField();
        message.setColumns(10);
        message.setEditable(false);

        send = new JButton("发送");
        send.setEnabled(false);
        send.addActionListener((x)->{
            String messageValue = message.getText();
            text.append("本机:"+messageValue+"\n");
            clientSocket.send(messageValue);
            message.setText("");
        });


        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(
                gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(gl_panel.createSequentialGroup()
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(gl_panel.createSequentialGroup()
                                                        .addContainerGap()
                                                        .addComponent(textPane))
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
                                .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(send))
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        panel.setLayout(gl_panel);
    }


    public class MyClientSocket extends Thread{

        private Socket socket;
        private OutputStream outputStream;

        public void initSocket(String socketHost, int socketPort) throws IOException {
            socket = new Socket(socketHost, socketPort);
            outputStream = socket.getOutputStream();

            Thread receivingThread = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        String line;
                        while ((line = reader.readLine()) != null){
                            if(line.equals("服务器关闭")){
                                clientSocket.close();
                                text.append("已断开连接，服务器关闭\n");
                                message.setEditable(false);
                                send.setEnabled(false);
                                host.setEditable(true);
                                port.setEditable(true);
                                connect.setText("连接");
                                return;
                            }else {
                                text.append(line + CRLF);
                            }

                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            receivingThread.start();
        }

        private static final String CRLF = "\r\n"; // newline

        /** Send a line of text */
        public void send(String message) {
            try {
                outputStream.write((message + CRLF).getBytes());
                outputStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        /** Close the socket */
        public void close() {
            try {
                send("退出连接");
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



}
