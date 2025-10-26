package mst;

import java.util.List;

public class MSTResult {
    public final List<Edge> mstEdges;
    public final int totalCost;
    public final long operationCount;
    public final double timeMs;
    public final boolean success;

    public MSTResult(List<Edge> mstEdges, int totalCost, long operationCount, double timeMs, boolean success) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationCount = operationCount;
        this.timeMs = timeMs;
        this.success = success;
    }
}
