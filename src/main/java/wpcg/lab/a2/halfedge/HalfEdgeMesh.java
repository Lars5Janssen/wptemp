/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a2.halfedge;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import wpcg.base.misc.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalfEdgeMesh {

    /**
     * List of triangles in the mesh.
     */
    private final List<HalfEdgeFacet> facets;

    /**
     * List of vertices in the mesh.
     */
    private final List<HalfEdgeVertex> vertices;


    /**
     * List of half edges in the mesh.
     */
    private final List<HalfEdge> halfEdges;

    /**
     * This is a cache data structure used in the split() routine: remember for
     * each half edge (and its opposite)
     * the vertex created in the middle.
     */
    private Map<HalfEdge, HalfEdgeVertex> cacheSplitHe2VertexMap;

    /**
     * This is a cache data structure used in the split() routine: remember of
     * each newly created Vertex the half edge
     * which created it.
     */
    private Map<HalfEdgeVertex, HalfEdge> cacheSplitVertex2HeMap;

    // +++ CONSTRUCTION/TRANSFORMATION ACCESS ++++++++++++++++++

    public HalfEdgeMesh() {
        facets = new ArrayList<HalfEdgeFacet>();
        vertices = new ArrayList<HalfEdgeVertex>();
        halfEdges = new ArrayList<HalfEdge>();
        cacheSplitHe2VertexMap = null;
        cacheSplitVertex2HeMap = null;
    }

    // +++ WRITING ACCESS ++++++++++++++++++

    /**
     * Add a triangle connecting the three vertices. Internally generates the
     * required half edges, and the facet.
     */
    public int addFacet(int vertexIndex1, int vertexIndex2,
                        int vertexIndex3) {
        HalfEdge halfEdge1 = new HalfEdge();
        HalfEdge halfEdge2 = new HalfEdge();
        HalfEdge halfEdge3 = new HalfEdge();
        HalfEdgeFacet facet = new HalfEdgeFacet();
        halfEdge1.setNext(halfEdge2);
        halfEdge2.setNext(halfEdge3);
        halfEdge3.setNext(halfEdge1);
        halfEdge1.setStartVertex(vertices.get(vertexIndex1));
        halfEdge2.setStartVertex(vertices.get(vertexIndex2));
        halfEdge3.setStartVertex(vertices.get(vertexIndex3));
        halfEdge1.setFacet(facet);
        halfEdge2.setFacet(facet);
        halfEdge3.setFacet(facet);
        facet.setHalfEdge(halfEdge1);
        halfEdges.add(halfEdge1);
        halfEdges.add(halfEdge2);
        halfEdges.add(halfEdge3);
        facets.add(facet);
        return facets.size() - 1;
    }

    /**
     * Reset the mesh to empty.
     */
    public void clear() {
        vertices.clear();
        facets.clear();
        halfEdges.clear();
    }

    public int addVertex(HalfEdgeVertex v) {
        vertices.add(v);
        return vertices.size() - 1;
    }

    public int addVertex(Vector2f position) {
        return addVertex(new HalfEdgeVertex(position));
    }

    public void addHalfEdge(HalfEdge halfEdge) {
        halfEdges.add(halfEdge);
    }

    public void addFacet(HalfEdgeFacet t) {
        facets.add(t);
    }

    // +++ READING ACCESS ++++++++++++++++++

    public HalfEdgeVertex getVertex(int index) {
        return vertices.get(index);
    }

    public HalfEdgeFacet getTriangle(int triangleIndex) {
        return facets.get(triangleIndex);
    }

    public int getNumberOfTriangles() {
        return facets.size();
    }

    public int getNumberOfVertices() {
        return vertices.size();
    }

    public int getNumberOfHalfEdges() {
        return halfEdges.size();
    }

    /**
     * Get the index'th vertex in the triangle. Valid values for index are 0, 1, 2.
     */
    public HalfEdgeVertex getVertex(HalfEdgeFacet triangle, int index) {
        HalfEdgeFacet het = triangle;
        HalfEdge he = het.getHalfEdge();
        for (int i = 0; i < index; i++) {
            he = he.getNext();
        }
        return he.getStartVertex();
    }

    public HalfEdge getHalfEdge(int halfEdgeIndex) {
        return halfEdges.get(halfEdgeIndex);
    }

    /**
     * Returns true if the mesh has a boundary.
     */
    public boolean hasBoudary() {
        for (HalfEdge he : halfEdges) {
            if (he.getOpposite() == null) {
                return true;
            }
        }
        return false;
    }

    // +++ OPERATIONS ++++++++++++++++++

    /**
     * Finally, opposite half edges must be connected and each vertex must be
     * assigned a outgoing half edge.
     * <p>
     * Attention: O(n^2)!
     */
    public void connectHalfEdges() {
        // Set half edge for each vertex
        for (int halfEdgeIndex = 0; halfEdgeIndex < halfEdges
                .size(); halfEdgeIndex++) {
            HalfEdge halfEdge = halfEdges.get(halfEdgeIndex);
            halfEdge.getStartVertex().setHalfEdge(halfEdge);
        }

        // Connect opposite halfEdges
        for (int i = 0; i < getNumberOfHalfEdges(); i++) {
            HalfEdge halfEdge1 = getHalfEdge(i);
            if (halfEdge1.getOpposite() == null) {
                for (int j = i + 1; j < getNumberOfHalfEdges(); j++) {
                    HalfEdge halfEdge2 = getHalfEdge(j);
                    if (halfEdge1.getStartVertex() == halfEdge2.getEndVertex()
                            && halfEdge2.getStartVertex() == halfEdge1.getEndVertex()) {
                        halfEdge1.setOpposite(halfEdge2);
                        halfEdge2.setOpposite(halfEdge1);
                        break;
                    }
                }
            }
        }
        Logger.getInstance().debug("Successfully connected half edges.");
    }


    /**
     * Returns the AABB bounding box of the mesh vertices.
     */
    public Vector2f[] getBoundingBox() {
        Vector2f ll = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
        Vector2f ur = new Vector2f(Float.MIN_VALUE, Float.MIN_VALUE);
        for (HalfEdgeVertex v : vertices) {
            if (v.getPosition().getX() < ll.getX()) {
                ll.setX(v.getPosition().getX());
            }
            if (v.getPosition().getX() > ur.getX()) {
                ur.setX(v.getPosition().getX());
            }
            if (v.getPosition().getY() < ll.getY()) {
                ll.setY(v.getPosition().getY());
            }
            if (v.getPosition().getY() > ur.getY()) {
                ur.setY(v.getPosition().getY());
            }
        }
        Vector2f extend = ur.subtract(ll);
        return new Vector2f[]{ll, ur};
    }


    /**
     * Split each triangle into four sub-triangles.
     * <p>
     * The old half edges and facets are kept and should be replaced later.
     *
     * @return Mapping between the newly created vertices and the old half edges
     * they were created from.
     */
    public Map<HalfEdgeVertex, HalfEdge> split() {
        // Create 4 triangles for each old triangle
        int oldNumTriangles = getNumberOfTriangles();
        cacheSplitHe2VertexMap = new HashMap<>();
        cacheSplitVertex2HeMap = new HashMap<>();
        for (int i = 0; i < oldNumTriangles; i++) {
            HalfEdgeFacet triangle = facets.get(i);
            HalfEdge he = triangle.getHalfEdge();
            HalfEdge heNext = he.getNext();
            HalfEdge heNextNext = heNext.getNext();
            HalfEdgeVertex va = he.getStartVertex();
            HalfEdgeVertex vb = heNext.getStartVertex();
            HalfEdgeVertex vc = heNextNext.getStartVertex();
            HalfEdgeVertex va_ = createVertexHalfEdgeSplit(he);
            HalfEdgeVertex vb_ = createVertexHalfEdgeSplit(heNext);
            HalfEdgeVertex vc_ = createVertexHalfEdgeSplit(heNextNext);
            createHalfEdgeTriangle(va, va_, vc_, triangle.getColor());
            createHalfEdgeTriangle(vb, vb_, va_, triangle.getColor());
            createHalfEdgeTriangle(vc, vc_, vb_, triangle.getColor());
            createHalfEdgeTriangle(va_, vb_, vc_, triangle.getColor());
        }
        connectHalfEdges();

        // Reset cache data structure
        cacheSplitHe2VertexMap = null;
        return cacheSplitVertex2HeMap;
    }

    /**
     * Create a new facet for the given vertices.
     */
    private void createHalfEdgeTriangle(HalfEdgeVertex a, HalfEdgeVertex b,
                                        HalfEdgeVertex c, ColorRGBA color) {
        HalfEdge he1 = new HalfEdge(a);
        HalfEdge he2 = new HalfEdge(b);
        HalfEdge he3 = new HalfEdge(c);
        a.setHalfEdge(he1);
        b.setHalfEdge(he2);
        c.setHalfEdge(he3);
        HalfEdgeFacet triangle = new HalfEdgeFacet();
        triangle.setColor(color);
        triangle.setHalfEdge(he1);
        he1.setNext(he2);
        he2.setNext(he3);
        he3.setNext(he1);
        he1.setFacet(triangle);
        he2.setFacet(triangle);
        he3.setFacet(triangle);
        halfEdges.add(he1);
        halfEdges.add(he2);
        halfEdges.add(he3);
        facets.add(triangle);
    }

    /**
     * Create a new half edge vertex in the middle of the given half edge.
     */
    private HalfEdgeVertex createVertexHalfEdgeSplit(HalfEdge he) {

        HalfEdgeVertex v = cacheSplitHe2VertexMap.get(he);
        if (v != null) {
            return v;
        }

        HalfEdgeVertex va = he.getStartVertex();
        HalfEdgeVertex vb = he.getEndVertex();
        Vector2f pos = va.getPosition().add(vb.getPosition()).mult(0.5f);
        v = vertices.get(addVertex(pos));
        cacheSplitHe2VertexMap.put(he, v);
        cacheSplitVertex2HeMap.put(v, he);
        if (he.getOpposite() != null) {
            cacheSplitHe2VertexMap.put(he.getOpposite(), v);
        }
        return v;
    }

    /**
     * Removes the triangles (not its vertices/half edges) from the mesh.
     */
    public void removeTriangle(int index) {
        facets.remove(index);
    }

    /**
     * Removes the half edge (not its vertices/triangles) from the mesh.
     */
    public void removeHalfEdge(int index) {
        halfEdges.remove(index);
    }

    /**
     * Create a dummy mesh of the regular grid
     */
    public void createDummyMesh(boolean innerPositionJiggle) {
        int resX = 3;
        int resY = 5;
        float dX = 1.0f / resX;
        float dY = 1.0f / resY;

        clear();

        // Vertices
        for (int j = 0; j <= resY; j++) {
            for (int i = 0; i <= resX; i++) {
                Vector2f p = new Vector2f(-0.5f + i * dX, -0.5f + j * dY);
                if (innerPositionJiggle && i != 0 && j != 0 && i != resX && j != resY) {
                    float offsetX = (float) (Math.random() * dX - dX / 2);
                    float offsetY = (float) (Math.random() * dY - dY / 2.0f);
                    p = p.add(new Vector2f(offsetX, offsetY));
                }
                addVertex(new HalfEdgeVertex(p));
            }
        }

        // Half edges + facets
        for (int j = 0; j < resY; j++) {
            for (int i = 0; i < resX; i++) {
                int i00 = j * (resX + 1) + i;
                int i01 = j * (resX + 1) + (i + 1);
                int i10 = (j + 1) * (resX + 1) + i;
                int i11 = (j + 1) * (resX + 1) + (i + 1);
                HalfEdge he0 = new HalfEdge(getVertex(i00));
                HalfEdge he1 = new HalfEdge(getVertex(i01));
                HalfEdge he2 = new HalfEdge(getVertex(i11));
                HalfEdge he3 = new HalfEdge(getVertex(i10));
                getVertex(i00).setHalfEdge(he0);
                getVertex(i01).setHalfEdge(he1);
                getVertex(i11).setHalfEdge(he2);
                getVertex(i10).setHalfEdge(he3);
                he0.setNext(he1);
                he1.setNext(he2);
                he2.setNext(he3);
                he3.setNext(he0);
                addHalfEdge(he0);
                addHalfEdge(he1);
                addHalfEdge(he2);
                addHalfEdge(he3);
                HalfEdgeFacet facet = new HalfEdgeFacet();
                facet.setHalfEdge(he0);
                addFacet(facet);
            }
        }

        // Set opposites
        for (int j = 0; j < resY; j++) {
            for (int i = 0; i < resX; i++) {
                int cellIndex = j * resX + i;

                // along x-axis
                if (i < resX - 1) {
                    HalfEdge he0 = halfEdges.get(cellIndex * 4 + 1);
                    HalfEdge he0Opp = halfEdges.get((cellIndex + 1) * 4 + 3);
                    he0.setOpposite(he0Opp);
                    he0Opp.setOpposite(he0);
                }

                // along y-axis
                if (j < resY - 1) {
                    HalfEdge he1 = halfEdges.get(cellIndex * 4 + 2);
                    HalfEdge he1Opp = halfEdges.get((cellIndex + resX) * 4);
                    he1.setOpposite(he1Opp);
                    he1Opp.setOpposite(he1);
                }
            }
        }
    }
}
