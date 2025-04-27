package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.IList;
import list.ArrayList;
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
		this.plazas = new Plaza[noPlazas];
		for(int k = 0; k < noPlazas; k++) {
			plazas[k] = new Plaza(k);
		}
		this.gestorHuecos = new GestorHuecos(plazas);
		this.listaEspera = new ArrayList<SolicitudReservaAnticipada>();
		this.huecosReservados = new ArrayList<Hueco>();
		
		
	}
	
	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		if(gestorHuecos.existeHueco(tI, tF)) {
			Hueco res = gestorHuecos.reservarHueco(tI, tF);
			huecosReservados.add(huecosReservados.size(), res);
			return res;
		}else {
			return null;
		}
	}
	
	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		return gestorHuecos.existeHueco(tI, tF);
	}
	
	
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {
		listaEspera.add(listaEspera.size(), solicitud);
	}
	
	public boolean existeHuecoReservado(Hueco hueco) {
		return huecosReservados.indexOf(hueco) != -1;
	}
	
	//TO-DO alumno opcionales
	
	public void liberarHueco(Hueco hueco) {
		huecosReservados.remove(hueco);
		gestorHuecos.liberarHueco(hueco);
	}

	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {
		IList<SolicitudReservaAnticipada> res = new ArrayList<SolicitudReservaAnticipada>();
		IList<Hueco> huecosALiberar = new ArrayList<Hueco>();
		for(int i = listaEspera.size() - 1; i > 0; i--) {
			if(gestorHuecos.existeHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal())) {
				huecosALiberar.add(huecosALiberar.size(), 
						gestorHuecos.reservarHueco(listaEspera.get(i).getTInicial(), listaEspera.get(i).getTFinal()));
				res.add(0, listaEspera.get(i));
				listaEspera.remove(listaEspera.get(i));
				System.out.println("a-- " + i +  " -- " + res.toString());
			}else {
				System.out.println("b-- " + i +  " -- " + res.toString());
			}
		}
		int huecosALiberarSize = huecosALiberar.size();
		for(int i = 0; i < huecosALiberarSize; i++) {
			gestorHuecos.liberarHueco(huecosALiberar.get(i));
			}
		
		return res;
	}

	


}
