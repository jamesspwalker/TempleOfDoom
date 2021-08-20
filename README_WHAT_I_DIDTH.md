###### EXPLORE

To start with I coded directly in the explore() method of the Explorer(Algernon) class. To see how _Algernon_ moved, I moved the character to the unvisited neighbouring tile closest to the orb, as expected the Explorer would move towards the orb but the moment it reached a dead end, it would stop and throw an error.

My second adventure with the Explorer had me implementing a way to backtrack whenever the Explorer got stuck. I implemented a List to keep track of the previously visited locations and if the Explorer could no longer move forward he would move back to the previous position and attempt to continue from there, once again the Explorer failed to escape, he could now backtrack certain single path dead ends, but anything more complex was too tough a task.

I then decided to attempt to implement a Recursive Back Tracking(Depth First Search). I created a new Class _ArkRaider_ outside the Explorer class to be called upon, with a method _search()_. I created an ArrayList _visited_ of all previously visited locations and a Stack _path_ of the current path the explorer is traversing. The _search_ function uses DFS to map a path towards the orb, upon reaching a dead end it backtracks until a point that it can continue in a different direction.



###### ESCAPE

Here I created a new Class _ArkEscape_ to deal with the _escape()_ section of the _Explorer_. I split this into 2 sections, firstly a method, _shortestPath()_ finding the shortest possible path to the exit, so the _Explorer_ will always return to the exit before time is up and secondly a method, _searchForGold()_, to gather as much gold as possible before returning to the exit. Alongside these 2 main methods, I also implemented a helper method _gatherGold()_ to pick up gold from the current tile. 

In the method _shortestPath()_ I repurposed the _minPathLengthToTarget()_ method found in the _Cavern_ class which is an implementation of Dijkstra's algorithm. The original method used the Class _InternalMinHeap_, I originally had this in my implementation but this required making the _InternalMinHeap_ public, so a workaround was required. The _InternalMinHeap_ is "An instance is a priority queue of elements of type E implemented as a min-heap.", so the logical replacement would be a _PriorityQueue_. The _InternalMinHeap_ accepted a Node and a weight when a new element is added to it, so a _PriorityQueue_ must be of a type that consisted of a Node and an Integer weight. For this it required creating a custom variable type, _WeightedNodeTuple_. The _WeightedNodeTuple_ implements _Comparable_ to use the method _equals()_ in place of using the _changePriority()_ method from the _InternalMinHeap_

The method _findLargestGold_ searches out all the gold values in the cavern and sorts the tiles by the value of the gold upon them. It then checks to see if the gold is within reachable distance using the _shortestPath_ method, if it is, it is added on to the stack. Once all reachable gold is added on to the stack, the _shortestPath_ method is then used to find the shortest path to the exit which also added onto the stack. This method makes use of the helper method calcDistance which calculates the total amount of time taken to traverse the nodes in a stack.

###### LIMITATIONS/PROBLEMS

The limitations of a DFS are the fact it doesn't scale well, larger more complex mazes will require larger amounts of backtracking. Implementing a Shortest Path(Breadth-first search) would require the use of a Queue rather than a stack and would find the shortest possible path by first considering all the neighbours, this would work for smaller mazes and in this case would probably be more effective. The current DFS tends to find the exit in a timely fashion but can also require copious amounts of backtracking.

The ArkEscape also isn't the efficient at finding gold, it simply weighs the tiles to visit solely based on the amount of gold on the tile, it fails to consider the distance and the amount of gold available on the path to the tile. I've also had to include a buffer of 10 for calculating whether the tile with gold is within reach, this is because I have overlooked the traversal of one step at some point but haven't been able to fix this bug and 15 is the maximum amount of time that can be consumed by moving tiles. Previously the solution would tend to have a higher score but unfortunately would fail about 30% of the time, which would mean the death of Algernon.

Writing tests before tackling the code also fell to the wayside, and if not for time constraints would have been much more thorough. 


###### Files created/modified:

    ArkEscape

    ArkRaider
    
    Explorer
    
    WeightedNodeTuple(Interface)

    GamesState(renamed the explorer)