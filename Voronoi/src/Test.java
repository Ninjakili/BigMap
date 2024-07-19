import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import il.ac.idc.jdt.BoundingBox;
import il.ac.idc.jdt.DelaunayTriangulation;
import il.ac.idc.jdt.Point;

import types.*;

public class Test extends JFrame {
    private Vertex[] vertices;
    private Triangle[] triangles;
    private Polygon[] polys;
    private List<Vertex> vertexl;
    private List<String> ids;
    private List<Triangle> trigl;
    private BoundingBox bb;

    public Test() {
        super("Tesselation");
        ids = new ArrayList<String>();
        vertexl = new ArrayList<Vertex>();

        /* vertexl.add(new Vertex(1, 1));
        vertexl.add(new Vertex(-16, -20));
        vertexl.add(new Vertex(-28, 2));
        vertexl.add(new Vertex(-14, 22));
        vertexl.add(new Vertex(15, 23));
        vertexl.add(new Vertex(28, 0));
        vertexl.add(new Vertex(13, -19));
        vertexl.add(new Vertex(30, 30));
        vertexl.add(new Vertex(35, -25));
        vertexl.add(new Vertex(46, 4)); */

        readFile();
        nodeCSV();
        

        List<Point> pts = Helper.convertToPoints(vertexl);
        DelaunayTriangulation dt = new DelaunayTriangulation(pts);
        trigl = Helper.toTriangle(dt.getTriangulation());
        bb = dt.getBoundingBox();

        int agag = vertexl.size();
        for(int i = 0; i < agag; i++) {
            Vertex v = vertexl.get(i);
            dt.insertPoint(Helper.toPoint(Helper.mirrorHorz(v, bb.maxY()+100000000)));
            dt.insertPoint(Helper.toPoint(Helper.mirrorHorz(v, bb.minY()-100000000)));
            dt.insertPoint(Helper.toPoint(Helper.mirrorVert(v, bb.maxX()+100000000)));
            dt.insertPoint(Helper.toPoint(Helper.mirrorVert(v, bb.minX()-100000000)));
            vertexl.add(Helper.mirrorHorz(v, bb.maxY()+100000000));
            vertexl.add(Helper.mirrorHorz(v, bb.minY()-100000000));
            vertexl.add(Helper.mirrorVert(v, bb.maxX()+100000000));
            vertexl.add(Helper.mirrorVert(v, bb.minX()-100000000));
        }

        trigl = Helper.toTriangle(dt.getTriangulation());


        triangles = new Triangle[trigl.size()];
        for (int i = 0; i < trigl.size(); i++) {
            triangles[i] = trigl.get(i);
        }

        vertices = new Vertex[vertexl.size()];
        for (int i = 0; i < vertexl.size(); i++) {
            vertices[i] = vertexl.get(i);
        }


        for (int i = 0; i < vertices.length; i++) {
            for (int j = 0; j < triangles.length; j++) {
                for (Vertex v : triangles[j].getVertices()) {
                    if (vertices[i].getX() == v.getX() && vertices[i].getY() == v.getY()) {
                        vertices[i].addTriangle(triangles[j]);
                    }
                }
            }
        }

        /*
         * triangles[0] = new Triangle(vertices[0], vertices[1], vertices[2]);
         * triangles[1] = new Triangle(vertices[0], vertices[2], vertices[3]);
         * triangles[2] = new Triangle(vertices[0], vertices[3], vertices[4]);
         * triangles[3] = new Triangle(vertices[0], vertices[4], vertices[5]);
         * triangles[4] = new Triangle(vertices[0], vertices[5], vertices[6]);
         * triangles[5] = new Triangle(vertices[0], vertices[6], vertices[1]);
         * 
         * triangles[6] = new Triangle(vertices[4], vertices[5], vertices[7]);
         * triangles[7] = new Triangle(vertices[6], vertices[5], vertices[8]);
         * triangles[8] = new Triangle(vertices[9], vertices[5], vertices[7]);
         * triangles[9] = new Triangle(vertices[9], vertices[5], vertices[8]);
         */

        polys = new Polygon[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            polys[i] = vertices[i].genPolygon();
            System.out.println(i + ": " + vertices[i].getTriangleCount() + "triangles, " + polys[i].getVertices().length + " points in Polygon");

        }
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        JPanel panel = new JPanel();
        this.getContentPane().add(panel);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < triangles.length; i++) {
            Line2D[] lines = triangles[i].getLines();
            for (int j = 0; j < lines.length; j++) {
                g2.draw(lines[j]);
            }
            for(Vertex v : triangles[i].getVertices()) {
                Ellipse2D point = new Ellipse2D.Double(Helper.OFF_X - 5 + v.getX() / Helper.DIVIDE, Helper.OFF_Y - 5 + v.getY() / Helper.DIVIDE, 10, 10);
                g2.draw(point);
                g2.fill(point);
            }
        }

