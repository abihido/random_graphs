
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import static java.lang.Math.cos;
import static java.lang.Math.random;

public class Ambiente extends JFrame {

    int x,y,N,salto,height,width;
    int angle=0;
    int Ww=1200;
    int Hw=700;
    Boolean state=false;

    private Agente[] nodos;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public Ambiente(int numeroNodos,Double probabilidadConexion) { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
        N=numeroNodos;
        salto=360/N;
        crearGrafo(numeroNodos, probabilidadConexion);
    }

    void getYXC(){

        int r= (Hw-200)/2;
        y= (int) ( Hw/2 + r*Math.sin(Math.toRadians(angle)));
        x= (int) (Ww/2 + r*Math.cos(Math.toRadians(angle)));
        angle = angle+salto;
        System.out.println("x "+Integer.toString(x)+" y " +Integer.toString(y)+" a "+Integer.toString(angle));
    }
    void crearGrafo(int n, double p) {
        nodos = new Agente[n];
        nodos[0]=new Agente();
        getYXC();
        addNode(Integer.toString(0),x,y);
        for (int i = 1; i < n; i++) {
            nodos[i]=new Agente();
            getYXC();
            addNode(Integer.toString(i),x,y);
            for (int j = 0; j < i; j++) {
                if (random() < p) {
                    nodos[i].nuevoAmigo(nodos[j]);
                    nodos[j].nuevoAmigo(nodos[i]);
                    addEdge(i,j);
                }
            }
        }
    }


    class Node {
        int x, y;
        String name;

        public Node(String myName, int myX, int myY) {
            x = myX;
            y = myY;
            name = myName;
        }
    }

    class edge {
        int i, j;

        public edge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    public void addNode(String name, int x, int y) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name, x, y));
    }

    public void addEdge(int i, int j) {
        //add an edge between nodes i and j
        edges.add(new edge(i, j));
    }

    public void paint(Graphics g) { // draw the nodes and edges
        FontMetrics f = g.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());

        g.setColor(Color.black);
        for (edge e : edges) {
            g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
                    nodes.get(e.j).x, nodes.get(e.j).y);
        }

        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name) + width / 2);
            g.setColor(Color.white);
            g.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g.setColor(Color.black);
            g.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);

            g.drawString(n.name, n.x - f.stringWidth(n.name) / 2,
                    n.y + f.getHeight() / 2);
        }
    }
}

class testGraphDraw {
    //Here is some example syntax for the GraphDraw class
    public static void main(String[] args) {
        Ambiente frame = new Ambiente(15,0.5);

        frame.setSize(1200, 700);

        frame.setVisible(true);

    }
}

