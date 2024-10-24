/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.shapes;

import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a polygons with at least three points.
 */
public class Polygon2D extends Shape2D {

  /**
   * Corner points of the poly.
   */
  private List<Vector2f> points;

  public Polygon2D(Symbol symbol, Scope2D scope2D, Vector2f... points) {
    this(symbol, scope2D, Arrays.asList(points));
  }

  public Polygon2D(Symbol symbol, Scope2D scope2D, List<Vector2f> points) {
    super(symbol, scope2D);
    this.points = points;
  }

  public List<Vector2f> getPoints() {
    return points;
  }

  @Override
  public String toString() {
    return "Poly2D: (" + getSymbol().toString() + ")";
  }

  @Override
  public Vector2f computeSize() {
    Vector2f size = new Vector2f();
    for (Vector2f p : points) {
      if (p.x > size.x) {
        size.x = p.x;
      }
      if (p.y > size.y) {
        size.y = p.y;
      }
    }
    return size;
  }

  @Override
  public Shape2D makeCopy(Symbol symbol) {
    return new Polygon2D(symbol, new Scope2D(), points.stream().map(p -> new Vector2f(p)).collect(Collectors.toList()));
  }
}
