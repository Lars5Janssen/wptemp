/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d.grammar;

import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.Grammar;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.GrammarParser;
import wpcg.base.grammar.Symbol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A parser for the CGAShape2D grammar.
 */
public class CGAShape2DParser implements GrammarParser<Shape2D> {

  @Override
  public void parse(Grammar<Shape2D> grammar, String grammarText) throws GrammarException {
    try (Reader reader = new StringReader(grammarText)) {
      BufferedReader br = new BufferedReader(reader);
      String line;
      grammar.clearRules();
      while ((line = br.readLine()) != null) {
        line = line.trim();
        if (line.startsWith("#")) {
          continue;
        }
        parseLine(grammar, line.trim());
      }
    } catch (IOException e) {
      throw new GrammarException("Failed to parseFile rule file.");
    }
  }

  /**
   * Parse a line in the grammar file. Detect and read rule content via reg expressions.
   */
  private void parseLine(Grammar<Shape2D> grammar, String line) throws GrammarException {
    line = line.trim();
    if (line.isEmpty()) {
      return;
    }

    String opNameRegex = "(?<opName>\\w+)";
    String opParamsRegex = "\\((?<opParams>[\\w\\.,]*)\\)";
    String opSuccRegex = "\\{(?<opSucc>[\\w,]*)\\}";
    String operationRegex = opNameRegex + "\\s*" + opParamsRegex + "\\s*" + opSuccRegex + "\\s*";
    String predRegex = "(?<pred>\\w+)";
    String ruleRegex = predRegex + "\\s*-->\\s*" + "(" + operationRegex + ")+";
    Pattern patternRule = Pattern.compile(ruleRegex);
    Matcher matcherRule = patternRule.matcher(line);
    if (matcherRule.matches()) {
      String pred = matcherRule.group("pred").trim();
      List<Operation> operations = new ArrayList<>();
      Matcher opMatcher = Pattern.compile(operationRegex).matcher(line);
      int lastFound = 0;
      while (opMatcher.find(lastFound)) {
        int start = opMatcher.start();
        int end = opMatcher.end();
        lastFound = end;
        String opString = line.substring(start, end);
        var op = parseOperation(opString, operationRegex, pred);
        if (op.isPresent()) {
          operations.add(op.get());
        }
      }
      if (operations.size() > 0) {
        var rule = new CGAShape2DRule(new Symbol(pred), operations);
        grammar.addRule(rule);
        return;
      }
    }
    throw new GrammarException("Failed to parse " + line);
  }

  /**
   * Parse an operation from the extracted sub-information.
   */
  private Optional<Operation> parseOperation(String opString, String operationRegex, String pred) throws GrammarException {
    Pattern patternOperation = Pattern.compile(operationRegex);
    Matcher matcherOperation = patternOperation.matcher(opString);
    if (matcherOperation.matches()) {
      String op = matcherOperation.group("opName").trim();
      String paramsString = matcherOperation.group("opParams").trim();
      String succString = matcherOperation.group("opSucc").trim();
      String[] params = paramsString.trim().split(",");
      String[] succ = succString.trim().split(",");
      return makeOperation(pred, op, params, succ);
    }
    return Optional.empty();
  }

  /**
   * Generate an operations from the parsed information.
   */
  private Optional<Operation> makeOperation(String pred, String op, String[] params, String[] succ) throws GrammarException {
    List<Symbol> successors = Arrays.stream(succ).map(s -> new Symbol(s)).collect(Collectors.toList());
    return switch (op.toUpperCase()) {
      case OperationExtrude.NAME -> Optional.of(new OperationExtrude(params, successors));
      case OperationSplit.NAME -> Optional.of(new OperationSplit(params, successors));
      case OperationComponentSplit.NAME -> Optional.of(new OperationComponentSplit(params, successors));
      case OperationSpecial.NAME -> Optional.of(new OperationSpecial(params, successors));
      case OperationIdentity.NAME -> Optional.of(new OperationIdentity(successors));
      default -> Optional.empty();
    };
  }
}
