/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

import java.util.List;

/**
 * Shared interface for a rule in a grammar. A rule allows to derive a symbol or a list of
 * symbols in the grammar.
 */
public interface Rule {

  /**
   * Return the list of predecessors of the rule.
   */
  List<Symbol> getPred();
}
