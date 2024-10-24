/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

/**
 * Represents a symbol in a grammar. Two symbols are equal if their literals match (ignoring the case).
 */
public class Symbol {

  /**
   * Literal for the symbol.
   */
  private final String literal;

  public Symbol(String literal) {
    this.literal = literal.trim();
  }

  /**
   * Comparison between symbol and literal.
   */
  public boolean is(String line) {
    return literal.trim().equals(line.trim());
  }

  @Override
  public String toString() {
    return literal;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Symbol)) {
      return false;
    }
    Symbol otherSymbol = (Symbol) other;
    return literal.equals(otherSymbol.literal);
  }
}
