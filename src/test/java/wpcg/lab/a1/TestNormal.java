package wpcg.lab.a1;

import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;
import wpcg.base.mesh.Triangle;
import wpcg.lab.a1.TriangleMeshScene;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestNormal {
    @Test
    public void testNormalCalc() {
        wpcg.base.mesh.TriangleMesh mesh = new wpcg.base.mesh.TriangleMesh();
        mesh.addVertex(new Vector3f(0, 0, 0)); // 0
        mesh.addVertex(new Vector3f(0, 1, 0)); // 1
        mesh.addVertex(new Vector3f(1, 1, 0)); // 2
        mesh.addTriangle(new Triangle(0,1,2));
        Vector3f expected = new Vector3f(0F, 0F, 1F);
        TriangleMeshScene scene = new TriangleMeshScene();
        Vector3f got = scene.calcNormal(mesh, mesh.getTriangle(0));
        assertEquals(expected, got);
    }
}
