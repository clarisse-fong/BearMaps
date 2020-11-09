package bearmaps.utils.ps;
import java.util.List;

public class KDTree implements PointSet {
    private List<Point> _points;
    private KDTreeNode root;

    /* Constructs a KDTree using POINTS. You can assume POINTS contains at least one
       Point object. */
    public KDTree(List<Point> points) {
        _points = points;
        for (Point point : _points) {
            insert(point);
        }
    }

    /*

    You might find this insert helper method useful when constructing your KDTree!
    Think of what arguments you might want insert to take in. If you need
    inspiration, take a look at how we do BST insertion!

    */
    private KDTreeNode insert(Point p) {
        if (root == null) {
            root = new KDTreeNode(p, true);
        } else {
            insertHelper(root, p);
        }
        return root;
    }

    public void insertHelper(KDTreeNode t, Point newP) {
        if (t.point.equals(newP)) {
            t.point = newP;
        } else if (t.compare(newP, t.point) >= 0 && t.right == null) {
            t.right = new KDTreeNode(newP, !t.xCoor);
        } else if (t.compare(newP, t.point) >= 0) {
            insertHelper(t.right, newP);
        } else if (t.compare(newP, t.point) < 0 && t.left == null) {
            t.left = new KDTreeNode(newP, !t.xCoor);
        } else {
            insertHelper(t.left, newP);
        }
    }

    /* Returns the closest Point to the inputted X and Y coordinates. This method
       should run in O(log N) time on average, where N is the number of POINTS. */
    public Point nearest(double x, double y) {
        if (root == null) {
            return null;
        } else {
            Point targetPoint = new Point(x, y);
            return nearestHelper(root, targetPoint, root.point());
        }
    }

    private Point nearestHelper(KDTreeNode node, Point g, Point currBest) {
        if (node == null) {
            return currBest;
        }
        if (Point.distance(g, node.point) < Point.distance(g, currBest)) {
            currBest = node.point();
        }

        KDTreeNode good;
        KDTreeNode bad;

        if (node.compare(g, node.point()) < 0) {
            good = node.left;
            bad = node.right;
        } else {
            good = node.right;
            bad = node.left;
        }

        currBest = nearestHelper(good, g, currBest);

        Point potPoint;
        if (!node.xCoor) {
            potPoint = new Point(g.getX(), node.point.getY());
        } else {
            potPoint = new Point(node.point.getX(), g.getY());

        }

        if (Point.distance(g, potPoint) < Point.distance(g, currBest)) {
            currBest = nearestHelper(bad, g, currBest);
        }
        return currBest;
    }

    private class KDTreeNode {

        private Point point;
        private KDTreeNode left;
        private KDTreeNode right;
        private boolean xCoor;


        KDTreeNode(Point p, Boolean xCoor) {
            this.point = p;
            this.xCoor = xCoor;
        }

        KDTreeNode(Point p, KDTreeNode left, KDTreeNode right, Boolean xCoor) {
            this.point = p;
            this.left = left;
            this.right = right;
            this.xCoor = xCoor;
        }

        Point point() {
            return point;
        }

        KDTreeNode left() {
            return left;
        }

        KDTreeNode right() {
            return right;
        }

        public double compare(Point o1, Point o2) {
            if (xCoor) {
                return o1.getX() - o2.getX();
            } else {
                return o1.getY() - o2.getY();
            }
        }

    }
}
