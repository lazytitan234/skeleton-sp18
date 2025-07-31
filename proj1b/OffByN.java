public class OffByN implements CharacterComparator{

    private int offset;

    public OffByN(int N) {
        this.offset = N;
    }

    @Override
    public boolean equalChars(char a, char b) {
        if ((a - b) == this.offset || (a - b) == -this.offset) {
            return true;
        }
        return false;
    }
}