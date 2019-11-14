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
    
    public void executeRMI(int id) throws RemoteException, AlreadyBoundException, MalformedURLException{
        
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
              
        System.out.println("Estoy ante de registro");
        if(registry == null)
            this.registry = LocateRegistry.createRegistry(PUERTO); //Me cagas mi mB we
        System.out.println("Despues de registro");
        //System.out.println("Llgue a ligantion e id es: " + id);
        String ruta = "//localhost:8085/Calculadora" + id;
        //System.out.println("Ruta = " + ruta);
        //registry.bind(ruta, remote);
        Naming.rebind(ruta, remote);   
        //System.out.println("Pase e a ligantion");
    }
    
    public void creaReg() throws RemoteException {
        this.registry = LocateRegistry.createRegistry(PUERTO);
    }
    
    public void apagaRMI(int id) throws RemoteException, NotBoundException, MalformedURLException{
        String ruta = "//localhost:" + PUERTO + "/Calculadora" + id;
        Naming.unbind(ruta);
        System.out.println("Pase naming");
        //registry.unbind("Calculadora" + id);
        System.out.println("Pase unbindg");
        registry = null;
        //registry.unbind("//localhost:" + PUERTO + "/Calculadora" + id);
    }
}