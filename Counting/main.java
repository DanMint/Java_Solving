import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;


public class trial {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int numToIter = input.nextInt();
        int jumps = input.nextInt();

        System.out.print(toFindWhatToTakeOut(numToIter, jumps));
    }

    public static int toFindWhatToTakeOut(int a, int b) {
        if (a > 1) {
            int curr = toFindWhatToTakeOut(a-1, b);
            curr = (curr + (b - 1)) % a + 1;
            return curr;
        }
        else
            return 1;
    }
}
