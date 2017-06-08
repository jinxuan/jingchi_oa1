package com.jingchi;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Hello world!
 */

class Node {
    public int x, y;
    public HashSet<Integer> checkPointSet;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.checkPointSet = new HashSet<>();
    }

    public Node(int x, int y, HashSet<Integer> checkPointSet) {
        this.x = x;
        this.y = y;
        this.checkPointSet = new HashSet<>();
        for (int item : checkPointSet) {
            this.checkPointSet.add(new Integer(item));
        }
    }
}

public class OA1 {
    protected int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};

    /**
     * Main entry point.
     *
     * @param townmap       Matrix with '.', 'S', 'G' and '#'
     * @param validateInput flag to indicate whether input matrix need to be validated
     * @return
     */
    public int minTraverseDistance(char[][] townmap, boolean validateInput) {
        if (validateInput) {
            if (!validate(townmap))
                return -1;
        }
        return minTraverseDistance(townmap);
    }

    /**
     * Use BFS to calculate min distance from S go G while pass all check point.
     *
     * @param townmap
     * @return
     */
    public int minTraverseDistance(char[][] townmap) {
        int upperBound = greedyMinTraverseDistance(townmap);
        int[] coordinate = findStartPointEndPoint(townmap);
        int goal_x = coordinate[2];
        int goal_y = coordinate[3];
        List<Node> curLevel = new ArrayList<>();
        curLevel.add(new Node(coordinate[0], coordinate[1]));
        int checkPointCount = countCheckPoint(townmap);
        int step = 1;
        while (true) {
            List<Node> nextLevel = new ArrayList<>();
            for (Node node : curLevel) {
                for (int[] direction : directions) {
                    int x = node.x + direction[0];
                    int y = node.y + direction[1];
                    if (x == goal_x && y == goal_y && node.checkPointSet.size() == checkPointCount) {
                        return step;
                    }
                    if (x >= 0 && y >= 0 && x < townmap.length && y < townmap[0].length && townmap[x][y] != '#') {
                        //Prune
                        //min distance to nearest checkPoint
                        int minDistanceToNearest = minNearestCheckPointDistance(townmap, x, y);
                        //min distance to the goal
                        int minDistanceToGoal = minNearestCheckPointDistance(townmap, goal_x, goal_y);
                        int unFoundCheckPointNum = checkPointCount - node.checkPointSet.size();
                        if (minDistanceToNearest * (unFoundCheckPointNum - 1) + minDistanceToGoal + step > upperBound) {
                            continue;
                        }
                        HashSet<Integer> tmp = new HashSet<>();
                        for (Integer item : node.checkPointSet) {
                            tmp.add(item);
                        }

                        if (townmap[x][y] == 'X') {
                            tmp.add(x * townmap.length + y);
                        }
                        nextLevel.add(new Node(x, y, tmp));
                    }
                }
            }
            step += 1;
            curLevel = nextLevel;
        }
    }

    /**
     * A helper function, for a given point, check its distance to nearest check point.
     *
     * @param townmap
     * @param x
     * @param y
     * @return
     */
    public int minNearestCheckPointDistance(char[][] townmap, int x, int y) {
        char[][] duplicate_townmap = new char[townmap.length][townmap[0].length];
        for (int i = 0; i < townmap.length; i++) {
            for (int j = 0; j < townmap[0].length; j++) {
                duplicate_townmap[i][j] = townmap[i][j];
            }
        }
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{x, y});
        while (q.size() != 0) {
            int[] cur = q.poll();
            for (int[] direction : directions) {
                int i = cur[0] + direction[0];
                int j = cur[1] + direction[1];
                if (duplicate_townmap[i][j] == 'X') {
                    return Math.abs(i - x) + Math.abs(j - y);
                }
                if (i >= 0 && j >= 0 && i <= duplicate_townmap.length && j <= duplicate_townmap[0].length &&
                        duplicate_townmap[i][j] != '#' && duplicate_townmap[i][j] != 'B') {
                    q.add(new int[]{i, j});
                }
                duplicate_townmap[i][j] = 'B';
            }
        }
        return -1; // This shouldn't happen
    }


    /**
     * Greedy algorithm may not generate the best result. But it can used as upper bound
     *
     * @param townmap
     * @return
     */
    public int greedyMinTraverseDistance(char[][] townmap) {
        int[] coordinate = findStartPointEndPoint(townmap);
        int start_x = coordinate[0];
        int start_y = coordinate[1];
        int goal_x = coordinate[2];
        int goal_y = coordinate[3];
        int distance = 0;
        List<int[]> checkPointList = new LinkedList<>();
        int checkpointCount = 0;
        for (int i = 0; i < townmap.length; i++) {
            for (int j = 0; j < townmap[0].length; j++) {
                if (townmap[i][j] == 'X') {
                    checkpointCount += 1;
                    checkPointList.add(new int[]{i, j});
                }
            }
        }
        while (checkpointCount != 0) {
            Map.Entry<Integer, int[]> res = findNearest(start_x, start_y, checkPointList);
            distance += res.getKey();
            int[] value = res.getValue();
            Iterator<int[]> it = checkPointList.iterator();
            while (it.hasNext()) {
                int[] cur = it.next();
                if (cur[0] == value[0] && cur[1] == value[1]) {
                    it.remove();
                    break;
                }
            }
            checkpointCount--;
            start_x = value[0];
            start_y = value[1];
        }
        distance += Math.abs(start_x - goal_x) + Math.abs(start_y - goal_y);
        return distance;
    }

    /**
     * A helper function, that find the nearest checkpoint from input list.
     * @param start_x
     * @param start_y
     * @param checkPointList
     * @return
     */
    private Map.Entry<Integer, int[]> findNearest(int start_x, int start_y, List<int[]> checkPointList) {
        TreeMap<Integer, int[]> treemap = new TreeMap<>();
        for (int[] checkPoint : checkPointList) {
            int pair_distance = Math.abs(checkPoint[0] - start_x) + Math.abs(checkPoint[1] - start_y);
            treemap.put(pair_distance, new int[]{checkPoint[0], checkPoint[1]});
        }
        return treemap.pollFirstEntry();
    }

    /**
     * Check if the input is an valid town map. x.e. it must follow the following rules:
     * <ul>
     * <li>Must contains character 'S' and 'G'</li>
     * <li>There exists a path from S to G</li>
     * </ul>
     *
     * @return return whether the input is valid
     */
    public boolean validate(char[][] townmap) {
        if (townmap == null || townmap.length == 0 || townmap[0].length == 0) {
            return false;
        }
        boolean foundS = false, foundG = false;
        int[] coordinate = findStartPointEndPoint(townmap);
        if (coordinate[0] != -1)
            foundS = true;
        if (coordinate[2] != -1)
            foundG = true;

        if (!(foundS && foundG)) {
            return false;
        }
        boolean[][] visited = new boolean[townmap.length][townmap[0].length];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[0].length; j++) {
                visited[i][j] = false;
            }
        }
        visited[coordinate[0]][coordinate[1]] = true;
        boolean found = reachable(townmap, visited, coordinate[0], coordinate[1], coordinate[2], coordinate[3]);
        return found;
    }

    /**
     * Validate function, check if start point is able to arrive end point
     * @param townmap
     * @param visited
     * @param start_x
     * @param start_y
     * @param goal_x
     * @param goal_y
     * @return
     */
    public boolean reachable(char[][] townmap, boolean[][] visited, int start_x, int start_y, int goal_x, int goal_y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start_x, start_y});
        while (queue.peek() != null) {
            int[] cur = queue.poll();
            if (cur[0] == goal_x && cur[1] == goal_y)
                return true;
            for (int[] direction : directions) {
                int x = direction[0] + cur[0];
                int y = direction[1] + cur[1];
                if (visited[x][y])
                    continue;
                visited[x][y] = true;
                if (x >= 0 && y >= 0 && x < townmap.length && y < townmap[0].length && townmap[x][y] != '#') {
                    queue.add(new int[]{x, y});
                }
            }
        }
        return false;
    }

    /**
     * Validate function Check if 'S' and 'G' exists in the input matrix
     *
     * @param townmap input 2-dimension character
     * @return size 4 int array
     */
    public int[] findStartPointEndPoint(char[][] townmap) {
        int[] coordinate = new int[]{-1, -1, -1, -1};
        for (int i = 0; i < townmap.length; i++) {
            for (int j = 0; j < townmap[0].length; j++) {
                if (townmap[i][j] == 'S') {
                    coordinate[0] = i;
                    coordinate[1] = j;
                } else if (townmap[i][j] == 'G') {
                    coordinate[2] = i;
                    coordinate[3] = j;
                }
            }
        }
        return coordinate;
    }

    /**
     * A helper function that return the number of check points in the input matrix
     * @param townmap
     * @return
     */
    public int countCheckPoint(char[][] townmap) {
        int count = 0;
        for (int i = 0; i < townmap.length; i++) {
            for (int j = 0; j < townmap[0].length; j++) {
                if (townmap[i][j] == 'X')
                    count += 1;
            }
        }
        return count;
    }
}
