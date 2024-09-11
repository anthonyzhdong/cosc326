import java.util.*;


public class carpetMethods {
    
    // no matches
    public static List<String> noMatches(int carpetSize, List<String> stockList){
        List<String> carpet = new ArrayList<>();
        // if found no match -> print else no combinations found
        if (findNoMatchesCarpet(carpet, carpetSize, stockList, new HashMap<>())) {
            //System.out.println("no matches\nCombination with the least matches:");
            carpet.forEach(System.out::println);
            //System.out.println("Number of matches: ");
        } else {
            System.out.println("not possible.");
        }
        return carpet;
    }


    private static boolean findNoMatchesCarpet(List<String> carpet, int sizeLeft, List<String> stockList, Map<String,Integer> usedCarpetPieces){
        // can add matches variable if we want to keep track of total matches -> will be 0 if it's returned though?
        // stop recursion
        if(sizeLeft == 0){
            return true;
        }
        // loop through each carpet piece in stockList
        for(String carpetPiece: stockList){

            // counts the amount of times this carpet has been used
            int timesUsed = usedCarpetPieces.getOrDefault(carpetPiece, 0);
            // checks if piece can stll be used -> not exceeding its frequency in stockList
            if(timesUsed < Collections.frequency(stockList, carpetPiece)){

                // checks if carpets is empty or last piece doesn't match next piece 
                if(carpet.isEmpty() || matchChecker(carpet.get(carpet.size() - 1), carpetPiece)){
                    // adds piece to carpet and updates it's usage by 1
                    carpet.add(carpetPiece);
                    usedCarpetPieces.put(carpetPiece, timesUsed + 1);
                    // recursion call to try complete carpet with another piece
                    if(findNoMatchesCarpet(carpet, sizeLeft - 1, stockList, usedCarpetPieces)){
                        return true;
                    }
                    // back tracks and removes last piece and decreases usage
                    carpet.remove(carpet.size() - 1);
                    usedCarpetPieces.put(carpetPiece, timesUsed);
                }

                // flipped version of carpetPiece -> does same as above but uses the flipped version instead
                String flippedCarpetPiece = new StringBuilder(carpetPiece).reverse().toString();
                if (carpet.isEmpty() || matchChecker(carpet.get(carpet.size() - 1), flippedCarpetPiece) ) {
                    carpet.add(flippedCarpetPiece);
                    usedCarpetPieces.put(carpetPiece, timesUsed + 1);
                    if (findNoMatchesCarpet(carpet, sizeLeft - 1, stockList, usedCarpetPieces)) {
                        return true;
                    }
                    carpet.remove(carpet.size() - 1);
                    usedCarpetPieces.put(carpetPiece, timesUsed);
                }
            }
        }
        // cannot find case
        return false;
    }

    // checks if piece matches 
    private static boolean matchChecker(String piece, String secondPiece){
        for(int x = 0; x < piece.length(); x++){
            if(piece.charAt(x) == secondPiece.charAt(x)){
                return false;
            }
        }
        return true;
    }


    // balanced
    public static List<String> findBalancedCarpet(List<String> stock, int size, int iterations) {
        List<String> bestCarpet = null;
        int bestBalance = Integer.MAX_VALUE; // Initialize best balance as maximum possible value
    
        for (int i = 0; i < iterations; i++) {
            List<String> currentCarpet = new ArrayList<>();
            int balance = 0;
            List<String> remainingStock = new ArrayList<>(stock); // Copy of the stock to modify
    
            // Randomly select a strip to start
            String initialStrip = remainingStock.get(new Random().nextInt(remainingStock.size()));
            currentCarpet.add(initialStrip);
            remainingStock.remove(initialStrip);
            remainingStock.remove(reverseCharList(initialStrip)); // Remove the reversed version
    
            // Add strips to the carpet until desired size is reached
            while (currentCarpet.size() < size) {
                // Find the strip that minimizes the balance when added to the current carpet
                String bestStrip = null;
                int minChange = Integer.MAX_VALUE;
    
                for (String strip : remainingStock) {
                    int change = calculateBalanceChange(currentCarpet, strip);
                    if (Math.abs(change) < Math.abs(minChange)) {
                        minChange = change;
                        bestStrip = strip;
                    }
                }
    
                // Add the best strip to the current carpet
                currentCarpet.add(bestStrip);
                remainingStock.remove(bestStrip);
                remainingStock.remove(reverseCharList(bestStrip)); // Remove the reversed version
                balance += minChange;
            }
    
            // Update the best carpet if the current one is more balanced
            if (Math.abs(balance) < Math.abs(bestBalance)) {
                bestBalance = balance;
                bestCarpet = new ArrayList<>(currentCarpet);
            }
        }
        if(bestCarpet!=null){
            for(String piece : bestCarpet){
                System.out.println(piece);
            }
        }
        System.out.println("Balance: " + (countMatches(bestCarpet)-countNonMatches(bestCarpet)));
    
        return bestCarpet;
    }

