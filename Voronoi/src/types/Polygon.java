package types;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Polygon {
    private Vertex[] vertices;
    private Vertex center;

    public Polygon(Vertex center, Vertex... vertices) {
        this.vertices = vertices;
        this.center = center;
        sort();
        //sortVertices();
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public String toString() {
        String out = "";
        for(Vertex v : vertices) {
            out+= v + "\n";
        }
        return out;
    }

    public Line2D[] getLines() {
        Line2D[] lines = new Line2D[vertices.length];
        for(int i = 0; i < vertices.length - 1; i++) {
            lines[i] = createLine(vertices[i], vertices[i+1]);
        }
        lines[lines.length-1] = createLine(vertices[vertices.length-1], vertices[0]);
        return lines;
    }

    private Line2D createLine(Vertex v1, Vertex v2) {
        return new Line2D.Double(Helper.OFF_X + v1.getX() / Helper.DIVIDE, Helper.OFF_Y + v1.getY() / Helper.DIVIDE, Helper.OFF_X + v2.getX() / Helper.DIVIDE, Helper.OFF_Y + v2.getY() / Helper.DIVIDE);
    }

    public void sortVertices() {
        for(int i = 0; i < vertices.length; i++) {
            for(int j = 0; j < vertices.length-1; j++) {
                if(Helper.isLess(vertices[j], vertices[j+1], this.center));
                else {
                    Vertex tmp = vertices[j];
                    vertices[j] = vertices[j+1];
                    vertices[j+1] = tmp;
                }
            }
        }
    }

    public void sort() {
        int n = vertices.length;
        /** if less than 3 points return **/        
             
        ArrayList<Integer> next=new ArrayList<>();
 
        /** find the leftmost point **/
        int leftMost = findLeft();
        int p = leftMost, q;
        
        next.add(p);
        /** iterate till p becomes leftMost **/
        do
        {
            /** wrapping **/
            q = (p + 1) % n;
            for (int i = 0; i < n; i++)
              if (Helper.CCW(vertices[p], vertices[i], vertices[q]))
                 q = i;
 
            next.add(q);
            p = q; 
        } while (p != leftMost);

        Vertex[] tmp = new Vertex[next.size()];

        for(int i = 0; i < next.size(); i++) {
            tmp[i] = vertices[next.get(i)];
        }

        vertices = tmp;
    }

    private int findLeft() {
        double leftestVal = Double.MAX_VALUE;
        int leftest = -1;
        for(int i = 0; i < vertices.length; i++) {
            if(vertices[i].getX() < leftestVal) {
                leftest = i;
            }
        }
        return leftest;
    }
}
