package hilostareas;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HiloA2 extends Thread{
    
    String contenido;
    String letra;
    List<String> arrayPalabras = new ArrayList<>();

    public HiloA2(String contenido, String letra) {
        this.contenido = contenido;
        this.letra = letra;
    }
    
    public List<String> getArrayPalabras() {
        return arrayPalabras;
    }
    
    public void run() {        
        try {         

                StringTokenizer conteToken = new StringTokenizer(contenido);
                String auxT;
                
                while(conteToken.hasMoreTokens())
                {
                    auxT = conteToken.nextToken();
                    if(auxT.startsWith(letra)) {
                        arrayPalabras.add(auxT);
                    }
                }
                
                //System.out.println("Las palabras son:" + arrayPalabras);
        } catch (Exception e) {
            System.out.println("Error durante la ejecucion hilo 2: " + e);
            System.exit(0);
        }
    }    
}
