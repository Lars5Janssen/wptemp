package wpcg.base.cgashape2d;

import org.junit.jupiter.api.Test;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarException;
import wpcg.base.grammar.Symbol;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCGAShape2D {

  @Test
  public void testCgaShape2D() {
    CGAShape2DParameters params = new CGAShape2DParameters();
    try {
      params.readGrammarFromFile("cgashape2d/basic.grammar");
    } catch (GrammarException e) {
      assertTrue(false);
    }
    List<Shape2D> word = null;
    try {
      word = params.getGrammar().derive(new Symbol("Origin"));
    } catch (GrammarException e) {
      assertTrue(false);
    }
    assertNotNull(word);
    assertTrue(word.size() > 0 && word.get(0) != null);
    assertTrue(word.get(0).getNumShapesInTree() > 10);
  }
}
