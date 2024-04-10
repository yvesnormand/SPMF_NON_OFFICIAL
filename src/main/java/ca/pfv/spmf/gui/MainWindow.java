package ca.pfv.spmf.gui;

/*
 * Copyright (c) 2008-2022 Philippe Fournier-Viger
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

import ca.pfv.spmf.algorithmmanager.AlgorithmManager;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;
import ca.pfv.spmf.algorithmmanager.descriptions.DescriptionAlgoClusterViewer;
import ca.pfv.spmf.algorithmmanager.descriptions.DescriptionAlgoGraphViewerOpenFile;
import ca.pfv.spmf.algorithms.timeseries.TimeSeries;
import ca.pfv.spmf.algorithms.timeseries.reader_writer.AlgoTimeSeriesReader;
import ca.pfv.spmf.gui.patternvizualizer.PatternVizualizer;
import ca.pfv.spmf.gui.texteditor.SPMFTextEditor;
import ca.pfv.spmf.gui.timeseriesviewer.TimeSeriesViewer;
import ca.pfv.spmf.test.MainTestApriori_saveToFile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * This class is the user interface of SPMF (the main Window). It allows the
 * user to launch single algorithms.
 *
 * @author Philippe Fournier-Viger
 */
public class MainWindow extends JFrame implements ThreadCompleteListener, UncaughtExceptionHandler {

    /**
     * serial UID for serialization
     */
    private static final long serialVersionUID = 1L;
    /**
     * The current data mining task
     */
    private static NotifyingThread currentRunningAlgorithmThread = null;
    /**
     * the current data mining process
     */
    private static Process currentExternalProcess = null;
    /**
     * The following fields are components of the user interface. They are generated
     * automatically by the Visual Editor plugin of Eclipse.
     */
    private final JPanel contentPane;
    private final JTextField textFieldParam1;
    private final JTextField textFieldParam2;
    private final JTextField textFieldParam3;
    private final JTextField textFieldParam4;
    private final JTextField textFieldParam5;
    private final JTextField textFieldParam6;
    private final JTextField textFieldParam7;
    private final JTextField textFieldParam8;
    private final JLabel labelParam1;
    private final JLabel labelParam2;
    private final JLabel labelParam3;
    private final JLabel labelParam4;
    private final JLabel labelParam5;
    private final JLabel labelParam6;
    private final JLabel labelParam7;
    private final JLabel labelParam8;
    private final JLabel lbHelp1;
    private final JLabel lbHelp2;
    private final JLabel lbHelp3;
    private final JLabel lbHelp4;
    private final JLabel lbHelp5;
    private final JLabel lbHelp6;
    private final JLabel lbHelp7;
    private final JLabel lbHelp8;
    private final JTextField textFieldInput;
    private final JTextField textFieldOutput;
    private final JComboBox<String> comboBox;
    private final JTextArea textArea;
    private final JButton buttonRun;
    private final JRadioButton checkboxOpenOutputText;
    private final JButton buttonExample;
    private final JLabel lblSetOutputFile;
    private final JButton buttonOutput;
    private final JButton buttonInput;
    private final JLabel lblChooseInputFile;
    private final JProgressBar progressBar;

    // VARIABLES USED TO RUN AN ALGORITHM IN A SEPARATED THREAD
    private final JRadioButton checkboxOpenOutputPatternViewer;

    // VARIABLES USED TO RUN AN ALGORITHM AS AN EXTERNAL PROGRAM
    private final JRadioButton checkboxOpenOutputTimeSeriesViewer;
    private final JLabel lblOpenOutputFile;
    private final JRadioButton checkboxClusterViewer;
    private final JRadioButton checkboxSubgraphViewer;
    private final JLabel lblOptions;
    private final JCheckBox chckbxRunAsExternal;
    private final JTextField textMaxSeconds;
    private final JCheckBox chckbxMaxSeconds;
    private final MainWindow self = this;
    private final JRadioButton checkboxOpenSPMFEditor;
    /**
     * current input file
     */
    private String inputFile = null;
    /**
     * current output file
     */
    private String outputFile = null;
    private int maxTime;

    public MainWindow() throws Exception {
        this(true, true, true);
        setVisible(true);
    }


