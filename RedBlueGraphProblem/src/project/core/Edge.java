package project.core;

import java.util.Objects;

public class Edge {

    private ColorBR color;

    public Edge(ColorBR color){
        this.color = color;
    }

    public ColorBR getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Edge "+color;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return color == edge.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
