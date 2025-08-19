package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int[] buckets = new int[M];
        for (Oomage oomage : oomages) {
            int bucketNum = (oomage.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNum]++;
        }
        int N = oomages.size();
        for (int size : buckets) {
            if (size < N / 50 || size > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
