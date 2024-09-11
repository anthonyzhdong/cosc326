import java.util.*;

public class SocialDistance {
    public static int[][] grid;
    public static int[][] distanceToNearestSeated;
    public static List<Point> seatedPoints;

    public static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class PathPoint extends Point {
        int minDistance;
        int[] closestDistances;
        List<Point> path;
        int cost;

        PathPoint(int x, int y, int minDistance, int[] closestDistances, List<Point> path, int cost) {
            super(x, y);
            this.minDistance = minDistance;
            this.closestDistances = closestDistances.clone();
            this.path = new ArrayList<>(path);
            this.cost = cost;
        }

        public int getTotalDistance() {
            return Arrays.stream(this.closestDistances).sum();
        }

        public int getHeuristicCost(Point goal) {
            return Math.abs(this.x - goal.x) + Math.abs(this.y - goal.y);
        }
    }

    public static void setGrid(String input) {
        String[] size = input.split("\\s+");
        int xLength = Integer.parseInt(size[0]);
        int yLength = Integer.parseInt(size[1]);
        grid = new int[xLength][yLength];
        distanceToNearestSeated = new int[xLength][yLength];
        seatedPoints = new ArrayList<>();
        for (int[] row : distanceToNearestSeated) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
    }

    public static void setPoint(int x, int y) {
        grid[x][y] = 1;
        seatedPoints.add(new Point(x, y));
    }

    public static void calculateDistances() {
        Queue<Point> queue = new LinkedList<>();
        for (Point p : seatedPoints) {
            queue.add(p);
            distanceToNearestSeated[p.x][p.y] = 0;
        }

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[0].length) {
                    if (distanceToNearestSeated[nx][ny] > distanceToNearestSeated[current.x][current.y] + 1) {
                        distanceToNearestSeated[nx][ny] = distanceToNearestSeated[current.x][current.y] + 1;
                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }
    }

    public static void findOptimalPath() {
        PriorityQueue<PathPoint> pq = new PriorityQueue<>((a, b) -> {
            int costA = a.cost + a.getHeuristicCost(new Point(grid.length - 1, grid[0].length - 1));
            int costB = b.cost + b.getHeuristicCost(new Point(grid.length - 1, grid[0].length - 1));
            if (costA != costB) return costA - costB;
            if (b.minDistance != a.minDistance) return b.minDistance - a.minDistance;
            return b.getTotalDistance() - a.getTotalDistance();
        });

        int[] initialClosestDistances = new int[seatedPoints.size()];
        Arrays.fill(initialClosestDistances, Integer.MAX_VALUE);
        pq.add(new PathPoint(0, 0, distanceToNearestSeated[0][0], initialClosestDistances, new ArrayList<>(), 0));
        boolean[][] visited = new boolean[grid.length][grid[0].length];

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        List<Point> finalPath = new ArrayList<>();
        int finalMinDistance = -1;
        int finalTotalDistance = -1;

        while (!pq.isEmpty()) {
            PathPoint current = pq.poll();
            if (visited[current.x][current.y]) continue;
            visited[current.x][current.y] = true;
            current.path.add(new Point(current.x, current.y));

            System.out.println("Visiting (" + current.x + ", " + current.y + ") with minDistance " + current.minDistance + " and totalDistance " + current.getTotalDistance() + " and cost " + current.cost);

            if (current.x == grid.length - 1 && current.y == grid[0].length - 1) {
                finalPath = current.path;
                finalMinDistance = current.minDistance;
               // if(finalTotalDistance < current.getTotalDistance()){
                if (finalTotalDistance == -1 || finalTotalDistance > current.getTotalDistance()) {
                    finalTotalDistance = current.getTotalDistance();
                }
                    System.out.println("cur : " + current.getTotalDistance() + " " + Arrays.toString(current.closestDistances));
                    //finalTotalDistance = current.getTotalDistance();
                //}
                break;
            }

            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < grid.length && ny < grid[0].length && !visited[nx][ny]) {
                    int newMinDistance = Math.min(current.minDistance, distanceToNearestSeated[nx][ny]);
                    int[] newClosestDistances = current.closestDistances.clone();
                    for (int i = 0; i < seatedPoints.size(); i++) {
                        Point sp = seatedPoints.get(i);
                        int distanceToSP = Math.abs(nx - sp.x) + Math.abs(ny - sp.y);
                        newClosestDistances[i] = Math.min(newClosestDistances[i], distanceToSP);
                    }
                    int newTotalDistance = Arrays.stream(newClosestDistances).sum();
                    List<Point> newPath = new ArrayList<>(current.path);
                    int newCost = current.cost + 1;
                    pq.add(new PathPoint(nx, ny, newMinDistance, newClosestDistances, newPath, newCost));

                    System.out.println("Adding (" + nx + ", " + ny + ") with newMinDistance " + newMinDistance + " and newTotalDistance " + newTotalDistance + " and cost " + newCost);
                }
            }
        }

        System.out.println("min " + finalMinDistance + ", total " + finalTotalDistance);
        System.out.println("Path Taken: ");
        for (Point p : finalPath) {
            System.out.println("(" + p.x + ", " + p.y + ")");
        }
    }
    
    
    
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<ArrayList<String>> scenarios = new ArrayList<>();
        ArrayList<String> scenario = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                scenarios.add(scenario);
                scenario = new ArrayList<>();
            } else {
                scenario.add(line);
            }
        }
        scenarios.add(scenario);

        for (ArrayList<String> s : scenarios) {
            if (s.isEmpty()) continue;
            setGrid(s.get(0));
            for (int i = 1; i < s.size(); i++) {
                String[] coords = s.get(i).split("\\s+");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                setPoint(x, y);
            }
            calculateDistances();
            findOptimalPath();
        }

        scanner.close();
    }
}
