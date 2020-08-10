import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.border.EmptyBorder;

/**
 * author youlanqiang
 */
public class MyServerSocketGUI extends JFrame {


    private JPanel panel;

    //端口号
    private JTextField port;

    //消息栏
    private JTextField message;

    //启动按钮
    private  JButton start;

    //消息框
    private JTextArea text;

    //发送
    private JButton send;

    private  MyServerSocket serverSocket;



    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MyServerSocketGUI frame = new MyServerSocketGUI();
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
    public MyServerSocketGUI()  {

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
        port.setText("855");

        start = new JButton("启动");
        start.addActionListener((e)->{
            JButton button = (JButton) e.getSource();
            if(button.getText().equals("启动")){
                try {
                    int portValue = Integer.parseInt(port.getText());
                    serverSocket = new MyServerSocket(portValue);
                    serverSocket.start();
                    port.setEditable(false);
                    message.setEditable(true);
                    send.setEnabled(true);
                    text.append("服务器已启动\n");
                    start.setText("关闭");
                } catch (Exception ex) {
                    text.append("服务器启动错误\n");
                    text.append(ex.toString());
                }
            }else {
                serverSocket.close();
                port.setEditable(true);
                message.setEditable(false);
                send.setEnabled(false);
                text.append("服务器已关闭\n");
                start.setText("启动");
            }
        });

         text = new JTextArea();

        message = new JTextField();
        message.setColumns(10);
        message.setEditable(false);

        send = new JButton("发送");
        send.setEnabled(false);
        send.addActionListener((e)->{
            String messageText = message.getText();
            text.append("本机:"+messageText+"\n");
            serverSocket.send("服务器:"+messageText);
            message.setText("");
        });
        JScrollPane textPane = new JScrollPane(text);


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
                                                        .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 328, GroupLayout.PREFERRED_SIZE)
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
                                .addComponent(textPane, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                .addGroup(gl_panel.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(message, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(send))
                                .addContainerGap())
        );

        panel.setLayout(gl_panel);
    }

    class MyServerSocket extends Thread{

        private ServerSocket serverSocket;


        private List<Socket> socketList;

        private static final String CRLF = "\r\n"; // newline

        public MyServerSocket(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
            this.socketList = new CopyOnWriteArrayList<>();
        }

        public void send(String message){
            for (Socket socket : socketList) {
                try{
                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    writer.write(message+CRLF);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void close(){
            try {
                send("服务器关闭");
                for (Socket socket : socketList) {
                    socket.close();
                }
                socketList.clear();
                serverSocket.close();
                this.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            while(!serverSocket.isClosed()){
                try {
                    Socket socket = serverSocket.accept();
                    if(!socket.isClosed()){
                        socketList.add(socket);
                        Thread receiveThread = new Thread(()->{
                            InputStream inputStream;
                            InputStreamReader inputStreamReader;
                            BufferedReader bufferedReader;
                            try{
                                inputStream = socket.getInputStream();
                                inputStreamReader = new InputStreamReader(inputStream);
                                bufferedReader = new BufferedReader(inputStreamReader);
                                String result = "";
                                while((result = bufferedReader.readLine()) != null){
                                    text.append("客户["+socket.getInetAddress().getHostAddress()+"]:"+result+"\n");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                        receiveThread.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }



}
