public class FastTester {

    public static void main(String[] args) throws InterruptedException {



        double argumentos[][]  = {{60.0,0.8,0,0.2,0,0.4,0,0.3,0.7,10},{1000.0,0.2,0.4,1,0,1,0.6,1,0.5,200.0},{500.0,0.7,0.7,1,0.7,1,0.5,1,0.2,50},{15,0.7,0,0.4,0.4,1,0,1,0.8,2}};
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 100; i++) {


                Observador espia = new Observador((int)(argumentos[j][0]));
                Ambiente frame = new Ambiente(
                        (int)(argumentos[j][0]),
                        argumentos[j][1],
                        argumentos[j][2], argumentos[j][3],
                        argumentos[j][4], argumentos[j][5],
                        argumentos[j][6], argumentos[j][7], argumentos[j][8],
                        espia
                );

                //frame.setSize(1800, 1080);
                frame.initProp();
                for (int u = 0; u < argumentos[j][9]; u++) {
                    frame.setAgentState((int) (Math.random() * argumentos[j][0]), Agente.estado.contagiado);
                }

                frame.setVisible(true);
                int t = 0;
                while (frame.getAct()) {

                    frame.setT(t);
                    frame.setAct(false);
                    frame.Rutine();
                    frame.Actualizar();
                    frame.deleteDeads();
                    frame.repaint();
                    t++;
                }

                espia.estadisticas();
            }
        }
    }
    }


