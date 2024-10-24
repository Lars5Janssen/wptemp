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

    protected ArrayList<Integer> count(wpcg.base.mesh.TriangleMesh m, Vertex a, Vertex b, Vertex c) {
        ArrayList<Integer> counts = new ArrayList<>();
        counts.add(0);
        counts.add(0);
        counts.add(0);
        for (int i = 0; i < m.getNumberOfTriangles(); i++) {
            Triangle t = m.getTriangle(i);
            Vertex v1 = m.getVertex(t.getVertexIndex(0));
            Vertex v2 = m.getVertex(t.getVertexIndex(1));
            Vertex v3 = m.getVertex(t.getVertexIndex(2));

            if (v1 == a || v1 == b || v1 == c) {counts.set(0,counts.get(0)+1);}
            if (v2 == a || v2 == b || v2 == c) {counts.set(1,counts.get(1)+1);}
            if (v3 == a || v3 == b || v3 == c) {counts.set(2,counts.get(2)+1);}

        }
        return counts;
    }

    protected ArrayList<Vertex> getLongTriangleSide(wpcg.base.mesh.TriangleMesh m, Triangle t) {
        Vector3f v1 = m.getVertex(t.getVertexIndex(0)).getPosition();
        Vector3f v2 = m.getVertex(t.getVertexIndex(1)).getPosition();
        Vector3f v3 = m.getVertex(t.getVertexIndex(2)).getPosition();

        Vertex vv1 = m.getVertex(t.getVertexIndex(0));
        Vertex vv2 = m.getVertex(t.getVertexIndex(1));
        Vertex vv3 = m.getVertex(t.getVertexIndex(2));

        ArrayList<Vertex> sides = new ArrayList<>();
        if (v1.distance(v2) > 1F) {
            sides.add(vv1);
            sides.add(vv2);
        }
        if (v1.distance(v3) > 1F) {
            sides.add(vv1);
            sides.add(vv3);
        }
        if (v2.distance(v3) > 1F) {
            sides.add(vv2);
            sides.add(vv3);
        }
        return sides;
    }

    protected ArrayList<Vertex> getMiddlePoints(wpcg.base.mesh.TriangleMesh m, Triangle t) {
        Vertex v1 = m.getVertex(t.getVertexIndex(0));
        Vertex v2 = m.getVertex(t.getVertexIndex(1));
        Vertex v3 = m.getVertex(t.getVertexIndex(2));
        Vertex rightAngleVertex = v1;
        Vertex aVertex = v1;
        Vertex bVertex = v1;

        Vector3f vv1 = m.getVertex(t.getVertexIndex(0)).getPosition();
        Vector3f vv2 = m.getVertex(t.getVertexIndex(1)).getPosition();
        Vector3f vv3 = m.getVertex(t.getVertexIndex(2)).getPosition();

        if (vv1.distance(vv2) == 1F && vv1.distance(vv3) == 1F) {rightAngleVertex = v1; aVertex = v2; bVertex = v3;}
        else if (vv2.distance(vv1) == 1F && vv2.distance(vv3) == 1F) {rightAngleVertex = v2; aVertex = v3; bVertex = v1;}
        else if (vv3.distance(vv1) == 1F && vv3.distance(vv2) == 1F) {rightAngleVertex = v3; aVertex = v2; bVertex = v1;}
        else {
            System.out.println("CRASH~1");
        }

        Vector3f middlePointOnLong = aVertex.getPosition().scaleAdd(0.5F, bVertex.getPosition());


        return null;
    }

    protected boolean addableTriangle(wpcg.base.mesh.TriangleMesh m, Triangle t) {
        ArrayList<Integer> tIndex = new ArrayList<>();
        tIndex.add(t.getVertexIndex(0));
        tIndex.add(t.getVertexIndex(1));
        tIndex.add(t.getVertexIndex(2));
        ArrayList<Vertex> tSides = getLongTriangleSide(m, t);

        for (int i = 0; i < m.getNumberOfTriangles(); i++) {
            Triangle compTriangle = m.getTriangle(i);
            ArrayList<Integer> compIndex = new ArrayList<>();
            compIndex.add(compTriangle.getVertexIndex(0));
            compIndex.add(compTriangle.getVertexIndex(1));
            compIndex.add(compTriangle.getVertexIndex(2));

            int count = 0;
            for (Integer index : compIndex) {
                if (tIndex.contains(index)) {
                    count++;
                }
            }

            if (count == 3) {return false;}
            //if (count == 2) {
            //    if (compTriangle.getNormal().equals(t.getNormal())) {
            //        ArrayList<Vertex> compSides = getLongTriangleSide(m, compTriangle);
            //        if (compSides.containsAll(tSides)) {
            //            return false;
            //        }
            //    }
            //}
        }
        //Vertex t1 = m.getVertex(t.getVertexIndex(0));
        //Vertex t2 = m.getVertex(t.getVertexIndex(1));
        //Vertex t3 = m.getVertex(t.getVertexIndex(2));
        //ArrayList<Integer> counts = count(m, t1, t2, t3);
        //if (counts.size() != 3) {return false;}
        //for (Integer count : counts) {
        //    if (count > 3) {return false;}
        //}
        return true;
    }

    protected Triangle calculateNormal(wpcg.base.mesh.TriangleMesh m, Triangle t, Vector3f middle) {
        Vector3f normalNormal = t.getNormal();
        Vector3f invertedNormal = normalNormal.mult(-1);

        float iDist = invertedNormal.distance(middle);
        float nDist = normalNormal.distance(middle);
        if (nDist == iDist) {
            System.out.println("CRASH");
        } else if (nDist > iDist) {
            t.setNormal(invertedNormal);
        }
        return t;


    }
    protected TriangleMesh makeSimpleMesh() {
        wpcg.base.mesh.TriangleMesh mesh = new wpcg.base.mesh.TriangleMesh();
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    mesh.addVertex(new Vector3f(x, y, z));
                }
            }
        }
       Vector3f middleOfCube = new Vector3f(0.5F, 0.5F, 0.5F);

            for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
                for (int j = 0; j < mesh.getNumberOfVertices(); j++) {
                    for (int k = 0; k < mesh.getNumberOfVertices(); k++) {
                        if (i == j || j == k || i == k) {
                            continue;
                        }

                        Vertex v1 = mesh.getVertex(i);
                        Vertex v2 = mesh.getVertex(j);
                        Vertex v3 = mesh.getVertex(k);

                        float dist1 = v1.getPosition().distance(v2.getPosition());
                        float dist2 = v1.getPosition().distance(v3.getPosition());
                        float dist3 = v2.getPosition().distance(v3.getPosition());

                        if (dist1 == 1.0 && dist2 == 1.0 && dist3 >= 1.414 && dist3 <= 1.5) { // TODO not clean for dist3
                            Triangle toAdd = new Triangle(i, j, k);
                            if (addableTriangle(mesh, toAdd)) {
                                mesh.addTriangle(toAdd);
                            }
                        }

                    }
                }

            }
        System.out.println(mesh.getNumberOfVertices());
        System.out.println(mesh.getNumberOfTriangles());

        //for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
        //    Triangle t = mesh.getTriangle(i);
        //    Vector3f normal = t.getNormal();

        //    if (normal.distance(mesh.getVertex(middleIndex).getPosition()) <= 0.5) {
        //        t.setNormal(normal.mult(-1));
        //    }
        //}
        //mesh.addVertex(new Vector3f(0, 0, 0)); // 0
        //mesh.addVertex(new Vector3f(0, 1, 0)); // 1
        //mesh.addVertex(new Vector3f(1, 1, 0)); // 2
        //mesh.addVertex(new Vector3f(1, 0, 0)); // 3

        //mesh.addVertex(new Vector3f(0, 0, 1)); // 4
        //mesh.addVertex(new Vector3f(0, 1, 1)); // 5
        //mesh.addVertex(new Vector3f(1, 1, 1)); // 6
        //mesh.addVertex(new Vector3f(1, 0, 1)); // 7

        //mesh.addTriangle(new Triangle(0, 1, 2));
        //mesh.addTriangle(new Triangle(0, 2, 3));
        //mesh.addTriangle(new Triangle(5, 4, 6));
        //mesh.addTriangle(new Triangle(6, 4, 7));

        //mesh.addTriangle(new Triangle(1, 5, 6));
        //mesh.addTriangle(new Triangle(2, 1, 6));

        //mesh.addTriangle(new Triangle(1, 4, 5));
        //mesh.addTriangle(new Triangle(1, 0, 4));

        //mesh.addTriangle(new Triangle(2, 6, 7));
        //mesh.addTriangle(new Triangle(3, 2, 7));

        //mesh.addTriangle(new Triangle(0, 3, 4));
        //mesh.addTriangle(new Triangle(3, 7, 4));

        //for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
        //    Vector3f triangelNormal = mesh.getTriangle(i).getNormal();
        //    mesh.getTriangle(i).setNormal(triangelNormal.mult(-1));
        //}
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
