/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.shapes;

import com.google.common.base.Preconditions;
import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.grammar.Symbol;

/**
 * Represents a line from (0,0) -> (length,0)
 */
public class Line2D extends Shape2D {

  /**
   * Length of the line.
   */
  private float length;

  public Line2D(Symbol symbol, Scope2D scope, float length) {
    super(symbol, scope);
    Preconditions.checkArgument(length > 0);
    this.length = length;
  }

  public Vector2f getStart() {
    return new Vector2f(0, 0);
  }

  public Vector2f getEnd() {
    return new Vector2f(length, 0);
  }

  public float getLength() {
    return length;
  }

  @Override
  public String toString() {
    return "Line2D: (" + getSymbol().toString() + ")";
  }

  public Vector2f computeSize() {
    return new Vector2f(length, 0);
  }

  @Override
  public Shape2D makeCopy(Symbol symbol) {
    return new Line2D(symbol, new Scope2D(), length);
  }
}
