import java.util.*;

public class socialDistancing {
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
        if (!scenario.isEmpty()) {
            scenarios.add(scenario);
        }
        scanner.close();

        for (ArrayList<String> s : scenarios) {
            if (s.isEmpty()){
                continue;
            } 
            String[] dimensions = s.get(0).split("\\s+");
            int height = Integer.parseInt(dimensions[0]);
            int width = Integer.parseInt(dimensions[1]);
            ArrayList<Point> pointsList = new ArrayList<>();
            for (int x = 1; x < s.size(); x++) {
                String[] coords = s.get(x).split("\\s+");
                int row = Integer.parseInt(coords[0]);
                int col = Integer.parseInt(coords[1]);
                pointsList.add(new Point(row, col));
            }
            int minimumDistance = Math.max(height, width);
            //System.out.println(minimumDistance);
            
            while (optimalPath(pointsList, height, width, minimumDistance) == - 1) {
                minimumDistance--;
            }    
            System.out.println(minimumDistance + " " + optimalPath(pointsList, height, width, minimumDistance));
        }
    }

    public static class Point {
        public int row, col;
        public Point() {
            this(0, 0); 
        }
        public Point(int row, int col) { 
            this.row = row; 
            this.col = col; 
        }
        public int distTo(Point p2) { 
            return Math.abs(this.row - p2.row) + Math.abs(this.col - p2.col); 
        }
    }

    public static class Path {
        public Point position;
        public ArrayList<Point> pointsList;
        public int width, height, minimumDistance;
        public int[] distances;

        public Path(int height, int width, ArrayList<Point> pointsList, int minimumDistance) {
            this(height, width, pointsList, minimumDistance, new Point(), new int[pointsList.size()]);
            Arrays.fill(distances, Integer.MAX_VALUE);
            for (int x = 0; x < pointsList.size(); x++){
                distances[x] = position.distTo(pointsList.get(x));
            } 
        }

        public Path(int height, int width, ArrayList<Point> pointsList, int minimumDistance, Point pos, int[] distances) {
            this.height = height;
            this.width = width;
            this.pointsList = pointsList;
            this.position = pos;
            this.minimumDistance = minimumDistance;
            this.distances = Arrays.copyOf(distances, distances.length);
        }

        public boolean isFarEnough() {
            for (int x = 0; x < pointsList.size(); x++) {
                int dist = position.distTo(pointsList.get(x));
                if (this.minimumDistance > dist){
                    return false;
                } else if (this.distances[x] > dist){
                    this.distances[x] = dist;
                } 
            }
            return true;
        }

        public boolean move(int rowDir, int colDir) {
            int newRow = position.row + rowDir;
            int newCol = position.col + colDir;
            if (newRow > -1 && newCol > -1 && newRow < this.height && newCol < this.width) {
                position.row = newRow;
                position.col = newCol;
                return true;
            }
            return false;
        }

        public boolean isFinished() { 
            return this.position.row == height - 1 && this.position.col == width - 1; 
        }
    }

    public static int optimalPath(ArrayList<Point> pointsList, int height, int width, int minimumDistance) {
        Queue<Path> queue = new LinkedList<>();
        queue.offer(new Path(height, width, pointsList, minimumDistance));
        int[][] directions = { 
            { 1, 0 }, 
            { 0, 1 } 
        };
        ArrayList<Path> completePaths = new ArrayList<>();
        while (!queue.isEmpty()) {
            Path currentPath = queue.poll();
            if (currentPath.isFinished()){
                completePaths.add(currentPath);
            } 
            for (int[] dir : directions) {
                Path temp = new Path(height, width, pointsList, minimumDistance, new Point(currentPath.position.row, currentPath.position.col), currentPath.distances);
                if (temp.move(dir[0], dir[1]) && temp.isFarEnough()){
                    queue.offer(temp);
                }
            }
        }
        int max = 0;
        for (Path p : completePaths) {
            int sum = 0;
            for (int dist : p.distances){
                sum += dist;
            } 
            if (sum > max) {
                max = sum;
            }
        }
        return completePaths.isEmpty() ? -1 : max;
    } 
}
