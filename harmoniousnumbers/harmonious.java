package etude12;
import java.util.*;

//donan928 2169260
public class harmonious{
    public static void main(String []args){

        ArrayList<Integer> duplicateNumbers = new ArrayList<>();
        int max = 2000000;

        for(int x = 1; x < max; x++){
            int sum1 = sumOfProperDivisors(x);
            int sum2 = sumOfProperDivisors(sum1);
            if(x == sum2 && x != sum1){
                if(!duplicateNumbers.contains(x)){
                    System.out.println(x + " " + sum1);
                    duplicateNumbers.add(sum1);
                }
            }
        }
        
    }

    public static int sumOfProperDivisors(int num){
        int sum = 0;
        List<Integer> divisibleNumbers = new ArrayList<>();
        for(int x = 2; x <= Math.sqrt(num);x++){
            if(num % x == 0){
                divisibleNumbers.add(x);
                if(x != num / x){
                    divisibleNumbers.add(num/x);
                }
            }
        }
        for(int number : divisibleNumbers){
            sum += number;
        }

        return sum;
    }


}