import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Map;
import static java.lang.Math.abs;
 
 
public class trial {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int amountOfSides = input.nextInt();
        int[] x = new int[amountOfSides];
        int[] y = new int[amountOfSides];
 
        for(int i = 0; i < amountOfSides; i ++) {
            x[i] = input.nextInt();
            y[i] = input.nextInt();
        }
 
        double ans = 0;
        for(int i = 0; i < amountOfSides; i ++) {
            int el = i + 1;
            if(el == amountOfSides)
                el = 0;
            ans += (x[i] * y[el]);
            ans -= (y[i] * x[el]);
        }
 
        ans /= 2.0;
 
        System.out.print(abs(ans));
    }
}
