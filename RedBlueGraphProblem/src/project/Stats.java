package project;

import project.core.ColorBR;
import project.core.Edge;
import project.core.TrafficNeighbor;
import project.core.Vertex;
import project.graph.Graph;
import project.strategies.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Stats {

    /**
     * QUESTION 7
     * @param strategy
     * @return the results for 1 heuristic
     */
    public static double[][] run(Strategy strategy){
        double[][] res = new double[11][11];
        int nVertices = 100;
        int nTests = 100;
        for(int i=0; i<=10; i++){
            for(int j=0; j<=10; j++){
                double total = 0.;
                for(int k=0; k<nTests; k++){
                    Graph g = createRandomCompleteGraph(nVertices, i/10., j/10.);
                    total += nVertices - deleteAuto(g, strategy);
                }
                res[i][j] = total/nTests;
            }
        }
        return res;
    }

    /**
     * Treatment of deletion
     * @param graph
     * @param strategy
     * @return number of vertices left in the graph
     */
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

    /**
     * QUESTION 5
     * @param n -> number of vertices
     * @param p -> probability for vertex to be red
     * @param q --> probability for edge to be blue
     * @return graph created
     */
    public static Graph createRandomCompleteGraph(int n, double p, double q){
        List<Vertex> vertices = new ArrayList<>();
        Edge[][] edges = new Edge[n][n];
        for(int i = 0; i<n; i++){
            Arrays.fill(edges[i],new Edge(ColorBR.NONE));
        }
        for(int i =0; i<n; i++){
            ColorBR color = (Math.random() <= p)? ColorBR.RED: ColorBR.BLUE;
            vertices.add(new Vertex(color, i));
        }
        for(Vertex v : vertices){
            for(Vertex nei : vertices){
                if(v != nei){
                    //v.addNeighbor(nei);
                    ColorBR color = (Math.random() <= q)? ColorBR.BLUE: ColorBR.RED;
                    Edge e = new Edge(color);
                    edges[v.getId()][nei.getId()] = e;
                    v.addTrafficNeighbor(new TrafficNeighbor(nei, e));
                }
            }
        }
        return new Graph(vertices,edges);
    }
}
