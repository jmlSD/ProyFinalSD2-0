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
    ServidorHilos ssck;
    Socket conexionSCK;
    DataInputStream dis;
    DataOutputStream dos;
    String menuSCK;
    int clientssck = 0;
    XmlRpcClient conexionRPC;    
    Server_rpc srpc;
    Object menuRPC;
    int clientrpc = 0;
    InterfazRMI interfazRMI;
    ServerRMI sRMI;
    Registry registry;
    int idRMI = 0, clientRMI = 0;
    String menuRMI;
    private static final int PuertoRMi = 8085, PuertoRPC = 8081, PuertoSCK = 8084; //Si cambias aquí el puerto, recuerda cambiarlo en el servidor
    
    public String desplegaMenu(){
        return "----- Bienvenido al Sistema Jhonys -----\n"
                + "¿Con qué tipo de servidor desear trabajar?\n"
                + "1.- Sockets\n"
                + "2.- RPC\n"
                + "3.- RMI\n\n"
                + "Presiona el numero correspondiente a la opción: ";
    }
    
    public String solicitudMenu(int opc) throws IOException{
        String resultado = "";
                    
        if(opc == 1){
            System.out.println("llegue a opc 1");
            if(conexionSCK == null) {
                this.conexionSCK = conectaSCK(); 
                this.dis = new DataInputStream(conexionSCK.getInputStream());
            
                menuSCK = dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n";
            }
            System.out.println("Conexion exitosa al servidor Sockets");
            clientssck++;
            return menuSCK;
            
        }else if(opc == 2){
            try{
                if(conexionRPC == null){
                    this.conexionRPC = conectaRPC();
                
                Vector<Integer> params = new Vector<Integer>();
                System.out.println("Conexion exitosa al servidor RPC");
                menuRPC = conexionRPC.execute("myServerRPC.desplegaMenu", params);
                }
                clientrpc++;
                resultado = (String) menuRPC;

            }catch(Exception ex){
                System.err.println("Client: " + ex);
            }
            return resultado;
            
        }else if(opc == 3){            
            try{
                if(interfazRMI == null) {
                    this.interfazRMI = conectaRMI(idRMI);
                    menuRMI = (String) interfazRMI.mostrarMenu();
                }  
                System.out.println("Conexion exitosa al servidor RMI"); 
                idRMI += 1;
                clientRMI += 1;
                
            } catch (Exception ex) {
                System.err.println("Client: " + ex);
            }
            return menuRMI;
            
        }else{
            return "Opcion no valida";
        }        
    }
    
    
    public Socket conectaSCK() throws IOException {
        this.ssck = new ServidorHilos();
        ssck.levantaSCK();
        Socket sk = new Socket("localhost", PuertoSCK);
        ssck.executeSCK(0);        
        return sk;
    }
    
    public String procesamientoSCK(int opc, String x1) throws RemoteException, NotBoundException, 
                        MalformedURLException, IOException, InterruptedException{        

        int resultado;
        this.dos  = new DataOutputStream(conexionSCK.getOutputStream());
        System.out.println("lleggue a dos");
        
        if(opc < 3){
        dos.writeInt(opc);
        this.ssck.executeSCK(1, conexionSCK);
        
        String s = x1;
        dos.writeUTF(s);
        this.ssck.executeSCK(2, conexionSCK);
        
        resultado = dis.readInt();
        clientssck--;
        return "El resultado es " + resultado;
        }
        
        if(opc == 3)
            clientssck--;
        
        if(clientssck == 0){
            System.out.println("Entre a cerrar");
            dis.close();
            dos.close();
            conexionSCK.close();
            conexionSCK = null;
            this.ssck.apagaSCK();
        }
        return "";      
    }

    
    public String procesamientoRPC(int opc, String pal) throws XmlRpcException, IOException{
        int resultado = 0;
        Object resS = null;
        Vector<Object> params = new Vector<>();
        params.add(pal);
        
        if(opc == 1){
            resS = conexionRPC.execute("myServerRPC.buscaLetra", params);
            
        }else if(opc == 2){
            resS = conexionRPC.execute("myServerRPC.buscaPalabra", params);
        }else if(opc == 3) {
                clientrpc--;
                if(clientrpc == 0){
                    this.srpc.apagaRPC();
                    conexionRPC = null;
                    System.out.println("Servidor RPC apagado por eleccion\n");
                }
                return "byebye";
        }else
            return "Opcion no valida";  
        
        /*if(resS != null) {
            resultado = ((Integer)resS);
            System.out.println("Numero de coinicidencias: " + resultado);
            return "Numero de coinicidencias: " + resultado;
        }*/
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
    
    public XmlRpcClient conectaRPC() throws MalformedURLException{
        XmlRpcClient conexionRPC;
        
        this.srpc = new Server_rpc();
        srpc.executeRPC();        
        conexionRPC = new XmlRpcClient("http://localhost:" + PuertoRPC);
        
        return conexionRPC;
    }
    /*public String pideLetraoPal(int opc){
        String pide = "";
        if(opc == 1){
            pide = "Dame la letra a buscar";
        }else if(opc == 2){
            pide = "Dame la palabra a buscar";
        }
        
        return pide;
    }*/
    
    public String procesamientoRMI(int opc, String pal) throws RemoteException, NotBoundException, 
            MalformedURLException, IOException, InterruptedException{        
        
        int resultado;                      
        if(opc == 1){                               
            resultado = interfazRMI.buscaLetra(pal);           
            
        }else if(opc == 2){                        
            resultado = interfazRMI.buscaPalabra(pal);                      
            
        }else if(opc == 3){
            clientRMI--;
            
            if(clientRMI == 0){
                this.sRMI.apagaRMI(idRMI);
                interfazRMI = null;
                System.out.println("Servidor RMI apagado por 3");
            }
            return "Servidor RMI apago";
            
        }else{
            System.out.println("Opcion no valida");
            return "Opcion no valida";
        }
        
        System.out.println("Numero de coinicidencias: " + resultado);
        System.out.println("mi idrmi es : " + idRMI);
        
        clientRMI--;
        System.out.println("tengo n clientes: " + clientRMI);
        if(clientRMI == 0){
            this.sRMI.apagaRMI(idRMI-1);
            interfazRMI = null;
            //registry.unbind(null);
            System.out.println("Servidor RMI Apagado po proceso");
        }
        return "Numero de coinicidencias: " + resultado;
    }
    
    public InterfazRMI conectaRMI(int id) throws RemoteException, AlreadyBoundException, MalformedURLException, AccessException, NotBoundException{
        
        this.sRMI = new ServerRMI(); 
        sRMI.executeRMI(id);

        registry = LocateRegistry.getRegistry("localhost",PuertoRMi);
        
        
        //System.out.println("Pase registro del servidor prin RMi y idRMI es: " + idRMI);
        
        String ruta = "Calculadora" + idRMI;
        System.out.println("resula = " + ruta);
        
        interfazRMI = (InterfazRMI) registry.lookup(ruta);
        
        //System.out.println("Termine conecta RMi");
        
        return interfazRMI;
    }
    
}

