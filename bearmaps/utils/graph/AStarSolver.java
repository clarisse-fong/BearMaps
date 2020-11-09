package bearmaps.utils.graph;
import bearmaps.utils.pq.MinHeapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class AStarSolver <Vertex> implements ShortestPathsSolver<Vertex> {
    private HashMap<Vertex, Double> distTo;
    private SolverOutcome outcome;
    private int polls = 0;
    private MinHeapPQ<Vertex> pq;
    private List<Vertex> solution;
    private double solutionWeight = 0;
    private double timeSpent = 0;
    private HashMap<Vertex, WeightedEdge<Vertex>> vParentEdge;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout){
        pq = new MinHeapPQ<>();
        distTo = new HashMap<>();
        vParentEdge = new HashMap<>();
        pq.insert(start, input.estimatedDistanceToGoal(start, end));
        distTo.put(start, 0.0);
        vParentEdge.put(start, null);
        solution = new ArrayList<>();

        Stopwatch sw = new Stopwatch();
        while (pq.size() != 0 && !pq.peek().equals(end)) {
            polls += 1;
            Vertex p = pq.poll();
            Vertex q;
            double w;

            for (WeightedEdge<Vertex> e :input.neighbors(p)) {
                if (e.to().equals(end)) {
                    Vertex currVertex = e.to();
                    vParentEdge.put(currVertex, e);
                    solution.add(currVertex);
                    while(!currVertex.equals(start)) {
                        currVertex = vParentEdge.get(currVertex).from();
                        solution.add(currVertex);
                    }
                    Collections.reverse(solution);
                    solutionWeight = e.weight();
                    outcome = SolverOutcome.SOLVED;
                    timeSpent = sw.elapsedTime() + timeSpent;
                    return;
                }

                p = e.from();
                q = e.to();
                w = e.weight();

                if (!distTo.containsKey(q)) {
                    distTo.put(q, distTo.get(p) + w);
                    vParentEdge.put(q, e);
                    pq.insert(q, distTo.get(q)
                            + input.estimatedDistanceToGoal(q,end));
                } else {
                    if (distTo.get(p) + w < distTo.get(q)) {
                        distTo.put(q, distTo.get(p) + w);
                        vParentEdge.put(q, e);
                        if (pq.contains(q)) {
                            pq.changePriority(q, distTo.get(q)
                                    + input.estimatedDistanceToGoal(q,end));
                        } else {
                            pq.insert(q, distTo.get(q)
                                    + input.estimatedDistanceToGoal(q,end));
                        }
                    }
                }

            }
            if (sw.elapsedTime() > timeout) {
                outcome = SolverOutcome.TIMEOUT;
                return;
            }
        }
        outcome = SolverOutcome.UNSOLVABLE;
        timeSpent = timeSpent + sw.elapsedTime();

    }
    public SolverOutcome outcome(){
        return outcome;
    }
    public List<Vertex> solution(){
        return solution;
    }
    public double solutionWeight(){
        return solutionWeight;
    }
    public int numStatesExplored(){
        return polls;
    }
    public double explorationTime() {
        return timeSpent;
    }
}
