package wpcg.lab.a6;

import com.jme3.math.Vector2f;

import java.util.List;

/**
 * Generates a KD-tree for a list of points
 */
public class KDTreeBuilder {
    /**
     * Create a KD tree from the current data.
     */
    public KDTreeNode create(List<KDTreeData> points) {

        // TODO: Implement this functionality
        return new KDTreeNode(null,
                KDTreeNode.SplitDirection.X,
                new Vector2f(0, 0), new Vector2f(0, 0));
    }
}
