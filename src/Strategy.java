
public interface Strategy {

    public void init(Graph graph);

    public Vertex getNextVertexToDelete(Graph graph);
}
