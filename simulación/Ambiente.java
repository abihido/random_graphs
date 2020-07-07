
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.random;

public class Ambiente extends JFrame {

	int x, y, N, height, width, t, sanos, contagiados, recuperados, muertos;
	int times[]= new int [5];
	double angle = 0;
	double salto, a_firewall, b_firewall, a_antiv, b_antiv, a_usuario, b_usuario,recontagio;
	int Ww = 1800;
	int Hw = 1080;

	int[][] adyacensia;

	private Boolean act=true;
	BufferedImage bi;
	Insets insets;
	Observador observador;

	private Agente[] nodos;
	ArrayList<Node> nodes;
	ArrayList<edge> edges;

	public Ambiente(
			int numeroNodos,
			Double probabilidadConexion,
			double a_firewall, double b_firewall,
			double a_antiv, double b_antiv,
			double a_usuario, double b_usuario,
			double recontagio,
			Observador observador
	) { //Constructor
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nodes = new ArrayList<>();
		edges = new ArrayList<>();
		width = 10;
		height = 10;
		this.a_firewall = a_firewall;
		this.b_firewall = b_firewall;
		this.a_antiv = a_antiv;
		this.b_antiv = b_antiv;
		this.a_usuario = a_usuario;
		this.b_usuario = b_usuario;
		this.recontagio=recontagio;
		N = numeroNodos;
		salto = 360.0 / (double) (N);
		bi = new BufferedImage(Ww, Hw, BufferedImage.TYPE_INT_RGB);
		insets = getInsets();
		adyacensia = new int [N][N];
		crearGrafo(numeroNodos, probabilidadConexion);
		this.observador = observador;
	}

	void setT(int x) {
		this.t = x;
	}
	public Boolean getAct() {
		return act;
	}

	public void setAct(Boolean act) {
		this.act = act;
	}

	void getYXC() {

		int r = (Hw - 200) / 2;
		y = (int) (Hw / 2 + r * Math.sin(Math.toRadians(angle)));
		x = (int) (Ww / 2 + r * Math.cos(Math.toRadians(angle)));
		angle = angle + salto;
	}

	void crearGrafo(int n, double p) {
		nodos = new Agente[n];
		nodos[0] = new Agente(a_firewall, b_firewall, a_antiv, b_antiv, a_usuario, b_usuario,recontagio);
		getYXC();
		addNode(Integer.toString(0), x, y, nodos[0]);
		for (int i = 1; i < n; i++) {
			nodos[i] = new Agente(a_firewall, b_firewall, a_antiv, b_antiv, a_usuario, b_usuario,recontagio);
			getYXC();
			addNode(Integer.toString(i), x, y, nodos[i]);
			for (int j = 0; j < i; j++) {
				if (random() < p) {
					nodos[i].nuevoAmigo(nodos[j]);
					nodos[j].nuevoAmigo(nodos[i]);
					adyacensia[i][j]=1;
					addEdge(i, j);
				}
			}
		}
	}

	void setAgentState(int index, Agente.estado estado) {
		nodos[index].setEstado(estado);
	}

	class Node {
		int x, y;
		String name;
		Agente model;

