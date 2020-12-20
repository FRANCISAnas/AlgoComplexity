package project;

import project.core.Vertex;
import project.graph.Graph;
import project.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

public class RunTests {

    public static double runTests(int nb, Strategy strategy){
        List<Double> score = new ArrayList<>();
        nb = nb/2;
        double n = 4./Math.sqrt(nb);
        for(double i=0.1;i<1;i+=n){
            for(double j=0.1;j<1;j+=n){
                for(double k=0.1;k<1;k+=n){
                    for(double l=1;l<nb/4.;l++){
                        Graph g = Main.createRandomGraph(i,j,k,(int)l,-1);
                        score.add(deleteAuto(g,strategy)/l*100);
                    }
                }
            }
        }
        double res = 0;
        for(double i : score){
            res+=i;
        }
        return res/score.size();
    }

    public static double[][] launchStats(Strategy strategy){
        double[][] res = new double[11][11];
        int nbSommet = 100;
        int nbTest = 100;
        for(int i =0 ;i<=10;i++){
            for(int j=0;j<=10;j++){
                List<Integer> scores = new ArrayList<>();
                for(int k =0;k<nbTest;k++){
                    Graph g = Main.createRandomCompleteGraph(i/10.,j/10.,nbSommet);
                    scores.add(nbSommet-deleteAuto(g,strategy));
                }
                double r = 0.;
                for(int s : scores){
                    r+=s;
                }
                res[i][j] = r/scores.size();
            }
        }
        return res;
    }
    public static int deleteAuto(Graph graph, Strategy strategy){
        Vertex sommetTODelete;
        while (true){
            sommetTODelete = strategy.getNextVertexToDelete(graph);
            if(sommetTODelete ==null){
                return graph.getVertices().size();
            }

            graph.deleteVertex(sommetTODelete.getId());
        }
    }
}
