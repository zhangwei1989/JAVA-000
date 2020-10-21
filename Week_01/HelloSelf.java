public class HelloSelf {
    public static void main(String[] args) {
        /** 基础类型 */
        byte a = 1;
        short b = 2;
        char c = 'a';
        int d = 3;
        long e = 4l;
        boolean f = false;
        boolean j = true;
        float g = 1.2f;
        double h = 2.4d;
        
        /** 四则运算 */
        a = (byte) (a + 5);
        b = (short) (b - 1);
        d = d * 6;
        h = h / g;
        d = d % 2;
        
        /** if */
        if (d == 10) {
            System.out.println("bingo");
        }
        
        /** for */
        for (int i=1;i<=10;i++) {
            i++;
        }
    }
}