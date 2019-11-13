package funciontareas;

import hilostareas.HiloA1;
import hilostareas.HiloB2;
import java.io.IOException;

public class Tarea3B {
    
    private int numeroApariciones = 0;
    
    public void tareaB(String palabra) throws IOException, InterruptedException {       
        HiloA1 h1 = new HiloA1();      
        
        h1.setPriority(10);
        h1.start();
        h1.join();
        
        HiloB2 h2 = new HiloB2(h1.getContenido());
        h2.start();
        this.numeroApariciones = h2.buscaPalabra(palabra);        
        h2.join();        
    }
    
    public int getApariciones(){
        return this.numeroApariciones;
    }
}
