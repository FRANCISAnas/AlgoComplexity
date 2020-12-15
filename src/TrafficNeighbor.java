

public class TrafficNeighbor {

    private Vertex v;
    private Edge edge;

    public TrafficNeighbor(Vertex vertex, Edge edge){
        this.v = vertex;
        this.edge = edge;
    }


    public Vertex getVertex() {
        return v;
    }

    public Edge getEdge(){
        return edge;
    }
}
