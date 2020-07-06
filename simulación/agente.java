import java.util.ArrayList;
import java.util.List;

class Agente {

    Agente(double a_firewall, double b_firewall,double a_antiv, double b_antiv,double a_usuario, double b_usuario) {
        this.amigos = new ArrayList<>();
        this.antivirus = Math.random()*(b_antiv-a_antiv)+a_antiv;//con 0 no tener antivirus y cercano a 1 buen antivirus
        this.usuario = Math.random()*(b_usuario-a_usuario)+a_usuario;//con 0 no tener  y cercano a 1 buen antivirus
        this.firewall = Math.random()*(b_firewall-a_firewall)+a_firewall;//con 0 deja pasar casi todo_  y cercano a 1 es mas estricto
        this.estadoActual= estado.Normal;
        this.estadoFuturo=estado.Normal;
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
          //  System.out.println("el lunes sin falta carnal");
            System.out.println(probabilidad_contagio);
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
          //  System.out.print("me voy a morir xd");
         //   System.out.println(p);
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

       // distribucion_comunicacion_amigos();
        if(this.estadoActual==estado.contagiado){
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

