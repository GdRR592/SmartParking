package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;
import modelo.vehiculos.Vehiculo;

public class TestGestorLocalidadOpcional {

	@Rule //Se establece un time out general para todos los tests. Se debe comentar esta línea y la de abajo para depurar
	public TestRule  globalTimeout = new DisableOnDebug(Timeout.millis(100)); // 100 milisegundos máximos por test	


	private GestorLocalidad gestorLocalidad;
	private int[][] plazas = {{1, 2},
			{3, 4}};
	private double[][] precios = {{1.0, 1.0}, {1.0, 2.0}};


	@Before
	public void setUp() {
		gestorLocalidad = new GestorLocalidad(plazas, precios);
	}

	private void aplazarSolicitud(GestorZona gestor, int i, int j, int hi, int mi, int hf, int mf, String matricula) {
		LocalDateTime tI = LocalDateTime.of(2021, 10, 5, hi, mi);
		LocalDateTime tF = LocalDateTime.of(2021, 10, 5, hf, mf);
		Vehiculo car = new Vehiculo(matricula);

		SolicitudReservaAnticipada solicitud = new SolicitudReservaAnticipada(i, j, tI, tF, car);
		gestor.meterEnListaEspera(solicitud);
	}

	/**
	 * Comprueba el método getSolicitudesAtendidasListaEspera() para todas las zonas
	 */
	@Test
	public void testGetSolicitudesAtendidasListaEspera() {
		for (int i=0; i<plazas.length; i++)
			for (int j=0; j<plazas[0].length; j++) {
				System.out.println(".-----------------------NUEVA ITERACION------------------------.");
				Hueco hueco = null;
				Hueco hueco2 = null;
				GestorZona gestor = null;
				// se reserva todo el tiempo en todas las plazas de la zona i, j
				for (int k=0; k<plazas[i][j]; k++) {
					gestor = gestorLocalidad.getGestorZona(i, j);
					LocalDateTime tI = LocalDateTime.of(2021, 10, 5, 0, 0);
					LocalDateTime tF = LocalDateTime.of(2021, 10, 5, 2, 30);
					hueco = gestor.reservarHueco(tI, tF);
					System.out.println("Hueco reservado: " + hueco.toString());
					
					 
					LocalDateTime tI1 = LocalDateTime.of(2021, 10, 5, 2, 30);
					LocalDateTime tF1 = LocalDateTime.of(2021, 10, 5, 3, 0);
					hueco2 = gestor.reservarHueco(tI1, tF1);
					System.out.println("Hueco reservado: " + hueco.toString());
					if(hueco2 == null) {
						System.out.println("RIP hueco2");
					}else {
						System.out.println("Hueco reservado 2: " + hueco2.toString());
					}
				}
				// se crea la lista de espera para la zona i, j
				aplazarSolicitud(gestor, i, j, 0, 30, 2, 0, "car1");
				aplazarSolicitud(gestor, i, j, 0, 45, 1, 0, "car2");
				aplazarSolicitud(gestor, i, j, 2, 30, 2, 45, "car3");
				aplazarSolicitud(gestor, i, j, 0, 0, 0, 15, "car4");
				
				// se libera un hueco en una plaza de la zona i, j
				gestor.liberarHueco(hueco);
				System.out.println("Hueco liberado: " + hueco.toString() );
				assertEquals("No se han devuelto las solicitudes atendidas correctas para la zona " 
						+ i + " " + j, "[(Sol:"+ i + " " + j + " 00:30 02:00 car1),\n"
								+ "(Sol:"+ i + " " + j + " 00:00 00:15 car4)]", gestorLocalidad.getSolicitudesAtendidasListaEspera(i, j).toString());
			}
	}



}
