package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.ArrayList;
import list.IList;
import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

public class GestorZona {
	private int iZona;
	private int jZona;
	private Plaza[] plazas;
	private double precio;
	private IList<SolicitudReservaAnticipada> listaEspera;
	private GestorHuecos gestorHuecos;
	private IList<Hueco> huecosReservados;

	public int getI() {
		return iZona;
	}

	public int getJ() {
		return jZona;
	}

	public double getPrecio() {
		return precio;
	}

	public String getId() {
		return "z" + iZona + ":" + jZona;
	}

	public String getEstadoHuecosLibres() {
		return this.gestorHuecos.toString();
	}

	public String getEstadoHuecosReservados() {
		return this.huecosReservados.toString();
	}

	public String getListaEspera() {
		return this.listaEspera.toString();
	}

	public String getPlazas() {
		return Arrays.toString(this.plazas);
	}

	public String toString() {
		return getId() + ": " + getEstadoHuecosReservados();
	}

	//TO-DO alumno obligatorios

	public GestorZona(int i, int j, int noPlazas, double precio) {
		this.iZona = i;
		this.jZona = j;
		this.precio = precio;
		this.plazas = new Plaza[noPlazas];
		for(int k = 0; k < noPlazas; k++) {
			plazas[k] = new Plaza(k);
		}
		this.gestorHuecos = new GestorHuecos(plazas);
		this.huecosReservados = new ArrayList<>();
		this.listaEspera = new ArrayList<>();
	}
	/**
	 * Intenta reservar un hueco en el intervalo temporal dado utilizando el gestor de huecos. 
	 * Si existe, lo añade al final de la lista de huecos reservados y lo devuelve. 
	 * En caso contrario, devuelve null.
	 * @param tI Extremo inicial del intervalo
	 * @param tF Extremo final del intervalo
	 */
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		if(gestorHuecos.existeHueco(tI, tF)) {
			Hueco res = gestorHuecos.reservarHueco(tI, tF);
			huecosReservados.add(huecosReservados.size(), res);
			return res;
		}
		else {
			return null;
		}
	}

	/**
	 * Indica si existe un hueco libre en el intervalo temporal dado
	 * @param tI Extremo inicial del intervalo
	 * @param tF Extremo final del intervalo
	 */
	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		return gestorHuecos.existeHueco(tI, tF);
	}

	/**
	 * Añade la solicitud dada al final de la lista de espera
	 * @param solicitud Solicitud dada
	 */
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {
		listaEspera.add(listaEspera.size(), solicitud);
	}

	/**
	 * Indica si el hueco dado está reservado en esta zona
	 * @param hueco Hueco dado
	 */
	public boolean existeHuecoReservado(Hueco hueco) {
		return huecosReservados.indexOf(hueco) != -1;
	}

	//TO-DO alumno opcionales

	/**
	 * Extrae el hueco dado de la lista de huecos reservados y lo libera en el gestor de huecos
	 * @param hueco Hueco dado
	 */
	public void liberarHueco(Hueco hueco) {
		huecosReservados.remove(hueco);
		gestorHuecos.liberarHueco(hueco);
	}

	/**
	 * PRE: las solicitudes de la lista de espera son válidas
	 * Devuelve las solicitudes de reserva que pueden ser atendidas en esta zona y las elimina de la lista de espera
	 */
	/*public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
			IList<SolicitudReservaAnticipada> res = new ArrayList<SolicitudReservaAnticipada>();

			/* Crea un nuevo gestorHuecos en el que se ocupa un hueco por cada solicitud de la lista de espera.
			De esta forma, no se aceptan dos solicitudes que pueden atenderse (por existir un único hueco libre
			con las características requeridas por tal solicitud) que, sin embargo, coinciden, de forma que hipotéticamente
			reserven la misma plaza única libre ambos en paralelo. */
	/*
			GestorHuecos gestorHuecosPrima = new GestorHuecos(plazas);

			/* Al recorrer de alante hacia atrás el bucle, se desplazan solicitudes que podrían ser atendidas. Decrementando el contador
			  no siempre se consigue enmendar esto, así que lo mejor es crear una lista con los íncides a eliminar de la
			  lista de espera y eliminarlos una vez recorrida la lista de espera */
	/*
			IList<Integer> indicesAEliminar = new ArrayList<>();
			for(int i = 0; i < listaEspera.size(); i++) {
				if(gestorHuecos.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal()) 
						&& gestorHuecosPrima.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal())) {

					gestorHuecosPrima.reservarHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal());
					res.add(res.size(), listaEspera.get(i));
					indicesAEliminar.add(indicesAEliminar.size(), i);
				}
			}

			/* Ahora sí se puede recorrer de atrás hacia delante sin problema */
	/*
			for (int i = indicesAEliminar.size() - 1; i >= 0; i--) {
		        int indice = indicesAEliminar.get(i);
		        listaEspera.removeElementAt(indice);
		    }
			return res;
		}*/

	/*public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		IList<SolicitudReservaAnticipada> res = new ArrayList<SolicitudReservaAnticipada>();

		/* Crea un nuevo gestorHuecos en el que se ocupa un hueco por cada solicitud de la lista de espera.
			De esta forma, no se aceptan dos solicitudes que pueden atenderse (por existir un único hueco libre
			con las características requeridas por tal solicitud) que, sin embargo, coinciden, de forma que hipotéticamente
			reserven la misma plaza única libre ambos en paralelo. */

		/*GestorHuecos gestorHuecosPrima = new GestorHuecos(plazas);

		/* Al recorrer de alante hacia atrás el bucle, se desplazan solicitudes que podrían ser atendidas. Decrementando el contador
			  no siempre se consigue enmendar esto, así que lo mejor es crear una lista con los íncides a eliminar de la
			  lista de espera y eliminarlos una vez recorrida la lista de espera */

		/*IList<Integer> indicesAEliminar = new ArrayList<>();
		for(int i = 0; i < listaEspera.size(); i++) {
			if(gestorHuecos.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal())
					&& gestorHuecosPrima.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal())) {
				gestorHuecosPrima.reservarHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal());
				res.add(res.size(), listaEspera.get(i));
				indicesAEliminar.add(indicesAEliminar.size(), i);

			}
		}

		/* Ahora sí se puede recorrer de atrás hacia delante sin problema */

		/*for (int i = indicesAEliminar.size() - 1; i >= 0; i--) {
			int indice = indicesAEliminar.get(i);
			listaEspera.removeElementAt(indice);
		}
		return res;
	}*/

	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		IList<SolicitudReservaAnticipada> res = new ArrayList<SolicitudReservaAnticipada>();
		IList<Hueco> huecosALiberar = new ArrayList<Hueco>();
		IList<Integer> indicesAEliminar = new ArrayList<>();
		for(int i = 0; i < listaEspera.size(); i++) {
			if(gestorHuecos.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal())) {				
				huecosALiberar.add(huecosALiberar.size(), 
						gestorHuecos.reservarHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal()));
				res.add(res.size(), listaEspera.get(i));
				indicesAEliminar.add(indicesAEliminar.size(), i);
			}
		}
		int huecosALiberarSize = huecosALiberar.size();
		for(int i = 0; i < huecosALiberarSize; i++) {
			gestorHuecos.liberarHueco(huecosALiberar.get(0));
		}
		
		for (int i = indicesAEliminar.size() - 1; i >= 0; i--) {
			int indice = indicesAEliminar.get(i);
			listaEspera.removeElementAt(indice);
		}
		
		return res;
	}


}
