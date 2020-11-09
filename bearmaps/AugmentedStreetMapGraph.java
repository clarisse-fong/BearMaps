package bearmaps;

import bearmaps.utils.graph.streetmap.StreetMapGraph;
import bearmaps.utils.graph.streetmap.Node;

import java.util.*;

import bearmaps.utils.ps.KDTree;
import bearmaps.utils.ps.Point;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    List<Node> nodes;
    List<Point> points;
    HashMap<Point, Node> pointToNode;
    MyTrieSet autoComplete;
    Map<String, LinkedList<Node>> locNames;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
        this.nodes = this.getNodes();
        this.points = new ArrayList<>();
        this.pointToNode = new HashMap();
        this.locNames = new HashMap<>();
        this.autoComplete = new MyTrieSet();
        for (Node node : nodes) {
            if (!neighbors(node.id()).isEmpty()) {
                Point newP = new Point(lon(node.id()), lat(node.id()));
                points.add(newP);
                pointToNode.put(newP, node);
            }
            if (node.name() != null) {
                autoComplete.add(cleanString(node.name()));
                if (locNames.containsKey(cleanString(node.name()))) {
                    locNames.get(cleanString(node.name())).addLast(node);
                } else {
                    LinkedList<Node> l = new LinkedList<>();
                    l.add(node);
                    locNames.put(cleanString(node.name()), l);
                }
            }

        }
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        KDTree kd = new KDTree(points);
        Point best = kd.nearest(lon, lat);
        return pointToNode.get(best).id();
    }


    /**
     * For Project Part III (extra credit)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        List<String> prefixes = autoComplete.keysWithPrefix(cleanString(prefix));
        List<String> all = new LinkedList<>();
        if (prefixes == null) {
            return null;
        }
        for(String key : prefixes) {
            for (Node node : locNames.get(key)) {
                all.add(node.name());
            }
        }
        return all;
    }


    /**
     * For Project Part III (extra credit)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        if (!locNames.containsKey(cleanString(locationName))) {
            return null;
        }
        List<Map<String, Object>> mappings = new ArrayList<>();
        for (Node node : locNames.get(cleanString(locationName))) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("lat", node.lat());
            map.put("lon", node.lon());
            map.put("name", node.name());
            map.put("id", node.id());
            mappings.add(map);
        }
        return mappings;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

}
