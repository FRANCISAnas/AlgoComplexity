package project.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex {


    private ColorBR color;
    private final int id;
    public List<TrafficNeighbor> trafficNeighbors;

    public Vertex(ColorBR color, int id) {
        this.color = color;
        this.id = id;
        this.trafficNeighbors = new ArrayList<>();
    }

    public int calculateScore() {
        return ((this.nOutgoingRedEdgesToBlue()*trafficNeighbors.size()) + this.nOutgoingBlueEdgesToBlue())
                - ((this.nOutgoingBlueEdgesToRed()*trafficNeighbors.size()) + this.nOutgoingRedEdgesToRed());
    }

    public int nOutgoingBlueEdgesToRed() {
        int res = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorBR.BLUE) {
                if (t.getVertex().getColor() == ColorBR.RED)
                    res++;
            }
        }
        return res;
    }

    public int nOutgoingRedEdgesToBlue() {
        int res = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorBR.RED) {
                if (t.getVertex().getColor() == ColorBR.BLUE)
                    res++;
            }
        }
        return res;
    }

    public int nOutgoingRedEdgesToRed() {
        int res = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorBR.RED) {
                if (t.getVertex().getColor() == ColorBR.RED)
                    res++;
            }
        }
        return res;
    }

    public int nOutgoingBlueEdgesToBlue() {
        int res = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorBR.BLUE) {
                if (t.getVertex().getColor() == ColorBR.BLUE)
                    res++;
            }
        }
        return res;
    }

    public void setColor(ColorBR color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public ColorBR getColor() {
        return color;
    }

    public List<TrafficNeighbor> getTrafficNeighbors() {
        return trafficNeighbors;
    }

    public void addTrafficNeighbor(TrafficNeighbor tn) {
        this.trafficNeighbors.add(tn);
    }

    public void removeTrafficNeighborByVertex(Vertex v) {
        trafficNeighbors.removeIf(t -> t.getVertex() == v);
    }

    @Override
    public String toString() {
        return id + color.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id &&
                color == vertex.color &&
                trafficNeighbors.equals(vertex.trafficNeighbors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, trafficNeighbors, id);
    }
}
