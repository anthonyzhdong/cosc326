import java.util.ArrayList;
import java.util.List;

public class CarpetState {
    private List<List<Character>> strips; // list of carpet strips
    private int totalMatchCount; // cumulative count of matching characters

    public CarpetState() {
        this.strips = new ArrayList<>();
        this.totalMatchCount = 0; // initialize with zero
    }

    public void removeLastStrip() {
        if (!this.strips.isEmpty()) {
            // If there's more than one strip, update the total match count
            if (this.strips.size() > 1) {
                List<Character> lastStrip = this.strips.get(this.strips.size() - 1);
                List<Character> secondLastStrip = this.strips.get(this.strips.size() - 2);
                int matchCount = countMatchingCharacters(lastStrip, secondLastStrip); // count matches
                this.totalMatchCount -= matchCount; // update total match count
            }

            // Remove the last strip from the list
            this.strips.remove(this.strips.size() - 1);
        }
    }

    public void addStrip(List<Character> newStrip) {
        // if there's at least one strip, compare the new strip with the last one
        if (!this.strips.isEmpty()) {
            List<Character> lastStrip = this.strips.get(this.strips.size() - 1);
            int matchCount = countMatchingCharacters(lastStrip, newStrip); // count matches
            this.totalMatchCount += matchCount; // update total match count
        }

        // add the new strip to the list
        this.strips.add(newStrip);
    }

    public List<List<Character>> getStrips() {
        return this.strips;
    }

    public int getTotalMatchCount() {
        return this.totalMatchCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CarpetState:\n");

        sb.append("Strips:\n");
        for (List<Character> strip : this.strips) {
            for (Character c : strip) {
                sb.append(c).append(" "); // print each character in the strip
            }
            sb.append("\n"); // newline after each strip
        }

        sb.append("Total Match Count: ").append(this.totalMatchCount).append("\n");

        return sb.toString();
    }

    // Helper method to count matching characters between two lists
    private static int countMatchingCharacters(List<Character> list1, List<Character> list2) {
        int count = 0;
        int minLength = Math.min(list1.size(), list2.size());
        for (int i = 0; i < minLength; i++) {
            if (list1.get(i).equals(list2.get(i))) {
                count++;
            }
        }
        return count;
    }
}
