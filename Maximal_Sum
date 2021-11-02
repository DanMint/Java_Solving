import java.util.Scanner;
import java.util.ArrayList;


public class trial {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int arrLen = input.nextInt();

        int[] a = new int[arrLen];
        int[] b = new int[arrLen];

        for(int i = 0; i < a.length;  i++) {
            int newUserInp = input.nextInt();
            a[i] = newUserInp;
        }

        for(int i = 0; i < b.length;  i++) {
            int newUserInp = input.nextInt();
            b[i] = newUserInp;
        }

        int[] ans = findMaxPos(a,b);
        System.out.print(ans[1] + " ");
        System.out.print(ans[2]);
    }

    public static int[] findMaxPos(int[] a,int[] b) {
        int max = -2147483648;

        int[] c = new int[a.length];
        int[] d = new int[a.length];

        int maxOfC = -2147483648;;
        int lastIndex = -1;

        for(int i = b.length - 1; i >= 0; i --) {
            if(b[i] >= maxOfC) {
                maxOfC = b[i];
                lastIndex = i;
            }
            c[i] = maxOfC;
            d[i] = lastIndex;
        }

        /// 0 = max, 1 = index of a, 2 = index of b
        int[] curMax = new int[3];
        curMax[0] = -2147483648;

        for(int i = 0; i < a.length; i ++) {
            int tempSum = a[i] + c[i];
            if (tempSum > curMax[0]) {
                curMax[0] = tempSum;
                curMax[1] = i;
                curMax[2] = d[i];
            }
        }

        return curMax;
    }
}
