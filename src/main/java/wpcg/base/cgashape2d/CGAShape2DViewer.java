/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.cgashape2d;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import wpcg.base.cgashape2d.grammar.CGAShape2DGrammar;
import wpcg.base.cgashape2d.shapes.*;
import wpcg.base.grammar.Symbol;
import wpcg.base.math.Vectors;
import wpcg.base.misc.Logger;
import wpcg.base.misc.Observable;
import wpcg.base.misc.Observer;
import wpcg.ui.swing2D.Scene2D;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This viewer class is able to render a CGAShape shape tree
 */
public class CGAShape2DViewer extends Scene2D implements Observer {

  /**
   * Parameters with the grammar.
   */
  private CGAShape2DParameters parameters;

  /**
   * Flag to show only the leaf nodes (or all nodes).
   */
  private boolean showOnlyLeafs;

  public CGAShape2DViewer(int width, int height, CGAShape2DParameters parameters) {
    super(width, height, new Vector2f(-1, -1), new Vector2f(1, 1));
    parameters.addObserver(this);
    this.parameters = parameters;

    this.showOnlyLeafs = true;
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D graphics2D = (Graphics2D) g;
    graphics2D.clearRect(0, 0, getWidth(), getHeight());
    drawShape(graphics2D, getGrammar().getAxiom());
  }

  private CGAShape2DGrammar getGrammar() {
    return (CGAShape2DGrammar) parameters.getGrammar();
  }

  @Override
  public JPanel getUserInterface() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    JCheckBox cbOnlyLeafes = new JCheckBox("Show only leafs");
    cbOnlyLeafes.setSelected(showOnlyLeafs);
    cbOnlyLeafes.addChangeListener(e -> {
      showOnlyLeafs = cbOnlyLeafes.isSelected();
      repaint();
    });
    mainPanel.add(cbOnlyLeafes);

    return mainPanel;
  }

  /**
   * Draw a given shape.
   */
  private void drawShape(Graphics2D g2d, Shape2D shape) {
    if (!showOnlyLeafs || shape.isLeaf()) {
      if (shape instanceof Point2D) {
        drawPoint2D(g2d, (Point2D) shape);
      } else if (shape instanceof Line2D) {
        drawLine2D(g2d, (Line2D) shape);
      } else if (shape instanceof Polygon2D) {
        drawPoly2D(g2d, (Polygon2D) shape);
      } else if (shape instanceof SpecialShape2D) {
        drawSpecialShape(g2d, (SpecialShape2D) shape);
      }
    }
    for (Shape2D child : shape.getChildren()) {
      drawShape(g2d, child);
    }
  }

  /**
   * Drawing functionality for a special shape.
   */
  private void drawSpecialShape(Graphics2D g2d, SpecialShape2D shape) {
    if (shape.getType() == SpecialShape2D.Type.ROOF) {
      var points = shape.getRoofPoints();
      drawPoly(g2d, points.stream().map(p -> transform(p, shape)).collect(Collectors.toList()), Color.BLACK,
              getFillColor(shape));
    } else if (shape.getType() == SpecialShape2D.Type.WINDOW) {
      drawWindow(g2d, shape);
    } else if (shape.getType() == SpecialShape2D.Type.DOOR) {
      drawDoor(g2d, shape);
    } else {
      Logger.getInstance().error("Rendering not implemented yet for " + shape.getType());
    }
  }

  /**
   * Drawing functionality for a special shape: door.
   */
  private void drawDoor(Graphics2D g2d, SpecialShape2D shape) {
    Polygon2D parentPoly = (Polygon2D) shape.getParent();
    Vector2f size = parentPoly.computeSize();
    var framePoints = Arrays.asList(new Vector2f(0, 0), new Vector2f(0, size.y), new Vector2f(size.x, size.y),
            new Vector2f(size.x, 0));
    drawPoly(g2d, framePoints.stream().map(p -> transform(p, shape)).collect(Collectors.toList()), Color.BLACK,
            getFillColor(shape));
    float offset = Math.min(size.x, size.y) * 0.1f;
    var windowFramePoints = Arrays.asList(
            new Vector2f(offset, 0),
            new Vector2f(offset, size.y),
            new Vector2f(size.x - offset, size.y),
            new Vector2f(size.x - offset, 0));
    windowFramePoints = windowFramePoints.stream().map(p -> transform(p, shape)).collect(Collectors.toList());
    drawCubicCurve(g2d, windowFramePoints.get(0), windowFramePoints.get(1), windowFramePoints.get(2),
            windowFramePoints.get(3), Color.BLACK);
  }

  /**
   * Drawing functionality for a special shape: window.
   */
  private void drawWindow(Graphics2D g2d, SpecialShape2D shape) {
    Polygon2D parentPoly = (Polygon2D) shape.getParent();
    Vector2f size = parentPoly.computeSize();
    float offset = Math.min(size.x, size.y) * 0.2f;
    var framePoints = Arrays.asList(new Vector2f(0, 0), new Vector2f(size.x, 0), new Vector2f(size.x, size.y),
            new Vector2f(0, size.y));
    var windowFramePoints = Arrays.asList(new Vector2f(offset, offset), new Vector2f(size.x - offset, offset),
            new Vector2f(size.x - offset, size.y - offset), new Vector2f(offset, size.y - offset));
    Vector2f a = transform(new Vector2f(offset, size.y / 2), shape);
    Vector2f b = transform(new Vector2f(size.x - offset, size.y / 2), shape);
    Vector2f c = transform(new Vector2f(size.x / 2, offset), shape);
    Vector2f d = transform(new Vector2f(size.x / 2, size.y - offset), shape);

    drawPoly(g2d, framePoints.stream().map(p -> transform(p, shape)).collect(Collectors.toList()), Color.BLACK,
            getFillColor(shape));
    drawPoly(g2d, windowFramePoints.stream().map(p -> transform(p, shape)).collect(Collectors.toList()), Color.BLACK,
            getFillColor(shape));
    drawLine(g2d, a, b, Color.BLACK);
    drawLine(g2d, c, d, Color.BLACK);
  }

  /**
   * Provide a filling color based on the given shape.
   */
  private Color getFillColor(Shape2D shape) {
    if (shape.getSymbol().equals(new Symbol("Roof"))) {
      return Color.RED;
    }

    // Default
    return Color.LIGHT_GRAY;
  }

  /**
   * Drawing functionality for a point shape.
   */
  private void drawPoint2D(Graphics2D g2d, Point2D point) {
    drawPoint(g2d, transform(point.getPos(), point), Color.BLACK);
  }

  /**
   * Drawing functionality for a line shape.
   */
  private void drawLine2D(Graphics2D g2d, Line2D line) {
    drawLine(g2d, transform(line.getStart(), line), transform(line.getEnd(), line), Color.BLACK);
  }

  /**
   * Drawing functionality for a poly shape.
   */
  private void drawPoly2D(Graphics2D g2d, Polygon2D poly) {
    List<Vector2f> points = poly.getPoints();
    drawPoly(g2d, points.stream().map(p -> transform(p, poly)).collect(Collectors.toList()), Color.BLACK, getFillColor(poly));
  }

  /**
   * Transform the given point using the coordinate system of the shape.
   */
  private Vector2f transform(Vector2f p, Shape2D shape) {
    Matrix3f T = shape.getGlobalTransform();
    Vector2f res = Vectors.xy(T.mult(Vectors.xy1(p)));
    return res;
  }

  @Override
  public String getTitle() {
    return "View";
  }

  public void update(Observable sender, String descr, Object payload) {
    repaint();
  }
}
