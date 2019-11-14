package hilostareas;

import java.io.*;

public class HiloA1 extends Thread{
    
    String contenido;
    
    //  Devuelve en formato, el texto procesado
    public String getContenido() {
        return contenido;
    }
    public void run() {        
        try {            
            BufferedReader br = new BufferedReader(new FileReader("./src/datos/texto.txt"));
            String texto = "", s = br.readLine();
            while(s != null)
            {
                texto += s;
                s = br.readLine();
            }
            contenido = texto.toLowerCase();
            
        } catch (Exception e) {
            System.out.println("Error durante la ejecucion del hilo1: " + e);
            System.exit(0);
        }
    }    
}