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
    private static final int PUERTO = 8085; 
    Registry registry;
    Remote remote;
    int aux = 0;
    
    public void executeRMI(int id) throws RemoteException, AlreadyBoundException, MalformedURLException{
        
            remote = UnicastRemoteObject.exportObject(new InterfazRMI() {
            
            //  Se sobreescriben los metodos para utilizar las tareas con hilos
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
                return "\n\n------------------\n\n1: Buscar letra\n2: Buscar palabra\n3: Salir\nElige: ";
            };            
        }, 0);
              
        if(id == 0)     //Solo se ejecuta cuando el id es igual a 0
            this.registry = LocateRegistry.createRegistry(PUERTO); //Me cagas mi mB we

        // levanta y vincula el servidor con el objeto calculadora
        System.out.println("Inicio exitoso del server RMI \n");
        String ruta = "//localhost:8085/Calculadora";
        Naming.rebind(ruta, remote);   
    }
    
    //  Detiene el servidor RMI, desvincula con naming
    public void apagaRMI() throws RemoteException, NotBoundException, MalformedURLException{
        String ruta = "//localhost:" + PUERTO + "/Calculadora";
        Naming.unbind(ruta);
    }
}