		public Node(String myName, int myX, int myY, Agente ag) {
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

	public void addNode(String name, int x, int y, Agente modelo) {
		//add a node at pixel (x,y)
		nodes.add(new Node(name, x, y, modelo));
	}

	public void addEdge(int i, int j) {
		//add an edge between nodes i and j
		edges.add(new edge(i, j));
	}

	public void paint(Graphics g) { // draw the nodes and edges
		Graphics g1 = bi.getGraphics();
		g1.setColor(Color.white);
		g1.fillRect(0, 0, Ww, Hw);
		g1.setFont(new Font("Arial", Font.PLAIN, 12));
		FontMetrics f = g1.getFontMetrics();


		int nodeHeight = Math.max(height, f.getHeight());

		g1.setColor(Color.black);

		for (edge e : edges) {
			g1.drawLine(nodes.get(e.i).x, nodes.get(e.i).y,
					nodes.get(e.j).x, nodes.get(e.j).y);
		}

		for (Node n : nodes) {
			int nodeWidth = Math.max(width, f.stringWidth(n.name) + width / 2);
			switch (n.model.getEstado()) {
				case Normal:
					g1.setColor(Color.white);
					sanos++;
					break;

				case contagiado:
					g1.setColor(Color.red);
					contagiados++;
					break;

				case inmune:
					g1.setColor(Color.blue);
					recuperados++;
					break;
				case inservible:
					g1.setColor(Color.darkGray);
					muertos++;
					break;

			}
			final int x = n.x - nodeWidth / 2;
			final int y = n.y - nodeHeight / 2;
			g1.fillOval(x, y, nodeWidth, nodeHeight);
			g1.setColor(Color.black);
			g1.drawOval(x, y, nodeWidth, nodeHeight);

			g1.drawString(n.name, n.x - f.stringWidth(n.name) / 2,
					n.y + f.getHeight() / 2);
		}

		//indicadores
		g1.setFont(new Font("Arial", Font.BOLD, 42));
		FontMetrics f2 = g1.getFontMetrics();
		int j = f2.stringWidth("Tiempo = " + t);
		final int x = Ww - 20;
		g1.drawString("Tiempo = " + t, x - j, 200);

		g1.setFont(new Font("Arial", Font.BOLD, 42));
		f2 = g1.getFontMetrics();
		j = f2.stringWidth("Sanos = " + sanos);
		g1.drawString("Sanos = " + sanos, x - j, 300);
		g1.setColor(Color.red);
		j = f2.stringWidth("Contagiados = " + contagiados);
		g1.drawString("Contagiados = " + contagiados, x - j, 400);
		g1.setColor(Color.blue);
		j = f2.stringWidth("Recuperados = " + recuperados);
		g1.drawString("Recuperados = " + recuperados, x - j, 500);
		g1.setColor(Color.darkGray);
		j = f2.stringWidth("Inutilizables = " + muertos);
		g1.drawString("Inutilizables = " + muertos, x - j, 600);
		if(times[4]<(int)(100*contagiados/N)){
			times[4]=(int)(100*contagiados/N);
		}
		if(times[4]<50 && times[4]>=25 && times[0]==0){
			times[0]=t;
		}
		else if ( times[4]<75 && times[4]>=50 && times[1]==0){
			times[1]=t;
			if(times[0]==0){
				times[0]=t;
			}
		}
		else if(times[4]<100 && times[4]>=75 && times[2]==0){
			times[2]=t;
			if(times[1]==0){
				times[1]=t;
			}
			if(times[0]==0){
				times[0]=t;
			}
		}
		if(times[4]==100&&times[3]==0){
			times[3]=t;
			if(times[2]==0){
				times[2]=t;
			}
			if(times[0]==0){
				times[0]=t;

			}if(times[1]==0) {
				times[1] = t;
			}
		}
		System.out.println(Arrays.toString(times));
		j = f2.stringWidth("Max contagio = " + times[4] +"%");
		g1.drawString("Max contagio = " + times[4] +"%", x - j, 800);



		j = f2.stringWidth("Porcentaje contagio = " + (int)(100*contagiados/N) +"%");
		g1.drawString("Porcentaje contagio = " + (int)(100*contagiados/N) +"%", x - j, 900);
//Reset
		sanos = muertos = contagiados = recuperados = 0;




//redraw
		g.drawImage(bi, insets.left, insets.top, this);


	}

	public void initProp() {
		for (int i = 0; i < N; i++) {
			nodos[i].distribucion_comunicacion_amigos();
			// System.out.println(nodos[i].probabilidad_inservible);
		}

	}

	public void Rutine() {
		for (int i = 0; i < N; i++) {
			nodos[i].VamosAContagiar();
		}
	}

	public void Actualizar() {
		observador.updateTick();
		for (Agente nodo : nodos) {
			Agente.estado viejo = nodo.getEstado();
			nodo.actualizarEstado();
			Agente.estado nuevo = nodo.getEstado();
			if(viejo != nuevo || viejo== Agente.estado.contagiado){
				act=true;
			}
			if (viejo == Agente.estado.Normal) {
				if (nodo.getEstado() == Agente.estado.contagiado) {
					int numeroAmigosContagiados = 0;
					for (Agente ag : nodo.getAmigos()) {
						if (ag.getEstado() == Agente.estado.contagiado) {
							numeroAmigosContagiados++;
						}
					}
					observador.updateContagio(nodo.numeroAmigos(), numeroAmigosContagiados);
				}
			}
		}

	}

	public void deleteDeads() {
		revalidate();
		for (int u = 0; u < N; u++) {
			if (nodos[u].getEstado() == Agente.estado.inservible) {
				int finalU = u;
				edges.removeIf(e -> (e.i == finalU || e.j == finalU));
			}


		}
	}

}

class MainSimulation {
	//Here is some example syntax for the GraphDraw class
	public static void main(String[] args) throws InterruptedException {
		final int numeroNodos = 100;
		final int contagiados_iniciales = 10;
		Observador espia = new Observador(numeroNodos,"2",0);
		Ambiente frame = new Ambiente(
				numeroNodos,
				0.9,
				0.4, 0.6,
				0, 1,
				0, 1,0.2,
				espia
		);

		frame.setSize(1800, 1080);
		frame.initProp();
		for (int i = 0; i < contagiados_iniciales; i++) {
			frame.setAgentState((int)(Math.random()*numeroNodos), Agente.estado.contagiado);
		}

		frame.setVisible(true);
		int t =0;
		while(frame.getAct()){

			frame.setT(t);
			frame.setAct(false);
			frame.Rutine();
			Thread.sleep(2000);
			frame.Actualizar();
			frame.deleteDeads();
			frame.repaint();
			t++;
		}
		System.out.println("sali");
		espia.estadisticas();
		System.out.println("estadisticas listas");
	}
}

