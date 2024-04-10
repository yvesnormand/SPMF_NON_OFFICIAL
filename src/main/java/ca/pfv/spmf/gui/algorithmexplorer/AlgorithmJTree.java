package ca.pfv.spmf.gui.algorithmexplorer;

import ca.pfv.spmf.algorithmmanager.AlgorithmManager;
import ca.pfv.spmf.algorithmmanager.DescriptionOfAlgorithm;
import ca.pfv.spmf.algorithmmanager.DescriptionOfParameter;

import javax.swing.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.List;

/**
 * JTree that displays the algorithms offered in SPMF
 *
 * @author Philippe Fournier-Viger
 */
public class AlgorithmJTree extends JTree {

    /**
     *
     */
    private static final long serialVersionUID = -4896552831587301080L;
    /**
     * indicate if the highlight function is activated for the JTree
     */
    boolean activatedHighlight = false;

    public AlgorithmJTree(boolean showTools, boolean showAlgorithms, boolean showExperimentTools) {
        // Create the tree root
        super(new DefaultMutableTreeNode("Root"));
        this.setRootVisible(false);
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getModel().getRoot();

        DefaultTreeModel defaultTreeModel = new DefaultTreeModel(rootNode);
        setModel(defaultTreeModel);

        // Get the algorithm manager
        AlgorithmManager manager = null;
        try {
            manager = AlgorithmManager.getInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // -----------------------------------------------------------------------------------

        // Get the list of algorithms or categories
        List<String> algorithmList = manager.getListOfAlgorithmsAsString(showTools, showAlgorithms,
                showExperimentTools);
        DefaultMutableTreeNode categoryNode = null;

        // For each algorithm or category
        for (String algorithmOrCategoryName : algorithmList) {
            // If a new category, create a node for the category
            if (manager.getDescriptionOfAlgorithm(algorithmOrCategoryName) == null) {
                AlgoNode newNode = new AlgoNode(algorithmOrCategoryName, true);
                categoryNode = new DefaultMutableTreeNode(newNode);

                addNodeToDefaultTreeModel(defaultTreeModel, rootNode, categoryNode);
            } else {
                AlgoNode newNode = new AlgoNode(algorithmOrCategoryName, false);
                // Otherwise, create an algorithm node for the current category
                addNodeToDefaultTreeModel(defaultTreeModel, categoryNode, new DefaultMutableTreeNode(newNode));
            }
        }

//        //create the child nodes
//        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
//
//        vegetableNode.add(new DefaultMutableTreeNode("Carrot"));
//        vegetableNode.add(new DefaultMutableTreeNode("Tomato"));
//        vegetableNode.add(new DefaultMutableTreeNode("Potato"));
//
//        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
//        fruitNode.add(new DefaultMutableTreeNode("Banana"));
//        fruitNode.add(new DefaultMutableTreeNode("Mango"));
//        fruitNode.add(new DefaultMutableTreeNode("Apple"));
//        fruitNode.add(new DefaultMutableTreeNode("Grapes"));
//        fruitNode.add(new DefaultMutableTreeNode("Orange"));
//        //add the child nodes to the root node
//        root.add(vegetableNode);
//        root.add(fruitNode);

//        ImageIcon imageIcon = new ImageIcon(TreeExample.class.getResource("/leaf.jpg"));
        // set the renderer for the tree so as to be able to highlight tree node
        this.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
                                                                   boolean expanded, boolean leaf, int row, boolean hasFocus) {
                JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                        hasFocus);
                // Special case for the root
                if (((DefaultMutableTreeNode) value).getUserObject() instanceof String) {
                    return label;
                }
                // The case of other nodes
                AlgoNode node = (AlgoNode) ((DefaultMutableTreeNode) value).getUserObject();
                if (activatedHighlight && node.isHighlight()) {
                    label.setForeground(java.awt.Color.BLUE);
                } else {
                    label.setForeground(java.awt.Color.BLACK);
                }

                return label;
            }
        });
//        renderer.setLeafIcon(imageIcon);

        this.setShowsRootHandles(true);

