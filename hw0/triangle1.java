public class triangle1 {

    public static void drawTriangle(int N) {
        for (int i = 1; i < N; i += 1) {
            for (int j = 0; j < i; j += 1) {
                System.out.print("*");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        drawTriangle(10);
    }
}