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
    XmlRpcClient conexionRPC;    
    Server_rpc srpc;
    InterfazRMI interfazRMI;
    ServerRMI sRMI;
    Registry registry;
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
            this.conexionSCK = conectaSCK(); 
            System.out.println("Conexion exitosa al servidor Sockets");
            this.dis = new DataInputStream(conexionSCK.getInputStream());
            
            String menu = dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n" + dis.readUTF() + "\n";
            return menu;
            
        }else if(opc == 2){
            try{
                //if(conexionRPC == null)
                    this.conexionRPC = conectaRPC();
                
                Vector<Integer> params = new Vector<Integer>();
                System.out.println("Conexion exitosa al servidor RPC");
                Object menu = conexionRPC.execute("myServerRPC.desplegaMenu", params);
                resultado = (String) menu;

            }catch(Exception ex){
                System.err.println("Client: " + ex);
            }
            return resultado;
            
        }else if(opc == 3){            
            try{
                this.interfazRMI = conectaRMI();
                System.out.println("Conexion exitosa al servidor RMI");
                resultado = (String) interfazRMI.mostrarMenu();
                
            } catch (Exception ex) {
                System.err.println("Client: " + ex);
            }
            return resultado;
            
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

        dos.writeInt(opc);
        this.ssck.executeSCK(1, conexionSCK);
        
        String s = x1;
        dos.writeUTF(s);
        this.ssck.executeSCK(2, conexionSCK);
        
        resultado = dis.readInt();
    
        dis.close();
        dos.close();
        conexionSCK.close();
        this.ssck.apagaSCK();
        
        return "El resultado es " + resultado;
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
                this.srpc.apagaRPC();
                System.out.println("Servidor RPC apagado \n");
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
        this.srpc.apagaRPC();
        System.out.println("Servidor RPC apagado \n");
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
            this.sRMI.apagaRMI();
            System.out.println("Servidor RMI apagado");
            
            return "Servidor RMI apago";
            
        }else{
            System.out.println("Opcion no valida");
            return "Opcion no valida";
        }
        
        System.out.println("Numero de coinicidencias: " + resultado);
        this.sRMI.apagaRMI();
        //registry.unbind(null);
        System.out.println("Servidor RMI Apagado");
        
        return "Numero de coinicidencias: " + resultado;
    }
    
    public InterfazRMI conectaRMI() throws RemoteException, AlreadyBoundException, MalformedURLException, AccessException, NotBoundException{
        this.sRMI = new ServerRMI();
        sRMI.executeRMI();

        registry = LocateRegistry.getRegistry("localhost",PuertoRMi);
        interfazRMI = (InterfazRMI) registry.lookup("Calculadora");
        
        return interfazRMI;
    }
    
}

