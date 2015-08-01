import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Created by Stefan_ on 26.07.2015.
 */
public class Window extends JFrame implements ActionListener {
    String name = "";
    Socket s;



    Scanner sc = new Scanner(System.in);
    String msg;

    public static JTextArea chat;
    public  JPanel rechts = new JPanel();
    public static DefaultListModel listenModel = new DefaultListModel();
    public static JList<String> jList_namen = new JList<String>(listenModel);

    JScrollPane jschat;
    JTextField eingabe = new JTextField();
    JButton senden;

    FlowLayout fl = new FlowLayout(2);
    Container c = getContentPane();
    JPanel panel_unten = new JPanel(fl);



    public Window() throws IOException {
        super("Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(400, 200);
        setSize(400, 400);
        setLayout(new BorderLayout(2, 4));

        chat = new JTextArea();
        jschat  = new JScrollPane(chat);
        jschat.setPreferredSize(new Dimension(200,40));
        senden = new JButton("senden");

        senden.setEnabled(true);
        senden.addActionListener(this);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().setView(jList_namen);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jList_namen.setPreferredSize(new Dimension(80, HEIGHT));


        jList_namen.setCellRenderer(new Chat_Mates());
        chat.setEditable(false);
        panel_unten.add(eingabe);
        eingabe.setPreferredSize(new Dimension(240, 24));
        panel_unten.add(senden);
        c.add(jschat, BorderLayout.CENTER);
        c.add(panel_unten, BorderLayout.SOUTH);
        c.add(scrollPane, BorderLayout.EAST);


        setMinimumSize(getSize());
        setVisible(true);

        ipDialog();

        // JDialog mit anschlie�ender Anmeldung
        nameDialog();



        LeseThread_Client lt = new LeseThread_Client(s);
        lt.run();


    }

    private void nameDialog() throws IOException {
        name = JOptionPane.showInputDialog( "Name: ");
        System.out.println(name);
        if(name.isEmpty())
        {
            nameDialog();
        }
        else {
            msg = "0:SERVER:"+name+": Hallo\n";
            s.getOutputStream().write(msg.getBytes());
        }
        setTitle("Chat [" + name + "]");
    }

    private void ipDialog() {
        String ip = JOptionPane.showInputDialog("IP des Hosts eingeben: ", get_ip());
        try {
            s = new Socket(ip, 44444);
        } catch (IOException e) {
            ipDialog();
            System.out.println("Konnte nicht verbinden");
        }
    }

    private String get_ip()
    {
        String tmp = "";

        try {
            tmp = "" +InetAddress.getLocalHost();
          String ip[] = tmp.split("/");
            return ip[1];
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        return tmp;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if(e.getSource() == senden)
        {

            msg = "2::" + name + ":" + eingabe.getText() + "\n";
            System.out.println(msg);
            try {
                s.getOutputStream().write(msg.getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            eingabe.setText("");

        }

    }
}