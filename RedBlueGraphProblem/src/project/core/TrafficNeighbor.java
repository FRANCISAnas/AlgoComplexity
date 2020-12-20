package project.core;

import java.util.Objects;

public class TrafficNeighbor {

    private final Vertex v;
    private final Edge edge;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrafficNeighbor that = (TrafficNeighbor) o;
        return v.equals(that.v) &&
                edge.equals(that.edge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v, edge);
    }
}