//        setLayout(new BorderLayout());

        this.setVisible(true);
    }

    private static void addNodeToDefaultTreeModel(DefaultTreeModel treeModel, DefaultMutableTreeNode parentNode,
                                                  DefaultMutableTreeNode node) {

        treeModel.insertNodeInto(node, parentNode, parentNode.getChildCount());

        if (parentNode == treeModel.getRoot()) {
            treeModel.nodeStructureChanged((TreeNode) treeModel.getRoot());
        }
    }

    public boolean isActivatedHighlight() {
        return activatedHighlight;
    }
//    private JLabel selectedLabel;

    public void setActivatedHighlight(boolean activatedHighlight) {
        this.activatedHighlight = activatedHighlight;
        repaint();
    }

    public void addTreeSelectionListener(TreeSelectionListener listener) {
        addTreeSelectionListener(listener);
    }

    public void highlightSimilarAlgorithmsToSelection(boolean withParameters) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        String algoName = selectedNode.getUserObject().toString();
        // Get the algorithm manager
        AlgorithmManager manager = null;
        try {
            manager = AlgorithmManager.getInstance();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        DescriptionOfAlgorithm descriptionSelected = manager.getDescriptionOfAlgorithm(algoName);

        Enumeration<TreeNode> e = ((DefaultMutableTreeNode) getModel().getRoot()).depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node != getModel().getRoot()) {
                AlgoNode algonode = (AlgoNode) node.getUserObject();
                algonode.setHighlight(false);

                if (!algonode.isCategory()) {
                    DescriptionOfAlgorithm descriptionCurrent = manager.getDescriptionOfAlgorithm(algonode.getName());
                    boolean sameInput = same(descriptionCurrent.getInputFileTypes(), descriptionSelected.getInputFileTypes());
                    boolean sameOutput = same(descriptionCurrent.getOutputFileTypes(), descriptionSelected.getOutputFileTypes());

                    if (sameInput && sameOutput) {
                        if (!withParameters) {
                            algonode.setHighlight(true);
                        } else {
                            boolean sameParameters = sameMandatoryParameter(descriptionCurrent.getParametersDescription(),
                                    descriptionSelected.getParametersDescription(),
                                    descriptionCurrent.getNumberOfMandatoryParameters(),
                                    descriptionSelected.getNumberOfMandatoryParameters());
                            if (sameParameters) {
                                algonode.setHighlight(true);
                            }
                        }
                    }

                }
//				System.out.println(algonode.getName());
            }
        }
        setActivatedHighlight(true);
    }

    private boolean sameMandatoryParameter(DescriptionOfParameter[] parametersDescription,
                                           DescriptionOfParameter[] parametersDescription2, int numberMandatoryParameters,
                                           int numberMandatoryParameters2) {
        if (numberMandatoryParameters != numberMandatoryParameters2) {
            return false;
        }
        for (int i = 0; i < numberMandatoryParameters; i++) {
            if (!parametersDescription[i].getName().equals(parametersDescription2[i].getName())) {
                return false;
            }
        }

        return true;
    }

    private boolean same(String[] outputFileTypes, String[] outputFileTypes2) {
        if (outputFileTypes == null && outputFileTypes2 == null) {
            return true;
        }
        if (outputFileTypes == null) {
            return false;
        }
        if (outputFileTypes2 == null) {
            return false;
        }
        if (outputFileTypes.length != outputFileTypes2.length) {
            return false;
        }
        for (int i = 0; i < outputFileTypes.length; i++) {
            if (!outputFileTypes[i].equals(outputFileTypes2[i])) {
                return false;
            }
        }

        return true;
    }

    class AlgoNode {
        final String name;
        boolean isHighlight;
        boolean isCategory;

        AlgoNode(String name, boolean isCategory) {
            this.name = name;
            isHighlight = false;
            this.isCategory = isCategory;
        }

        public boolean isHighlight() {
            return isHighlight;
        }

        public void setHighlight(boolean isHighlight) {
            this.isHighlight = isHighlight;
        }

        public boolean isCategory() {
            return isCategory;
        }

        public void setCategory(boolean isCategory) {
            this.isCategory = isCategory;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }
    }

}