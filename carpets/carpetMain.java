import java.util.*;
import java.io.*;


public class carpetMain{

    public static void main(String[] args){

        String mode = args[0];
        int carpetSize = Integer.parseInt(args[1]);
        List<String> stockList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

            String line;
            while((line = reader.readLine()) != null){
                stockList.add(line);
            }

        }catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }

        switch(mode){
            case "-n":
                carpetMethods.noMatches(carpetSize, stockList);
                break;
            case "-b":
                //Collections.sort(stockList);
                stockList.sort((s1, s2) -> countMatches(Arrays.asList(s1, s2)) - countMatches(Arrays.asList(s2, s1)));
                for (String line : reverseCharList(stockList)) {
                    stockList.add(line);
                }
                //carpetMethods.balanced(carpetSize, stockList,1000);
                carpetMethods.findBalancedCarpet(stockList, carpetSize, 1000);
                break;
            case "-m":
                carpetMethods.max(carpetSize, stockList, carpetMatches(stockList));
                break;
            default:
                System.out.println("Invalid q");
                return;
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

    // generates matches between carpet pieces
    public static Map<String, Map<String, Integer>> carpetMatches(List<String> stockList){
        Map<String, Map<String, Integer>> matches = new HashMap<>();
        // loop through each piece in stockList
        for(String carpetPiece: stockList){
            // gets flipped version of carpetPiece
            String flippedVersion = new StringBuilder(carpetPiece).reverse().toString();
            // create hashmap to store pieces that match
            Map<String, Integer> piecesThatMatch = new HashMap<>();
            // loop through stockList again
            for(String carpetPiece2: stockList){
                // flipped version of carpetPiece2
                String flippedVersion2 = new StringBuilder(carpetPiece2).reverse().toString();

                // gets total matches between original version and other pieces
                int carpetMatch = matchesBetweenPieces(carpetPiece, carpetPiece2);
                int carpetPieceAndV2Flipped = matchesBetweenPieces(carpetPiece, flippedVersion2);
                piecesThatMatch.put(carpetPiece2, Math.max(carpetMatch, carpetPieceAndV2Flipped));

                // gets total matches between flipped and other pieces
                int flippedMatch = matchesBetweenPieces(flippedVersion, carpetPiece2);
                int flippedPieceAndV2Flipped = matchesBetweenPieces(flippedVersion, flippedVersion2);
                piecesThatMatch.put(flippedVersion2, Math.max(flippedMatch, flippedPieceAndV2Flipped));
            }
            // stores matches for each carpet into hashmap
            matches.put(carpetPiece, piecesThatMatch);
            matches.put(flippedVersion, piecesThatMatch);
        }
        return matches;
    }
   
    // method to calculate which carpetPiece1 and carpetPiece2 match
    public static int matchesBetweenPieces(String carpetPiece1, String carpetPiece2){
        int matches = 0;
        for(int x = 0; x < carpetPiece1.length(); x++){
            if(carpetPiece1.charAt(x) == carpetPiece2.charAt(x)){
                matches++;
            }
        }
        return matches;
    }



}