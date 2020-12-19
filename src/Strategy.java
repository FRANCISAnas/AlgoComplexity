import java.util.List;

public interface Strategy {

    public void init(Graph graph);

    static void deleteAllRedVertices(Graph graph){
        List<Vertex> redVertex = Utility.getRedVertex(graph);
        for(Vertex v: redVertex){
            graph.deleteVertex(v.getId());
        }
    }

    public Vertex getNextVertexToDelete(Graph graph);
}
