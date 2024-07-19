package types;

public class Circle {
    private double r;
    private Vertex c;

    public Circle(Vertex c, double r) {
        this.r = r;
        this.c = c;
    }

    public Vertex getC() {
        return c;
    }

    public String toString() {
        return String.format("[c: %s | r: %f]", c, r);
    }
}
