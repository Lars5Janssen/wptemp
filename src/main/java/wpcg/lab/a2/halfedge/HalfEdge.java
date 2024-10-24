/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a2.halfedge;

/**
 * A half edge has references to the next edge within the current facet, the
 * opposite edge, its start vertex and the facet it belongs to.
 */
public class HalfEdge {

    /**
     * Reference to the next edge in the mesh.
     */
    private HalfEdge next;

    /**
     * Reference to the opposite edge in the mesh.
     */
    private HalfEdge opposite;

    /**
     * Start vertex of the half edge.
     */
    private HalfEdgeVertex startVertex;

    /**
     * The half edge belongs to this facet.
     */
    private HalfEdgeFacet facet;

    public HalfEdge() {
        this.next = null;
        this.opposite = null;
        this.startVertex = null;
        this.facet = null;
    }

    public HalfEdge(HalfEdgeVertex v) {
        this.next = null;
        this.opposite = null;
        this.startVertex = v;
        this.facet = null;
    }

    @Override
    public String toString() {
        return getStartVertex().getPosition().toString() + " -> " + getEndVertex().getPosition().toString();
    }

    /**
     * Return the end vertex of a half edge (not directly saved.
     *
     * @return End index of the half edge, null on error.
     */
    public HalfEdgeVertex getEndVertex() {
        if (getNext() == null) {
            throw new IllegalArgumentException();
        }
        return getNext().getStartVertex();
    }

    /**
     * Returns true if the edge is at the boundary (has no opposite half edge).
     */
    public boolean isBoundary() {
        return opposite == null;
    }

    // +++ GETTER/SETTER +++++++++++++++++++++++++++++++++++++

    public HalfEdge getNext() {
        return next;
    }

    public void setNext(HalfEdge next) {
        this.next = next;
    }

    public HalfEdge getOpposite() {
        return opposite;
    }

    public void setOpposite(HalfEdge opposite) {
        this.opposite = opposite;
    }

    public HalfEdgeVertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(HalfEdgeVertex startVertex) {
        this.startVertex = startVertex;
    }

    public HalfEdgeFacet getFacet() {
        return facet;
    }

    public void setFacet(HalfEdgeFacet facet) {
        this.facet = facet;
    }
}
