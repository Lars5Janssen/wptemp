/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

/**
 * Shared interface for all grammar parsers. A parser is able to read the grammar content from a
 * text file.
 */
public interface GrammarParser<T> {

  /**
   * Parse a grammar text to generate a grammar from it.
   */
  void parse(Grammar<T> grammar, String grammarText) throws GrammarException;
}
