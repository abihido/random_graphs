
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static java.lang.Math.random;

public class Ambiente extends JFrame {
    int width;
    int height;


    private Agente[] nodos;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public Ambiente(int numeroNodos,Double probabilidadConexion) { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 30;
        height = 30;
        crearGrafo(numeroNodos, probabilidadConexion);
    }


    void crearGrafo(int n, double p) {
        nodos = new Agente[n];
        nodos[0]=new Agente();
        addNode(Integer.toString(0),10,width/2);
        for (int i = 1; i < n; i++) {
            nodos[i]=new Agente();
            addNode(Integer.toString(i),10+i*i,width/2+i*30);
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
        Ambiente frame = new Ambiente(5,0.5);

        frame.setSize(400, 300);

        frame.setVisible(true);

    }
}

