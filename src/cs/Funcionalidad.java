package cs;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import serverrmi.*;
import serverrpc.*;
import serversockets.ServidorHilos;

public class Funcionalidad {
    // Variables para servidor sockets
    ServidorHilos ssck;
    Socket conexionSCK;
    DataInputStream dis;
    DataOutputStream dos;
    String menuSCK;
    // Variables para servidor RPC
    XmlRpcClient conexionRPC;    
    Server_rpc srpc;
    Object menuRPC;
    // Variables para servidor RMI
    InterfazRMI interfazRMI;
    ServerRMI sRMI;
    Registry registry;
    String menuRMI;
    // Variables de control para usuarios, arriba es para createRegistry
    int clientssck = 0, clientrpc = 0, arribaRMI = 0, clientRMI = 0;
    //declaracion de numeros de puertos para cada servidor
    private static final int PuertoRMi = 8085, PuertoRPC = 8081, PuertoSCK = 8084; 
    
    public String desplegaMenu(){  //Menu principal
        return "----- Bienvenido al Sistema Jhonnys -----\n"
                + "¿Con qué tipo de servidor desear trabajar?\n"
                + "1.- Sockets\n"
                + "2.- RPC\n"
                + "3.- RMI\n\n"
                + "Presiona el numero correspondiente a la opción: ";
    }
    
    public String solicitudMenu(int opc) throws IOException{
                    
        if(opc == 1){  //averiguamos si existe una conexion, si no, la creamos
            if(conexionSCK == null) {
                this.conexionSCK = conectaSCK(); 
                this.dis = new DataInputStream(conexionSCK.getInputStream());
                //Se obtiene el menu del servidor sockets
                menuSCK = dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n";
            }
            System.out.println("Conexion exitosa al servidor Sockets");
            clientssck++;  //Se incrementa el numero de usuarios activos
            return menuSCK;
            
        }else if(opc == 2){     //averiguamos si existe una conexion, si no, la creamos
            try{
                if(conexionRPC == null){
                    this.conexionRPC = conectaRPC();
                //Se obtiene el menu del servidor sockets
                menuRPC = conexionRPC.execute("myServerRPC.desplegaMenu", new Vector<Integer>());
                }
            }catch(Exception ex){
                System.err.println("Funcionalidad: " + ex);
            }
            
            System.out.println("Conexion exitosa al servidor RPC");
            clientrpc++;  //Se incrementa el numero de usuarios activos
            return (String) menuRPC;
            
        }else if(opc == 3){     //averiguamos si existe una conexion, si no, la creamos       
            try{
                if(interfazRMI == null) {
                    this.interfazRMI = conectaRMI(arribaRMI);
                    //Se obtiene el menu del servidor sockets
                    menuRMI = (String) interfazRMI.mostrarMenu();
                    arribaRMI += 1; //Le indicamos que ya se hizo el createRegistry
                }   
            } catch (Exception ex) {
                System.err.println("Funcionalidad: " + ex);
            }
            System.out.println("Conexion exitosa al servidor RMI"); 
            clientRMI++;    //Se incrementa el numero de usuarios activos
            return menuRMI;
            
        }else{
            return "Opcion no valida";
        }        
    }
    
    
    public Socket conectaSCK() throws IOException {    //Levanta el servidor Sockets, hace una conexion y ejecuta el menu de secundario
        this.ssck = new ServidorHilos();
        ssck.levantaSCK();
        Socket sk = new Socket("localhost", PuertoSCK);
        ssck.executeSCK(0);        
        return sk;
    }
    
    // Funcion de ejecucion de para tarea3A o 3B con Sockets
    public String procesamientoSCK(int opc, String x1) throws RemoteException, NotBoundException, 
                        MalformedURLException, IOException, InterruptedException{        

        int resultado;
        this.dos  = new DataOutputStream(conexionSCK.getOutputStream());
        
        if(opc < 3){
        dos.writeInt(opc);      //Pasamos primero si buscamos letra o palabra
        this.ssck.executeSCK(1, conexionSCK);
        
        String s = x1;
        dos.writeUTF(s);        //Pasamos ahora el valor de letra o palabra
        this.ssck.executeSCK(2, conexionSCK);
        
        resultado = dis.readInt();
        clientssck--;       //Restamos el numero de usuarios activos en sockets
        apagaSCK(clientssck);
        return "El resultado es " + resultado;
        }
        
        if(opc == 3)  //para salir solo restamos un usuario
            clientssck--;
        
        apagaSCK(clientssck);
        return "Hasta luego, Sistema Jhonnys";      
    }
    
