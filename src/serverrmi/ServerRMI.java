package serverrmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import funciontareas.*;
import java.io.IOException;

public class ServerRMI {
    private static final int PUERTO = 8085; //Si cambias aquí el puerto, recuerda cambiarlo en el cliente
    Registry registry;
    Remote remote;
    int aux = 0;

    public ServerRMI() {
        this.registry = null;
    }
    
    public void executeRMI() throws RemoteException, AlreadyBoundException, MalformedURLException{
        
            remote = UnicastRemoteObject.exportObject(new InterfazRMI() {
            
            @Override
            public int buscaLetra(String pal) throws RemoteException, InterruptedException {
                Tarea3A tA = new Tarea3A();
                int matches = tA.tareaA(pal);
                                
                return matches;
            };

            @Override
            public int buscaPalabra(String pal) throws RemoteException, IOException, InterruptedException {                
                Tarea3B tB = new Tarea3B();
                tB.tareaB(pal);
                int matches = tB.getApariciones();
                
                return matches;
            };

            @Override
            public String mostrarMenu() throws RemoteException {
                return "\n\n------------------\n\n[1] => Buscar letra\n[2] => Buscar palabra\n[3] => Salir\nElige: ";
            };            
        }, 0);
      
                this.registry = LocateRegistry.createRegistry(PUERTO);
        
        System.out.println("Llgue a ligantion");
        //registry.bind("//localhost:8085/Calculadora", remote);
        Naming.rebind("//localhost:8085/Calculadora",remote);   
        System.out.println("Pase e a ligantion");
    }
    
    public void apagaRMI() throws RemoteException, NotBoundException, MalformedURLException{
        Naming.rebind("//localhost:" + PUERTO + "/Calculadora", registry);
        registry.rebind("//localhost:" + PUERTO + "/Calculadora", registry);
    }
}