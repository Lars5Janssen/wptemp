/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.shapes;

import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.grammar.Symbol;

/**
 * Represents the point (0,0)
 */
public class Point2D extends Shape2D {

  public Point2D(Symbol symbol, Scope2D scope2D) {
    super(symbol, scope2D);
  }

  @Override
  public String toString() {
    return "Point2D: (" + getSymbol().toString() + ")";
  }

  public Vector2f getPos() {
    return new Vector2f(0, 0);
  }

  public Vector2f computeSize() {
    return new Vector2f(0, 0);
  }

  @Override
  public Shape2D makeCopy(Symbol symbol) {
    return new Point2D(symbol, new Scope2D());
  }
}
