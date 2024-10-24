package wpcg.base.mesh;

/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

import com.jme3.math.Vector2f;

import java.util.List;

public class TriangleMeshTools {

    /**
     * Adds all content of the otherMesh to the meshBase.
     */
    public static void unite(TriangleMesh baseMesh, TriangleMesh otherMesh) {
        int vertexOffset = baseMesh.getNumberOfVertices();
        int texCoordOffset = baseMesh.getNumberOfTextureCoordinates();

        // Vertices
        for (int i = 0; i < otherMesh.getNumberOfVertices(); i++) {
            baseMesh.addVertex(new Vertex(otherMesh.getVertex(i)));
        }
        for (int i = 0; i < otherMesh.getNumberOfTextureCoordinates(); i++) {
            baseMesh.addTextureCoordinate(
                    new Vector2f(otherMesh.getTextureCoordinate(i)));
        }
        for (int i = 0; i < otherMesh.getNumberOfTriangles(); i++) {
            Triangle t = new Triangle(otherMesh.getTriangle(i));
            t.addVertexIndexOffset(vertexOffset);
            t.addTexCoordOffset(texCoordOffset);
            baseMesh.addTriangle(t);
        }

        vertexOffset += otherMesh.getNumberOfVertices();
        texCoordOffset += otherMesh.getNumberOfTextureCoordinates();
    }

    /**
     * Create a unified mesh from all meshes in the list. Not tested for meshes
     * using textures.
     */
    public static TriangleMesh unite(List<TriangleMesh> meshes) {

        if (meshes.size() == 0) {
            return null;
        }

        TriangleMesh mesh = meshes.get(0);

        int vertexOffset = mesh.getNumberOfVertices();
        int texCoordOffset = mesh.getNumberOfTextureCoordinates();
        for (int meshIndex = 1; meshIndex < meshes.size(); meshIndex++) {
            TriangleMesh m = meshes.get(meshIndex);
            // Vertices
            for (int i = 0; i < m.getNumberOfVertices(); i++) {
                mesh.addVertex(new Vertex(m.getVertex(i)));
            }
            for (int i = 0; i < m.getNumberOfTextureCoordinates(); i++) {
                mesh.addTextureCoordinate(new Vector2f(m.getTextureCoordinate(i)));
            }
            for (int i = 0; i < m.getNumberOfTriangles(); i++) {
                Triangle t = new Triangle(m.getTriangle(i));
                t.addVertexIndexOffset(vertexOffset);
                t.addTexCoordOffset(texCoordOffset);
                mesh.addTriangle(t);
            }

            vertexOffset += m.getNumberOfVertices();
            texCoordOffset += m.getNumberOfTextureCoordinates();
        }

        return mesh;
    }
}

