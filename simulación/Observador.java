import javax.print.DocFlavor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

class Observador {

	private int numeroContagiados;
	private int[] contagioPorAmigos;
	private int tick;
	private ArrayDeque<Integer> contagioPorTick;

	Observador(int numeroNodos) {
		contagioPorAmigos = new int[numeroNodos];
		tick = 0;
		contagioPorTick = new ArrayDeque<>();
		numeroContagiados = 0;
	}

	public void updateTick() {
		tick++;
		contagioPorTick.add(0);
	}

	public void updateContagio(int numeroConexiones) {
		contagioPorAmigos[numeroConexiones]++;
		contagioPorTick.addLast(contagioPorTick.removeLast() + 1);
		numeroContagiados++;
	}

	private double contagioPorTurno() {
		return (double) numeroContagiados / tick;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb);
		formatter.format("Contagiados: %3d \n", numeroContagiados);
		formatter.format("Contagios por ronda: %3f \n", contagioPorTurno());

		for (int i = 0; i < contagioPorAmigos.length; i++) {
			formatter.format("Contagiados dados %3d amigos: %3d \n", i, contagioPorAmigos[i]);
		}
		for (int i = 0; i < tick; i++) {
			formatter.format("Contagios en el %3d tick: %3d \n", i, (contagioPorTick.toArray())[i]);
		}
		for (int i = 0; i < contagioPorAmigos.length; i++) {
			double temp = contagioPorAmigos[i] / (double)contagioPorAmigos.length;
			formatter.format(
					"Porcentaje de contagio por %1$3d amigo/s: %2$3f",
					i, temp
			);
		}
		return sb.toString();
	}
}