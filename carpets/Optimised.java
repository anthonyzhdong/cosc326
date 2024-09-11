import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Optimised {
    private static int maxMatches = 0;
    private static List<String> maxMatchCombination = new ArrayList<>();
    private static int minMatches = 0;
    private static List<String> minMatchCombination = new ArrayList<>();
    
   
    
// Function to generate all possible combinations of the given carpet pieces
public static void generateCombinations(List<String> stock, int size, List<String> currentCombination) {
    // Base case: if current combination size is equal to desired size
    if (currentCombination.size() == size) {
        int matches = countMatches(currentCombination);
        if (matches > maxMatches) {
            maxMatches = matches;
            maxMatchCombination = new ArrayList<>(currentCombination);  // Save this combination as the current best
        }
        return;
    }

    // Recursive case: try adding each piece of carpet to the current combination
    for (int i = 0; i < stock.size(); i++) {
        String piece = stock.get(i);
        currentCombination.add(piece);
        List<String> remainingStock = new ArrayList<>(stock);
        remainingStock.remove(i);

        int pos=0;
        for(int x = 0; x<remainingStock.size(); x++){
            if(remainingStock.get(x).equals(reverseCharList(piece))){
                pos=x;
            }
        }

        // Pruning: if it is impossible to beat the current best score with the remaining positions, skip this branch
        int currentMatches = countMatches(currentCombination);
        int maxPossibleMatches = currentMatches + (size - currentCombination.size()) * 2;

        // Look-ahead: consider the next few strips
        for (int j = 0; j < Math.min(3, remainingStock.size()); j++) {
            maxPossibleMatches += countMatches(Arrays.asList(piece, remainingStock.get(j)));
        }

        if (maxPossibleMatches > maxMatches) {
            generateCombinations(remainingStock, size, currentCombination);
        }

        currentCombination.remove(currentCombination.size() - 1);
    }
}



public static void mingenerateCombinations(List<String> stock, int size, List<String> currentCombination, Set<String> usedElements) {
    // Base case: if current combination size is equal to desired size
    if (currentCombination.size() == size) {
        int matches = countNonMatches(currentCombination);
        if (matches > minMatches) {
            minMatches = matches;
            // matches=minMatches;
            minMatchCombination = new ArrayList<>(currentCombination);  // Save this combination as the current best
        }
        return;
    }

    // Recursive case: try adding each piece of carpet to the current combination
    for (int i = 0; i < stock.size(); i++) {
        String piece = stock.get(i);
        
        // Check if the piece or its reversed counterpart has been used before
        if (!usedElements.contains(piece)) {
            currentCombination.add(piece);
            usedElements.add(piece);
            
            // If the piece is original, add its reversed counterpart to usedElements
            if (!piece.equals(reverseCharList(piece))) {
                usedElements.add(reverseCharList(piece));
            }
            
            List<String> remainingStock = stock.subList(i + 1, stock.size()); // Avoid unnecessary list copying

            // Pruning: if it is impossible to beat the current best score with the remaining positions, skip this branch
            int currentNonMatches = countNonMatches(currentCombination);
            int minPossibleNonMatches = currentNonMatches;

            // Calculate the actual minimum possible non-matches
            for (int j = 0; j < Math.min(5, remainingStock.size()); j++) { //3 is an arbitary number to look forward. Increase at your peril
                minPossibleNonMatches += countNonMatches(Arrays.asList(piece, remainingStock.get(j)));
            }

            if (minPossibleNonMatches > minMatches) {
                mingenerateCombinations(remainingStock, size, currentCombination, usedElements);
            }

            // Remove the piece and its reversed counterpart from usedElements
            usedElements.remove(piece);
            usedElements.remove(reverseCharList(piece));
            
            currentCombination.remove(currentCombination.size() - 1);
        }
    }
}


public static List<String> reverseCharList(List<String> list) {
    List<String> temp= new ArrayList<String>();
    for (String line : list) {
        char ch;
        String nstr="";
        for (int i=0; i<line.length(); i++)
        {
            ch= line.charAt(i); //extracts each character
            nstr= ch+nstr; //adds each character in front of the existing string
        }
        temp.add(nstr);
    }
    return temp;
}


//--------------

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
        for (int i=0; i<list.length(); i++)
        {
            ch= list.charAt(i); //extracts each character
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



    //---------------------

    private static List<String> readFromStdinToList() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
        return lines;
    }

    public static void main(String[] args) {
        // Hardcoded input for now
//         List<String> stock = new ArrayList<>(Arrays.asList(
//     "RRR", "BBR", "BGG", "GGG", "BBB",
//     "RBR", "RGB", "BRB", "GRG", "RGG",
//     "BRG", "BBG", "RGB", "RGG", "BBR",
//     "GRR", "RRB", "BGR", "RGR", "GRB"
// ));

List<String> stock = readFromStdinToList();


        // Sort the stock in descending order of the number of matches
        stock.sort((s1, s2) -> countMatches(Arrays.asList(s1, s2)) - countMatches(Arrays.asList(s2, s1)));

        int size = 19; // Desired size of the carpet
        for (String line : reverseCharList(stock)) {
            stock.add(line);
        }


    if (args.length < 1) {
        size=stock.size();
        System.out.println("no matches");
        Set<String> usedElements = new HashSet<>();
        mingenerateCombinations(stock, size, new ArrayList<>(),usedElements);

        // Print the combination with the least matches
        if (!minMatchCombination.isEmpty()&& ((size-1)*3)-minMatches==0) {
            System.out.println("Combination with the least matches:");
            for (String strip : minMatchCombination) {
                System.out.println(strip);
            }
            System.out.println("Number of matches: " + minMatches);
        } else {
            System.out.println("No combinations found.");
        }
        return;
    }

    String option = args[0];

    switch (option) {
        case "-n":
            System.out.println("no matches");
            size=Integer.valueOf(args[1]);
            Set<String> usedElements = new HashSet<>();
            mingenerateCombinations(stock, size, new ArrayList<>(),usedElements);

            // Print the combination with the least matches
            // && ((size-1)*stock.get(0).length())-minMatches==0
            if (!minMatchCombination.isEmpty()) {
                System.out.println("Combination with the least matches:");
                for (String strip : minMatchCombination) {
                    System.out.println(strip);
                }
                System.out.println("Number of matches: " + minMatches);
            } else {
                System.out.println("No combinations found.");
            }
            break;
        case "-m":
            System.out.println("max matches");
            size=Integer.valueOf(args[1]);
            generateCombinations(stock, size, new ArrayList<>());

        if (!maxMatchCombination.isEmpty()) {
            System.out.println("Combination with the most matches:");
            for (String strip : maxMatchCombination) {
                System.out.println(strip);
            }
            System.out.println("Number of matches: " + maxMatches);
        } else {
            System.out.println("No combinations found.");
        }
            break;
        case "-b":
            size=Integer.valueOf(args[1]);
                int iterations = 1000; // Number of iterations

            List<String> bestCarpet = findBalancedCarpet(stock, size, iterations);
            if (bestCarpet != null) {
                // Print the best-balanced carpet found
                System.out.println("Best Balanced Carpet:");
                for (String strip : bestCarpet) {
                    System.out.println(strip);
                }
                int matches = countMatches(bestCarpet);
                int nonMatches = countNonMatches(bestCarpet);
                int balance = Math.abs(matches - nonMatches);
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("No balanced carpet found.");
            }
            break;
        default:
        size=(stock.size())/2;
        System.out.println("no matches");
        usedElements = new HashSet<>();
        mingenerateCombinations(stock, size, new ArrayList<>(),usedElements);

        // Print the combination with the least matches
        if (!minMatchCombination.isEmpty()&& ((size-1)*3)-minMatches==0) {
            System.out.println("Combination with the least matches:");
            for (String strip : minMatchCombination) {
                System.out.println(strip);
            }
            System.out.println("Number of matches: " + minMatches);
        } else {
            System.out.println("No combinations found.");
        }
        return;
    }

    }
    }

