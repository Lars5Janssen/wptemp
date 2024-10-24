/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

import wpcg.base.misc.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * A grammar contains a list of Rules which can be used to derive symbols.
 * T is used as the symbol type (right-hand-side of a rule).
 */
public abstract class Grammar<T> extends Observable {

  /**
   * List of rules in the grammar.
   */
  protected List<Rule> rules = new ArrayList<>();

  /**
   * Derive the axiom, return a list of resulting symbols.
   */
  public abstract List<T> derive(Symbol symbol) throws GrammarException;

  public void clearRules() {
    rules.clear();
  }

  public void addRule(Rule rule) {
    rules.add(rule);
  }

  @Override
  public String toString() {
    String res = "";
    for (Rule rule : rules) {
      res += rule + "\n";
    }
    return res;
  }
}
