package serverrmi;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

//  Se define la interfaz a utilizar y los metodos empleados
public interface InterfazRMI extends Remote{
    int buscaLetra(String pal) throws RemoteException, InterruptedException;
    int buscaPalabra(String pal) throws RemoteException, IOException, InterruptedException;    
    String mostrarMenu() throws RemoteException;
}