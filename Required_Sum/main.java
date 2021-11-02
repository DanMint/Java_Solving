import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;


public class trial {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int firstArrSize = input.nextInt();
        int[] firstArr = new int[firstArrSize];

        for(int i = 0; i < firstArrSize; i ++) {
            firstArr[i] = input.nextInt();
        }

        int secondArrSize = input.nextInt();
        int[] secondArr = new int[secondArrSize];

        for(int i = 0; i < secondArrSize; i ++) {
            secondArr[i] = input.nextInt();
        }

        int target = input.nextInt();

        System.out.print(numOfMaxes(firstArr, secondArr, target));
    }

    public static int numOfMaxes(int[] a, int[] b, int target) {
        /// first is the val equivilance and second is the amount of this value
        Map<Integer,Integer> forArrB = new HashMap<>();

        for(Integer el : b) {
            if(forArrB.containsKey(el))
                forArrB.replace(el, forArrB.get(el) + 1);
            else
                forArrB.put(el,1);
        }

        int amountOfTargetsHit = 0;

        for(Integer el : a)
            if (forArrB.containsKey(target - el))
                amountOfTargetsHit = amountOfTargetsHit + forArrB.get(target - el);


        return amountOfTargetsHit;
    }

}
