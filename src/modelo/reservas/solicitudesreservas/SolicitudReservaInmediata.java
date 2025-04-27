package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import anotacion.Programacion2;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.vehiculos.Vehiculo;
import list.IList;
import list.ArrayList;

@Programacion2 (
		nombreAutor1 = "Guillermo",
		apellidoAutor1 = "de Rada Riolobos",
		emailUPMAutor1 = "g.derada@alumnos.upm.es",
		nombreAutor2 = "Alfonso",
		apellidoAutor2 = "Fernández Sola", 
		emailUPMAutor2 = "alfonso.fernandez.sola@alumnos.upm.es"
	)

public class SolicitudReservaInmediata extends SolicitudReserva{

	private int radio;

	public SolicitudReservaInmediata(int i, int j, LocalDateTime tI, 
			LocalDateTime tF, Vehiculo vehiculo, int radio) {
		super(i, j, tI, tF, vehiculo);
		this.radio = radio;
	}

	/**
	 * Sobrescribe el método heredado extendiendo su definición de manera que comprueba 
	 * también que radio>0 y que existe alguna zona en la localidad en el límite del radio
	 * @param gestorLocalidad Gestor de localidad a comprobar
	 */
	public boolean esValida(GestorLocalidad gestorLocalidad) {
		boolean res = !(this.getIZona() + this.getJZona() < radio 
				&& gestorLocalidad.getRadioMaxI() + this.getJZona() - this.getIZona() < radio
				&& gestorLocalidad.getRadioMaxJ() + this.getIZona() - this.getJZona() < radio
				&& gestorLocalidad.getRadioMaxI() - this.getIZona() + gestorLocalidad.getRadioMaxJ() - this.getJZona() < radio);
		return super.esValida(gestorLocalidad) && res && this.radio > 0;
	}//Explicación del algoritmo al final (*)

