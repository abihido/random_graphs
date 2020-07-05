
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

import static java.lang.Math.random;

public class Ambiente extends JFrame {

    int x,y,N,height,width;
    double angle=0;
    double salto;
    int Ww=1800;
    int Hw=1080;
    BufferedImage bi;
    Insets insets;

    private Agente[] nodos;
    ArrayList<Node> nodes;
    ArrayList<edge> edges;

    public Ambiente(int numeroNodos,Double probabilidadConexion) { //Constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        width = 10;
        height = 10;
        N=numeroNodos;
        salto=360.0/(double)(N);
        bi= new BufferedImage(Ww,Hw,BufferedImage.TYPE_INT_RGB);
        insets = getInsets();
        crearGrafo(numeroNodos, probabilidadConexion);
    }

    void getYXC(){

        int r= (Hw-200)/2;
        y= (int) ( Hw/2 + r*Math.sin(Math.toRadians(angle)));
        x= (int) (Ww/2 + r*Math.cos(Math.toRadians(angle)));
        angle = angle+salto;
        System.out.println(angle);
    }
    void crearGrafo(int n, double p) {
        nodos = new Agente[n];
        nodos[0]=new Agente();
        getYXC();
        addNode(Integer.toString(0),x,y,nodos[0]);
        for (int i = 1; i < n; i++) {
            nodos[i]=new Agente();
            getYXC();
            addNode(Integer.toString(i),x,y,nodos[i]);
            for (int j = 0; j < i; j++) {
                if (random() < p) {
                    nodos[i].nuevoAmigo(nodos[j]);
                    nodos[j].nuevoAmigo(nodos[i]);
                    addEdge(i,j);
                }
            }
        }
    }

    void setAgentState(int index,Agente.estado estado){
        nodos[index].setEstado(estado);
    }
    class Node {
        int x, y;
        String name;
        Agente model;

        public Node(String myName, int myX, int myY,Agente ag) {
            x = myX;
            y = myY;
            name = myName;
            model = ag;
        }
    }

    class edge {
        int i, j;

        public edge(int ii, int jj) {
            i = ii;
            j = jj;
        }
    }

    public void addNode(String name, int x, int y,Agente modelo) {
        //add a node at pixel (x,y)
        nodes.add(new Node(name, x, y, modelo));
    }

    public void addEdge(int i, int j) {
        //add an edge between nodes i and j
        edges.add(new edge(i, j));
    }

    public void paint(Graphics g) { // draw the nodes and edges
        //super.paint(g);
        Graphics g1 = bi.getGraphics();
        g1.setColor(Color.white);
        g1.fillRect(0,0,Ww,Hw);
        FontMetrics f = g1.getFontMetrics();
        int nodeHeight = Math.max(height, f.getHeight());

        g1.setColor(Color.black);
        for (edge e : edges) {
            g1.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
                    nodes.get(e.j).x, nodes.get(e.j).y);
        }

        for (Node n : nodes) {
            int nodeWidth = Math.max(width, f.stringWidth(n.name) + width / 2);
            switch (n.model.getEstado()){
                case Normal:
                    g1.setColor(Color.white);break;
                case contagiado:
                    g1.setColor(Color.red);break;
                case inmune:
                    g1.setColor(Color.blue);break;
                case inservible:
                    g1.setColor(Color.darkGray);break;

            }
            g1.fillOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);
            g1.setColor(Color.black);
            g1.drawOval(n.x - nodeWidth / 2, n.y - nodeHeight / 2,
                    nodeWidth, nodeHeight);

            g1.drawString(n.name, n.x - f.stringWidth(n.name) / 2,
                    n.y + f.getHeight() / 2);
        }
        g.drawImage(bi,insets.left,insets.top,this);
    }

    public void initProp(){
        for(int i =0;i<N;i++){
            nodos[i].distribucion_comunicacion_amigos();
           // System.out.println(nodos[i].probabilidad_inservible);
        }

    }
    public void Rutine(){
        for(int i =0;i<N;i++){
            nodos[i].VamosAContagiar();
        }
    }
    public void Actualizar(){
        for(int i =0;i<N;i++){
            nodos[i].actualizarEstado();
        }

    }

    public void deleteDeads(){
        revalidate();
        for(int u =0;u<N;u++){
            if(nodos[u].getEstado()== Agente.estado.inservible){
                int finalU = u;
                edges.removeIf(e -> (e.i== finalU||e.j==finalU));
            }


        }
    }

}

class testGraphDraw {
    //Here is some example syntax for the GraphDraw class
    public static void main(String[] args) throws InterruptedException {
        Ambiente frame = new Ambiente(100,0.2);

        frame.setSize(1800, 1080);
        frame.initProp();
        for (int i = 0; i < 10; i=i+5) {
            frame.setAgentState(i, Agente.estado.contagiado);
        }

        frame.setVisible(true);
        for(int t=0;t<10;t++){
            System.out.println("estamos en " + t);

            frame.Rutine();
            Thread.sleep(1000);
            frame.Actualizar();
            frame.deleteDeads();
            frame.repaint();
        }
        System.out.println("sali");



    }
}

