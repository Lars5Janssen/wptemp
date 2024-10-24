/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.shapes;

import com.google.common.base.Preconditions;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.grammar.Symbol;

import java.util.HashSet;
import java.util.Set;

/**
 * Base class for all 2D shapes.
 */
public abstract class Shape2D {

  /**
   * The scope represents the local coordinate sytem.
   */
  private Scope2D scope2D;

  /**
   * This is the symbol of the shape.
   */
  private Symbol symbol;

  /**
   * Child shapes in the shape tree after derivation.
   */
  private Set<Shape2D> children;

  /**
   * Referent to the parent shape in the shape tree. null for the axiom.
   */
  private Shape2D parent;

  public Shape2D(Symbol symbol, Scope2D scope2D) {
    Preconditions.checkNotNull(symbol);
    Preconditions.checkNotNull(scope2D);
    this.symbol = symbol;
    this.scope2D = scope2D;
    children = new HashSet<>();
    parent = null;
  }

  public void addChild(Shape2D child) {
    Preconditions.checkNotNull(child);
    if (child != null) {
      children.add(child);
      child.parent = this;
    }
  }

  public Symbol getSymbol() {
    return symbol;
  }

  public void clearChildren() {
    children.clear();
  }

  /**
   * Return the global transformation of the shape be including the shapes of all parents in the tree.
   */
  public Matrix3f getGlobalTransform() {
    if (parent == null) {
      return scope2D.getCoordSys();
    } else {
      return parent.getGlobalTransform().mult(scope2D.getCoordSys());
    }
  }

  public Scope2D getScope() {
    return scope2D;
  }

  public Set<Shape2D> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return symbol.toString();
  }

  public boolean isLeaf() {
    return children.size() == 0;
  }

  public Shape2D getParent() {
    return parent;
  }

  /**
   * Compute the extend of the shape within its local coordinate system.
   */
  public abstract Vector2f computeSize();

  public abstract Shape2D makeCopy(Symbol symbol);

  /**
   * Helper method to compute the number of shapes in the subtree with this as root node.
   */
  public int getNumShapesInTree() {
    int number = 0;
    for (Shape2D child : children) {
      number += child.getNumShapesInTree();
    }
    return 1 + number;
  }

}
