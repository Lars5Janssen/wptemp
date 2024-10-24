/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a2.halfedge;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * A facet (here triangle) has a reference to one of its half edges. This
 * datastructure represents a general mesh (triangle, quad, ...). However, we
 * only use triangle meshes here.
 */
public class HalfEdgeFacet {

    /**
     * One of the half edges around the facet.
     */
    private HalfEdge halfEdge;

    /**
     * Normal of the facet.
     */
    private Vector2f normal;

    /**
     * Color of the facet.
     */
    private ColorRGBA color;

    public HalfEdgeFacet() {
        halfEdge = null;
        normal = new Vector2f(0, 1);
        color = ColorRGBA.Gray;
    }

    public HalfEdgeFacet(HalfEdgeFacet triangle) {
        HalfEdgeFacet t = new HalfEdgeFacet();
        t.halfEdge = halfEdge;
    }

    @Override
    public String toString() {
        return "HalfEdgeTriangle";
    }


    // +++ GETTER/SETTER +++++++++++++++++++++++++++

    public void setNormal(Vector2f normal) {
        this.normal.set(normal);
    }

    public void setColor(ColorRGBA color) {
        this.color = color;
    }

    public ColorRGBA getColor() {
        return color;
    }

    public HalfEdge getHalfEdge() {
        return halfEdge;
    }

    public void setHalfEdge(HalfEdge halfEdge) {
        this.halfEdge = halfEdge;
    }
}