     // Function to count the number of matches between adjacent squares in a carpet
     public static int countMatches(List<String> combination) {
        int matches = 0;
        for (int i = 0; i < combination.size() - 1; i++) {
            String currentStrip = combination.get(i);
            String nextStrip = combination.get(i + 1);
            for (int j = 0; j < currentStrip.length(); j++) {
                if (currentStrip.charAt(j) == nextStrip.charAt(j)) {
                    matches++;
                }
            }
        }
        return matches;
    }
    
    public static int calculateBalanceChange(List<String> carpet, String newStrip) {
        // Calculate the change in balance when adding the new strip to the carpet
        int matches = countMatches(carpet);
        int nonMatches = countNonMatches(carpet);
        int newMatches = countMatches(Arrays.asList(carpet.get(carpet.size() - 1), newStrip));
        int newNonMatches = countNonMatches(Arrays.asList(carpet.get(carpet.size() - 1), newStrip));
        return (matches + newMatches) - (nonMatches + newNonMatches);
    }

    public static String reverseCharList(String list) {
        char ch;
        String nstr="";
        for (int x = 0; x < list.length(); x++)
        {
            ch= list.charAt(x); //extracts each character
            nstr= ch+nstr; //adds each character in front of the existing string
        }
        return nstr;
    }

    public static int countNonMatches(List<String> combination) {
        int nonMatches = 0;
        for (int i = 0; i < combination.size() - 1; i++) {
            String currentStrip = combination.get(i);
            String nextStrip = combination.get(i + 1);
            for (int j = 0; j < currentStrip.length(); j++) {
                if (currentStrip.charAt(j) != nextStrip.charAt(j)) {
                    nonMatches++;
                }
            }
        }
        return nonMatches;
    }

    // maximum
    public static List<String> max(int carpetSize, List<String> stockList, Map<String, Map<String, Integer>> carpetMatches) {
        List<String> startingCarpet = startCarpet(stockList, carpetSize, carpetMatches);
        List<String> finalCarpet = new ArrayList<>(startingCarpet);
        // memoization map for pruning
        Map<String, Integer> memoizationMap = new HashMap<>();
        Map<String, Integer> stock = getStockCount(stockList);
        int twoPiecesMaxMatch = twoPieceMaxMatch(stockList, carpetMatches);
        int[] maxMatches = { totalMatches(startingCarpet, carpetMatches) };
       
        //dfs to find arrangement with maximum matches
        depthFirstSearch(carpetSize, stock, new ArrayList<>(), new HashMap<>(), 0, finalCarpet, maxMatches, twoPiecesMaxMatch, memoizationMap, carpetMatches);
        // check if flipping any pieces is better
        flipPieces(finalCarpet, carpetMatches);
        
        System.out.println("max matches\nCombination with the most matches:");
        for (String piece : finalCarpet) {
            System.out.println(piece);
        }
        System.out.println("Number of matches: " + maxMatches[0]);
    
        return finalCarpet;
    }

