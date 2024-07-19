package types;

public class Linear {
    public Vertex p;
    public Vertex r;

    public Linear(Vertex p, Vertex r) {
        this.p = p;
        this.r = r;
    }

    public String toString() {
        return String.format("p: %s\nr: %s", p, r);
    }

    public static void main(String[] args) {
        Edge test = new Edge(new Vertex(2, 3), new Vertex(4, 5));
        System.out.println(test);
        Linear test2 = test.getPerp();
        System.out.println(test2);
    }

    public Vertex intersects(Linear l) {
        //x1 = r1*s + p1;
        //l1 = rl1*t + pl1
        //x1 = l1 <=> r1*s +p1 = rl1*t + pl1
        return new Vertex(0, 0);
    }
}
