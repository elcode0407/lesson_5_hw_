import com.sun.tools.javac.Main;


import java.util.Arrays;

public class MultiTheard {
    static final int size = 1_000_000;
    static final int h = size / 2;
    static float[] a1 = new float[h];
    static float[] a2 = new float[size];
    static float[] a3 = new float[h];
    static float[] arr = new float[size];
    static float[] arr2 = new float[size];
    static long a = System.currentTimeMillis();
    static final Object mon = new Object();

    public static void main(String[] args) {
        MultiTheard multiTheard = new MultiTheard();
        Thread t = new Thread(multiTheard::calculateArr);
        Thread b = new Thread(multiTheard::calculateArr2);
        t.start();
        b.start();
        try {
            t.join();
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(equalsArr(arr, arr2));
    }

    synchronized void calculateArr() {
        Arrays.fill(arr, 1);
        Thread thread = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long calculateArrTime = System.currentTimeMillis() - a;
        System.out.println("Time to fill array: " + calculateArrTime + " ms");

    }

    synchronized void calculateArr2() {
        Arrays.fill(arr2,1);
        System.arraycopy(arr2, 0, a1, 0, h);
        System.arraycopy(arr2, h, a3, 0, h);
        long separationArrTime = System.currentTimeMillis() - a;
        Thread thread = new Thread(() -> {
            for (int i = 0; i < h; i++) {
                a1[i] = (float) (a1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < size; i++) {
                a2[i] = (float) (a2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        });
        Thread thread3 = new Thread(() -> {
            int j = h;
            for (int i = 0; i < h; i++) {
                a3[i] = a2[j];
                j++;
            }
        });
        thread.start();
        thread2.start();
        thread3.start();

        try {
            thread.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long calculateArr2Time = System.currentTimeMillis() - a;
        System.arraycopy(a1, 0, arr2, 0, h);
        System.arraycopy(a3, 0, arr2, h, h);
        long gluingArrTime = System.currentTimeMillis() - a;
        System.out.println("Time to separation arrays: " + separationArrTime + " ms");
        System.out.println("Time to fill arrays: " + calculateArr2Time + " ms");
        System.out.println("Time to gluing arrays: " + gluingArrTime + " ms");
        System.out.println(Arrays.equals(arr,arr2));
    }

    static boolean equalsArr(float[] f, float[] b) {
        return Arrays.equals(f, b);
    }
}
