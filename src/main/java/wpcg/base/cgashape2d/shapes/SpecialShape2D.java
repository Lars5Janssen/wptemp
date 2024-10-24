/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.shapes;

import com.google.common.base.Preconditions;
import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;

/**
 * This shape can be used to represent details.
 */
public class SpecialShape2D extends Shape2D {

  /**
   * Currently implemented detail types.
   */
  public enum Type {ROOF, WINDOW, DOOR}

  /**
   * Type of the current shape.
   */
  private Type type;

  public SpecialShape2D(Symbol symbol, Scope2D scope2D, Type type) {
    super(symbol, scope2D);
    this.type = type;
  }

  /**
   * Helper method for the corner of a roof. Only valid of type == roof.
   */
  public List<Vector2f> getRoofPoints() {
    Preconditions.checkArgument(type == Type.ROOF);
    Line2D parentLine = (Line2D) getParent();
    Vector2f a = parentLine.getStart();
    Vector2f b = parentLine.getEnd();
    Vector2f d = b.subtract(a);
    Vector2f normal = new Vector2f(-d.y, d.x);
    Vector2f c = a.add(b).mult(0.5f).add(normal.mult(0.5f));
    return Arrays.asList(a, b, c);
  }

  public Type getType() {
    return type;
  }

  public Vector2f computeSize() {
    throw new IllegalArgumentException("computeSize() not implemented for SpecialShape2D");
  }

  @Override
  public Shape2D makeCopy(Symbol symbol) {
    return new SpecialShape2D(symbol, new Scope2D(), type);
  }
}
