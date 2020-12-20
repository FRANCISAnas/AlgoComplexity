package project;

import project.core.Vertex;
import project.graph.Graph;
import project.strategies.Strategy;

import java.util.ArrayList;
import java.util.List;

public class Stats {

    public static double[][] run(Strategy strategy){
        double[][] res = new double[11][11];
        int nVertices = 100;
        int nTests = 100;
        for(int i =0 ;i<=10;i++){
            for(int j=0;j<=10;j++){
                List<Integer> scores = new ArrayList<>();
                for(int k =0; k<nTests; k++){
                    Graph g = Main.createRandomCompleteGraph(i/10.,j/10.,nVertices);
                    scores.add(nVertices-deleteAuto(g,strategy));
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
        Vertex toDelete;
        while (true){
            toDelete = strategy.getNextVertexToDelete(graph);
            if(toDelete ==null){
                return graph.getVertices().size();
            }
            graph.deleteVertex(toDelete.getId());
        }
    }
}