        g2.setColor(Color.RED);
        for (Polygon poly : polys) {
            Line2D[] lines = poly.getLines();
            for (Line2D line : lines) {
                if (lines.length > 1)
                    g2.draw(line);
            }
        }

        g2.setColor(Color.BLUE);
        Ellipse2D maxmax = new Ellipse2D.Double(Helper.OFF_X - 5 + bb.maxX() / Helper.DIVIDE + 10, Helper.OFF_Y - 5 + bb.maxY() / Helper.DIVIDE + 10, 10, 10);
        g2.draw(maxmax);
        g2.fill(maxmax);

        Ellipse2D maxmin = new Ellipse2D.Double(Helper.OFF_X - 5 + bb.maxX() / Helper.DIVIDE + 10, Helper.OFF_Y - 5 + bb.minY() / Helper.DIVIDE - 10, 10, 10);
        g2.draw(maxmin);
        g2.fill(maxmin);

        Ellipse2D minmax = new Ellipse2D.Double(Helper.OFF_X - 5 + bb.minX() / Helper.DIVIDE - 10, Helper.OFF_Y - 5 + bb.maxY() / Helper.DIVIDE + 10, 10, 10);
        g2.draw(minmax);
        g2.fill(minmax);

        Ellipse2D minmin = new Ellipse2D.Double(Helper.OFF_X - 5 + bb.minX() / Helper.DIVIDE - 10, Helper.OFF_Y - 5 + bb.minY() / Helper.DIVIDE - 10, 10, 10);
        g2.draw(minmin);
        g2.fill(minmin);

    }

    public static void main(String[] args) {
        Test test = new Test();
        test.output();
    }

    public void nodeCSV() {
        List<String> lines = new ArrayList<String>();
        lines.add("nodeID;;;xml\n");
        try {
            BufferedReader myReader = new BufferedReader(new FileReader("input.xml"));
            while(myReader.ready()) {
                String line = "";
                String tmp = myReader.readLine();
                if(tmp.trim().startsWith("<node")) {
                    line += (tmp.substring(tmp.indexOf("id=")+4, tmp.indexOf("\"", tmp.indexOf("id=")+4))) + ";;;";
                    line += tmp;
                    for(tmp = myReader.readLine(); tmp.trim().startsWith("<tag"); tmp = myReader.readLine()) {
                        line += tmp + "\\n";
                    }
                    lines.add(line + "\n");
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter("./maegges/backend/data/nodes.csv");
            for(String line : lines) {
                myWriter.write(line);
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void output() { 
        try {
            FileWriter myWriter = new FileWriter("./maegges/backend/data/polys.csv");
            myWriter.write("nodeID;first;second\n");
            int i = 0;
            for(Polygon poly : polys) {
                try {
                    String nodeID = ids.get(i);
                    myWriter.write(nodeID + ";");
                    myWriter.write("\\t<polygon " + "id=\\\"" + ids.get(i) + "\\\" visited=\\\"");
                    myWriter.write(";\\\">\\n");
                i++;
                for(Vertex v : poly.getVertices()) {
                    myWriter.write(String.format(Locale.ENGLISH,"\\t\\t<vertex lat=\\\"%f\\\" lon=\\\"%f\\\"/>\\n", v.getY()/100_000_000, v.getX()/100_000_000));
                }
                myWriter.write("\\t</polygon>\n");
                } catch(Exception e) {

                }
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void readFile() {
        try {
            BufferedReader myReader = new BufferedReader(new FileReader("input.xml"));
            while(myReader.ready()) {
                String tmp = myReader.readLine();
                if(tmp.trim().startsWith("<node")) {
                    ids.add(tmp.substring(tmp.indexOf("id=")+4, tmp.indexOf("\"", tmp.indexOf("id=")+4)));
                    double x = Double.parseDouble(tmp.substring(tmp.indexOf("lat=")+5, tmp.indexOf("\"", tmp.indexOf("lat=")+5))) * 100_000_000;
                    double y = Double.parseDouble(tmp.substring(tmp.indexOf("lon=")+5, tmp.indexOf("\"", tmp.indexOf("lon=")+5))) * 100_000_000;
                    vertexl.add(new Vertex(x, y));
                    
                }
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }
}
