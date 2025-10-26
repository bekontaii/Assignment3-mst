package mst;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class Main {
    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/assign_3_input.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray graphs = (JSONArray) jsonObject.get("graphs");

            FileWriter fw = new FileWriter("data/output.json");
            fw.write("{\n  \"results\": [\n");

            int index = 0;
            for (Object o : graphs) {
                JSONObject gObj = (JSONObject) o;
                String graphName = (String) gObj.get("name");
                JSONArray vArr = (JSONArray) gObj.get("vertices");
                JSONArray eArr = (JSONArray) gObj.get("edges");

                // Конвертируем JSON → Java
                List<String> vertices = new ArrayList<>();
                for (Object v : vArr) vertices.add(v.toString());

                List<Edge> edges = new ArrayList<>();
                for (Object e : eArr) {
                    JSONObject edge = (JSONObject) e;
                    String u = (String) edge.get("u");
                    String v = (String) edge.get("v");
                    int w = ((Long) edge.get("weight")).intValue();
                    edges.add(new Edge(u, v, w));
                }

                Graph graph = new Graph(vertices, edges);

                MSTResult prim = PrimMST.run(graph);
                MSTResult kruskal = KruskalMST.run(graph);

                fw.write(makeJsonBlock(graphName, graph, prim, kruskal));
                if (index < graphs.size() - 1) fw.write(",\n");
                index++;
            }

            fw.write("\n  ]\n}\n");
            fw.close();
            System.out.println("✅ Output generated successfully: data/output.json");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String makeJsonBlock(String graphName, Graph g,
                                        MSTResult prim, MSTResult kruskal) {
        return "    {\n" +
                "      \"graph\": \"" + graphName + "\",\n" +
                "      \"numVertices\": " + g.getVertexCount() + ",\n" +
                "      \"numEdges\": " + g.getEdgeCount() + ",\n" +
                "      \"algorithms\": {\n" +
                jsonAlg("prim", prim) + ",\n" +
                jsonAlg("kruskal", kruskal) + "\n" +
                "      }\n" +
                "    }";
    }

    private static String jsonAlg(String name, MSTResult r) {
        StringBuilder sb = new StringBuilder();
        sb.append("        \"").append(name).append("\": {\n");
        sb.append("          \"totalCost\": ").append(r.totalCost).append(",\n");
        sb.append("          \"timeMs\": ").append(r.timeMs).append(",\n");
        sb.append("          \"operationCount\": ").append(r.operationCount).append(",\n");
        sb.append("          \"success\": ").append(r.success).append(",\n");
        sb.append("          \"mstEdges\": [\n");
        for (int i = 0; i < r.mstEdges.size(); i++) {
            Edge e = r.mstEdges.get(i);
            sb.append("            { \"u\": \"").append(e.u)
                    .append("\", \"v\": \"").append(e.v)
                    .append("\", \"weight\": ").append(e.weight).append(" }");
            if (i < r.mstEdges.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("          ]\n");
        sb.append("        }");
        return sb.toString();
    }
}
