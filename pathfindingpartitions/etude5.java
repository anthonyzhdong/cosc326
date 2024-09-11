package etude5;
import java.util.*;

// donan928 2169260
public class etude5 {
    public static void main(String[]args){
        
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<String>();
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.length()>0){
                if(line.charAt(0) == '#'){
                    continue;
                }
            }
            lines.add(line);
        }
        pathfindingPartitions(lines);
        scanner.close();

    }

    public static void pathfindingPartitions(ArrayList<String> arr){
        int inputIndex = 0;
        for(int i = 0; i < arr.size(); i++){
            int outputIndex = 0;
            int end = 0;
            String line = arr.get(i);
            if(line.matches("-*")){
                outputIndex = i;
            }else if(i == arr.size() -1){
                outputIndex = i + 1;
                end = 1;
            }else{
                continue;
            }

            List<String> combination = arr.subList(inputIndex, outputIndex);
            List<Integer> startList = convert(combination.get(0));
            List<Integer> endList = convert(combination.get(1));

            Collections.sort(startList);
            Collections.sort(endList);
            Collections.reverse(startList);
            Collections.reverse(endList);
            
            List<List<Integer>> partitionsList = moveList(startList, endList);
            
            if (partitionsList == null || partitionsList.isEmpty()) {
                System.out.println("# No solution possible");
                System.out.println(combination.get(0));
                System.out.println(combination.get(1));
            } else {
                System.out.println("# Moves required: " + (partitionsList.size() - 1));
                for (List<Integer> list : partitionsList) {
                    for (int c : list) {
                        System.out.print(c);
                        System.out.print(" ");
                    }
                    System.out.println();
                }
            }
            if (end != 1){
                System.out.println("-------");
            }
            inputIndex = outputIndex + 1;
        }
    }

    public static List<List<Integer>> moveList(List<Integer> start, List<Integer> end){
        Queue<List<Integer>> queue = new LinkedList<>();
        queue.add(start);
        Map<List<Integer>,List<Integer>> path = new HashMap<>();
        path.put(start,null);
        while(!queue.isEmpty()){
            List<Integer> currentQueue = queue.poll();
            if(currentQueue.equals(end)){
                List<List<Integer>> partitionPath = new ArrayList<>();
                while(currentQueue != null){
                    partitionPath.add(0,currentQueue);
                    currentQueue = path.get(currentQueue);
                }
                return partitionPath;
            }
            for(List<Integer> nextMove : nextPartition(currentQueue)){
                if(!path.containsKey(nextMove)){
                    queue.add(nextMove);
                    path.put(nextMove, currentQueue);
                }
            }
        }
        return null;
    }

    public static List<List<Integer>> nextPartition(List<Integer> partition){
        List<List<Integer>> partitionList = new ArrayList<>();
        for(int column = 0; column < partition.get(0); column++){
            List<Integer> nextPartition = new ArrayList<>(partition);
            int dotsDeleted = 0;
            for(int y = 0; y<nextPartition.size(); y++){
                if(nextPartition.get(y) > column){
                    nextPartition.set(y, nextPartition.get(y)-1);
                    dotsDeleted++;
                }
            }
            if(dotsDeleted == 0){
                continue;
            }else{
                nextPartition.removeIf(n -> (n==0));
                int newRow = dotsDeleted;
                int insertIndex = 0;
                while(insertIndex < nextPartition.size() && nextPartition.get(insertIndex) >= newRow){
                    insertIndex++;
                }
                nextPartition.add(insertIndex, newRow);
                if(!partitionList.contains(nextPartition)){
                    partitionList.add(nextPartition);
                }
            }
        }
        return partitionList;
    }

    public static List<Integer> convert(String str) {
        List<Integer> list = new ArrayList<>();
        for (String num : str.split(" ")) {
            list.add(Integer.parseInt(num));
        }
        return list;
    }

}
