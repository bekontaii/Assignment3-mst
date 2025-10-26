package mst;

import java.util.*;

public class PrimMST {

    private static class PQItem implements Comparable<PQItem> {
        String from;
        String to;
        int weight;
        PQItem(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        @Override
        public int compareTo(PQItem o) {
            return Integer.compare(this.weight, o.weight);
        }
    }

    public static MSTResult run(Graph graph) {
        long opCount = 0;
        long start = System.nanoTime();

        if (!graph.isConnected()) {
            long end = System.nanoTime();
            return new MSTResult(
                    Collections.emptyList(),
                    0,
                    opCount,
                    (end - start) / 1_000_000.0,
                    false
            );
        }

        Map<String, List<Edge>> adj = graph.buildAdjacency();

        Set<String> visited = new HashSet<>();
        PriorityQueue<PQItem> pq = new PriorityQueue<>();
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // стартуем с первой вершины
        String startVertex = graph.getVertices().get(0);
        visited.add(startVertex);

        // добавляем все рёбра из стартовой вершины
        for (Edge e : adj.get(startVertex)) {
            String other = e.u.equals(startVertex) ? e.v : e.u;
            pq.add(new PQItem(startVertex, other, e.weight));
            opCount++;
        }

        while (!pq.isEmpty() && mstEdges.size() < graph.getVertexCount() - 1) {
            PQItem item = pq.poll();
            opCount++;

            if (visited.contains(item.to)) {
                continue;
            }

            // добавляем ребро в MST
            mstEdges.add(new Edge(item.from, item.to, item.weight));
            totalCost += item.weight;
            visited.add(item.to);

            // добавляем новые граничные рёбра
            for (Edge e : adj.get(item.to)) {
                String other = e.u.equals(item.to) ? e.v : e.u;
                if (!visited.contains(other)) {
                    pq.add(new PQItem(item.to, other, e.weight));
                    opCount++;
                }
            }
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        boolean success = mstEdges.size() == graph.getVertexCount() - 1;

        return new MSTResult(mstEdges, totalCost, opCount, timeMs, success);
    }
}
