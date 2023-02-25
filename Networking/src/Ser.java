import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.nio.file.FileSystems;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Ser extends JPanel {
    String content[];
    public static JButton buttonCopy;
    public static DefaultListModel model;
    public static JList jList;
    public static DefaultListModel modelChange;
    public static JList jListChange;

    public static ArrayList<Socket> pack = new ArrayList<Socket>();
    public static File mSelectedFolder;
    public static WatchService mWatchService;
    public static Socket selectSocket;
    
    public Ser() {
        JFrame jFrame = new JFrame("");
        jFrame.setTitle("Multithreaded programming");
        jFrame.setSize(700, 500);
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        this.setVisible(true);
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        Font font1 = new Font("Times New Roman", Font.BOLD, 15);
        Font font2 = new Font("Times New Roman", Font.BOLD, 25);
        topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(700, 500));
        topPanel.setLayout(null);

        JLabel jLabel = new JLabel("MONITOR THE CHANGE PROGRAMMING", SwingConstants.CENTER);
        jLabel.setBounds(0, 75, 700, 40);
        jLabel.setFont(font2);

        JLabel labelFileCopy = new JLabel("Server: ");
        labelFileCopy.setBounds(50, 130, 400, 40);
        labelFileCopy.setFont(font1);

        buttonCopy = new JButton("Select Folder");

        buttonCopy.setBounds(500, 130, 150, 40);
        model = new DefaultListModel();
        jList = new JList(model);

        jList.getSelectedValue();

        jList.setBounds(50, 210, 280, 200);

        JLabel jlabelClient = new JLabel("List client:");
        jlabelClient.setBounds(50, 165, 600, 50);
        jlabelClient.setFont(font1);

        JLabel jlabelChange = new JLabel("List change:");
        jlabelChange.setBounds(350, 165, 600, 50);
        jlabelChange.setFont(font1);

        modelChange = new DefaultListModel();
        jListChange = new JList(modelChange);
        jListChange.setBounds(370, 210, 280, 200);

        topPanel.add(jlabelChange);
        topPanel.add(jlabelClient);
        topPanel.add(jLabel);
        topPanel.add(labelFileCopy);
        topPanel.add(buttonCopy);

        
        JScrollPane scrollPane = new JScrollPane(jList);
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);

        topPanel.add(jList);

        JScrollPane scrollPane1 = new JScrollPane(jListChange);
        scrollPane1.setAlignmentX(LEFT_ALIGNMENT);
        topPanel.add(jListChange);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(50, 400, 600, 40);


//
//        panelHome.add(buttonCopy);
        topPanel.add(progressBar);

        add(topPanel);
    }

    public static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jf = new JFrame("Multithreaded programming");
        JPanel jPanel1 = new JPanel();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Ser vidu = new Ser();
        vidu.setOpaque(true);
        jf.setContentPane(vidu);

        jf.pack();
        jf.setVisible(true);

        buttonCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // register directory and process its events
                
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // File oldDir = mSelectedFolder;
                    // unregisterWatchDir(oldDir);
                    mSelectedFolder = fileChooser.getSelectedFile();
                    registerWatchDir(mSelectedFolder);
                } 
            }
        });
    }


    public static void unregisterWatchDir(File dir) {
        if(dir != null && mWatchService != null) {
            try {
                Path path = dir.toPath();
                path.register(mWatchService, StandardWatchEventKinds.ENTRY_CREATE, 
                                            StandardWatchEventKinds.ENTRY_DELETE, 
                                            StandardWatchEventKinds.ENTRY_MODIFY);
    
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void registerWatchDir(File dir) {
        Thread t1 = new Thread() {
            public void run() {
                if(dir.isDirectory()) {
                    try {
                        WatchService watchService = FileSystems.getDefault().newWatchService();

                        Path path = dir.toPath();
                        path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, 
                                                    StandardWatchEventKinds.ENTRY_DELETE, 
                                                    StandardWatchEventKinds.ENTRY_MODIFY);
                                     
                                                    
                        int index = jList.getSelectedIndex();
                        if(index == -1) index = 0;
                        BufferedWriter output = null;
                        if(index < pack.size()) {
                            Socket sock = pack.get(index);
                            output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                            output.write(dir.getPath());
                            output.newLine();
                            output.flush();
                        }

                        WatchKey key;
                        while ((key = watchService.take()) != null) {
                            for (WatchEvent<?> event : key.pollEvents()) {
                                String ev = "Event kind:" + event.kind()  + ". File affected: " + event.context() + ".";
                                System.out.println(ev);
                                if(output != null) {
                                    modelChange.addElement(ev);
                                }
                            }
                            key.reset();
                        }
                        if(output != null) {
                            output.close();
                        }

                    }catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        };
        t1.start();
    }

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(5001);
            createAndShowGUI();

            

            Thread t1 = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            System.out.println("wait");
                            Socket s = ss.accept();
                            System.out.println("Connect to client");
                            pack.add(s);
                            System.out.println("pack.size(): " + pack.size());
                            model.add(pack.size() - 1, "Client port: " + s.getPort());
                            //System.out.println("Connect to client");
                            if(pack.size() == 1 && jList.getSelectedIndex() == -1) {
                                jList.setSelectedIndex(0);
                            }
                            System.out.println(s.getPort());
                            BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                           

                        }
                    } catch (IOException e) {
                        // throw new RuntimeException(e);
                        System.out.println(e.getMessage());
                    }
                }
            };
            t1.start();
        } catch (IOException e) {
            // throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
    }


}