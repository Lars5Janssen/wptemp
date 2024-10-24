/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import com.google.common.base.Preconditions;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Rule;
import wpcg.base.grammar.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A rule in the CGAShape2D grammar.
 */
public class CGAShape2DRule implements Rule {

  /**
   * Predecessor symbol
   */
  private Symbol pred;

  /**
   * Operations that shall be applied.
   */
  private List<Operation> operations;

  public CGAShape2DRule(Symbol pred, List<Operation> operation) {
    Preconditions.checkNotNull(pred);
    Preconditions.checkNotNull(operation);
    this.pred = pred;
    this.operations = operation;
  }

  /**
   * Returns true if the rule can be applied to the given symbol.
   */
  public boolean canBeAppliedTo(Symbol symbol) {
    return pred.equals(symbol);
  }

  @Override
  public List<Symbol> getPred() {
    return Arrays.asList(pred);
  }

  @Override
  public String toString() {
    String res = pred + " --> ";
    for (Operation op : operations) {
      res += op.toString() + " ";
    }
    return res;
  }

  /**
   * Apply the operations to the pred. symbol.
   */
  public List<Shape2D> apply(Shape2D shape) throws GrammarException {
    Preconditions.checkArgument(canBeAppliedTo(shape.getSymbol()));
    List<Shape2D> shapes = new ArrayList<>();
    for (Operation op : operations) {
      shapes.addAll(op.apply(shape));
    }
    return shapes;
  }
}
