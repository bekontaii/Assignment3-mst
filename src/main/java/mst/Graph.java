package mst;

import java.util.*;

public class Graph {
    private final List<String> vertices;
    private final List<Edge> edges;

    public Graph(List<String> vertices, List<Edge> edges) {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>(edges);
    }

    public List<String> getVertices() {
        return Collections.unmodifiableList(vertices);
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public int getVertexCount() {
        return vertices.size();
    }

    public int getEdgeCount() {
        return edges.size();
    }

    // adjacency list for Prim
    public Map<String, List<Edge>> buildAdjacency() {
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String v : vertices) {
            adj.put(v, new ArrayList<>());
        }
        for (Edge e : edges) {
            adj.get(e.u).add(e);
            adj.get(e.v).add(e);
        }
        return adj;
    }

    public boolean isConnected() {
        if (vertices.isEmpty()) return true;
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(vertices.get(0));
        while (!stack.isEmpty()) {
            String cur = stack.pop();
            if (!visited.add(cur)) continue;
            for (Edge e : edges) {
                if (e.u.equals(cur) && !visited.contains(e.v)) stack.push(e.v);
                if (e.v.equals(cur) && !visited.contains(e.u)) stack.push(e.u);
            }
        }
        return visited.size() == vertices.size();
    }
}
