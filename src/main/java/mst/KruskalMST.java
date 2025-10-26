package mst;

import java.util.*;

public class KruskalMST {

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

        List<Edge> edges = new ArrayList<>(graph.getEdges());
        Collections.sort(edges); // sort by weight
        opCount += edges.size(); // считать как сортировку/сравнения (упрощённо)

        DisjointSet ds = new DisjointSet();
        ds.makeSet(graph.getVertices());

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge e : edges) {
            // попытка добавить это ребро
            boolean merged = ds.union(e.u, e.v);
            opCount += ds.operations; // считаем операции union/find
            ds.operations = 0;        // сбрасываем, чтобы не дублировать

            if (merged) {
                mstEdges.add(e);
                totalCost += e.weight;
            }
            if (mstEdges.size() == graph.getVertexCount() - 1) break;
        }

        long end = System.nanoTime();
        double timeMs = (end - start) / 1_000_000.0;

        boolean success = mstEdges.size() == graph.getVertexCount() - 1;

        return new MSTResult(mstEdges, totalCost, opCount, timeMs, success);
    }
}