	/**
	 * Comprueba si, después de ejecutarse el método del padre, se ha asignado un hueco a la solicitud,
	 *  o por el contrario, el atributo sigue valiendo null, en cuyo caso,
	 *  se busca un hueco en un gestor de zona dentro del radio máximo establecido para esta solicitud
	 *  @param gestor Gestor de localidad en el que se desea realizar la reserva
	 */
	//Queda pendiente encapsular la parte del código que se repite 4 veces
	public void gestionarSolicitudReserva(GestorLocalidad gestor){

		System.out.println("Entrando en gestionarSolicitudReserva");
		double precio;						//Almacena el mejor precio dentro de un radio y se actualiza cada vez que encuenta otro mejor
		int[] candidato = {-1, -1};			//Almacena las coordenadas de la plaza (x, y) candidata a ser elegida (por precio) en un mismo radio

		super.gestionarSolicitudReserva(gestor);

		if (this.esValida(gestor) && this.getHueco() == null) {		//Comprueba que la solicitud sea valida y que no haya un hueco asignado todavia

			System.out.println("Entrando en el if");
			int i = this.getIZona();		//Será constante 
			int j = this.getJZona();		//Será constante
			int x, y;						//Varía con cada iteracion del segundo for. Para hacer legible el codigo

			System.out.println("Centro: " + i + ", " + j);
			
			boolean encontrado = false; //Condición de parada

			for(int r = 1; r <= radio && !encontrado; r++) {

				System.out.println("radio = " + r);
				precio = -1;				//Cada iteración restablece el valor del precio al valor por defecto
				

				for(int n = 0;  n < r; n++) {		//recorre la arista inferior izquierda del rombo de radio r y centro (i, j)
					x = i + n;		//modifica las coordenadas x e y
					y = j - r + n;	//modifica las coordenadas x e y
								
					System.out.println("     Casilla: " + x + ", " + y);

					if(gestor.existeZona(x, y)) {			//comprueba que exista la zona y si el precio es mejor que el anterior mejor precio, establece la nueva plaza como candidata
						System.out.println("Existe la Casilla: " + x + ", " + y + "Existe hueco: " + gestor.getGestorZona(x, y).existeHueco(this.getTInicial(), this.getTFinal()));
						
						if(gestor.getGestorZona(x, y).existeHueco(this.getTInicial(), this.getTFinal()) 		//Si en una plaza (x, y) hay un hueco y tiene mejor precio que la anterior la elige
								&& (gestor.getGestorZona(x, y).getPrecio() < precio
										|| precio == -1)) {				//este OR es necesario, ya que si es la primera plaza libre, no habría un mejor precio previo
							candidato[0] = x;
							candidato[1] = y;
							precio = gestor.getGestorZona(x, y).getPrecio();
							System.out.println("candidato: " + x + ", " + y);
							System.out.println("precio: " + precio);
							
						}
					}
				}

				for(int n = 0;  n < r; n++) {		//recorre la arista inferior derecha del rombo de radio r y centro (i, j)
					x = i + r - n;					//modifica las coordenadas x e y variables
					y = j + n;						//modifica las coordenadas x e y variables

					System.out.println("     Casilla: " + x + ", " + y);

					if(gestor.existeZona(x, y)) {			//comprueba que exista la zona y si el precio es mejor que el anterior mejor precio, establece la nueva plaza como candidata
						System.out.println("Existe la Casilla: " + x + ", " + y + "Existe hueco: " + gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()));
						
						if(gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()) 		//Si en una plaza (x, y) hay un hueco y tiene mejor precio que la anterior la elige
								&& (gestor.getGestorZona(x, y).getPrecio() < precio
										|| precio == -1)) {				//este OR es necesario, ya que si es la primera plaza libre, no habría un mejor precio previo
							candidato[0] = x;
							candidato[1] = y;
							precio = gestor.getGestorZona(x, y).getPrecio();
							System.out.println("candidato: " + x + ", " + y);
							System.out.println("precio: " + precio);

						}
					}
				}

				for(int n = 0;  n < r; n++) {		//recorre la arista superior derecha del rombo de radio r y centro (i, j)
					x = i - n;					//modifica las coordenadas x e y variables
					y = j + r - n;						//modifica las coordenadas x e y variables

					System.out.println("     Casilla: " + x + ", " + y);

					if(gestor.existeZona(x, y)) {			//comprueba que exista la zona y si el precio es mejor que el anterior mejor precio, establece la nueva plaza como candidata
						System.out.println("Existe la Casilla: " + x + ", " + y + "Existe hueco: " + gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()));
						if(gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()) 		//Si en una plaza (x, y) hay un hueco y tiene mejor precio que la anterior la elige
								&& (gestor.getGestorZona(x, y).getPrecio() < precio
										|| precio == -1)) {				//este OR es necesario, ya que si es la primera plaza libre, no habría un mejor precio previo
							candidato[0] = x;
							candidato[1] = y;
							precio = gestor.getGestorZona(x, y).getPrecio();
							System.out.println("candidato: " + x + ", " + y);
							System.out.println("precio: " + precio);

						}
					}
				}

				for(int n = 0;  n < r; n++) {		//recorre la arista superior izquierda del rombo de radio r y centro (i, j)
					x = i - r + n;					//modifica las coordenadas x e y variables
					y = j - n;						//modifica las coordenadas x e y variables

					System.out.println("     Casilla: " + x + ", " + y);

					if(gestor.existeZona(x, y)) {			//comprueba que exista la zona
						System.out.println("Existe la Casilla: " + x + ", " + y + "Existe hueco: " + gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()));
						
						if(gestor.getGestorZona(x, y).existeHueco(getTInicial(), getTFinal()) 		//Si en una plaza (x, y) hay un hueco y tiene mejor precio que la anterior la elige
								&& (gestor.getGestorZona(x, y).getPrecio() < precio
										|| precio == -1)) {				//este OR es necesario, ya que si es la primera plaza libre, no habría un mejor precio previo
							candidato[0] = x;
							candidato[1] = y;
							precio = gestor.getGestorZona(x, y).getPrecio();
							System.out.println("candidato: " + x + ", " + y);
							System.out.println("precio: " + precio);

						}
					}
				}

				if(candidato[0] != -1) {			//Si hay una plaza candidata (está libre) termina el bucle y la reserva
					this.setGestorZona(gestor.getGestorZona(candidato[0], candidato[1]));
					this.setHueco(gestor.getGestorZona(candidato[0], candidato[1]).reservarHueco(this.getTInicial(), this.getTFinal()));
					System.out.println("El hueco de esta gestion es: " + this.getHueco());
					encontrado = true;
				}
			}		

		}
	}


}
/*
	(*) Lo que hacemos en la primera línea es comprobar si la distancia al origen es mayor o igual (es decir, no es menor)
	que el radio. Si esto no se cumple, lo que hacemos se puede ver como desplazar el origen a otra esquina de la matriz
	del gestor de localidad e invertir una (o ambas en caso de la esquina opuesta al origen) y comprobar esta nueva distancia
 */
