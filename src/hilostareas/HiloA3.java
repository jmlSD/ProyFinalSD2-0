package hilostareas;

import java.util.List;

public class HiloA3 extends Thread{
    List<String> arrayConte;
    int numApa;

    public HiloA3(List<String> arrayConte) {
        this.arrayConte = arrayConte;
    }

    public int getNumApa() {
        return numApa;
    }
        
    public void run() {        
        try {   
            numApa = arrayConte.size();
        } catch (Exception e) {
            System.out.println("Error durante la ejecucion hilo 3: " + e);
            System.exit(0);
        }
    }    
}
