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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

public class Cli extends JPanel {
    String content[];
    public static DefaultListModel model;

    public  static void showFileAndFolder (String path){
        //Creating a File object for directory
        File directoryPath = new File(path);
        //List of all files and directories
        String contents[] = directoryPath.list();
        System.out.println("List of files and directories in the specified directory:");
        for(int i = 0; i < contents.length; i++) {
            model.add(i, contents[i]);
        }
    }
    public Cli() {
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

        JLabel labelFileCopy = new JLabel("Client: ");
        labelFileCopy.setBounds(50, 150, 400, 40);
        labelFileCopy.setFont(font1);




        model = new DefaultListModel();


        JList jList =  new JList(model);

//        model.remove(jList.get\)
        jList.getSelectedValue();
        jList.setBounds(50, 210, 600, 200);

        topPanel.add(jLabel);
        topPanel.add(labelFileCopy);

//        String district[] = {"district 1", "district 2","district 3"};
//        JComboBox distr = new JComboBox(district);
//        topPanel.setLayout(new FlowLayout());
//        topPanel.add(distr);
//        add(topPanel, BorderLayout.PAGE_START);
//
//        String war[] = {"Bến Nghé", "Bến Thành", "Đa Kao","Đa Kao","Đa Kao","Đa Kao"};
//        JList jList = new JList(war);
//        add(jList, BorderLayout.CENTER);
//        JScrollPane scrollPane = new JScrollPane(jList);
//        add(scrollPane, BorderLayout.CENTER);


//        JPanel bottomPanel = new JPanel();
//        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS));



        topPanel.add(jList);



        JProgressBar progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(50, 400, 600, 40);



        topPanel.add(progressBar);

        add(topPanel);
    }

    public static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jf = new JFrame("Multithreaded programming");
        JPanel jPanel1 = new JPanel();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Cli vidu = new Cli();
        vidu.setOpaque(true);
        jf.setContentPane(vidu);

        jf.pack();
        jf.setVisible(true);
    }

    public static void main(String[] args) {

        try {
            createAndShowGUI();
            Socket ss = new Socket("localhost",5001);

            System.out.println(ss.getPort());

            BufferedReader input = new BufferedReader( new InputStreamReader(ss.getInputStream()));
            System.out.println("enter");
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(ss.getOutputStream()));

            System.out.println("connected");

            String path;
            path = input.readLine();


            System.out.println(path);
            showFileAndFolder(path);

        } catch (IOException ex) {
            System.out.println("error");
        }
    }


}