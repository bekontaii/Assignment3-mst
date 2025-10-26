package mst;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class MSTTest {


    private Graph buildSampleGraph() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D", "E");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("A", "C", 2),
                new Edge("B", "C", 1),
                new Edge("B", "D", 5),
                new Edge("C", "D", 8),
                new Edge("C", "E", 10),
                new Edge("D", "E", 2)
        );
        return new Graph(vertices, edges);
    }


    @Test
    public void testSameTotalCost() {
        Graph g = buildSampleGraph();
        MSTResult prim = PrimMST.run(g);
        MSTResult kruskal = KruskalMST.run(g);

        Assert.assertTrue(prim.success);
        Assert.assertTrue(kruskal.success);
        Assert.assertEquals(prim.totalCost, kruskal.totalCost);
    }


    @Test
    public void testEdgeCountIsVminus1() {
        Graph g = buildSampleGraph();
        MSTResult prim = PrimMST.run(g);

        Assert.assertEquals(g.getVertexCount() - 1, prim.mstEdges.size());
    }


    @Test
    public void testNoCycles() {
        Graph g = buildSampleGraph();
        MSTResult prim = PrimMST.run(g);

        DisjointSet ds = new DisjointSet();
        ds.makeSet(g.getVertices());

        for (Edge e : prim.mstEdges) {
            String pa = ds.find(e.u);
            String pb = ds.find(e.v);
            Assert.assertNotEquals("Cycle detected between " + e.u + " and " + e.v, pa, pb);
            ds.union(e.u, e.v);
        }
    }


    @Test
    public void testConnectedAfterMST() {
        Graph g = buildSampleGraph();
        MSTResult prim = PrimMST.run(g);

        DisjointSet ds = new DisjointSet();
        ds.makeSet(g.getVertices());

        for (Edge e : prim.mstEdges) {
            ds.union(e.u, e.v);
        }

        String root = ds.find(g.getVertices().get(0));
        for (String v : g.getVertices()) {
            Assert.assertEquals("Vertex " + v + " not connected", root, ds.find(v));
        }
    }


    @Test
    public void testDisconnectedGraphFails() {
        List<String> vertices = Arrays.asList("A", "B", "C", "D");
        List<Edge> edges = Arrays.asList(
                new Edge("A", "B", 1),
                new Edge("C", "D", 2)
        );
        Graph g = new Graph(vertices, edges);

        MSTResult prim = PrimMST.run(g);
        MSTResult kruskal = KruskalMST.run(g);

        Assert.assertFalse(prim.success);
        Assert.assertFalse(kruskal.success);
    }
}
