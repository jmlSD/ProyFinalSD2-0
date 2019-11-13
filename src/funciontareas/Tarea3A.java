package funciontareas;

import hilostareas.HiloA1;
import hilostareas.HiloA2;
import hilostareas.HiloA3;


public class Tarea3A{    
    public int tareaA(String letra) throws InterruptedException{
        HiloA1 hilo1 = new HiloA1();    
        hilo1.setPriority(10);
        hilo1.start();
        hilo1.join();
        
        HiloA2 hilo2 = new HiloA2(hilo1.getContenido(),letra.toLowerCase());
        hilo2.setPriority(10);
        hilo2.start();
        hilo2.join();
        
        HiloA3 hilo3 = new HiloA3(hilo2.getArrayPalabras());
        hilo3.setPriority(10);
        hilo3.start();
        hilo3.join();
        
        return (hilo3.getNumApa());        
    }
}
