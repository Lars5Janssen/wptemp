/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import org.jetbrains.annotations.NotNull;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.cgashape2d.shapes.Line2D;
import wpcg.base.cgashape2d.shapes.Point2D;
import wpcg.base.cgashape2d.shapes.Polygon2D;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Splits the pred. shape into shapes of a lower dimension.
 */
public class OperationComponentSplit extends Operation {

  public static final String NAME = "COMPONENT_SPLIT";

  public OperationComponentSplit(String[] params, List<Symbol> succ) {
    super(succ);
  }

  @Override
  public List<Shape2D> apply(Shape2D shape) throws GrammarException {
    if (shape instanceof Line2D) {
      return applyToLine((Line2D) shape);
    } else if (shape instanceof Polygon2D) {
      return applyToPoly((Polygon2D) shape);
    }
    throw new GrammarException("Operation " + this + " does not support shape type of  " + shape);
  }

  @Override
  protected String paramsToString() {
    return "";
  }

  @NotNull
  private List<Shape2D> applyToPoly(Polygon2D shape) throws GrammarException {
    if (succ.size() != shape.getPoints().size()) {
      throw new GrammarException("Invalid number of successors in " + this);
    }
    List<Shape2D> lines = new ArrayList<>();
    for (int i = 0; i < shape.getPoints().size(); i++) {
      Vector2f a = shape.getPoints().get(i);
      Vector2f b = shape.getPoints().get((i + 1) % shape.getPoints().size());
      Vector2f x = a.subtract(b);
      float length = x.length();
      x = x.normalize();
      Vector2f y = new Vector2f(-x.y, x.x);
      Matrix3f coordSys = new Matrix3f(
              x.x, y.x, b.x,
              x.y, y.y, b.y,
              0, 0, 1);
      Scope2D scope = new Scope2D(coordSys);
      Shape2D line = new Line2D(succ.get(i), scope, length);
      lines.add(line);
    }
    return lines;
  }

  @NotNull
  private List<Shape2D> applyToLine(Line2D line2D) throws GrammarException {
    if (succ.size() != 2) {
      throw new GrammarException("Invalid number of successors in " + this);
    }
    Point2D a = new Point2D(succ.get(0), new Scope2D());
    Point2D b = new Point2D(succ.get(1), new Scope2D().createTranslatedCopy(new Vector2f(line2D.getLength(), 0)));
    return Arrays.asList(a, b);
  }

  @Override
  public String getName() {
    return NAME;
  }
}
