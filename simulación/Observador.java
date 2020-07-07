import org.apache.commons.collections.Bag;
import org.apache.commons.collections.bag.HashBag;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Formatter;
import java.util.Set;

class Observador {

	private int numeroContagiados;
	private int[] contagioPorAmigos;

	public void setTick(int tick) {
		this.tick = tick;
	}

	private int tick;
	public int[] contagioPorTick;
	private int numNodos;
	private Bag porcentajeAmigosContagiados;
	JFreeChart barChart;
	static int Contador = 0;
	private int id;
	public String nombreCaso;

	Observador(int numeroNodos, String nombreCaso, int ticks) {
		contagioPorAmigos = new int[numeroNodos];
		tick = 0;
		contagioPorTick = new int[ticks];
		numeroContagiados = 0;
		numNodos = numeroNodos;
		porcentajeAmigosContagiados = new HashBag();
		id = Contador++;
		this.nombreCaso = nombreCaso;
	}

	static void compararTicks(Observador[] observadores, int width, int height) {
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (Observador obs : observadores) {
			for (int i = 0; i < obs.tick; i++) {
				barDataset.addValue(
						(obs.contagioPorTick[i]),
						obs.nombreCaso,
						String.valueOf(i)
				);
			}
		}
		JFreeChart barChart = ChartFactory.createBarChart(
				"Comparación contagios por tick",
				"casos", "numero por turno",
				barDataset, PlotOrientation.VERTICAL,
				true, true, false
		);
		File BarChart = new File("comparaciónTicks.jpeg");
		try {
			ChartUtils.saveChartAsJPEG(BarChart, barChart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void estadisticas() {
		final int width = 1080;    /* Width of the image */
		final int height = 960;   /* Height of the image */

		createBarChar(width, height);
		createPieChar(width, height);
		createInfectedFriendsChar(width, height);
	}

	private void createPieChar(int width, int height) {
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (int i = 0; i < contagioPorTick.length; i++) {
			pieDataset.setValue("turno " + i, new Double(contagioPorTick[i]));
		}

		JFreeChart pieChart = ChartFactory.createPieChart(
				"Infectados por turno",   // chart title
				pieDataset,          // data
				false,             // include legend
				true,
				false
		);
		File PieChart = new File("PieChart" + id + ".jpeg");
		try {
			ChartUtils.saveChartAsJPEG(PieChart, pieChart, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createBarChar(int width, int height) {
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		for (int i = 0; i < numNodos; i++) {
			if (contagioPorAmigos[i] == 0) continue;
			barDataset.addValue(
					(contagioPorAmigos[i] / (double) numNodos) * 100,
					"Porcentaje",
					String.valueOf(i)
			);
		}
		barChart = ChartFactory.createBarChart(
				"% Contagio por # conexiones",
				"Category", "Probabilidad Contagio",
				barDataset, PlotOrientation.VERTICAL,
				true, true, false
		);
		File BarChart = new File("BarChart" + id + ".jpeg");
		try {
			ChartUtils.saveChartAsJPEG(BarChart, barChart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void createInfectedFriendsChar(int width, int height) {
		DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
		Set<Double> valoresUnicos = porcentajeAmigosContagiados.uniqueSet();
		for (Double valor : valoresUnicos) {
			barDataset.addValue(valor, Integer.valueOf(porcentajeAmigosContagiados.getCount(valor)), "valores");
		}
		JFreeChart barChart = ChartFactory.createBarChart(
				"numero de vece que se presenta (amigos cont / # amigos)",
				"numero repeticiones",
				"amigos contagiados / # amigos",
				barDataset, PlotOrientation.VERTICAL,
				true, true, false
		);
		File BarChart = new File("relacionAmigosContagiados" + id + ".jpeg");
		try {
			ChartUtils.saveChartAsJPEG(BarChart, barChart, width, height);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTick() {
		tick++;
	}

	public void updateContagio(
			int numeroConexiones,
			int numeroConexionesContagiadas
	) {
		contagioPorAmigos[numeroConexiones]++;
		contagioPorTick[tick]++;
		numeroContagiados++;
		porcentajeAmigosContagiados.add(numeroConexionesContagiadas / (double) numeroConexiones);
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

		for (int i = 0; i < numNodos; i++) {
			formatter.format("Contagiados dados %3d amigos: %3d \n", i, contagioPorAmigos[i]);
		}
		for (int i = 0; i < tick; i++) {
			formatter.format("Contagios en el %3d tick: %3d \n", i, contagioPorTick[i]);
		}
		for (int i = 0; i < numNodos; i++) {
			double temp = contagioPorAmigos[i] / (double) numNodos;
			formatter.format(
					"Porcentaje de contagio por %3d amigo/s: %2$3f",
					i, temp
			);
		}
		return sb.toString();
	}
}