/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.cgashape2d.shapes.Line2D;
import wpcg.base.cgashape2d.shapes.Polygon2D;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.cgashape2d.shapes.SpecialShape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;

/**
 * This Operations allows the insert details instead of the pred. shape.
 */
public class OperationSpecial extends Operation {

  public static final String NAME = "SPECIAL";

  private SpecialShape2D.Type type;

  public OperationSpecial(String[] params, List<Symbol> succ) throws GrammarException {
    super(succ);
    if (params.length != 1) {
      throw new GrammarException("Invalid params in " + this + ": " + params);
    }
    if (params.length != 1) {
      throw new GrammarException("Invalid params in " + this + ": " + Arrays.stream(params).reduce("", (p1, p2) -> p1 +
              " " + p2));
    }
    try {
      type = SpecialShape2D.Type.valueOf(params[0].toUpperCase());
    } catch (Exception e) {
      throw new GrammarException("Invalid params in " + this + ": " + Arrays.stream(params).reduce("", (p1, p2) -> p1 +
              " " + p2));
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public List<Shape2D> apply(Shape2D shape) throws GrammarException {
    if (succ.size() != 1) {
      throw new GrammarException("Grammar error: invalid number of successors in " + this);
    }
    if (shape instanceof Line2D && type == SpecialShape2D.Type.ROOF) {
      return Arrays.asList(new SpecialShape2D(succ.get(0), new Scope2D(), SpecialShape2D.Type.ROOF));
    } else if (shape instanceof Polygon2D && type == SpecialShape2D.Type.WINDOW) {
      return Arrays.asList(new SpecialShape2D(succ.get(0), new Scope2D(), SpecialShape2D.Type.WINDOW));
    } else if (shape instanceof Polygon2D && type == SpecialShape2D.Type.DOOR) {
      return Arrays.asList(new SpecialShape2D(succ.get(0), new Scope2D(), SpecialShape2D.Type.DOOR));
    }

    throw new GrammarException("Operation " + this + " cannot be applied to " + shape);
  }

  @Override
  protected String paramsToString() {
    return type != null ? type.toString() : "null";
  }
}