    private static void depthFirstSearch(int carpetSize, Map<String, Integer> stock, List<String> current, Map<String,Integer> usedCarpetPieces,int currentMatch, List<String> finalCarpet, int[] maximumMatches, int twoPiecesMaxMatch, Map<String, Integer> memoizationMap, Map<String, Map<String, Integer>> carpetMatches){
        // key for current carpet
        String memoKey = getMemMapKey(usedCarpetPieces);
        // if we've been here, go next
        if (memoizationMap.getOrDefault(memoKey, -1) >= currentMatch){
            return;
        } 

        // update map with key
        memoizationMap.put(memoKey, currentMatch);
        
        if (current.size() == carpetSize) {
            if (currentMatch > maximumMatches[0]) {
                finalCarpet.clear();
                finalCarpet.addAll(current);
                maximumMatches[0] = currentMatch;
            }
            return;
        }
        
        // prune tree if adding pieces won't improve matches
        if (currentMatch + (carpetSize - current.size()) * twoPiecesMaxMatch <= maximumMatches[0]){
            return;
        }
    
        List<String> availablePieces = new ArrayList<>(stock.keySet());
        availablePieces.sort((a, b) -> Integer.compare(carpetPieceMatches(b, availablePieces, carpetMatches), carpetPieceMatches(a, availablePieces, carpetMatches)));
        
        // explore all available pieces
        for (String piece : availablePieces) {
            // check if its in stock
            if (stock.get(piece) > usedCarpetPieces.getOrDefault(piece, 0)) {
                // get match total with last piece on the carpet
                int matches = current.isEmpty() ? 0 : carpetMatches.get(current.get(current.size() - 1)).get(piece);
                // add & update usage counter
                current.add(piece);
                usedCarpetPieces.put(piece, usedCarpetPieces.getOrDefault(piece, 0) + 1);
                // recursive call
                depthFirstSearch(carpetSize, stock, current, usedCarpetPieces, currentMatch + matches, finalCarpet, maximumMatches, twoPiecesMaxMatch,memoizationMap,carpetMatches);
                // backtrack -> remove last piece and update usage count
                current.remove(current.size() - 1);
                usedCarpetPieces.put(piece, usedCarpetPieces.get(piece) - 1);
            }
        }
       
    }

    // creates starting carpet with best starting piece -> baseline carpet 
    private static List<String> startCarpet(List<String> stock, int carpetSize, Map<String, Map<String, Integer>> carpetMatches) {
        // add best starting piece to carpet
        List<String> startingCarpet = new ArrayList<>();
        String startingPiece = startPiece(stock, carpetMatches);
        startingCarpet.add(startingPiece);
        // add best starting piece to used
        Map<String, Integer> usedCarpetPieces = new HashMap<>();
        usedCarpetPieces.put(startingPiece, 1);
        // make a baseline carpet that we can work from
        while (startingCarpet.size() < carpetSize) {
            String bestPiece = bestPiece(stock, startingCarpet, usedCarpetPieces, carpetMatches);
            startingCarpet.add(bestPiece);
            usedCarpetPieces.put(bestPiece, usedCarpetPieces.getOrDefault(bestPiece, 0) + 1);
        }
        return startingCarpet;
    }

    // finds best starting point for carpet
    private static String startPiece(List<String> stockList, Map<String, Map<String, Integer>> carpetMatches) {
        return stockList.parallelStream().max(Comparator.comparingInt(piece -> getPotentialMatches(piece, stockList, carpetMatches))).orElse(null);
    }
    
    // potential matches piece can get with all pieces in stock
    private static int getPotentialMatches(String carpetPiece, List<String> stockList, Map<String, Map<String, Integer>> carpetMatches) {
        int totalMatches = 0;
        for (String piece : stockList) {
            if (!carpetPiece.equals(piece)) {
                totalMatches += carpetMatches.get(carpetPiece).get(piece);
            }
        }
        return totalMatches;
    }

