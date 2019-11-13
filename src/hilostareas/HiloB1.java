package hilostareas;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloB1 extends Thread{

    private String lineas = "";    
    private BufferedReader br ;
    
    public HiloB1(String ruta) throws FileNotFoundException, IOException{
        br = new BufferedReader(new FileReader(ruta));
    }
    
    @Override
    public void run(){                
        System.out.println("Leyendo archivo...\n");
        try {
            lineas = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(HiloB1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getLineas(){
        return lineas;
    }

}
