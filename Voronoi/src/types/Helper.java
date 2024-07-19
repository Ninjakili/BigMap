package types;

import java.util.ArrayList;
import java.util.List;

import il.ac.idc.jdt.Triangle;
import il.ac.idc.jdt.DelaunayTriangulation;
import il.ac.idc.jdt.Point;

public class Helper {
    private static final double ZOOM = 0.5;
    public static final double DIVIDE = 2_000_000 / ZOOM;
    public static final double OFF_X = -1810 * ZOOM;
    public static final double OFF_Y = 200 * ZOOM;

    public static double[] lineFromPoints(Vertex p, Vertex q) {
        double[] out = new double[3];
        out[0] = q.getY() - p.getY();
        out[1] = p.getX() - q.getX();
        out[2] = out[0] * p.getX() + out[1] * p.getY();
        return out;
    }

    public static double[] perp(Vertex p, Vertex q) {
        double[] out = new double[3];
        double[] in = lineFromPoints(p, q);
        Vertex midPoint = new Vertex((p.getX() + q.getX()) / 2, (p.getY() + q.getY()) / 2);
        out[2] = -in[1] * midPoint.getX() + in[0] * midPoint.getY();
        out[1] = -in[1];
        out[0] = in[0];
        return out;
    }

    public static List<types.Triangle> getDelaunay(List<Vertex> l) {
        List<Point> pts = convertToPoints(l);
        DelaunayTriangulation dt = new DelaunayTriangulation(pts);
        return toTriangle(dt.getTriangulation());

    }

    public static List<Point> convertToPoints(List<Vertex> l) {
        List<Point> pts = new ArrayList<Point>();
        for (Vertex v : l) {
            pts.add(new Point(v.getX(), v.getY()));
        }
        return pts;
    }

    public static List<types.Triangle> toTriangle(List<Triangle> ts) {
        List<types.Triangle> trigs = new ArrayList<types.Triangle>();
        for (Triangle t : ts) {
            if (t.getC() == null || t.getB() == null || t.getA() == null)
                ;
            else
                trigs.add(new types.Triangle(new Vertex(t.getA().getX(), t.getA().getY()),
                        new Vertex(t.getB().getX(), t.getB().getY()), new Vertex(t.getC().getX(), t.getC().getY())));
        }
        return trigs;
    }

    public static Vertex intersection(double[] l1, double[] l2) {
        double det = l1[0] * l2[1] - l2[0] * l1[1];
        if (det == 0)
            return new Vertex(Double.MAX_VALUE, Double.MAX_VALUE);
        else
            return new Vertex((l1[0] * l2[2] - l2[0] * l1[2]) / det, (l2[1] * l1[2] - l1[1] * l2[2]) / det);
    }

    public static Vertex getCircumcenter(types.Triangle t) {
        double[] perp1 = perp(t.getVertices()[0], t.getVertices()[1]);
        double[] perp2 = perp(t.getVertices()[1], t.getVertices()[2]);

        return intersection(perp1, perp2);
    }

    public static boolean isLess(Vertex a, Vertex b, Vertex center) {
        /*
         * if (a.getX() - center.getX() >= 0 && b.getX() - center.getX() < 0)
         * return true;
         * if (a.getX() - center.getX() < 0 && b.getX() - center.getX() >= 0)
         * return false;
         * if (a.getX() - center.getX() == 0 && b.getX() - center.getX() == 0) {
         * if (a.getY() - center.getY() >= 0 || b.getY() - center.getY() >= 0)
         * return a.getY() > b.getY();
         * return b.getY() > a.getY();
         * }
         * 
         * // compute the cross product of vectors (center -> a) x (center -> b)
         * double det = (a.getX() - center.getX()) * (b.getX() - center.getX()) -
         * (b.getX() - center.getX()) * (a.getY() - center.getY());
         * if (det < 0)
         * return true;
         * if (det > 0)
         * return false;
         * 
         * // points a and b are on the same line from the center
         * // check which point is closer to the center
         * double d1 = (a.getX() - center.getX()) * (a.getX() - center.getX()) +
         * (a.getY() - center.getY()) * (a.getY() - center.getY());
         * double d2 = (b.getX() - center.getX()) * (b.getX() - center.getX()) +
         * (b.getY() - center.getY()) * (b.getY() - center.getY());
         * return d1 > d2;
         */

        int dax = ((a.getX() - center.getX()) > 0) ? 1 : 0;
        int day = ((a.getY() - center.getY()) > 0) ? 1 : 0;
        int qa = (1 - dax) + (1 - day) + ((dax & (1 - day)) << 2);

        int dbx = ((b.getX() - center.getX()) > 0) ? 1 : 0;
        int dby = ((b.getY() - center.getY()) > 0) ? 1 : 0;
        int qb = (1 - dbx) + (1 - dby) + ((dbx & (1 - dby)) << 1);

        if (qa == qb) {
            return (b.getX() - center.getX()) * (a.getY() - center.getY()) < (b.getY() - center.getY())
                    * (a.getX() - center.getX());
        } else {
            return qa < qb;
        }
    }

    public static boolean CCW(Vertex p, Vertex q, Vertex r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val >= 0)
            return false;
        return true;
    }

    public static Vertex toVertex(Point p) {
        return new Vertex(p.getX(), p.getY());
    }

    public static Vertex mirrorVert(Vertex v, double x) {
        return new Vertex(v.getX() - 2*(v.getX() - x), v.getY());
    }

    public static Vertex mirrorHorz(Vertex v, double y) {
        return new Vertex(v.getX(), v.getY() - 2*(v.getY() - y));
    }

    public static Point toPoint(Vertex v) {
        return new Point(v.getX(), v.getY());
    }

}
