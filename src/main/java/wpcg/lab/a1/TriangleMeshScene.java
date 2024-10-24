/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */
package wpcg.lab.a1;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import wpcg.base.mesh.*;
import wpcg.ui.jmonkey.AbstractCameraController;
import wpcg.ui.jmonkey.Scene3D;

/**
 * This introduction scene is a 3d scene which shall be used to display a 3d mesh.
 */
public class TriangleMeshScene extends Scene3D {
    /**
     * Material properties - required for lighting.
     */
    protected static PhongMaterialProps defaultPhongMatProps = new PhongMaterialProps(
            ColorRGBA.DarkGray, ColorRGBA.White, ColorRGBA.DarkGray, 20.0f);

    @Override
    public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
        // Create a simple mesh consisting of a triangle.
        wpcg.base.mesh.TriangleMesh mesh = makeSimpleMesh();

        // Add mesh to scene graph
        Geometry node = JMonkeyTools.createGeometry(assetManager, mesh,
                defaultPhongMatProps, null, JMonkeyTools.Shading.PER_FACET);
        node.setShadowMode(RenderQueue.ShadowMode.Cast);
        rootNode.attachChild(node);

        // Adjust camera
        cameraController.adjustViewTo(mesh.getBoundingBox());
    }

    protected TriangleMesh makeSimpleMesh() {
        wpcg.base.mesh.TriangleMesh mesh = new wpcg.base.mesh.TriangleMesh();
        mesh.addVertex(new Vector3f(0, 0, 0));
        mesh.addVertex(new Vector3f(0, 1, 0));
        mesh.addVertex(new Vector3f(1, 1, 0));
        mesh.addTriangle(new Triangle(0, 1, 2));
        return mesh;
    }

    @Override
    public void update(float time) {
        // Unused here.
    }

    @Override
    public void render() {
        // Unused here.
    }

    @Override
    public String getTitle() {
        return "Triangle Mesh";
    }
}