    // finds best piece of carpet using a greedy search
    private static String bestPiece(List<String> stockList, List<String> currentCarpet, Map<String, Integer> usedCarpetPieces, Map<String, Map<String, Integer>> carpetMatches) {
        String bestPiece = null;
        int bestMatches = Integer.MIN_VALUE;
        // iterates through stockList and finds best piece by finding how many carpets it can match with
        for(int x = 0; x < stockList.size(); x++){
            String piece = stockList.get(x);
            if(stock(piece, stockList)> usedCarpetPieces.getOrDefault(piece, 0)){
                int matchCount = carpetMatches(piece, currentCarpet, carpetMatches) + getPotentialMatches(piece, stockList, carpetMatches);
                    if (matchCount > bestMatches) {
                        bestPiece = piece;
                        bestMatches = matchCount;
                    }
            }
        }
        return bestPiece;
    }

     // gets max matches between two pieces
     private static int twoPieceMaxMatch(List<String> stockList, Map<String, Map<String, Integer>> carpetMatches) {
        int maxMatches = 0;
        for(int x = 0; x < stockList.size(); x++){
            for(int y = 0; y < stockList.size(); y++){
                if(x != y){
                    maxMatches = Math.max(maxMatches, carpetMatches.get(stockList.get(x)).get(stockList.get(y)));
                }
            }
        }
        return maxMatches;
    }

    // total matches in carpet
    private static int totalMatches(List<String> carpet, Map<String, Map<String, Integer>> carpetMatches) {
        int totalMatches = 0;
        for (int x = 1; x < carpet.size(); x++) {
            totalMatches += carpetMatches.get(carpet.get(x - 1)).get(carpet.get(x));
        }
        return totalMatches;
    }

    // calculates matches a piece can make with current carpet
    private static int carpetMatches(String carpetPiece, List<String> currentCarpet, Map<String, Map<String, Integer>> carpetMatches) {
        int totalMatches = 0;
        for (int x = 0; x < currentCarpet.size(); x++) {
            totalMatches += carpetMatches.get(currentCarpet.get(x)).get(carpetPiece);
        }
        return totalMatches;
    }

   
    // creates key for memoizationMap based on used pieces
    private static String getMemMapKey(Map<String, Integer> usedCarpetPieces) {
        StringBuilder sb = new StringBuilder();
        usedCarpetPieces.forEach((key, value) -> sb.append(key).append(" ").append(value));
        return sb.toString();
    }

    

    // counts number of matches between two pieces
    private static int twoPieceMatchCount(String piece1, String piece2) {
        int matches = 0;
        for (int x = 0; x < piece1.length(); x++) {
            if (piece1.charAt(x) == piece2.charAt(x)) {
                matches++;
            }
        }
        return matches;
    }

    // checks carpetMatches and returns the total matches the carpetPiece has
    private static int carpetPieceMatches(String carpetPiece, List<String> stockList, Map<String, Map<String, Integer>> carpetMatches) {
        int total = 0;
        for(int x = 0; x < stockList.size(); x++){
            String piece = stockList.get(x);
            if(!carpetPiece.equals(piece)){
                total += carpetMatches.get(carpetPiece).get(piece);
            }
        }
        return total;
    }

    // counts piece occurences in stockList
    private static int stock(String carpetPiece, List<String> stockList) {
        return Collections.frequency(stockList, carpetPiece);
    }
    
    // Map of each carpetPiece and its count in stockList
    private static Map<String, Integer> getStockCount(List<String> stockList) {
        Map<String, Integer> stockCount = new HashMap<>();
        for (String carpetPiece : stockList) {
            stockCount.put(carpetPiece, stockCount.getOrDefault(carpetPiece, 0) + 1);
        }
        return stockCount;
    }
    
    // flips carpetPieces to check if there's better matches
    private static void flipPieces(List<String> carpet, Map<String, Map<String, Integer>> carpetMatches) {
        for (int x = 1; x < carpet.size(); x++) {
            String carpetPiece = carpet.get(x);
            String flippedPiece = new StringBuilder(carpetPiece).reverse().toString();
            if (!carpetPiece.equals(flippedPiece)) {
                int originalMatch = twoPieceMatchCount(carpetPiece, carpet.get(x - 1));
                int flippedMatch = twoPieceMatchCount(flippedPiece, carpet.get(x - 1));
                if (flippedMatch >= originalMatch) {
                    carpet.set(x, flippedPiece);
                }
            }
        }
    }
}