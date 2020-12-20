package project.strategies;

import project.graph.Graph;
import project.core.Vertex;

import java.util.List;

public interface Strategy {

    default void deleteAllRedVertices(Graph graph){
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        for(Vertex v: redVertex){
            graph.deleteVertex(v.getId());
        }
    }

    public Vertex getNextVertexToDelete(Graph graph);
}
