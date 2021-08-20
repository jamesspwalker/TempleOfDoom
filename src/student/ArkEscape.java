package student;

import game.*;

import javax.swing.*;
import java.util.*;
import java.util.stream.Stream;

public class ArkEscape {

    private final EscapeState state;

//    private final List<Node> escapeRoute;

    public ArkEscape(EscapeState state) {
        this.state = state;
//        this.escapeRoute = shortestPath(state.getCurrentNode(), state.getExit());
    }

    //    Picks up gold on tile currently standing on
    private void gatherGold(Tile tile) {
        if (tile.getGold() != 0) {
            state.pickUpGold();
        }
    }

    // Find the largest stashes of gold in the cavern and visit as many as possible before the time runs out
    private Stack<Node> findLargestGold(Integer timeRemaining) {
//        weight all tiles by amount of gold on them
        Stack<WeightedNodeTuple> goldTiles = new Stack<>();
        for (Node node : state.getVertices()) {
            goldTiles.add(new WeightedNodeTuple(node, node.getTile().getGold()));
        }
        goldTiles.sort(Comparator.reverseOrder());
        Stack<Node> goldRoute = new Stack<>();
        goldRoute.add(state.getCurrentNode());
//        Filter out only the tiles which can be reached within the time limit
        for (WeightedNodeTuple tuple : goldTiles) {
            if (tuple.node.getTile().getGold() > 0) {
//                System.out.println(calcDistance(goldRoute) + calcDistance(shortestPath(goldRoute.lastElement(), tuple.node)) + calcDistance(shortestPath(tuple.node, state.getExit())));
                if ((calcDistance(goldRoute) + calcDistance(shortestPath(goldRoute.lastElement(), tuple.node)) + calcDistance(shortestPath(tuple.node, state.getExit()))) + 15 < timeRemaining) {
                    goldRoute.addAll(shortestPath(goldRoute.lastElement(), tuple.node));
//                    System.out.println(calcDistance(goldRoute));

                }
            }
        }
//        System.out.println(calcDistance(shortestPath(goldRoute.lastElement(), state.getExit())));
        goldRoute.addAll(shortestPath(goldRoute.lastElement(), state.getExit()));

        Collections.reverse(goldRoute);
        goldRoute.pop();
        Collections.reverse(goldRoute);
        return goldRoute;
    }

    //    Calculate the distance of a path of nodes
    int calcDistance(Stack<Node> journey) {
        int total = 0;
        for (int i = 0; i < journey.size() - 1; i++) {
            System.out.println(journey.get(i).getEdge(journey.get(i + 1)).length);
            total += journey.get(i).getEdge(journey.get(i + 1)).length;
        }
        return total;
    }


    void leave(EscapeState state) {
        Stack<Node> escapeRoute = findLargestGold(state.getTimeRemaining());
        System.out.println("TIME REMAINING: " + state.getTimeRemaining() + " ESCAPE ROUTE: " + calcDistance(escapeRoute));
        for (Node node : escapeRoute) {
            this.gatherGold(state.getCurrentNode().getTile());
            state.moveTo(node);
        }
    }

    //    Repurposed the "minPath LengthToTarget" method from the "Cavern" class to find the shortest path.
    private Stack<Node> shortestPath(Node startNode, Node endNode) {

        Node start = startNode;
        Node end = endNode;

//        Implemented a Priority Queue to take the place of the private InternalMinHeap, taking in a Weighted Tuple
        PriorityQueue<WeightedNodeTuple> frontier = new PriorityQueue<>();

        /** Contains an entry for each node in the Settled and Frontier sets. */
        Map<Long, Integer> pathWeights = new HashMap<>();
        Stack<Node> path = new Stack<>();
//        pathMap keeps track of linked nodes, and the best possible node to move to next that shares a node.
        Map<Node, Node> linkedPath = new HashMap<>();
        pathWeights.put(startNode.getId(), 0);
        frontier.add(new WeightedNodeTuple(startNode, 0));

        while (!frontier.isEmpty()) {
            Node f = frontier.poll().node;
            int nWeight = pathWeights.get(f.getId());
            for (Edge e : f.getExits()) {
                Node w = e.getOther(f);
                int weightThroughN = nWeight + e.length();
                Integer existingWeight = pathWeights.get(w.getId());
                if (existingWeight == null) {
                    linkedPath.put(w, f);
                    pathWeights.put(w.getId(), weightThroughN);
                    frontier.add(new WeightedNodeTuple(w, weightThroughN));
                } else if (weightThroughN < existingWeight) {
                    linkedPath.put(w, f);
                    pathWeights.put(w.getId(), weightThroughN);
//                    frontier.changePriority(w, weightThroughN);
//                    remove tuple from frontier if edge is already node is already present, replaces the changePriority method from InternalMinHeap
                    frontier.removeIf(tuple -> tuple.node.equals(w));
                    frontier.add(new WeightedNodeTuple(w, weightThroughN));
                }
            }
        }
//        push the nodes in linkedPath into a Stack in order of where they can traverse from end to start
        while (end != start) {
            path.add(end);
            end = linkedPath.get(end);
        }
//        reverse the path to start to end.
        Collections.reverse(path);
        return path;
    }

    //  weighted tuple implements Comparable to allow for use of equals method
    public class WeightedNodeTuple implements Comparable<WeightedNodeTuple>, student.WeightedNodeTuple {
        private Node node;
        private int weight;

        private WeightedNodeTuple(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }

        public void print() {
            System.out.println("NODE: " + this.node + " WEIGHT: " + this.weight);
        }

        @Override
        public int compareTo(WeightedNodeTuple o) {
            if (o == null) {
                return -1;
            }
            if (o.weight == this.weight) {
                return 0;
            } else if (this.weight < o.weight) {
                return -1;
            } else {
                return 1;
            }
        }

        @Override
        public boolean equals(WeightedNodeTuple o) {
            if (o == null) {
                return false;
            }
            return this.node == o.node;
        }


    }


}


