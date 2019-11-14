package funciontareas;

import hilostareas.*;

// Controlador de los hilos para buscar una letra, es decir el procesamiento de la informacion
public class Tarea3A{    
    public int tareaA(String letra) throws InterruptedException{
        HiloA1 hilo1 = new HiloA1();    
        hilo1.start();
        hilo1.join();
        
        HiloA2 hilo2 = new HiloA2(hilo1.getContenido(),letra.toLowerCase());
        hilo2.start();
        hilo2.join();
        
        HiloA3 hilo3 = new HiloA3(hilo2.getArrayPalabras());
        hilo3.start();
        hilo3.join();
        
        return (hilo3.getNumApa());        
    }
}
