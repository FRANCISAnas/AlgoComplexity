import java.util.Objects;

public class Edge {

    private ColorG.COLOR color;

    public Edge(ColorG.COLOR color){
        this.color = color;
    }

    public ColorG.COLOR getColor() {
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
