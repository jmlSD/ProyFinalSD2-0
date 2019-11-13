package hilostareas;

public class HiloB2 extends Thread{
    private String entrada;
    
    public HiloB2(String entrada){        
        this.entrada = entrada;
    }
        
    public void setEntrada(String entrada){
        this.entrada = entrada;
    }
    
    public int buscaPalabra(String palabra){
        int nPalabra = 0;
        this.entrada = this.entrada.toLowerCase();
        palabra = palabra.toLowerCase();
        while( this.entrada.indexOf(palabra) > -1){
            this.entrada = this.entrada.substring(this.entrada.indexOf(palabra) + palabra.length(), this.entrada.length());
            nPalabra++;
        }        
        return nPalabra;
    }
}
