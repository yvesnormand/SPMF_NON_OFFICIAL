package ca.pfv.spmf.welwindow;

import ca.pfv.spmf.gui.AboutWindow;
import ca.pfv.spmf.gui.Main;
import ca.pfv.spmf.gui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
 * Copyright (c) 2008-2019 Philippe Fournier-Viger
 *
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 *
 * SPMF is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SPMF is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SPMF. If not, see <http://www.gnu.org/licenses/>.
 */

public class Welcome extends JFrame {
    public static final long serialVersionUID = 1L;
    private JButton buttonDatasetTools;
    private JButton buttonRunAlgorithm;
    private JButton buttonPlugins;
    private JButton buttonRunManyAlgorithms;
    private JButton buttonDocumentation;
    //	private JButton buttonPreferences;
    private JButton buttonAboutSPMF;
    private JLabel j1;
    private JLabel j2;
    private JPanel jPanel;
//
//	private MainWindow mainWindowTools;
//	private MainWindow mainWindowAlgorithms;


    public Welcome() {
        initComponents();
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Welcome();
            }
        });
    }

    private void initComponents() {
        JFrame frame = this;
        frame.setTitle("SPMF v." + Main.SPMF_VERSION + " - Welcome");
        frame.setSize(900, 175);
        frame.setLocationRelativeTo(null);
//		frame.setLocation(500, 500);
        frame.setResizable(false);


        buttonDatasetTools = new JButton();
        buttonRunAlgorithm = new JButton();
        buttonPlugins = new JButton();

        //  BUTTON FOR EXPERIMENTS
        buttonRunManyAlgorithms = new JButton();
        buttonRunManyAlgorithms.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MainWindow mainWindowTools;
                try {
                    mainWindowTools = new MainWindow(false, false, true);
                    mainWindowTools.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
                    mainWindowTools.setVisible(true);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        buttonDocumentation = new JButton();
        buttonDocumentation.setIcon(new ImageIcon(Welcome.class.getResource("/ca/pfv/spmf/gui/icons/Information24.gif")));
//		buttonPreferences = new JButton();
        buttonAboutSPMF = new JButton();
        buttonAboutSPMF.setIcon(new ImageIcon(Welcome.class.getResource("/ca/pfv/spmf/gui/icons/About24.gif")));
        j1 = new JLabel();
        j2 = new JLabel();
        jPanel = new JPanel();

        buttonDatasetTools.setText("Prepare data (dataset tools)");
        buttonRunAlgorithm.setText("Run an algorithm");
        buttonPlugins.setText("Add/Remove plugins");
        buttonRunManyAlgorithms.setText("Run an experiment");
//		buttonRunManyAlgorithms.setEnabled(false);
        buttonDocumentation.setText("Online documentation");
//		buttonPreferences.setText("Preferences");
        buttonAboutSPMF.setText("About SPMF");

        j1.setText("What would you like to do?");
        j1.setBounds(15, 70, 300, 20);
        frame.getContentPane().add(j1);

        j2.setBounds(15, 15, 150, 50);
        j2.setIcon(new ImageIcon(Welcome.class.getResource("spmf.png")));
        frame.getContentPane().add(j2);

        buttonDatasetTools.setBounds(10, 100, 200, 30);
        frame.getContentPane().add(buttonDatasetTools);
        buttonDatasetTools.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    b1ActionPerformed(evt);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        buttonRunAlgorithm.setBounds(220, 100, 140, 30);
        frame.getContentPane().add(buttonRunAlgorithm);
        buttonRunAlgorithm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    b2ActionPerformed(evt);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        buttonPlugins.setBounds(680, 75, 190, 30);
        frame.getContentPane().add(buttonPlugins);
        buttonPlugins.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b3ActionPerformed(evt);
            }
        });

        buttonRunManyAlgorithms.setBounds(370, 100, 170, 30);
        frame.getContentPane().add(buttonRunManyAlgorithms);

        buttonDocumentation.setBounds(680, 40, 190, 30);
        frame.getContentPane().add(buttonDocumentation);
        buttonDocumentation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b5ActionPerformed(evt);
            }
        });

//		buttonPreferences.setBounds(710, 40, 160, 25);
//		frame.add(buttonPreferences);

        buttonAboutSPMF.setBounds(680, 10, 190, 25);
        frame.getContentPane().add(buttonAboutSPMF);
        buttonAboutSPMF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                b7ActionPerformed(evt);
            }
        });

        frame.getContentPane().add(jPanel);
        jPanel.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(Welcome.EXIT_ON_CLOSE);
    }

    private void b1ActionPerformed(ActionEvent evt) throws Exception {
        MainWindow mainWindowTools = new MainWindow(true, false, false);
        mainWindowTools.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
        mainWindowTools.setVisible(true);
    }

    private void b2ActionPerformed(ActionEvent evt) throws Exception {
        MainWindow window = new MainWindow(false, true, false);
        window.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);
        window.setVisible(true);
    }

    private void b3ActionPerformed(ActionEvent evt) {
        PluginWindow mainplugin = new PluginWindow(this);
        mainplugin.setDefaultCloseOperation(Welcome.HIDE_ON_CLOSE);

    }

    private void b5ActionPerformed(ActionEvent evt) {

        String url = "http://www.philippe-fournier-viger.com/spmf/index.php?link=documentation.php";
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void b7ActionPerformed(ActionEvent evt) {

//		String url = "http://www.philippe-fournier-viger.com/spmf/";
//		try {
//			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
//		} catch (java.io.IOException e) {
//			System.out.println(e.getMessage());
//		}
        AboutWindow about;
        try {
            about = new AboutWindow(this);// set JFrame in center of the screen
//			about.setLocationRelativeTo(null);
            about.setVisible(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

