package wpcg;

import wpcg.base.cgashape2d.CGAShape2DParameters;
import wpcg.base.cgashape2d.CGAShape2DTreeViewer;
import wpcg.base.cgashape2d.CGAShape2DViewer;
import wpcg.base.cgashape2d.shapes.Shape2D;
import wpcg.base.grammar.GrammarEditor;
import wpcg.base.grammar.GrammarException;
import wpcg.lab.a3.MyRendererScene;
import wpcg.lab.a2.HalfEdgeScene;
import wpcg.lab.a4.RasterizationScene;
import wpcg.lab.a6.KDTreeScene;
import wpcg.lab.a5.CurveScene;
import wpcg.lab.a7.LSystemScene2D;
import wpcg.base.misc.Logger;
import wpcg.ui.swing2D.CG2DApplication;

/**
 * Entry class for all 2d exercises.
 */
public class CGApp2D extends CG2DApplication {
    public CGApp2D() {
        super("HAW Hamburg: 2D and 3D Graphics");

        // Assignment 2
        var rendererScene = new MyRendererScene(600, 600);
        addPanel(rendererScene, rendererScene.getUserInterface(), "Rendering Pipepine");

        // Assignment 3
        var halfEdgeScene = new HalfEdgeScene(600, 600);
        //addScene2D(halfEdgeScene);

        // Assignment 4
        var rasterizationScene = new RasterizationScene(600, 600);
        //addPanel(rasterizationScene, rasterizationScene.getUserInterface(), "Rasterization");

        // Assignment 5
        var kdTreeScene = new KDTreeScene(600, 600);
        //addScene2D(kdTreeScene);

        // Assignment 6
        var curveScene = new CurveScene(600, 600);
        //addScene2D(curveScene);

        // Assignment 7
        var lSystemScene2D = new LSystemScene2D(600, 600);
        //addScene2D(lSystemScene2D);

        // 2D CGA Shape
        CGAShape2DParameters params = new CGAShape2DParameters();
        try {
            params.readGrammarFromFile("cgashape2d/basic.grammar");
        } catch (GrammarException e) {
            Logger.getInstance().error("Grammar error: " + e.getMessage());
        }
        var grammarEditor = new GrammarEditor<Shape2D>(params, "cgashape2d");
        CGAShape2DTreeViewer shapeTreeViewer = new CGAShape2DTreeViewer(params);
        CGAShape2DViewer viewer = new CGAShape2DViewer(600, 600, params);
        // Uncomment to use the 2D CGA shape implementation
        //addPanel(grammarEditor, "Grammar");
        //addPanel(shapeTreeViewer, "Shape Tree");
        //addPanel(viewer, "Result");
    }

    public static void main(String[] args) {
        new CGApp2D();
    }
}
