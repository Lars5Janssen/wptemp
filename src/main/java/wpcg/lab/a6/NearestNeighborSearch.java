/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
 */

package wpcg.lab.a6;

import com.jme3.math.Vector2f;

import java.util.List;

/**
 * Use a KD tree to search the closest point from a given position.
 */
public class NearestNeighborSearch {

    /**
     * This three must contain all data points and is used to search the closest data point.
     */
    private final KDTreeNode kdTree;

    /**
     * The list of all data points (only required for naive search)
     */
    private final List<KDTreeData> points;

    public NearestNeighborSearch(KDTreeNode kdTree, List<KDTreeData> points) {
        this.kdTree = kdTree;
        this.points = points;
    }

    /**
     * Find an return the closest data point from the position p in the tree.
     * O(log n) complexity.
     */
    public KDTreeData getNearestNeighbor(Vector2f p) {
        if (kdTree == null) {
            System.out.println("Cannot apply nearest neighbor search on null tree");
            return null;
        }
        return getNearestNeighbor(p, kdTree);
    }

    /**
     * Find an return the closest data point from the position p using linear
     * search. O(n) complexity.
     */
    public KDTreeData getNearestNeighborNaive(Vector2f p) {
        if (points == null) {
            System.out.println("Cannot apply nearest neighbor search on null list " +
                    "of points");
            return null;
        }

        float minDist = Float.MAX_VALUE;
        KDTreeData closest = null;
        for (KDTreeData data : points) {
            float sqrDist = p.distanceSquared(data.getP());
            if (sqrDist < minDist || closest == null) {
                minDist = sqrDist;
                closest = data;
            }
        }
        return closest;
    }

    /**
     * Internal method for getNearestNeighbor(Vector2f) searching in a given node
     * (recursively).
     */
    private KDTreeData getNearestNeighbor(Vector2f p, KDTreeNode node) {
        if (node == null || node.getData() == null) {
            return null;
        }

        // First guess: the date in the node is the closest.
        KDTreeData closest = node.getData();
        float sqrBestDist = p.distanceSquared(node.getData().getP());

        // Alternative: look in the child which contains p -> is there a closer
        // data point?
        float distanceToPlaneInDimension = node.getSplittingDirection()
                == KDTreeNode.SplitDirection.X ?
                p.x - node.getData().getP().x :
                p.y - node.getData().getP().y;
        KDTreeNode child = child = distanceToPlaneInDimension < 0 ?
                node.getNegChild() : node.getPosChild();
        if (child != null) {
            KDTreeData dataChild = getNearestNeighbor(p, child);
            float sqrDistChild = p.distanceSquared(dataChild.getP());
            if (sqrDistChild < sqrBestDist) {
                sqrBestDist = sqrDistChild;
                closest = dataChild;
            }
        }

        // Alternative: look in the child which does NOT contain p -> is there a
        // closer data point only do this if the distance to closest is larger
        // than the distance to the separating hyperplane (otherwise all nodes
        // are considered -> O(n) instead of O(log n)).
        if (distanceToPlaneInDimension * distanceToPlaneInDimension < sqrBestDist) {
            // Check other child
            child = distanceToPlaneInDimension < 0 ? node.getPosChild() :
                    node.getNegChild();
            if (child != null) {
                KDTreeData dataChild = getNearestNeighbor(p, child);
                float sqrDistChild = p.distanceSquared(dataChild.getP());
                if (sqrDistChild < sqrBestDist) {
                    closest = dataChild;
                }
            }
        }

        return closest;
    }
}