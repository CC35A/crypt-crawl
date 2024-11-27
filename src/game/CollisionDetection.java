package game;

import vector.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetection {
    public static Vector2 checkCollision(GameObject actor1, GameObject actor2) {
        Vector2 MTV = null;
        double minOverlap = Double.MAX_VALUE;
        for (Vector2 axis : getAxes(actor1, actor2)) {
            // Project vertices of both actors onto the axis
            Projection proj1 = project(actor1, axis);
            Projection proj2 = project(actor2, axis);

            // Check for overlap
            if (!proj1.overlaps(proj2)) {
                // No overlap found, actors are not colliding
                return null;
            }
            // Calculate overlap
            double overlap = Math.min(proj1.max, proj2.max) - Math.max(proj1.min, proj2.min);

            // If this overlap is smaller than the minimum overlap found so far, update MTV
            if (overlap < minOverlap) {
                minOverlap = overlap;
                MTV = axis;
            }
        }
        // Scale the MTV by the minimum overlap to get the minimum translation vector
        if (MTV != null) {
            MTV = MTV.scale(minOverlap);
        }

        // Overlap found on all axes, actors are colliding. Return minimum translation vector
        return MTV;
    }

    private static List<Vector2> getAxes(GameObject actor1, GameObject actor2) {
        List<Vector2> axes = new ArrayList<>();
        // Add axes from actor1
        for (int i = 0; i < actor1.collider.vertices.size(); i++) {
            Vector2 edge = actor1.collider.vertices.get(i).subtract(actor1.collider.vertices.get((i + 1) % actor1.collider.vertices.size()));
            axes.add(edge.perpendicular()); // Perpendicular to the edge
        }
        // Add axes from actor2
        for (int i = 0; i < actor2.collider.vertices.size(); i++) {
            Vector2 edge = actor2.collider.vertices.get(i).subtract(actor2.collider.vertices.get((i + 1) % actor2.collider.vertices.size()));
            axes.add(edge.perpendicular()); // Perpendicular to the edge
        }
        return axes;
    }

    private static Projection project(GameObject actor, Vector2 axis) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (Vector2 vertex : actor.collider.vertices) {
            Vector2 adjustedVertex = vertex.add(actor.pos);
            double projection = adjustedVertex.dotProduct(axis);
            min = Math.min(min, projection);
            max = Math.max(max, projection);
        }
        return new Projection(min, max);
    }

    private static class Projection {
        double min;
        double max;

        Projection(double min, double max) {
            this.min = min;
            this.max = max;
        }

        boolean overlaps(Projection other) {
            return !(this.max < other.min || other.max < this.min); // AABB in 1d
        }
    }
}
