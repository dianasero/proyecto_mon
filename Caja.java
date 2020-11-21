public class Caja {
    private Heladeria heladeria;
    
    public Caja(){

    }

    public void pagarHelado(int persona, int grupo){
        try {
            System.out.println("El cliente " + persona + " del grupo "+grupo+ " comienza a pagar su helado ");
            Thread.sleep(2000);// Tiempo para pagar
            System.out.println("El cliente " + persona + " del grupo "+grupo+ " terminó de pagar su helado ");
        } catch (InterruptedException E) {
            System.out.println("Se genero una excepcion pidiendo helado");
        }
    }

    public void pagarGrupo (int grupo){
        System.out.println("El grupo "+ grupo+" ha terminado de pedir sus helados y comieza a pagar");
    }

    public void setHeladeria(Heladeria heladeria){
        this.heladeria = heladeria;
    }
}
