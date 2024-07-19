package types;

import java.awt.geom.Line2D;

public class Triangle {
    private Vertex v1;
    private Vertex v2;
    private Vertex v3;

    private Edge   e1;
    private Edge   e2;
    private Edge   e3;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        this.e1 = new Edge(v1, v2);
        this.e2 = new Edge(v1, v3);
        this.e3 = new Edge(v2, v3);

        v1.addTriangle(this);
        v2.addTriangle(this);
        v3.addTriangle(this);
    }

    public String toString() {
        return String.format("Vertices:\n%s\n%s\n%s\n\nEdges:\n%s\n%s\n%s", v1, v2, v3, e1, e2, e3);
    }

    public Vertex[] getVertices() {
        return new Vertex[] {v1, v2, v3};
    }

    public Vertex getCircumcenter() {
        return Helper.getCircumcenter(this);
    }

    public Edge[] getEdges() {
        return new Edge[] {e1, e2, e3};
    }

    public Line2D[] getLines() {
        Edge[] edges = this.getEdges();
        Line2D[] lines = new Line2D[3];
        for(int i = 0; i < edges.length; i++) {
            lines[i] = new Line2D.Double( Helper.OFF_X+edges[i].getV1().getX()/Helper.DIVIDE, Helper.OFF_Y+edges[i].getV1().getY()/Helper.DIVIDE, Helper.OFF_X+edges[i].getV2().getX()/Helper.DIVIDE, Helper.OFF_Y+edges[i].getV2().getY()/Helper.DIVIDE);
        }
        return lines;
    }
}
