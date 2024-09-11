// donan928 2169260
import java.util.*;

public class etude10 {

    private static HashSet<ArrayList<Integer>> happyPositions = new HashSet<>();
    private static HashSet<ArrayList<Integer>> sadPositions = new HashSet<>();
    private static HashSet<ArrayList<Integer>> visited = new HashSet<>();
    private static ArrayList<Integer> startPosition;
    private static List<ArrayList<Integer>> targetPartitions;

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            // Skip lines that are comments or empty
            if ((line.startsWith("#") && line.length() > 0) || line.isEmpty()) {
                continue;
            }
            lines.add(line);
        }

        // Get partitions from input lines
        ArrayList<ArrayList<String>> partitions = extractPartitions(lines);

        // Process each partition
        for (int x = 0; x < partitions.size(); x++) {
            ArrayList<String> partition = partitions.get(x);
            ArrayList<String> targets = new ArrayList<>(partition.subList(1, partition.size()));
            String start = partition.get(0);
            
            // Get game result
            String result = determineGameResult(start, targets);
            System.out.println(result);

            // Separator between partitions
            if (x != partitions.size() - 1) {
                System.out.println("--------");
            }

            // Reset game state
            happyPositions.clear();
            sadPositions.clear();
            visited.clear();
        }
        scan.close();
    }

    /**
     * Extracts partitions from the input lines.
     */
    private static ArrayList<ArrayList<String>> extractPartitions(ArrayList<String> input) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        ArrayList<ArrayList<String>> partitions = new ArrayList<>();
        ArrayList<String> partition = new ArrayList<>();

        for (int x = 0; x < input.size(); x++) {
            String line = input.get(x);
            if (partition == null) {
                partition = new ArrayList<>();
            }
            if (isSeparator(line) || x == input.size() - 1) {
                if (!isSeparator(line)) {
                    partition.add(line);
                }
                partitions.add(partition);
                partition = null;
            } else {
                partition.add(line);
            }
        }
        
        for (ArrayList<String> scenario : partitions) {
            boolean isScenarioEmpty = scenario.stream().allMatch(String::isBlank);
            if (!isScenarioEmpty) {
                result.add(scenario);
            }
        }
    
        return result;
    }
    
    /**
     * Checks if the input line is a separator.
     */
    private static boolean isSeparator(String input) {
        return input.length() >= 3 && input.chars().allMatch(c -> c == '-');
    }

    /**
     * Converts a space-separated string of numbers into an ArrayList of integers.
     */
    private static ArrayList<Integer> convertToIntegerList(String line) {
        String[] separated = line.split(" ");
        ArrayList<Integer> partition = new ArrayList<>();
        for (String individual : separated) {
            partition.add(Integer.parseInt(individual));
        }
        return partition;
    }

    /**
     * Determines the game result for a given start position and target partitions.
     */
    public static String determineGameResult(String start, ArrayList<String> partitions) {
        startPosition = convertToIntegerList(start);
        targetPartitions = new ArrayList<>();
        for (String s : partitions) {
            targetPartitions.add(convertToIntegerList(s));
        }

        String gameResult = evaluateGameOutcome(startPosition, targetPartitions);
        StringBuilder outcome = new StringBuilder(start + "\n\n");
        for (String s : partitions) {
            outcome.append(s).append("\n");
        }
        return outcome + gameResult;
    }

    /**
     * Evaluates the game outcome given the start position and target partitions.
     */
    private static String evaluateGameOutcome(ArrayList<Integer> start, List<ArrayList<Integer>> targets) {
        Queue<ArrayList<Integer>> queue = new LinkedList<>();
        for (ArrayList<Integer> target : targets) {
            queue.add(target);
            sadPositions.add(target);
        }

        while (!queue.isEmpty()) {
            ArrayList<Integer> curr = queue.poll();
            visited.add(curr);
            if (sadPositions.contains(curr)) {
                for (ArrayList<Integer> move : allBackwardMoves(curr)) { 
                    if (!visited.contains(move) && !sadPositions.contains(move)) {
                        happyPositions.add(move);
                        queue.add(move);
                    }
                }
            } else {
                for (ArrayList<Integer> move : allBackwardMoves(curr)) {
                    if (!visited.contains(move)) {
                        if (allLeadToHappy(move)) {
                            sadPositions.add(move);
                            queue.add(move);
                        } else if (anyLeadToSad(move)) {
                            happyPositions.add(move);
                            queue.add(move);
                        }
                    }
                }
            }
        }

        if (visited.contains(start)) {
            if (happyPositions.contains(start)) {
                return "# WIN";
            } else {
                return "# LOSE";
            }
        } else {
            return "# DRAW";
        }
    }

    /**
     * Checks if any forward moves lead to a sad position.
     */
    private static boolean anyLeadToSad(ArrayList<Integer> partition) {
        for (ArrayList<Integer> move : allForwardMoves(partition)) {
            if (sadPositions.contains(move)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if all forward moves lead to a happy position.
     */
    private static boolean allLeadToHappy(ArrayList<Integer> partition) {
        for (ArrayList<Integer> move : allForwardMoves(partition)) {
            if (!happyPositions.contains(move)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generates all possible forward moves for a given partition.
     */
    private static ArrayList<ArrayList<Integer>> allForwardMoves(ArrayList<Integer> partition) {
        HashSet<ArrayList<Integer>> forwardMoves = new HashSet<>();
        for (int x : potentialForwardMoves(partition)) {
            forwardMoves.add(executeForwardMove(partition, x));
        }
        return new ArrayList<>(forwardMoves);
    }

    /**
     * Determines potential forward moves for a given partition.
     */
    private static int[] potentialForwardMoves(ArrayList<Integer> partition) {
        int[] moves = new int[partition.get(0)];
        for (int x = 0; x < moves.length; x++) {
            moves[x] = x + 1;
        }
        return moves;
    }

    /**
     * Executes a forward move on a partition.
     */
    private static ArrayList<Integer> executeForwardMove(ArrayList<Integer> partition, int colToMove) {
        int rowSize = 0;
        ArrayList<Integer> newPartition = new ArrayList<>();
        for (int x : partition) {
            if (x >= colToMove) {
                rowSize++;
                if (x - 1 != 0) {
                    newPartition.add(x - 1);
                }
            } else {
                newPartition.add(x);
            }
        }
        int index = 0;
        while (index < newPartition.size() && newPartition.get(index) >= rowSize) {
            index++;
        }
        newPartition.add(index, rowSize);
        Collections.sort(newPartition, Collections.reverseOrder());
        return newPartition;
    }

    /**
     * Generates all possible backward moves for a given partition.
     */
    private static ArrayList<ArrayList<Integer>> allBackwardMoves(ArrayList<Integer> partition) {
        HashSet<ArrayList<Integer>> backwardMoves = new HashSet<>();
        for (int x : potentialMovesBack(partition)) {
            backwardMoves.add(executeBackwardMove(partition, x));
        }
        return new ArrayList<>(backwardMoves);
    }

    /**
     * Determines potential backward moves for a given partition.
     */
    private static int[] potentialMovesBack(ArrayList<Integer> partition){
        int[] moveList = new int[partition.size()];
        for (int x = 0; x < moveList.length; x++) {
            moveList[x] = x;
        }
        return moveList;
    }

    /**
     * Executes a backward move on a partition.
     */
    private static ArrayList<Integer> executeBackwardMove(ArrayList<Integer> partition, int colToMove) {
        ArrayList<Integer> newPartition = new ArrayList<>(partition);
        int amountToAdd = newPartition.get(colToMove);
        newPartition.remove(colToMove);
        for (int x = 0; x < amountToAdd; x++) {
            if (x >= newPartition.size()) {
                newPartition.add(1);
            } else {
                newPartition.set(x, newPartition.get(x) + 1);
            }
        }
        Collections.sort(newPartition, Collections.reverseOrder());
        return newPartition;
    }
}
