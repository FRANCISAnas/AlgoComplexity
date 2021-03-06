import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex {


    private ColorG.COLOR color;
    private List<Vertex> neighbors;
    private int id;
    public List<TrafficNeighbor> trafficNeighbors;
    public int nbArcBleuSortantVersRouge;
    public int nbArcBleuSortantVersBleu;
    public int nbArcRougeSortantVersBleu;
    public int nbArcRougeSortantVersRouge;
    public int ratio;
    public int nbArcRougeSortant;

    public Vertex(ColorG.COLOR color, List<Vertex> neighbors, int id) {
        this.color = color;
        this.neighbors = neighbors;
        this.id = id;

    }

    public Vertex(ColorG.COLOR color, int id) {
        this.color = color;
        this.id = id;
        this.neighbors = new ArrayList<>();
        this.trafficNeighbors = new ArrayList<>();
    }

    public void calculateRatio() {
        this.nbArcBleuSortantVersRouge();
        this.nbArcRougeSortantVersBleu();
        ratio = this.nbArcRougeSortantVersBleu - this.nbArcBleuSortantVersRouge;
    }

    public void calculateRatioV2() {
        this.nbArcBleuSortantVersRouge();
        this.nbArcRougeSortantVersBleu();
        this.nbArcBleuSortantVersBleu();
        this.nbArcRougeSortantVersRouge();

        ratio = ((this.nbArcRougeSortantVersBleu * 1000) + this.nbArcBleuSortantVersBleu)
                - ((this.nbArcBleuSortantVersRouge * 1000) + this.nbArcRougeSortantVersRouge);
    }


    public void nbArcBleuSortantVersRouge() {
        nbArcBleuSortantVersRouge = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorG.COLOR.BLUE) {
                if (t.getVertex().getColor() == ColorG.COLOR.RED) {
                    nbArcBleuSortantVersRouge++;
                }
            }
        }
    }

    public void nbArcRougeSortantVersBleu() {
        nbArcRougeSortantVersBleu = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorG.COLOR.RED) {
                if (t.getVertex().getColor() == ColorG.COLOR.BLUE) {
                    nbArcRougeSortantVersBleu++;
                }
            }

        }
    }

    public void nbArcRougeSortantVersRouge() {
        nbArcRougeSortantVersRouge = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorG.COLOR.RED) {
                if (t.getVertex().getColor() == ColorG.COLOR.RED) {
                    nbArcRougeSortantVersRouge++;
                }
            }
        }
    }

    public void nbArcBleuSortantVersBleu() {
        nbArcBleuSortantVersBleu = 0;
        for (TrafficNeighbor t : trafficNeighbors) {
            if (t.getEdge().getColor() == ColorG.COLOR.BLUE) {
                if (t.getVertex().getColor() == ColorG.COLOR.BLUE) {
                    nbArcBleuSortantVersBleu++;
                }
            }

        }
    }

    public void nbArcRougeSortant() {
        nbArcRougeSortant = 0;
        for(TrafficNeighbor t : trafficNeighbors){
            if(t.getEdge().getColor() == ColorG.COLOR.RED){
                nbArcRougeSortant++;
            }
        }
    }

    public void addNeighbor(Vertex vertex) {
        this.neighbors.add(vertex);
    }

    public void setColor(ColorG.COLOR color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public ColorG.COLOR getColor() {
        return color;
    }

    public List<Vertex> getNeighbors() {
        return neighbors;
    }

    @Override
    public String toString() {
        return id + color.toString();
    }

    public int nbArcSortant() {
        return trafficNeighbors.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id &&
                color == vertex.color &&
                neighbors.equals(vertex.neighbors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, neighbors, id);
    }
}
