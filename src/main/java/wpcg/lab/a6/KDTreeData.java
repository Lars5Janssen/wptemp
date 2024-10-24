/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */


package wpcg.lab.a6;

import com.jme3.math.Vector2f;
import java.util.Comparator;

/**
 * This class represents a data point in the data structure. It has a 2D
 * location.
 */
public class KDTreeData {

    /**
     * This position is used to build the kd tree.
     */
    private final Vector2f p;
    public KDTreeData(Vector2f p) {
        this.p = p;
    }

    /**
     * Creates a comparator based on the current split direction (x or y). Can be
     * helpful for the tree builder.
     */
    public static Comparator<KDTreeData> getComparator(
            KDTreeNode.SplitDirection splitDirection) {
        if (splitDirection == KDTreeNode.SplitDirection.X) {
            return (d1, d2) -> {
                float d = (d1.p.x - d2.p.x);
                return d < 0 ? -1 : (d > 0 ? 1 : 0);
            };
        } else {
            return (d1, d2) -> {
                float d = (d1.p.y - d2.p.y);
                return d < 0 ? -1 : (d > 0 ? 1 : 0);
            };
        }
    }

    @Override
    public String toString() {
        return p.toString();
    }

    public Vector2f getP() {
        return p;
    }
}
