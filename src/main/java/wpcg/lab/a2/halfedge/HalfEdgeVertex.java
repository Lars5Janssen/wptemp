/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a2.halfedge;

import com.jme3.math.Vector2f;

/**
 * Representation of a vertex.
 *
 * @author Philipp Jenke
 */
public class HalfEdgeVertex {

    /**
     * Reference to one of the outgoing half edges.
     */
    private HalfEdge halfEgde = null;

    /**
     * Position.
     */
    private Vector2f pos;

    /**
     * Constructor.
     *
     * @param position Initial value for position.
     */
    public HalfEdgeVertex(Vector2f position) {
        this.pos = new Vector2f(position);
    }

    @Override
    public String toString() {
        return pos.toString();
    }

    // +++ GETTER/SETTER ++++++++++++

    public HalfEdge getHalfEdge() {
        return halfEgde;
    }

    public void setHalfEdge(HalfEdge halfEgde) {
        this.halfEgde = halfEgde;
    }

    public Vector2f getPosition() {
        return pos;
    }
}