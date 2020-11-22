import java.util.concurrent.Semaphore;

import javax.print.DocFlavor.INPUT_STREAM;

import java.util.Random;

public class Persona extends Thread {
    private int nombre;
    private String helado;
    private boolean pedido;
    private boolean pagado;
    private Mostrador mostrador;
    private Grupo grupo;
    static Semaphore get = new Semaphore(1);
    static Semaphore pay = new Semaphore(1);


    public Persona(int nombre, Mostrador mostrador) {
        this.mostrador = mostrador;
        this.nombre = nombre;
        this.pedido = false;
        this.pagado = false;
        this.helado = "";
        this.grupo = grupo;
    }

    public void run() {
        Random r = new Random();
        int num = r.nextInt(2);
        System.out.println("Persona " + nombre + " del grupo " + grupo.getNombre() + " eligiendo helado....");
        try {
            Thread.sleep(num * 1000);
            helado = elegirHelado(mostrador.getSabores(), num);
            System.out.println("Persona " + nombre + " del grupo " + grupo.getNombre() + " eligió helado: " + helado);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        while(!grupo.isGrupoTerminado()){
            try {
                get.acquire();
                grupo.pedirHelado(nombre, helado);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //
            if(grupo.isGrupoPedido())
                try{
                    pay.acquire();
                    grupo.pagarHelado(nombre);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
        }
        
    }
    public void regresarSemPay(){
        pay.release();
    }
    public void regresarSemGet(){
        System.out.println(get.toString() + nombre);
        get.release();
        System.out.println(get.toString() + nombre);

    }
    public String elegirHelado(String [] sabores,int num){
        return sabores[num];
    }

    public boolean isPagado(){
        return pagado;
    }

    public boolean isPedido(){
        return pedido;
    }
    public void setPedido(boolean pedido){
        this.pedido = pedido;
    }
    public void setPagado (boolean pagado){
        this.pagado = pagado;
    }
    public void setGrupo(Grupo grupo){
        this.grupo = grupo;
    }
}