    /**
     * Create the frame.
     *
     * @throws Exception
     */
    public MainWindow(boolean showTools, boolean showAlgorithms, boolean showExperimentTools) throws Exception {
        setIconImage(Toolkit.getDefaultToolkit().getImage(MainWindow.class.getResource("/ca/pfv/spmf/gui/spmf.png")));

        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent arg0) {
                System.exit(0);
            }
        });

        // For the new version of SPMF
        // We want to distinguish if the windows is used to run a dataset tool or
        // another algorithm.
        if (showTools && !showAlgorithms && !showExperimentTools) {
            setTitle("Prepare data (run a dataset tool)");
        } else if (!showTools && showAlgorithms && !showExperimentTools) {
            setTitle("Run an algorithm");
        } else if (!showTools && !showAlgorithms && showExperimentTools) {
            setTitle("Run an experiment");
        } else {
            // set the title of the window
            setTitle("SPMF v" + Main.SPMF_VERSION);
        }
        // When the user clicks the "x" the software will close.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // size of the window
        this.setSize(719, 782);
        this.setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Combo box to store the list of algorithms.
        comboBox = new JComboBox<String>(new Vector<String>());
        comboBox.setMaximumRowCount(20);

        // ************************************************************************
        // ********* Use the algorithm manager to populate the list of algorithms
        // ******* //
        comboBox.addItem("");

        AlgorithmManager manager = null;
        try {
            manager = AlgorithmManager.getInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        List<String> algorithmList = manager.getListOfAlgorithmsAsString(showTools, showAlgorithms,
                showExperimentTools);
        for (String algorithmOrCategoryName : algorithmList) {
            comboBox.addItem(algorithmOrCategoryName);
        }

        // ************ NEW 2022
        // ************************************************************
        // Add colors to the JComboBox for categories of algorithm
        AlgorithmComboBoxRenderer comboBoxRenderer = new AlgorithmComboBoxRenderer(comboBox);
        comboBox.setRenderer(comboBoxRenderer);

        // **********************************************************************************

        // What to do when the user choose an algorithm :
        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                // We need to update the user interface:
                try {
                    updateUserInterfaceAfterAlgorithmSelection(evt.getItem().toString(),
                            evt.getStateChange() == ItemEvent.SELECTED);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        comboBox.setBounds(246, 73, 367, 20);
        contentPane.add(comboBox);

        // The button "Run algorithm"
        buttonRun = new JButton("Run algorithm");
        buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Play24.gif")));
        buttonRun.setEnabled(false);
        buttonRun.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {

                // When the user clicks "run":
                processRunAlgorithmCommandFromGUI();

            }
        });
        buttonRun.setBounds(278, 479, 163, 29);
        contentPane.add(buttonRun);

        JLabel lblChooseAnAlgorithm = new JLabel("Choose an algorithm:");
        lblChooseAnAlgorithm.setBounds(22, 73, 204, 20);
        contentPane.add(lblChooseAnAlgorithm);

        JLabel lblNewLabel = new JLabel("New label");
        lblNewLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent arg0) {
                AboutWindow about;
                try {
                    about = new AboutWindow(self);
                    about.setVisible(true);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Show the webpage of the SPMF project
//				openWebPage("http://www.philippe-fournier-viger.com/spmf/");
            }
        });
        lblNewLabel.setIcon(new ImageIcon(MainWindow.class.getResource("spmf.png")));
        lblNewLabel.setBounds(12, 13, 140, 47);
        contentPane.add(lblNewLabel);

        textFieldParam1 = new JTextField();
        textFieldParam1.setBounds(263, 164, 157, 20);
        contentPane.add(textFieldParam1);
        textFieldParam1.setColumns(10);

        // {String buffer = new String(new byte[]{83,80,77,70});
        // if(getTitle().startsWith(buffer) != true){setTitle(buffer);}}

        buttonInput = new JButton("...");
        buttonInput.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                askUserToChooseInputFile();
            }
        });

        buttonInput.setBounds(430, 104, 32, 23);
        contentPane.add(buttonInput);

        buttonOutput = new JButton("...");
        buttonOutput.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                askUserToChooseOutputFile();
            }
        });
        buttonOutput.setBounds(430, 133, 32, 23);
        contentPane.add(buttonOutput);

        labelParam1 = new JLabel("Parameter 1:");
        labelParam1.setBounds(22, 167, 204, 14);
        contentPane.add(labelParam1);

        labelParam2 = new JLabel("Parameter 2:");
        labelParam2.setBounds(22, 192, 204, 14);
        contentPane.add(labelParam2);

        labelParam3 = new JLabel("Parameter 3:");
        labelParam3.setBounds(22, 217, 204, 14);
        contentPane.add(labelParam3);

        labelParam4 = new JLabel("Parameter 4:");
        labelParam4.setBounds(22, 239, 231, 14);
        contentPane.add(labelParam4);

        labelParam5 = new JLabel("Parameter 5:");
        labelParam5.setBounds(22, 264, 231, 14);
        contentPane.add(labelParam5);

        labelParam6 = new JLabel("Parameter 6:");
        labelParam6.setBounds(22, 289, 231, 14);
        contentPane.add(labelParam6);

        labelParam7 = new JLabel("Parameter 7:");
        labelParam7.setBounds(22, 310, 231, 14);
        contentPane.add(labelParam7);

        labelParam8 = new JLabel("Parameter 8:");
        labelParam8.setBounds(22, 332, 231, 14);
        contentPane.add(labelParam8);

        textFieldParam2 = new JTextField();
        textFieldParam2.setColumns(10);
        textFieldParam2.setBounds(263, 189, 157, 20);
        contentPane.add(textFieldParam2);

        textFieldParam3 = new JTextField();
        textFieldParam3.setColumns(10);
        textFieldParam3.setBounds(263, 214, 157, 20);
        contentPane.add(textFieldParam3);

        textFieldParam4 = new JTextField();
        textFieldParam4.setColumns(10);
        textFieldParam4.setBounds(263, 236, 157, 20);
        contentPane.add(textFieldParam4);

        textFieldParam5 = new JTextField();
        textFieldParam5.setColumns(10);
        textFieldParam5.setBounds(263, 261, 157, 20);
        contentPane.add(textFieldParam5);

        textFieldParam6 = new JTextField();
        textFieldParam6.setColumns(10);
        textFieldParam6.setBounds(263, 286, 157, 20);
        contentPane.add(textFieldParam6);

        textFieldParam7 = new JTextField();
        textFieldParam7.setColumns(10);
        textFieldParam7.setBounds(263, 310, 157, 20);
        contentPane.add(textFieldParam7);

        textFieldParam8 = new JTextField();
        textFieldParam8.setColumns(10);
        textFieldParam8.setBounds(263, 332, 157, 20);
        contentPane.add(textFieldParam8);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 542, 683, 194);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        System.setOut(new PrintStream(new TextAreaOutputStream(textArea)));

        textFieldInput = new JTextField();
        textFieldInput.setEditable(false);
        textFieldInput.setBounds(263, 105, 157, 20);
        contentPane.add(textFieldInput);
        textFieldInput.setColumns(10);

        textFieldOutput = new JTextField();
        textFieldOutput.setEditable(false);
        textFieldOutput.setColumns(10);
        textFieldOutput.setBounds(263, 134, 157, 20);
        contentPane.add(textFieldOutput);

        checkboxOpenOutputText = new JRadioButton("System text editor");
        checkboxOpenOutputText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (checkboxOpenOutputText.isSelected()) {
                    checkboxOpenOutputPatternViewer.setSelected(false);
                    checkboxOpenOutputTimeSeriesViewer.setSelected(false);
                    checkboxClusterViewer.setSelected(false);
                    checkboxSubgraphViewer.setSelected(false);
                    checkboxOpenSPMFEditor.setSelected(false);
                    PreferencesManager.getInstance().setShouldUseSystemTextEditor(true);
                }
            }
        });

        checkboxOpenSPMFEditor = new JRadioButton("SPMF text editor");
        checkboxOpenSPMFEditor.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (checkboxOpenSPMFEditor.isSelected()) {
                    checkboxOpenOutputPatternViewer.setSelected(false);
                    checkboxOpenOutputTimeSeriesViewer.setSelected(false);
                    checkboxClusterViewer.setSelected(false);
                    checkboxOpenOutputText.setSelected(false);
                    checkboxSubgraphViewer.setSelected(false);
                    PreferencesManager.getInstance().setShouldUseSystemTextEditor(false);
                }
            }
        });

        checkboxOpenOutputText.setBounds(34, 391, 140, 23);
        contentPane.add(checkboxOpenOutputText);

        checkboxOpenOutputPatternViewer = new JRadioButton("Pattern viewer");
        checkboxOpenOutputPatternViewer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (checkboxOpenOutputPatternViewer.isSelected()) {
                    checkboxOpenOutputText.setSelected(false);
                    checkboxOpenOutputTimeSeriesViewer.setSelected(false);
                    checkboxClusterViewer.setSelected(false);
                    checkboxOpenSPMFEditor.setSelected(false);
                    checkboxSubgraphViewer.setSelected(false);
                }
            }
        });
        checkboxOpenOutputPatternViewer.setBounds(303, 388, 125, 29);
        contentPane.add(checkboxOpenOutputPatternViewer);

        buttonExample = new JButton("?");
        buttonExample.setEnabled(false);
        buttonExample.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                // When the user clicks on the "?",
                // we open the webpage corresponding to the algorithm
                // that is currently selected.
                String choice = (String) comboBox.getSelectedItem();
                try {
                    openHelpWebPageForAlgorithm(choice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        buttonExample.setBounds(642, 73, 49, 23);
        contentPane.add(buttonExample);

        lblChooseInputFile = new JLabel("Choose input file");
        lblChooseInputFile.setBounds(22, 108, 130, 14);
        contentPane.add(lblChooseInputFile);

        lblSetOutputFile = new JLabel("Set output file");
        lblSetOutputFile.setBounds(22, 137, 130, 14);
        contentPane.add(lblSetOutputFile);

        lbHelp1 = new JLabel("help1");
        lbHelp1.setBounds(430, 167, 211, 14);
        contentPane.add(lbHelp1);

        lbHelp2 = new JLabel("help2");
        lbHelp2.setBounds(430, 192, 211, 14);
        contentPane.add(lbHelp2);

        lbHelp3 = new JLabel("help3");
        lbHelp3.setBounds(430, 217, 200, 14);
        contentPane.add(lbHelp3);

        lbHelp4 = new JLabel("help4");
        lbHelp4.setBounds(430, 239, 200, 14);
        contentPane.add(lbHelp4);

        lbHelp5 = new JLabel("help5");
        lbHelp5.setBounds(430, 264, 200, 14);
        contentPane.add(lbHelp5);

        lbHelp6 = new JLabel("help6");
        lbHelp6.setBounds(430, 289, 200, 14);
        contentPane.add(lbHelp6);

        lbHelp7 = new JLabel("help7");
        lbHelp7.setBounds(430, 310, 200, 14);
        contentPane.add(lbHelp7);

        lbHelp8 = new JLabel("help8");
        lbHelp8.setBounds(430, 332, 200, 14);
        contentPane.add(lbHelp8);

        progressBar = new JProgressBar();
        progressBar.setBounds(278, 515, 163, 16);
        contentPane.add(progressBar);

        lblOpenOutputFile = new JLabel("Open output file using: ");
        lblOpenOutputFile.setBounds(24, 369, 191, 20);
        contentPane.add(lblOpenOutputFile);

        checkboxOpenOutputTimeSeriesViewer = new JRadioButton("Time series viewer");
        checkboxOpenOutputTimeSeriesViewer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkboxOpenOutputTimeSeriesViewer.isSelected()) {
                    checkboxOpenOutputText.setSelected(false);
                    checkboxOpenOutputPatternViewer.setSelected(false);
                    checkboxClusterViewer.setSelected(false);
                    checkboxSubgraphViewer.setSelected(false);
                    checkboxOpenSPMFEditor.setSelected(false);
                }
            }
        });
        checkboxOpenOutputTimeSeriesViewer.setBounds(427, 388, 132, 29);
        checkboxOpenOutputTimeSeriesViewer.setVisible(false);
        contentPane.add(checkboxOpenOutputTimeSeriesViewer);

        checkboxClusterViewer = new JRadioButton("Cluster viewer");
        checkboxClusterViewer.setBounds(427, 388, 119, 29);
        checkboxClusterViewer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkboxClusterViewer.isSelected()) {
                    checkboxOpenOutputText.setSelected(false);
                    checkboxOpenOutputPatternViewer.setSelected(false);
                    checkboxSubgraphViewer.setSelected(false);
                    checkboxOpenOutputTimeSeriesViewer.setSelected(false);
                    checkboxOpenSPMFEditor.setSelected(false);
                }
            }
        });
        contentPane.add(checkboxClusterViewer);

        checkboxSubgraphViewer = new JRadioButton("Graph viewer");
        checkboxSubgraphViewer.setBounds(427, 388, 119, 29);
        checkboxSubgraphViewer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkboxSubgraphViewer.isSelected()) {
                    checkboxOpenOutputText.setSelected(false);
                    checkboxOpenOutputPatternViewer.setSelected(false);
                    checkboxClusterViewer.setSelected(false);
                    checkboxOpenOutputTimeSeriesViewer.setSelected(false);
                    checkboxOpenSPMFEditor.setSelected(false);
                }
            }
        });
        contentPane.add(checkboxSubgraphViewer);

        lblOptions = new JLabel("Options:");
        lblOptions.setBounds(23, 428, 140, 14);
        lblOptions.setVisible(false);
        contentPane.add(lblOptions);

        chckbxRunAsExternal = new JCheckBox("Run in a separated process");
        boolean runAsExternal = PreferencesManager.getInstance().getRunAsExternalProgram();
        chckbxRunAsExternal.setSelected(runAsExternal);
        chckbxRunAsExternal.setVisible(false);
        chckbxRunAsExternal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Save preference
                PreferencesManager.getInstance().setRunAsExternalProgram(chckbxRunAsExternal.isSelected());
            }
        });
        chckbxRunAsExternal.setBounds(45, 447, 182, 23);
        contentPane.add(chckbxRunAsExternal);

        textMaxSeconds = new JTextField();
        textMaxSeconds.setEditable(false);
        textMaxSeconds.setEnabled(false);
        textMaxSeconds.setVisible(false);
        textMaxSeconds.setBounds(348, 448, 56, 20);
        textMaxSeconds.setText("");
        contentPane.add(textMaxSeconds);
        textMaxSeconds.setColumns(10);

        chckbxMaxSeconds = new JCheckBox("Time limit (s):");
        chckbxMaxSeconds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textMaxSeconds.setEnabled(chckbxMaxSeconds.isSelected());
                textMaxSeconds.setEditable(chckbxMaxSeconds.isSelected());
            }
        });
        chckbxMaxSeconds.setSelected(false);
        chckbxMaxSeconds.setVisible(false);
        chckbxMaxSeconds.setBounds(234, 447, 108, 23);
        contentPane.add(chckbxMaxSeconds);

        // Load preference for text editor
        boolean useDefaultTextEditor = PreferencesManager.getInstance().getShouldUseSystemTextEditor();
        checkboxOpenOutputText.setSelected(useDefaultTextEditor);
        checkboxOpenSPMFEditor.setSelected(!useDefaultTextEditor);

        checkboxOpenSPMFEditor.setBounds(180, 391, 121, 23);
        contentPane.add(checkboxOpenSPMFEditor);

        hideAllParams();
    }

    /**
     * This method updates the user interface according to what the user has
     * selected or unselected in the list of algorithms. For example, if the user
     * choose the "PrefixSpan" algorithm the parameters of the PrefixSpan algorithm
     * will be shown in the user interface.
     *
     * @param algorithmName the algorithm name.
     * @throws Exception
     * @boolean isSelected indicate if the algorithm has been selected or unselected
     */
    private void updateUserInterfaceAfterAlgorithmSelection(String algorithmName, boolean isSelected) throws Exception {
        // COMBOBOX ITEM SELECTION - ITEM STATE CHANGED
        if (isSelected) {
            buttonRun.setEnabled(true);
            buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Play24.gif")));
            buttonExample.setEnabled(true);

            // ************************************************************************
            // ********* Prepare the user interface for this algorithm ******* //
            hideAllParams();

            JTextField[] textFieldsParams = new JTextField[] { textFieldParam1, textFieldParam2, textFieldParam3,
                    textFieldParam4, textFieldParam5, textFieldParam6, textFieldParam7, textFieldParam8 };

            JLabel[] labelsParams = new JLabel[] { labelParam1, labelParam2, labelParam3, labelParam4, labelParam5,
                    labelParam6, labelParam7, labelParam8 };

            AlgorithmManager manager = AlgorithmManager.getInstance();
            DescriptionOfAlgorithm algorithm = manager.getDescriptionOfAlgorithm(algorithmName);
            if (algorithm != null) {
                DescriptionOfParameter[] parameters = algorithm.getParametersDescription();
                for (int i = 0; i < parameters.length; i++) {
                    DescriptionOfParameter parameter = parameters[i];
                    String optional = parameter.isOptional ? " (optional)" : "";
                    setParam(textFieldsParams[i], parameter.name + optional, labelsParams[i], parameter.example);
                }

                if (algorithm.getInputFileTypes() != null) {
                    lblChooseInputFile.setVisible(true);
                    buttonInput.setVisible(true);
                    textFieldInput.setVisible(true);
                } else {
                    lblChooseInputFile.setVisible(false);
                    buttonInput.setVisible(false);
                    textFieldInput.setVisible(false);
                }

                if (algorithm.getOutputFileTypes() != null) {
                    lblSetOutputFile.setVisible(true);
                    buttonOutput.setVisible(true);
                    textFieldOutput.setVisible(true);
                    checkboxOpenOutputText.setVisible(true);
                    checkboxOpenSPMFEditor.setVisible(true);

                    lblOpenOutputFile.setVisible(true);
                    checkboxOpenOutputPatternViewer.setVisible(true);
                    if (algorithm.getOutputFileTypes()[0].equals("Time series database")) {
                        checkboxOpenOutputTimeSeriesViewer.setVisible(true);
                        checkboxOpenOutputPatternViewer.setVisible(false);
                    } else {
                        checkboxOpenOutputTimeSeriesViewer.setVisible(false);
                    }

                    //                    	checkboxOpenOutputPatternViewer.setVisible(false);
                    checkboxClusterViewer.setVisible(algorithm.getOutputFileTypes()[0].equals("Clusters"));

                    //                    	checkboxOpenOutputPatternViewer.setVisible(false);
                    checkboxSubgraphViewer.setVisible(algorithm.getOutputFileTypes()[algorithm.getOutputFileTypes().length - 1]
                                                              .equals("Top-k Frequent subgraphs")
                                                      || algorithm.getOutputFileTypes()[algorithm.getOutputFileTypes().length - 1]
                                                              .equals("Frequent subgraphs"));

                    if (algorithm.getOutputFileTypes().length > 1
                        && (algorithm.getOutputFileTypes()[1].equals("Subgraphs")
                            || algorithm.getOutputFileTypes()[1].equals("Subgraphs"))) {
                        checkboxOpenOutputPatternViewer.setVisible(false);
                    }
                }

                lblOptions.setVisible(true);
                chckbxRunAsExternal.setVisible(true);

                textMaxSeconds.setVisible(true);
                chckbxMaxSeconds.setVisible(true);

                // ************************************************************************
            } else {
                // This is for the command line version
                // If the name of the algorithm is not recognized:
                if (!isVisible()) {
                    System.out.println("There is no algorithm with this name. "
                                       + " To fix this problem, you may check the command syntax in the SPMF documentation"
                                       + " and/or verify if there is a new version of SPMF on the SPMF website.");
                }

                hideAllParams();
                buttonRun.setEnabled(false);
                buttonExample.setEnabled(false);

                lblOptions.setVisible(false);
                chckbxRunAsExternal.setVisible(false);

                textMaxSeconds.setVisible(false);
                chckbxMaxSeconds.setVisible(false);
            }
        } else {
            // if no algorithm is chosen, we hide all parameters.
            hideAllParams();
            buttonRun.setEnabled(false);
            buttonExample.setEnabled(false);

            lblOptions.setVisible(false);
            chckbxRunAsExternal.setVisible(false);

            textMaxSeconds.setVisible(false);
            chckbxMaxSeconds.setVisible(false);
        }
    }

    private void setParam(JTextField textfield, String name, JLabel label, String helpText) {
        label.setText(name);
        textfield.setEnabled(true);
        textfield.setVisible(true);
        label.setVisible(true);
        if (textfield == textFieldParam1) {
            lbHelp1.setText(helpText);
            lbHelp1.setVisible(true);
        } else if (textfield == textFieldParam2) {
            lbHelp2.setText(helpText);
            lbHelp2.setVisible(true);
        } else if (textfield == textFieldParam3) {
            lbHelp3.setText(helpText);
            lbHelp3.setVisible(true);
        } else if (textfield == textFieldParam4) {
            lbHelp4.setText(helpText);
            lbHelp4.setVisible(true);
        } else if (textfield == textFieldParam5) {
            lbHelp5.setText(helpText);
            lbHelp5.setVisible(true);
        } else if (textfield == textFieldParam6) {
            lbHelp6.setText(helpText);
            lbHelp6.setVisible(true);
        } else if (textfield == textFieldParam7) {
            lbHelp7.setText(helpText);
            lbHelp7.setVisible(true);
        } else if (textfield == textFieldParam8) {
            lbHelp8.setText(helpText);
            lbHelp8.setVisible(true);
        }
    }

