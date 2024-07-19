package types;

public class Edge {
    private Vertex v1;
    private Vertex v2;
    private Linear perp;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public String toString() {
        return String.format("(%s - %s)", v1, v2);
    }

    public Linear getPerp() {
        if(perp != null) return perp;
        else perp = new Linear(Vertex.add(v2,Vertex.scale(Vertex.subtract(v1, v2), 0.5)), Vertex.normalize(Vertex.perp(Vertex.subtract(v1, v2))));
        return perp;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }
}
