package mst;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();
    public long operations = 0; // считаем union/find операции

    public void makeSet(Iterable<String> vertices) {
        for (String v : vertices) {
            parent.put(v, v);
            rank.put(v, 0);
        }
    }

    public String find(String x) {
        operations++;
        if (!parent.get(x).equals(x)) {
            parent.put(x, find(parent.get(x))); // path compression
        }
        return parent.get(x);
    }

    public boolean union(String a, String b) {
        operations++;
        String pa = find(a);
        String pb = find(b);
        if (pa.equals(pb)) return false;

        int ra = rank.get(pa);
        int rb = rank.get(pb);

        if (ra < rb) {
            parent.put(pa, pb);
        } else if (ra > rb) {
            parent.put(pb, pa);
        } else {
            parent.put(pb, pa);
            rank.put(pa, ra + 1);
        }
        return true;
    }
}
