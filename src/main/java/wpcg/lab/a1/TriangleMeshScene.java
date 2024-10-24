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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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


    protected Vector3f calcNormal(wpcg.base.mesh.TriangleMesh m, Triangle t) {
        Vertex v1 = m.getVertex(t.getVertexIndex(0));
        Vertex v2 = m.getVertex(t.getVertexIndex(1));
        Vertex v3 = m.getVertex(t.getVertexIndex(2));

        Vector3f vv1 = m.getVertex(t.getVertexIndex(0)).getPosition();
        Vector3f vv2 = m.getVertex(t.getVertexIndex(1)).getPosition();
        Vector3f vv3 = m.getVertex(t.getVertexIndex(2)).getPosition();

        Vector3f a = vv1.subtract(vv2);
        Vector3f b = vv3.subtract(vv1);

        return a.cross(b);

    }
    protected TriangleMesh makeSimpleMesh() {
        wpcg.base.mesh.TriangleMesh mesh = new wpcg.base.mesh.TriangleMesh();


        mesh.addVertex(new Vector3f(0, 0, 0)); // 0
        mesh.addVertex(new Vector3f(0, 1, 0)); // 1
        mesh.addVertex(new Vector3f(1, 1, 0)); // 2
        mesh.addVertex(new Vector3f(1, 0, 0)); // 3

        mesh.addVertex(new Vector3f(0, 0, 1)); // 4
        mesh.addVertex(new Vector3f(0, 1, 1)); // 5
        mesh.addVertex(new Vector3f(1, 1, 1)); // 6
        mesh.addVertex(new Vector3f(1, 0, 1)); // 7

        ArrayList<Triangle> triangles = new ArrayList<>();
        triangles.add(new Triangle(0, 1, 2));
        triangles.add(new Triangle(0, 2, 3));
        triangles.add(new Triangle(5, 4, 6));
        triangles.add(new Triangle(6, 4, 7));

        triangles.add(new Triangle(1, 5, 6));
        triangles.add(new Triangle(2, 1, 6));

        triangles.add(new Triangle(1, 4, 5));
        triangles.add(new Triangle(1, 0, 4));

        triangles.add(new Triangle(2, 6, 7));
        triangles.add(new Triangle(3, 2, 7));

        triangles.add(new Triangle(0, 3, 4));
        triangles.add(new Triangle(3, 7, 4));

        for (int i = 0; i < triangles.size(); i++) {
            mesh.addTriangle(triangles.get(i));
            Vector3f n = calcNormal(mesh, triangles.get(i));
            mesh.getTriangle(i).setNormal(n);
        }

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
