/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.cgashape2d.shapes.Line2D;
import wpcg.base.cgashape2d.shapes.Point2D;
import wpcg.base.cgashape2d.shapes.Polygon2D;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;

/**
 * Extrudes the pred. shape to a shape with dimension + 1.
 */
public class OperationExtrude extends Operation {

  public static final String NAME = "EXTRUDE";

  /**
   * Length of the extrusion.
   */
  private float value;

  public OperationExtrude(String[] params, List<Symbol> succ) throws GrammarException {
    super(succ);
    if (params == null || params.length != 1) {
      throw new GrammarException("Invalid params in " + this + ": " + params);
    }
    try {
      value = Float.parseFloat(params[0]);
    } catch (NumberFormatException e) {
      throw new GrammarException("Failed to parse param in " + this + ": " + params);
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public List<Shape2D> apply(Shape2D shape) throws GrammarException {
    if (succ.size() != 1) {
      throw new GrammarException("Invalid number of successors in " + this);
    }
    if (shape instanceof Point2D) {
      Line2D line2D = new Line2D(succ.get(0), new Scope2D(), value);
      return Arrays.asList(line2D);
    } else if (shape instanceof Line2D) {
      Line2D line2D = (Line2D) shape;
      Polygon2D polygon2D = new Polygon2D(succ.get(0), new Scope2D(), line2D.getStart(),
              line2D.getEnd(), line2D.getEnd().add(new Vector2f(0, value)),
              line2D.getStart().add(new Vector2f(0, value)));
      return Arrays.asList(polygon2D);
    }

    throw new GrammarException("Operation " + this + " does not support shape type of  " + shape);
  }

  @Override
  protected String paramsToString() {
    return String.valueOf(value);
  }
}