//    private  static void setHelpTextForParam(JLabel label, String name) {
//        label.setText(name);
//        label.setVisible(true);
//    }

    /**
     * Hide all parameters from the user interface. This is used to hide fields when
     * the user change algorithms or when the JFrame is first created.
     */
    private void hideAllParams() {
        labelParam1.setVisible(false);
        labelParam2.setVisible(false);
        labelParam3.setVisible(false);
        labelParam4.setVisible(false);
        labelParam5.setVisible(false);
        labelParam6.setVisible(false);
        labelParam7.setVisible(false);
        labelParam8.setVisible(false);
//		.setVisible(false);
        lbHelp1.setVisible(false);
        lbHelp2.setVisible(false);
        lbHelp3.setVisible(false);
        lbHelp4.setVisible(false);
        lbHelp5.setVisible(false);
        lbHelp6.setVisible(false);
        lbHelp7.setVisible(false);
        lbHelp8.setVisible(false);
        textFieldParam1.setVisible(false);
        textFieldParam2.setVisible(false);
        textFieldParam3.setVisible(false);
        textFieldParam4.setVisible(false);
        textFieldParam5.setVisible(false);
        textFieldParam6.setVisible(false);
        textFieldParam7.setVisible(false);
        textFieldParam8.setVisible(false);

        lblSetOutputFile.setVisible(false);
        buttonOutput.setVisible(false);
        textFieldOutput.setVisible(false);
        lblChooseInputFile.setVisible(false);
        buttonInput.setVisible(false);
        textFieldInput.setVisible(false);

        lblOpenOutputFile.setVisible(false);
        checkboxOpenOutputPatternViewer.setVisible(false);
        checkboxOpenOutputText.setVisible(false);

        checkboxOpenSPMFEditor.setVisible(false);
        checkboxOpenOutputTimeSeriesViewer.setVisible(false);
        checkboxClusterViewer.setVisible(false);
        checkboxSubgraphViewer.setVisible(false);
    }

    /**
     * This method open a URL in the default web browser.
     *
     * @param url : URL of the webpage
     */
    private void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    } // openWebPage("http://www.philippe-fournier-viger.com/spmf/");

    /**
     * This method show the help webpage for a given algorithm in the default
     * browser of the user.
     *
     * @param algorithmName the algorithm name (e.g. "PrefixSpan")
     * @throws Exception
     */
    private void openHelpWebPageForAlgorithm(String algorithmName) throws Exception {
        // ************************************************************************
        AlgorithmManager manager = AlgorithmManager.getInstance();
        DescriptionOfAlgorithm algorithm = manager.getDescriptionOfAlgorithm(algorithmName);
        if (algorithm != null) {
            openWebPage(algorithm.getURLOfDocumentation());
        }
        // ************************************************************************
    }

    /**
     * This method ask the user to choose the input file. This method is called when
     * the user click on the button to choose the input file.
     */
    private void askUserToChooseInputFile() {
        try {
            // WHEN THE USER CLICK TO CHOOSE THE INPUT FILE

            File path;
            // Get the last path used by the user, if there is one
            String previousPath = PreferencesManager.getInstance().getInputFilePath();
            if (previousPath == null) {
                // If there is no previous path (first time user),
                // show the files in the "examples" package of
                // the spmf distribution.
                URL main = MainTestApriori_saveToFile.class.getResource("MainTestApriori_saveToFile.class");
                if (!"file".equalsIgnoreCase(main.getProtocol())) {
                    path = null;
                } else {
                    path = new File(main.getPath());
                }
            } else {
                // Otherwise, the user used SPMF before, so
                // we show the last path that he used.
                path = new File(previousPath);
            }

            // Create a file chooser to let the user
            // select the file.
            final JFileChooser fc = new JFileChooser(path);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showOpenDialog(MainWindow.this);

            // if he chose a file
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                textFieldInput.setText(file.getName());
                inputFile = file.getPath(); // remember the file he chose
            }
            // remember this folder for next time.
            if (fc.getSelectedFile() != null) {
                PreferencesManager.getInstance().setInputFilePath(fc.getSelectedFile().getParent());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An error occured while opening the input file dialog. ERROR MESSAGE = " + e, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method ask the user to choose the output file. This method is called
     * when the user click on the button to choose the input file of the S P M F
     * interface.
     */
    private void askUserToChooseOutputFile() {
        try {
            // WHEN THE USER CLICK TO CHOOSE THE OUTPUT FILE

            File path;
            // Get the last path used by the user, if there is one
            String previousPath = PreferencesManager.getInstance().getOutputFilePath();
            // If there is no previous path (first time user),
            // show the files in the "examples" package of
            // the spmf distribution.
            if (previousPath == null) {
                URL main = MainTestApriori_saveToFile.class.getResource("MainTestApriori_saveToFile.class");
                if (!"file".equalsIgnoreCase(main.getProtocol())) {
                    path = null;
                } else {
                    path = new File(main.getPath());
                }
            } else {
                // Otherwise, use the last path used by the user.
                path = new File(previousPath);
            }

            // ASK THE USER TO CHOOSE A FILE
            final JFileChooser fc;
            if (path != null) {
                fc = new JFileChooser(path.getAbsolutePath());
            } else {
                fc = new JFileChooser();
            }
            int returnVal = fc.showSaveDialog(MainWindow.this);

            // If the user chose a file
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                textFieldOutput.setText(file.getName());
                outputFile = file.getPath(); // save the file path
                // save the path of this folder for next time.
                if (fc.getSelectedFile() != null) {
                    PreferencesManager.getInstance().setOutputFilePath(fc.getSelectedFile().getParent());
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "An error occured while opening the output file dialog. ERROR MESSAGE = " + e, "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        String buffer = new String(new byte[] { 83, 80, 77, 70 });
        if (!getTitle().startsWith(buffer)) {
            setTitle(buffer);
        }
    }

    /**
     * This method receives a notifications when an algorithm terminates that was
     * launched by the user by clicking "Run algorithm..."
     */
    @Override
    public void notifyOfThreadComplete(Thread thread, boolean succeed) {

        // IF - the algorithm terminates... and there is an output file
        if (succeed && lblSetOutputFile.isVisible()) {
            // if using wants to use the text editor to open the file
            if (checkboxOpenOutputText.isSelected()) {
                // open the output file if the checkbox is checked
                Desktop desktop = Desktop.getDesktop();
                // check first if we can open it on this operating system:
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    try {
                        // if yes, open it
                        desktop.open(new File(outputFile));
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,
                                "The output file failed to open with the default application. "
                                + "\n This error occurs if there is no default application on your system "
                                + "for opening the output file or the application failed to start. " + "\n\n"
                                + "To fix the problem, consider changing the extension of the output file to .txt."
                                + "\n\n ERROR MESSAGE = " + e,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (SecurityException e) {
                        JOptionPane.showMessageDialog(null,
                                "A security error occured while trying to open the output file. ERROR MESSAGE = "
                                + e,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "An error occured while opening the output file. ERROR MESSAGE = " + e,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (checkboxOpenSPMFEditor.isSelected()) {

                // if the user choose to open the file with the SPMF editor
                SPMFTextEditor editor = new SPMFTextEditor(false);
                editor.openAFile(new File(outputFile));

            }
            // pattern viewer
            // if using wants to use the text editor to open the file
            else if (checkboxOpenOutputPatternViewer.isSelected()) {
                // open the output file if the checkbox is checked
                try {
                    new PatternVizualizer(outputFile);
                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(null,
                            "A security error occured while trying to open the output file. ERROR MESSAGE = "
                            + e,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "An error occured while opening the output file. ERROR MESSAGE = " + e, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (checkboxOpenOutputTimeSeriesViewer.isSelected()) {
                // open the output file if the checkbox is checked
                try {

                    // We need to know what is the separator in the output file
                    String separator = ",";
                    // But we don't have that information

                    // So we need to do a hack to find what is the file separator used in the output
                    // file...
                    // We will check all the field of the user interface to find the separator
                    if (labelParam1.getText().equals("Separator")) {
                        separator = textFieldParam1.getText();
                    }

                    if (labelParam2.getText().equals("Separator")) {
                        separator = textFieldParam2.getText();
                    }

                    if (labelParam3.getText().equals("Separator")) {
                        separator = textFieldParam3.getText();
                    }

                    if (labelParam4.getText().equals("Separator")) {
                        separator = textFieldParam4.getText();
                    }

                    if (labelParam5.getText().equals("Separator")) {
                        separator = textFieldParam5.getText();
                    }

                    if (labelParam6.getText().equals("Separator")) {
                        separator = textFieldParam6.getText();
                    }

                    if (labelParam7.getText().equals("Separator")) {
                        separator = textFieldParam7.getText();
                    }

                    if (labelParam8.getText().equals("Separator")) {
                        separator = textFieldParam8.getText();
                    }

                    // Then call the time series viewer
//					DescriptionAlgoTimeSeriesViewer clusterViewer = new DescriptionAlgoTimeSeriesViewer();
//					clusterViewer.runAlgorithm(new String[]{separator}, outputFile, null);

                    AlgoTimeSeriesReader reader = new AlgoTimeSeriesReader();
                    List<TimeSeries> timeSeries = reader.runAlgorithm(outputFile, separator);
                    TimeSeriesViewer viewer = new TimeSeriesViewer(timeSeries);
                    viewer.setVisible(true);

                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(null,
                            "A security error occured while trying to open the output file. ERROR MESSAGE = "
                            + e,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "An error occured while opening the output file. ERROR MESSAGE = " + e, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (checkboxClusterViewer.isSelected()) {
                // open the output file if the checkbox is checked
                try {

                    // Then call the time series viewer
                    DescriptionAlgoClusterViewer clusterViewer = new DescriptionAlgoClusterViewer();
                    clusterViewer.runAlgorithm(new String[] { }, outputFile, null);

                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(null,
                            "A security error occured while trying to open the output file. ERROR MESSAGE = "
                            + e,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "An error occured while opening the output file. ERROR MESSAGE = " + e, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (checkboxSubgraphViewer.isSelected()) {
                // open the output file if the checkbox is checked
                try {

                    // Then call the time series viewer
                    DescriptionAlgoGraphViewerOpenFile clusterViewer = new DescriptionAlgoGraphViewerOpenFile();
                    clusterViewer.runAlgorithm(new String[] { }, outputFile, null);

                } catch (SecurityException e) {
                    JOptionPane.showMessageDialog(null,
                            "A security error occured while trying to open the output file. ERROR MESSAGE = "
                            + e,
                            "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "An error occured while opening the output file. ERROR MESSAGE = " + e, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        buttonRun.setText("Run algorithm");
        buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Play24.gif")));
        progressBar.setIndeterminate(false);
        comboBox.setEnabled(true);
        chckbxMaxSeconds.setEnabled(true);
        chckbxRunAsExternal.setEnabled(true);
    }

    /**
     * This method receives the notifications when an algorithm launched by the user
     * throw an exception
     */
    @Override
    public void uncaughtException(Thread thread, Throwable e) {
        // If the thread just die because the user click on the "Stop algorithm" button
        if (e instanceof ThreadDeath) {
            // we just let the thread die.
        } else if (e instanceof NumberFormatException) {
            // if it is a number format exception, meaning that the user enter a string as a
            // parameter instead
            // of an integer or double value.
            JOptionPane.showMessageDialog(null,
                    "Error. Please check the parameters of the algorithm.  The format for numbers is incorrect. \n"
                    + "\n ERROR MESSAGE = " + e,
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // If another kind of error occurred while running the algorithm, show the
            // error.
            JOptionPane.showMessageDialog(null,
                    "An error occurred while trying to run the algorithm. \n ERROR MESSAGE = " + e.toString(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        textArea.setText("");
    }

    /**
     * This method is called when the user click the "Run" or "Stop" button of the
     * user interface, to launch the chosen algorithm and thereafter catch exception
     * if one occurs.
     */
    private void processRunAlgorithmCommandFromGUI() {

        // If the algorithm is running, try to kill it
        boolean killed = tryToKillProcess();
        if (killed) {
            return;
        }

        // Get the parameters
        final String choice = (String) comboBox.getSelectedItem();

        // Get the current time
        SimpleDateFormat dateTimeInGMT = new SimpleDateFormat("hh:mm:ss aa");
        String time = dateTimeInGMT.format(new Date());

        textArea.setText("Algorithm is running... (" + time + ")  \n");

        progressBar.setIndeterminate(true);
        buttonRun.setText("Stop algorithm");
        buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Stop24.gif")));
        comboBox.setEnabled(false);
        chckbxMaxSeconds.setEnabled(false);
        chckbxRunAsExternal.setEnabled(false);

        // RUN THE SELECTED ALGORITHM in a new thread
        // create a thread to execute the algorithm
        if (PreferencesManager.getInstance().getRunAsExternalProgram()) {
            // If the algorithm is run as an external program
            currentRunningAlgorithmThread = new NotifyingThread() {
                @Override
                public boolean doRun() throws Exception {
                    List<String> commandWithParameters = new ArrayList<String>(15);
                    commandWithParameters.add("java");
                    commandWithParameters.add("-jar");
                    commandWithParameters.add("spmf.jar");
                    commandWithParameters.add("run");

                    commandWithParameters.add(choice);
                    if (inputFile != null) {
                        commandWithParameters.add(inputFile);
                    }
                    if (outputFile != null) {
                        commandWithParameters.add(outputFile);
                    }
                    if (textFieldParam1.getText() != null & !textFieldParam1.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam1.getText());
                    }
                    if (textFieldParam2.getText() != null & !textFieldParam2.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam2.getText());
                    }
                    if (textFieldParam3.getText() != null & !textFieldParam3.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam3.getText());
                    }
                    if (textFieldParam4.getText() != null & !textFieldParam4.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam4.getText());
                    }
                    if (textFieldParam5.getText() != null & !textFieldParam5.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam5.getText());
                    }
                    if (textFieldParam6.getText() != null & !textFieldParam6.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam6.getText());
                    }
                    if (textFieldParam7.getText() != null & !textFieldParam7.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam7.getText());
                    }
                    if (textFieldParam8.getText() != null & !textFieldParam8.getText().isEmpty()) {
                        commandWithParameters.add(textFieldParam8.getText());
                    }

                    // Call the JAR file to run the algorithm
                    System.out.println("===== RUN AS EXTERNAL PROGRAM ========");
                    StringBuffer singleLineCommand = new StringBuffer(80);
                    singleLineCommand.append(" COMMAND: ");
                    for (String value : commandWithParameters) {
                        singleLineCommand.append(value);
                        singleLineCommand.append(" ");
                    }
                    System.out.println(singleLineCommand);
                    ProcessBuilder pb = new ProcessBuilder(commandWithParameters);
                    pb.redirectOutput(Redirect.INHERIT);
                    pb.redirectError(Redirect.INHERIT);

                    int exitValue = 1;
                    try {
                        currentExternalProcess = pb.start();
                        exitValue = currentExternalProcess.waitFor();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        throw new IllegalArgumentException(
                                System.lineSeparator() + System.lineSeparator() + "I/O Error.");
                    }
                    return (exitValue == 0);
                }
            };
            // The main thread will listen for} the completion of the algorithm
            currentRunningAlgorithmThread.addListener(this);
            // The main thread will also listen for exception generated by the
            // algorithm.
            currentRunningAlgorithmThread.setUncaughtExceptionHandler(this);
            // Run the thread
            currentRunningAlgorithmThread.start();

        } else {

            final String[] parameters = new String[8];
            parameters[0] = textFieldParam1.getText();
            parameters[1] = textFieldParam2.getText();
            parameters[2] = textFieldParam3.getText();
            parameters[3] = textFieldParam4.getText();
            parameters[4] = textFieldParam5.getText();
            parameters[5] = textFieldParam6.getText();
            parameters[6] = textFieldParam7.getText();
            parameters[7] = textFieldParam8.getText();

            // If the algorithm is run as a thread
            currentRunningAlgorithmThread = new NotifyingThread() {
                @Override
                public boolean doRun() throws Exception {
                    CommandProcessor.runAlgorithm(choice, inputFile, outputFile, parameters);
                    return true;
                }
            };
            // The main thread will listen for the completion of the algorithm
            currentRunningAlgorithmThread.addListener(this);
            // The main thread will also listen for exception generated by the
            // algorithm.
            currentRunningAlgorithmThread.setUncaughtExceptionHandler(this);
            // Run the thread
            currentRunningAlgorithmThread.start();
        }

        // If the user set a max time limit, we launch a thread to monitor and kill the
        // process
        // if the time limit is exceeded
        if (chckbxMaxSeconds.isSelected()) {
            // get the maximum amount of itme
            maxTime = -1;
            try {
                maxTime = Integer.parseInt(textMaxSeconds.getText());
            } catch (NumberFormatException exception) {

            }
            // if the maximum amount of time is a valid number
            if (maxTime > 0) {
                // Create the killer thread
                NotifyingThread killerThread = new NotifyingThread() {
                    @Override
                    public boolean doRun() throws Exception {
                        int secondsElapsed = 0;

                        // While the algorithm is still running
                        while (currentRunningAlgorithmThread != null && currentRunningAlgorithmThread.isAlive()
                               || currentExternalProcess != null && currentExternalProcess.isAlive()) {

                            // wait one second
                            Thread.sleep(1000);

                            // increase number of seconds by 1
                            secondsElapsed++;

                            // If time is up
                            if (secondsElapsed >= maxTime) {

                                // Try to kill the algorithm
                                boolean killed = tryToKillProcess();
                                if (killed) {
                                    System.out.println(" Stopped because of time limit of " + maxTime + " seconds");
                                }
                            }
                        }
                        return false;
                    }
                };
                // Run the killer thread
                killerThread.start();
            }

        }
    }

    /**
     * Try to kill the current algorithm if it is running
     *
     * @return true if the algorithm is killed, otherwise false.
     */
    private boolean tryToKillProcess() {
        // if an external process is running
        if (currentExternalProcess != null && currentExternalProcess.isAlive()) {
            // stop that thread
            currentExternalProcess.destroyForcibly();

            textArea.setText("Algorithm stopped. \n");
            buttonRun.setText("Run algorithm");
            buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Play24.gif")));
            progressBar.setIndeterminate(false);
            comboBox.setEnabled(true);
            chckbxMaxSeconds.setEnabled(true);
            chckbxRunAsExternal.setEnabled(true);
            return true;
        }
        // If a thread is already running (the user click on the stop Button
        else if (currentRunningAlgorithmThread != null && currentRunningAlgorithmThread.isAlive()) {
            // stop that thread
            currentRunningAlgorithmThread.stop();

            textArea.setText("Algorithm stopped. \n");
            buttonRun.setText("Run algorithm");
            buttonRun.setIcon(new ImageIcon(MainWindow.class.getResource("/ca/pfv/spmf/gui/icons/Play24.gif")));
            progressBar.setIndeterminate(false);
            comboBox.setEnabled(true);
            chckbxMaxSeconds.setEnabled(true);
            chckbxRunAsExternal.setEnabled(true);
            return true;
        }
        return false;
    }

    static class TextAreaOutputStream extends OutputStream {

        JTextArea textArea;

        public TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void flush() {
            textArea.repaint();
        }

        public void write(int b) {
            textArea.append(new String(new byte[] { (byte) b }));
        }
    }
} // S P M F
