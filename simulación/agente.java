import java.util.ArrayList;
import java.util.List;

class Agente {

    Agente() {
        this.amigos = new ArrayList<Agente>();
        this.antivirus = Math.random();//con 0 no tener antivirus y cercano a 1 buen antivirus
        this.usuario = Math.random();//con 0 no tener  y cercano a 1 buen antivirus
        this.firewall = Math.random();//con 0 deja pasar casi todo_  y cercano a 1 es mas estricto
        this.estado= estado.Normal;
        this.probabilidad_contagio=(3-antivirus*usuario-firewall*usuario-usuario)/3;
        this.probabilidad_recuperacion=(antivirus*usuario+usuario)/2;
        this.probabilidad_inservible=1-antivirus*usuario;
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
    private estado estado;
    private double probabilidad_contagio;
    private double probabilidad_recuperacion;
    private double probabilidad_inservible;
    private double[] distribucion_amigos;


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
        return estado;
    }

    public void setEstado(Agente.estado estado) {
        this.estado = estado;
    }

    public void contagiarse(){
        double p=Math.random();
        if(p<probabilidad_contagio){
            this.estado = estado.contagiado;
        }
    }
    public void recuperarse(){
        double p=Math.random();
        if(p<probabilidad_recuperacion){
            this.estado = estado.inmune;
        }
    }
    public void inservible(){
        double p=Math.random();
        if(p<probabilidad_inservible){
            this.estado = estado.inservible;
        }
    }

    public void distribucion_comunicacion_amigos(){
        double distribucion_acumulada=0;
        for(int i=0;i<numeroAmigos();i++){
            double p=Math.random();

            if(i==numeroAmigos()-1){
                distribucion_amigos[i]=distribucion_acumulada;

            }else if (i==0){
                distribucion_amigos[i]=p;
                distribucion_acumulada=1-p;

            }
            else{
                while(distribucion_acumulada<p){
                    p=Math.random();
                }
                distribucion_amigos[i]=p;
                distribucion_acumulada=distribucion_acumulada-p;
            }
        }
    }
    public void VamosAContagiar(){
        distribucion_comunicacion_amigos();
        if(this.estado==estado.contagiado){
            double p=Math.random();
            for(int i=0;i<numeroAmigos();i++){
                if(p<this.distribucion_amigos[i] && amigos.get(i).getEstado()==estado.Normal){
                    amigos.get(i).contagiarse();
                }
            }
            recuperarse();
            inservible();
        }

    }

}

