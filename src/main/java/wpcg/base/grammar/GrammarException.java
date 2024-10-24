/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

/**
 * Indicator that something went wrong with using the grammar.
 */
public class GrammarException extends Exception {
  public GrammarException(String message) {
    super(message);
  }
}
