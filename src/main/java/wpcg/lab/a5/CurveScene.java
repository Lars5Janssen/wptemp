/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a5;

import com.jme3.math.Vector2f;
import wpcg.ui.swing2D.Scene2D;

import javax.swing.*;
import java.awt.*;

/**
 * Experiment with Hermite curves and splines.
 */
public class CurveScene extends Scene2D {

    /**
     * Show/hide curve/spline.
     */
    protected boolean showCurve, showSpline;

    /**
     * Current t value - show tangent here.
     */
    protected float currentT;

    public CurveScene(int width, int height) {
        super(width, height,
                new Vector2f(-1, -1), new Vector2f(1, 1));
        showCurve = true;
        showSpline = true;
        currentT = 0.5f;
    }

    @Override
    public void paint(Graphics g) {
    }

    @Override
    public String getTitle() {
        return "Curves + Splines";
    }

    @Override
    public JPanel getUserInterface() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Curve
        JCheckBox cbShowCurve = new JCheckBox("Show curve", showCurve);
        panel.add(cbShowCurve);
        cbShowCurve.addActionListener(e -> {
            showCurve = cbShowCurve.isSelected();
            repaint();
        });

        // Spline
        JCheckBox cbShowSpline = new JCheckBox("Show spline", showSpline);
        panel.add(cbShowSpline);
        cbShowSpline.addActionListener(e -> {
            showSpline = cbShowSpline.isSelected();
            repaint();
        });

        // Current t
        JLabel labelT = new JLabel("t:");
        panel.add(labelT);
        JSlider sliderT = new JSlider(0, 100);
        sliderT.setValue((int) (currentT * 100));
        sliderT.addChangeListener(e -> {
            currentT = sliderT.getValue() / 100.0f;
            repaint();
        });
        panel.add(sliderT);

        return panel;
    }
}
