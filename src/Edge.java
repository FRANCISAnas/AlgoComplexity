public class Edge {

    private ColorG.COLOR color;

    public Edge(ColorG.COLOR color){
        this.color = color;
    }

    public void setColor(ColorG.COLOR color) {
        this.color = color;
    }

    public ColorG.COLOR getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Edge "+color;

    }
}
