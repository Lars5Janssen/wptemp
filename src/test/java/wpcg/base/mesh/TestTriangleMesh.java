/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for TriangleMesh
 */
public class TestTriangleMesh {

  @Test
  public void testTriangleMesh() {
    TriangleMesh mesh = new TriangleMesh();
    assertEquals(0, mesh.getNumberOfVertices());
    assertEquals(0, mesh.getNumberOfTriangles());
    assertEquals(0, mesh.getNumberOfTextureCoordinates());

    // Vertices
    Vector3f v1 = new Vector3f(0, 0, 0);
    Vector3f v2 = new Vector3f(1, 1, 1);
    Vector3f v3 = new Vector3f(0, 1, -1);
    mesh.addVertex(v1);
    mesh.addVertex(v2);
    mesh.addVertex(v3);
    assertEquals(3, mesh.getNumberOfVertices());
    assertEquals(v1, mesh.getVertex(0).position);
    assertEquals(v2, mesh.getVertex(1).position);
    assertEquals(v3, mesh.getVertex(2).position);

    // Texture coordinates
    Vector2f t1 = new Vector2f(0, 0);
    Vector2f t2 = new Vector2f(0, 1);
    Vector2f t3 = new Vector2f(1, 0);
    mesh.addTextureCoordinate(t1);
    mesh.addTextureCoordinate(t2);
    mesh.addTextureCoordinate(t3);
    assertEquals(3, mesh.getNumberOfTextureCoordinates());

    // Triangles
    mesh.addTriangle(new Triangle(0, 1, 2, 1, 2, 0));
    assertEquals(1, mesh.getNumberOfTriangles());
    assertEquals(0,
            v1.subtract(mesh.getVertex(mesh.getTriangle(0).getVertexIndex(0)).position).length(),
            1e-5);
    assertEquals(0,
            t2.subtract(mesh.getTextureCoordinate(mesh.getTriangle(0).getTextureCoordinate(0))).length(),
            1e-5);
  }
}
