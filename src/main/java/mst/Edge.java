package mst;

public class Edge implements Comparable<Edge> {
    public final String u;
    public final String v;
    public final int weight;

    public Edge(String u, String v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return u + " - " + v + " (" + weight + ")";
    }
}
