/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.google.common.base.Preconditions;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;

import java.util.List;

/**
 * Base class for an operation in the CGAShape2D grammar.
 */
public abstract class Operation {

  /**
   * List of successors in the operation.
   */
  protected List<Symbol> succ;

  public Operation(List<Symbol> succ) {
    Preconditions.checkNotNull(succ);
    this.succ = succ;
  }

  /**
   * Apply the operation to the shape using the successor ids
   */
  public abstract List<Shape2D> apply(Shape2D shape) throws GrammarException;

  @Override
  public String toString() {
    String res = getName() + "(" + paramsToString() + "){";
    for (int i = 0; i < succ.size(); i++) {
      res += succ.get(i);
      if (i < succ.size() - 1) {
        res += ",";
      }
    }
    res += "}";
    return res;
  }

  /**
   * Return a comma-separated string with all params
   */
  protected abstract String paramsToString();

  public abstract String getName();
}
