/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.base.mesh;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import wpcg.base.misc.AxisAlignedBoundingBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a indexed vertex list triangle mesh.
 */
public class TriangleMesh {

    /**
     * Vertices.
     */
    private List<Vertex> vertices;

    /**
     * Triangles.
     */
    private List<Triangle> triangles;

    /**
     * Texture coordinates.
     */
    private List<Vector2f> textureCoordinates;

    /**
     * Texture object, leave null if no texture is used.
     */
    private String textureName;

    public TriangleMesh() {
        textureCoordinates = new ArrayList<>();
        triangles = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.textureName = textureName;
    }

    /**
     * Copy constructor
     */
    public TriangleMesh(TriangleMesh mesh) {
        this();
        // Vertices
        for (int i = 0; i < mesh.getNumberOfVertices(); i++) {
            addVertex(new Vertex(mesh.getVertex(i)));
        }
        // Texture coordinates
        for (int i = 0; i < mesh.getNumberOfTextureCoordinates(); i++) {
            addTextureCoordinate(new Vector2f(mesh.getTextureCoordinate(i)));
        }
        // Triangles
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            addTriangle(new Triangle(mesh.getTriangle(i)));
        }
        textureName = mesh.textureName;
    }

    /**
     * Add triangles connecting the three incides. Returns index of the triangle.
     */
    public int addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        triangles.add(new Triangle(vertexIndex1, vertexIndex2, vertexIndex3));
        return triangles.size() - 1;
    }

    /**
     * Add triangle object, return index of the triangle in the triangle list.
     */
    public void addTriangle(Triangle t) {
        triangles.add(t);
    }

    /**
     * Create and add vertex for the given position, return index in vertex list.
     */
    public int addVertex(Vector3f position) {
        vertices.add(new Vertex(position));
        return vertices.size() - 1;
    }

    /**
     * Add vertex, return index in vertex list.
     */
    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    /**
     * Add the given texture coordinate, return index in tex coord list.
     */
    public int addTextureCoordinate(Vector2f t) {
        textureCoordinates.add(t);
        return textureCoordinates.size() - 1;
    }

    /**
     * Remove all triangles.
     */
    public void clearTriangles() {
        triangles.clear();
    }

    /**
     * Remove the triangle a the given index.
     */
    public void removeTriangle(int index) {
        triangles.remove(index);
    }

    /**
     * Compute and return the AABB bounding box of the vertices.
     */
    public AxisAlignedBoundingBox getBoundingBox() {
        Vector3f ll = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        Vector3f ur = new Vector3f(Float.MIN_VALUE, Float.MIN_VALUE, Float.MIN_VALUE);
        for (Vertex v : vertices) {
            for (int i = 0; i < 3; i++) {
                if (v.getPosition().get(i) < ll.get(i)) {
                    ll.set(i, v.getPosition().get(i));
                }
                if (v.getPosition().get(i) > ur.get(i)) {
                    ur.set(i, v.getPosition().get(i));
                }
            }
        }
        Vector3f extend = ur.subtract(ll);
        AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
        bbox.add(ll);
        bbox.add(ur);
        return bbox;
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++

    public Vertex getVertex(int index) {
        return vertices.get(index);
    }

    public int getNumberOfTriangles() {
        return triangles.size();
    }

    public int getNumberOfVertices() {
        return vertices.size();
    }

    public Triangle getTriangle(int triangleIndex) {
        return triangles.get(triangleIndex);
    }

    public Vector2f getTextureCoordinate(int texCoordIndex) {
        return textureCoordinates.get(texCoordIndex);
    }

    public int getNumberOfTextureCoordinates() {
        return textureCoordinates.size();
    }

    public void setColor(ColorRGBA color) {
        for (Triangle triangle : triangles) {
            triangle.setColor(color);
        }
        for (Vertex vertex : vertices) {
            vertex.setColor(color);
        }
    }

    public void setTextureName(String textureFilename) {
        this.textureName = textureFilename;
    }
}
