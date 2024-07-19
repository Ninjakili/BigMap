package types;

import java.util.ArrayList;

public class Vertex {
    private double x;
    private double y;
    private ArrayList<Triangle> triangles; 

    public Vertex(double x, double y) {
        triangles = new ArrayList<Triangle>();
        this.x = x;
        this.y = y;
    }

    public int getTriangleCount() {
        return triangles.size();
    }

    public String toString() {
        return String.format("[x: %f; y: %f]", x, y);
    }

    public void addTriangle(Triangle t) {
        triangles.add(t);
    }

    public Polygon genPolygon() {
        Vertex[] vertices = new Vertex[triangles.size()];
        int i = 0;
        for(Triangle t : triangles) {
            vertices[i] = t.getCircumcenter();
            i++;
        }
        return new Polygon(this, vertices);
    }

    public static Vertex normalize(Vertex v) {
        Double len = Math.sqrt(Math.abs(v.x) + Math.abs(v.y));
        return new Vertex(v.x * (1/len), v.y * (1/len));
    }

    public static Vertex scale(Vertex v, double scalar) {
        return new Vertex(v.x * scalar, v.y * scalar);
    }

    public static Vertex subtract(Vertex v1, Vertex v2) {
        return new Vertex(v1.x - v2.x, v1.y - v2.y);
    } 

    public static Vertex add(Vertex v1, Vertex v2) {
        return new Vertex(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vertex perp(Vertex v1) {
        return new Vertex(v1.y, -v1.x);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean equals(Vertex v) {
        return v.getX() == this.x && v.getY() == this.y;
    }
}
