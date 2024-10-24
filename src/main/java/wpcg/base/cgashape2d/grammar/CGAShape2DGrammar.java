/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import wpcg.base.cgashape2d.Scope2D;
import wpcg.base.cgashape2d.shapes.Point2D;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.Grammar;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Rule;
import wpcg.base.grammar.Symbol;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A grammar for 2D shapes.
 */
public class CGAShape2DGrammar extends Grammar<Shape2D> {

  /**
   * The axiom of the grammar is a point.
   */
  private Shape2D axiom = new Point2D(new Symbol("Origin"), new Scope2D());

  @Override
  public List<Shape2D> derive(Symbol symbol) throws GrammarException {
    axiom.clearChildren();
    derive(axiom);
    return Arrays.asList(axiom);
  }

  /**
   * Derive a symbol (recursively) using the grammar rules.
   */
  private void derive(Shape2D shape2D) throws GrammarException {
    Optional<CGAShape2DRule> rule = getRuleFor(shape2D.getSymbol());
    if (rule.isPresent()) {
      List<Shape2D> childShapes = rule.get().apply(shape2D);
      for (Shape2D child : childShapes) {
        shape2D.addChild(child);
      }
      for (Shape2D child : childShapes) {
        derive(child);
      }
    }
    notifyAllObservers();
  }

  /**
   * Find a fitting rule for a given symbol.
   */
  private Optional<CGAShape2DRule> getRuleFor(Symbol symbol) {
    for (Rule r : rules) {
      CGAShape2DRule rule = (CGAShape2DRule) r;
      if (rule.canBeAppliedTo(symbol)) {
        return Optional.of(rule);
      }
    }
    return Optional.empty();
  }

  public Shape2D getAxiom() {
    return axiom;
  }

  /**
   * Returns the number of shapes in the shape tree below the axiom.
   */
  public int getNumShapes() {
    return getNumShapes(axiom);
  }

  /**
   * Recursive helper method to return the number of shapes in the subtree.
   */
  private int getNumShapes(Shape2D shape2D) {
    int num = 1;
    for (Shape2D child : shape2D.getChildren()) {
      num += getNumShapes(child);
    }
    return num;
  }
}
