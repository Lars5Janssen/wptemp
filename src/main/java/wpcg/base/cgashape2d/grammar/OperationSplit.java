/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.jme3.math.Vector2f;
import org.jetbrains.annotations.NotNull;
import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.cgashape2d.shapes.Line2D;
import wpcg.base.cgashape2d.shapes.Polygon2D;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;
import wpcg.base.math.MathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This operation partitions the pred. shape into subshapes of the same type.
 */
public class OperationSplit extends Operation {

  public static final String NAME = "SPLIT";

  /**
   * Splitting is allowed in x and y direction.
   */
  public enum SplitDir {X, Y}

  /**
   * Splitting direction of this operations.
   */
  private SplitDir splitDir;

  /**
   * Contains the lengths (can also be relative) of the subshapes.
   */
  private List<String> values;

  public OperationSplit(String[] params, List<Symbol> succ) throws GrammarException {
    super(succ);
    values = new ArrayList<>();
    if (params == null || params.length < 2) {
      throw new GrammarException("Invalid params in " + this + ": " + params);
    }
    try {
      splitDir = SplitDir.valueOf(params[0].toUpperCase());
      for (int i = 1; i < params.length; i++) {
        values.add(params[i]);
      }
    } catch (Exception e) {
      throw new GrammarException("Invalid params in " + this + ": " + params);
    }
  }

  @Override
  public List<Shape2D> apply(Shape2D shape) throws GrammarException {
    if (values.size() != succ.size()) {
      throw new GrammarException("Invalid number of successors in " + this);
    }
    if (shape instanceof Line2D) {
      return applyLine((Line2D) shape);
    } else if (shape instanceof Polygon2D) {
      return applyPoly((Polygon2D) shape);
    } else {
      throw new GrammarException("Operation " + this + " cannot be applied to shape " + shape);
    }
  }

  @Override
  protected String paramsToString() {
    String res = splitDir != null ? splitDir.toString() : "null";
    for (String v : values) {
      res += "," + v;
    }
    return res;
  }

  @NotNull
  private List<Shape2D> applyPoly(Polygon2D poly) throws GrammarException {
    Vector2f size = poly.computeSize();
    List<Float> subvalues = createSubvalues(splitDir == SplitDir.X ? size.x : size.y);
    float lower = 0;
    float upper = 0;
    List<Shape2D> subPolys = new ArrayList<>();
    for (int i = 0; i < subvalues.size(); i++) {
      lower = upper;
      upper += subvalues.get(i);
      final float l = lower;
      final float u = upper;
      var newPoints = poly.getPoints()
              .stream()
              .map(p -> clip(p, l, u))
              .collect(Collectors.toList());
      Scope2D scope = new Scope2D().createTranslatedCopy(new Vector2f(
              splitDir == SplitDir.X ? lower : 0,
              splitDir == SplitDir.Y ? lower : 0));
      Polygon2D subPoly = new Polygon2D(succ.get(i), scope, newPoints);
      subPolys.add(subPoly);
    }
    return subPolys;
  }

  private Vector2f clip(Vector2f p, float lower, float upper) {
    if (splitDir == SplitDir.X) {
      return new Vector2f(Math.min(Math.max(p.x, lower), upper) - lower, p.y);
    } else {
      return new Vector2f(p.x, Math.min(Math.max(p.y, lower), upper) - lower);
    }
  }

  @NotNull
  private List<Shape2D> applyLine(Line2D shape) throws GrammarException {
    if (splitDir != SplitDir.X) {
      throw new GrammarException("Line only supports splitting along x-direction.");
    }
    Line2D line = shape;
    List<Float> subvalues = createSubvalues(line.getLength());
    List<Shape2D> sublines = new ArrayList<>();
    float accumulatedValue = 0;
    for (int i = 0; i < values.size(); i++) {
      Scope2D scope = line.getScope().createTranslatedCopy(new Vector2f(accumulatedValue, 0));
      Line2D subline = new Line2D(succ.get(i), scope, subvalues.get(i));
      sublines.add(subline);
      accumulatedValue += subvalues.get(i);
    }
    return sublines;
  }

  /**
   * Create the absolute sub values fiven the lenght of a line
   */
  private List<Float> createSubvalues(float length) throws GrammarException {
    try {
      float sumRelative = 0;
      float sumAbsolute = 0;
      for (String s : values) {
        if (s.endsWith("r")) {
          float v = Float.parseFloat(s.substring(0, s.length() - 1));
          sumRelative += v;
        } else {
          float v = Float.parseFloat(s);
          sumAbsolute += v;
        }
      }

      if (sumAbsolute > length + MathHelper.TOLERANCE) {
        throw new GrammarException("createSubvalues(): sum of absolute values is larger than length");
      }

      float r = (length - sumAbsolute) / sumRelative;
      List<Float> subvalues = new ArrayList<>();
      for (String s : values) {
        if (s.endsWith("r")) {
          float v = Float.parseFloat(s.substring(0, s.length() - 1));
          subvalues.add(v * r);
        } else {
          float v = Float.parseFloat(s);
          subvalues.add(v);
        }
      }
      return subvalues;

    } catch (NumberFormatException e) {
      throw new GrammarException("Failed to parse splitting values");
    }
  }

  @Override
  public String getName() {
    return NAME;
  }
}
