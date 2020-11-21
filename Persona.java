import java.util.concurrent.Semaphore;
import java.util.Random;

public class Persona extends Thread {
    private int nombre;
    private String helado;
    private boolean pedido;
    private boolean pagado;
    private Mostrador mostrador;
    private Grupo grupo;
   

    public Persona(int nombre, Mostrador mostrador, Grupo grupo) {
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
        try {
            grupo.pedirHelado(nombre,  helado);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        grupo.pagarHelado(nombre);
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
}