/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d;

import wpcg.base.cgashape2d.grammar.CGAShape2DGrammar;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.misc.Observable;
import wpcg.base.misc.Observer;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

/**
 * A simple tree viewer to visualize a shape tree in a CGAShape grammar.
 */
public class CGAShape2DTreeViewer extends JPanel implements Observer {

  /**
   * Parameters containing the grammar.
   */
  private CGAShape2DParameters params;

  /**
   * This is the tree model, which is displayed.
   */
  private JTree tree;

  /**
   * Root node of the tree model.
   */
  private DefaultMutableTreeNode root;

  public CGAShape2DTreeViewer(CGAShape2DParameters params) {
    this.params = params;
    params.addObserver(this);

    root = new DefaultMutableTreeNode("Root");
    CGAShape2DGrammar grammar = getGrammar();
    buildTree(root, grammar.getAxiom());
    tree = new JTree(root);
    expandTree();

    tree.setPreferredSize(new Dimension(600, 600));
    add(new JScrollPane(tree));

    setSize(600, 600);
  }

  /**
   * Expand all children.
   */
  private void expandTree() {
    for (int i = 0; i < tree.getRowCount(); i++) {
      tree.expandRow(i);
    }
  }

  /**
   * Build a tree based on the axiom shape tree in the grammar.
   */
  private void buildTree(DefaultMutableTreeNode node, Shape2D shape) {
    node.setUserObject(shape);
    for (Shape2D childShape : shape.getChildren()) {
      DefaultMutableTreeNode childNode = new DefaultMutableTreeNode();
      node.add(childNode);
      buildTree(childNode, childShape);
    }
  }

  private CGAShape2DGrammar getGrammar() {
    return (CGAShape2DGrammar) params.getGrammar();
  }

  public void update(Observable sender, String descr, Object payload) {
    root.removeAllChildren();
    buildTree(root, getGrammar().getAxiom());
    tree.updateUI();
    expandTree();
  }
}
