import java.util.ArrayList;
import java.util.List;

class Agente {

    Agente() {
        this.amigos = new ArrayList<Agente>();
        this.antivirus = Math.random();//con 0 no tener antivirus y cercano a 1 buen antivirus
        this.usuario = Math.random();//con 0 no tener  y cercano a 1 buen antivirus
        this.firewall = Math.random();//con 0 deja pasar casi todo_  y cercano a 1 es mas estricto
        this.estadoActual= estado.Normal;
        this.estadoFuturo=estado.Normal;
        this.probabilidad_contagio=(3-antivirus-firewall-usuario)/3;
        this.probabilidad_recuperacion=(antivirus*usuario+usuario)/2;
        this.probabilidad_inservible=(2-antivirus-usuario)/2;
        this.distribucion_amigos=null;

    }

    enum estado{
        Normal,
        contagiado,
        inservible,
        inmune
    }
    private List<Agente> amigos;
    public void nuevoAmigo(Agente conocido) {
        amigos.add(conocido);
    }
    private double antivirus;
    private double usuario ;
    private double firewall;
    private estado estadoActual;
    private estado estadoFuturo;
    private double probabilidad_contagio;
    private double probabilidad_recuperacion;
    public double probabilidad_inservible;
    public double[] distribucion_amigos;


    int numeroAmigos() {
        return amigos.size();
    }

    public List<Agente> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Agente> amigos) {
        this.amigos = amigos;
    }

    public double getAntivirus() {
        return antivirus;
    }

    public void setAntivirus(double antivirus) {
        this.antivirus = antivirus;
    }

    public double getUsuario() {
        return usuario;
    }

    public void setUsuario(double usuario) {
        this.usuario = usuario;
    }

    public double getFirewall() {
        return firewall;
    }

    public void setFirewall(double firewall) {
        this.firewall = firewall;
    }

    public Agente.estado getEstado() {
        return estadoActual;
    }

    public void setEstado(Agente.estado estado) {
        this.estadoActual = estado;
        this.estadoFuturo =estado;
    }

    public void contagiarse(){
        double p=Math.random();
        if(p<probabilidad_contagio){
            this.estadoFuturo= estado.contagiado;
            System.out.println("el lunes sin falta carnal");
        }
    }
    public void recuperarse(){
        double p=Math.random();
        if(p<probabilidad_recuperacion){
            this.estadoFuturo = estado.inmune;
        }
    }
    public void inservible(){
        double p=Math.random();
        if(p<probabilidad_inservible){
            this.estadoFuturo = estado.inservible;
            System.out.print("me voy a morir xd");
            System.out.println(p);
        }
    }

    public void distribucion_comunicacion_amigos(){
        distribucion_amigos= new double[numeroAmigos()];
        double distribucion_acumulada=0;
        /*
        for(int i=0;i<numeroAmigos();i++){
            double p=Math.random();

            if(i==numeroAmigos()-1){
                distribucion_amigos[i]=distribucion_acumulada;

            }else if (i==0){
                distribucion_amigos[i]=p;
                distribucion_acumulada=1-p;

            }
            else{

                distribucion_amigos[i]=p*distribucion_acumulada;
                distribucion_acumulada=distribucion_acumulada-p;
            }
        }*/
        for(int i=0;i<numeroAmigos();i++){
            double p=Math.random();
            distribucion_amigos[i]=p*0.5;
        }
    }
    public void actualizarEstado(){
        estadoActual=estadoFuturo;
    }
    public void VamosAContagiar(){

        Agente[] amigos_arr=new Agente[numeroAmigos()];
        amigos_arr= amigos.toArray(amigos_arr);
       // distribucion_comunicacion_amigos();
        if(this.estadoActual==estado.contagiado){
            double p=Math.random();
            for(int i=0;i<numeroAmigos();i++){
                if(p<this.distribucion_amigos[i] && amigos_arr[i].getEstado()==estado.Normal){
                    amigos_arr[i].contagiarse();
                }
            }
            recuperarse();
            inservible();
        }

    }

}