    // Funcion que si no hay usuarios activos apaga el servidor sockets
    public void apagaSCK(int numCli) throws IOException{
    
            if(numCli == 0){
            dis.close();
            dos.close();
            conexionSCK.close();
            conexionSCK = null;
            this.ssck.apagaSCK();
        }
    }
    
    // Funcion de ejecucion de para tarea3A o 3B con RPC
    public String procesamientoRPC(int opc, String pal) throws XmlRpcException, IOException{
        int resultado = 0;
        Object resS = null;
        Vector<Object> params = new Vector<>();
        params.add(pal);
        
        if(opc == 1){   //funcion que busca letra
            resS = conexionRPC.execute("myServerRPC.buscaLetra", params);
        }else if(opc == 2){     //funcion que busca palabra
            resS = conexionRPC.execute("myServerRPC.buscaPalabra", params);
        }else if(opc == 3) {    //funcion para salir
                clientrpc--;
                if(clientrpc == 0){     //solo si no hay usuario activos en RPC
                    this.srpc.apagaRPC();
                    conexionRPC = null;
                    System.out.println("Servidor RPC apagado por eleccion\n");
                }
                return "Hasta luego, Sistema Jhonnys";
        }else
            return "Opcion no valida";  
        
        // Esto se ejecuta solo si se busco letra o palabra
        resultado = ((Integer)resS);
        System.out.println("Numero de coinicidencias: " + resultado);
        
        clientrpc--;
        if(clientrpc == 0){
            this.srpc.apagaRPC();
            conexionRPC = null;
            System.out.println("Servidor RPC apagado por proceso \n");
        }
        return "Numero de coinicidencias: " + resultado;
        
    }
    
    //Levanta el servidor RPC, hace una conexion
    public XmlRpcClient conectaRPC() throws MalformedURLException{
        XmlRpcClient conexionRPC;
        
        this.srpc = new Server_rpc();
        srpc.executeRPC();        
        conexionRPC = new XmlRpcClient("http://localhost:" + PuertoRPC);
        
        return conexionRPC;
    }
    
    // Funcion de ejecucion de para tarea3A o 3B con RMI
    public String procesamientoRMI(int opc, String pal) throws RemoteException, NotBoundException, 
            MalformedURLException, IOException, InterruptedException{        
        
        int resultado;                      
        if(opc == 1){   //funcion que busca letra
            resultado = interfazRMI.buscaLetra(pal);           
            
        }else if(opc == 2){    //funcion que busca palabra
            resultado = interfazRMI.buscaPalabra(pal);                      
            
        }else if(opc == 3){     //funcion para salir
            clientRMI--;
            
            if(clientRMI == 0){     //Para apagar el servidor, si no hay usuarios activos en RMI
                this.sRMI.apagaRMI();
                interfazRMI = null;
                System.out.println("Servidor RMI apagado por eleccion");
            }
            return "Hasta luego, Sistemas Jhonnys";
            
        }else{      //Si se selecciona una opcion invalida
            System.out.println("Opcion no valida");
            return "Opcion no valida";
        }
        //Solo se ejecuta cuando se realizo una busqueda
        System.out.println("Numero de coinicidencias: " + resultado);
        
        clientRMI--;
        if(clientRMI == 0){     //Para apagar el servidor, si no hay usuarios activos en RMI
            this.sRMI.apagaRMI();
            interfazRMI = null;
            System.out.println("Servidor RMI Apagado por proceso");
        }
        return "Numero de coinicidencias: " + resultado;
    }
    
    //  Funcion para levantar el servidor RMI, establece una conexion, y regresa una interfaz
    public InterfazRMI conectaRMI(int opc) throws RemoteException, AlreadyBoundException, MalformedURLException, AccessException, NotBoundException{
        
        this.sRMI = new ServerRMI(); 
        sRMI.executeRMI(opc);

        registry = LocateRegistry.getRegistry("localhost",PuertoRMi);
        
        String ruta = "Calculadora";
        interfazRMI = (InterfazRMI) registry.lookup(ruta);
        return interfazRMI;
    }
    
}

