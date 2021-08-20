package student;

import game.ExplorationState;
import game.NodeStatus;

import java.util.ArrayList;
import java.util.Stack;

public class ArkRaider {

//    the state
    final private ExplorationState state;
//    Arraylist for all previously visited locations
    private ArrayList<Long> visited = new ArrayList<>();
//    Stack for path currently on, to allow for backtracking
    private Stack<Long> path = new Stack<>();

//    Constructor
    public ArkRaider(ExplorationState state) {
        this.state = state;
    }

    public void search() {
//        add current location to path
        path.add(state.getCurrentLocation());

//        checks if current location has been visited, if not adds it to the ArrayList
        if(!visited.contains(state.getCurrentLocation())){
            visited.add(state.getCurrentLocation());
        }

//        search until distance to the orb is 0
        while (state.getDistanceToTarget() != 0){
//            list of unvisited neighbouring nodes
            ArrayList<NodeStatus> unvisitedNeighbours = new ArrayList<>();
//            check if each neighbouring node has been visited and adds them to the unvisited list if not
            for (NodeStatus neighbour : state.getNeighbours()){
                if (!visited.contains(neighbour.nodeID())){
                    unvisitedNeighbours.add(neighbour);
                }
            }
//            Checks if there are unvisited neighbours, and finds the ID of the unvisitedNeighbour closest to the orb
            if (!unvisitedNeighbours.isEmpty()){
                unvisitedNeighbours.sort(NodeStatus::compareTo);
                Long nodeID = unvisitedNeighbours.get(0).nodeID();
                path.add(nodeID);
                visited.add(nodeID);
                state.moveTo(nodeID);
            }
//            Else removes the current node from the path and moves one step back
            else {
                path.pop();
                state.moveTo(path.peek());
            }

        }

    }


}

