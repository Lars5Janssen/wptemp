/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.grammar;

import com.google.common.base.Preconditions;
import wpcg.base.model.Parameters;
import wpcg.base.misc.AssetPath;
import wpcg.base.misc.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Represents the parameters of an application using grammars.
 * T is used as the symbol type (right-hand-side of a rule).
 */
public class GrammarParameters<T> extends Parameters {

  /**
   * Parsed grammar
   */
  protected Grammar<T> grammar;

  /**
   * Parser used to convert from text -> grammar.
   */
  protected GrammarParser<T> parser;

  public GrammarParameters(Grammar<T> grammar, GrammarParser<T> parser) {
    Preconditions.checkNotNull(parser);
    Preconditions.checkNotNull(grammar);
    this.grammar = grammar;
    this.parser = parser;
  }

  /**
   * Read grammar from a grammar text file.
   */
  public void readGrammarFromFile(String grammarFilename) throws GrammarException {
    Preconditions.checkNotNull(grammar);
    String assetPath = AssetPath.getInstance().getPathToAsset(grammarFilename);
    if (assetPath == null) {
      Logger.getInstance().error("Failed to read grammar from " + grammarFilename);
      return;
    }
    try (FileReader reader = new FileReader(assetPath)) {
      BufferedReader br = new BufferedReader(reader);
      String text = "";
      String line;
      while ((line = br.readLine()) != null) {
        text += line + "\n";
      }
      readGrammarFromString(text);
      notifyAllObservers();
      System.out.println("Successfully read grammar from " + grammarFilename);
    } catch (IOException e) {
      throw new GrammarException("Failed to read grammar from " + grammarFilename);
    }
  }

  /**
   * Read grammar from a string.
   */
  public void readGrammarFromString(String grammarText) throws GrammarException {
    parser.parse(grammar, grammarText);
    // Initial derivation
    var result = grammar.derive(null);
    notifyAllObservers();
  }

  public String getGrammarText() {
    return grammar.toString();
  }

  public Grammar<T> getGrammar() {
    return grammar;
  }
}
