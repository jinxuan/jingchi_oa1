# JingChi OA 1

This is the solution wrote by Jinxuan Wu for JingChi code assesment 1. 

# How to run
This is an Maven project. So you can just git clone this project and run it using `mvn test` .

# Thought process
This question is essentially a Travelling salesman problem. So we could solve it using bread-first-search.

# Algorithm
1. Check whether if input is valid. i.e. Whether there is a path from S to G. If not return none. 
2. Use a greedy algorithm to calculate the cost from S to G, set this cost upper bound.
3. Bread first search from S to G. In each step we moves in four direction and use a Map to record all the checkpoint we have visited. If all checkpoint have been visited and arrived at G. This is the min cost from S to G while traverse all checkpont.
4. Pruning. Remove node from queue in bread first search if `current cost + (remaining_node_num) * (min_distance_to_next_check_point) + min_distance_from_G_to_checkpoint` is larger than the upper bound we get in greedy algorithm. We stop expand this path.

To process large test cases such as test case 12. We add a condition to efficiently remove redudant result.

# Complexity
O(n!) This is an NP hard problem

# Test case
I manually construct 15 test cases. But this program will not finished in timely fashion if matrix size more than 20.    

# Limitation 
1. Current this is not able to handle the large input in the requirement. i.e. size 100 matrix and 18 check point. But we could improve this by using Dijskra to calculate distance between all pairs of point. Then this problem is converted it to the graph TSP problem. This is at most 18! and By using similar pruning strategy I think this strategy can handle the large test cases.
2. I only check whether the destination ponit G is achieveable. But due to time limitation I didn't have time to check whether all checkpoint is reachable from S. But same logic is used. 

 
 

