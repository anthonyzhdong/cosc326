
import java.util.*;

//donan928 2169260
public class parsingPartitions{

    public static void main(String[]args){
        
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> lines = new ArrayList<String>();
        while(scanner.hasNextLine()){
            lines.add(scanner.nextLine());
        }
        parsingPartitionsMethod(lines);
        scanner.close();

    }

    public static void parsingPartitionsMethod(ArrayList<String> arr){
        // Arraylist of completed lines
        ArrayList<String> valid = new ArrayList<String>();

        boolean containsValid = false; // flag to see if block is valid
        boolean previousBlank = true; // flag to see if previous line/s were blank spaces
        boolean start = true; // flag to see if it's the start of the block
        int elementsInBlock = 0; // counter to see if there's stuff that isnt a blank space in the block
        int startIndex = 0; // start index to keep track of where "INVALID SCENARIO" needs to go if the block is invalid
        boolean validSep = false;

        for(int x = 0; x<arr.size();x++){
            
            // checks if comment is valid
            if(!arr.get(x).isEmpty()){

                // checks if first line starts with '#'
                char first = arr.get(x).charAt(0);
                if(first == '#'){
                    elementsInBlock++;
                    previousBlank = false;
                    start = false;
                    valid.add(arr.get(x));
                    continue;
                }
            }
            
            // replaces all multi spaces to just one space e.g. "   " -> " "
            String spaceLine = arr.get(x).replaceAll("\\s+"," ");
            // turns all "(space-coma-space)" into standard " , " format -> ready to be split 
            String comaLine = spaceLine.replaceAll("\\s*,\\s*", " , ");
            // replaces all ("---- to x amount") to "---"
            String dashLine = comaLine.replaceAll("-{3,}", "---");

            if(dashLine.equals("---")){
                validSep = true;
            }

            // splits line into individual elements e.g "1 , 2 , 3" -> [1|,|2|,|3]
            String[] elements = dashLine.trim().split(" ");

            // if previous line isn't blank then add whitespace
            if(elements[0].isEmpty() && previousBlank == false && start == false){
                valid.add(elements[0]);
                previousBlank = true;

            }else if(isInteger(elements[0])){ // if its more than just a single digit

                // checks if first digit starts with 0
                char firstDigit = elements[0].charAt(0);
                if(firstDigit == '0'){
                    elementsInBlock++;
                    previousBlank = false;
                    start = false;
                    valid.add("# INVALID: " + arr.get(x));           
                    continue;
                }
                if(elements.length == 1){
                    elementsInBlock++;
                    previousBlank = false;
                    start = false;
                    containsValid = true;
                    valid.add(elements[0]);
                    continue;

                }
                
                boolean incorrect = false; // flag to see if sequence is incorrect
                // checks if there's a coma. If so, you want to iterate through every second element and check if it's a coma
                if(elements[1].equals(",")){
                    
                    // checks if last index is a coma
                    String lastIndex = "";
                    if(elements.length>1){
                            lastIndex = elements[elements.length-1];
                    }

                    for(int count = 1; count<elements.length;count++){
                        
                        // checks if number starts with 0 and is placed at even intervals e.g. index 0,2,4
                        if(count%2 == 0){
                            if(isInteger(elements[count])==false){
                                elementsInBlock++;
                                previousBlank = false;
                                start = false;
                                incorrect = true;
                                valid.add("# INVALID: " + arr.get(x));
                                break;
                            }else if(elements[count].charAt(0) == '0'){
                                elementsInBlock++;
                                previousBlank = false;
                                start = false;
                                incorrect = true;
                                valid.add("# INVALID: " + arr.get(x));
                                break;
                            }

                        }

                        // checks if there's a coma at the appropriate intervals e.g. 1,3,5 and if last index is a coma
                        if(count%2 != 0){
                            if(elements[count].equals(",") == false || lastIndex.equals(",") == true){
                                valid.add("# INVALID: " + arr.get(x));
                                elementsInBlock++;
                                previousBlank = false;
                                incorrect = true;
                                start = false;
                                break;
                            }
                        }
                    }
                    
                    //if it doesn't break then it's correct
                    if(incorrect == false){
                        valid.add(reverse(elements));
                        elementsInBlock++;
                        containsValid = true;
                        previousBlank = false;
                        start = false;
                    }
                }else{ // this case is for non coma sequences like 1 2 3
                    
                    for(int count = 0; count<elements.length;count++){

                        char firstIndex = elements[count].charAt(0);
                        
                        // checks to see if all elements are integers and that numbers don't start with 0
                        if(isInteger(elements[count]) == false || firstIndex == '0'){
                            valid.add("# INVALID: " + arr.get(x));
                            elementsInBlock++;
                            previousBlank = false;
                            incorrect = true;
                            start = false;
                            break;
                        }
                    }

                    // if it doesn't break then it's correct
                    if(incorrect == false){
                        valid.add(reverse(elements));
                        containsValid = true;
                        previousBlank = false;
                        elementsInBlock++;
                        start = false;
                    }  
                }  
            }else if(validSep){
                //elements[0].equals("---")){
                // checking if block is valid, if it isn't then add "# INVALID SCENARIO" to the startIndex (start of block) and set start index to new block
                if(containsValid == false && elementsInBlock>=1){ 
                    valid.add(startIndex, "# INVALID SCENARIO");
                    startIndex = valid.size()+1;
                }
                
                // if block is valid then add "--------"
                if(elementsInBlock>=1){ 
                    valid.add("--------");
                    startIndex = valid.size();
                    containsValid = false;
                    elementsInBlock = 0;
                    previousBlank = false;
                    start = true;
                }
                validSep = false;
            
            }else if(!elements[0].equals(" ") && !elements[0].isEmpty()){ //if line is not empty but is simply invalid e.g "  ! Something bad"
                
                valid.add("# INVALID: " + arr.get(x));
                elementsInBlock++;
                previousBlank = false;
                start = false;
                // && previousBlank == true
                // if(elements.length == 1 && containsValid == false && elementsInBlock == 1 && previousBlank == true){
                //     valid.add(startIndex, "# INVALID SCENARIO");
                // }
            }    

        }
        if(containsValid == false && !valid.get(valid.size()-1).equals("--------") ){
            valid.add(startIndex, "# INVALID SCENARIO");
        }
        // if last line is dash then remove it
        if(valid.size() > 1 && valid.get(valid.size()-1).equals("--------")){
            valid.remove(valid.size()-1);
        }
        // prints out array 
        for(int c = 0; c< valid.size();c++){
            System.out.println(valid.get(c));
        }
    }

    //checks if number is integer
    public static boolean isInteger(String str) {
        try {
            if(Integer.parseInt(str)>=1){
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //does the reversing of numbers 
    public static String reverse(String arr[]){
        //checks if number is integer then puts in array
        ArrayList<Integer> nums = new ArrayList<>();
        for(int x = 0; x<arr.length;x++){
            if(isInteger(arr[x])){
                nums.add(Integer.parseInt(arr[x]));
            }
        }
        //sorts it in order
        Collections.sort(nums);

        //adds to stringbuilder in reverse order
        StringBuilder result = new StringBuilder();
        for(int y = nums.size()-1; y>=0; y--){
            result.append(nums.get(y) + " ");
        }
        
        return result.toString();
    }
}
    
