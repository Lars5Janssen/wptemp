/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.google.common.base.Preconditions;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;

/**
 * The identity operations generates a copy of the pred. shape.
 */
public class OperationIdentity extends Operation {

  public static final String NAME = "IDENTITY";

  public OperationIdentity(List<Symbol> succ) {
    super(succ);
  }

  @Override
  public List<Shape2D> apply(Shape2D shape) {
    Preconditions.checkArgument(succ.size() == 1);
    return Arrays.asList(shape.makeCopy(succ.get(0)));
  }

  @Override
  protected String paramsToString() {
    return null;
  }

  @Override
  public String getName() {
    return NAME;
  }
}
