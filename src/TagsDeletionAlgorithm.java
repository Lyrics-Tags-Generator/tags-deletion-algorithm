import java.util.*;

/**
 * TagsDeletionAlgorithmJava
 * Java implementation for our tags deletion algorithm
 *
 * @author Nicholas Njoki
 * @version 1.0
 */

public class TagsDeletionAlgorithm {
    /**
     * Main entry point for the 'TagsDeletionAlgorithmJava' class.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Paste your tags: ");
        String tags = scanner.nextLine();

        // Checks if ',' is included in the tags prompt string
        if (!tags.contains(",")) {
            System.out.println("Tags must be separated by a ','.");
            scanner.close();
            return;
        }

        String[] tagsArray = tags.split(",");

        // Convert the tags array into a list and trim each element
        List<String> tagsList = new ArrayList<>(Arrays.stream(tagsArray).map(String::trim).toList());
        int tagsLength = countTagsLength(tags);

        // Shuffle the tags
        Collections.shuffle(tagsList);

        StringBuilder tagsToBeRemoved = new StringBuilder();

        if (tagsLength <= 500) {
            System.out.println("Provided tags do not exceed the 500 character limit.");
            System.out.println("Length: " + countTagsLength(joinTagsList(tagsList)));
            scanner.close();
            return;
        }

        Set<String> tagsSet = new HashSet<>(tagsList);

        /*
          Removes random element from the list, in the live production version
          we rearrange the tags from the least effective to most effective
         */
        for (String tag : new ArrayList<>(tagsList)) {
            if (tagsSet.contains(tag)) {
                int tagIndex = tagsList.indexOf(tag);

                if (tagIndex > -1) {
                    String removedTag = tagsList.get(tagIndex);
                    tagsList.remove(tagIndex);

                    // From the tag from the set
                    tagsSet.remove(removedTag);

                    // Update the tags count
                    tagsLength = countTagsLength(joinTagsList(tagsList));
                    tagsToBeRemoved.append(removedTag).append(",");
                }
            }

            // Break out of the loop once the length is below or exactly 500 characters.
            if (tagsLength <= 500) {
                break;
            }
        }

        System.out.println("Removed: " + tagsToBeRemoved.substring(0, tagsToBeRemoved.length() - 1));
        System.out.println("Length: " + joinTagsList(tagsList).length());

        scanner.close();
    }

    /**
     * Calculates the total length of a comma-separated string of tags.
     * Each tag is trimmed of whitespace, spaces within a tag count as 2 characters,
     * and additional characters are added for commas between multiple tags.
     *
     * @param tags a comma-separated string of tags
     * @return the calculated total length of all valid tags
     */
    public static int countTagsLength(String tags) {
        if (tags.isEmpty()) {
            return 0;
        }

        String[] tagsArray = tags.split(",");

        int validTagCount = 0;
        int totalLength = 0;

        for (String tag : tagsArray) {
            String trimmedTag = tag.trim();

            // Skip over the empty tags
            if (trimmedTag.isEmpty()) {
                continue;
            }

            int tagLength = trimmedTag.length();

            // " " is worth 2 characters (Ask YouTube why)
            if (trimmedTag.contains(" ")) {
                tagLength += 2;
            }

            totalLength += tagLength;
            validTagCount++;
        }

        if (validTagCount > 1) {
            totalLength += validTagCount - 1;
        }

        return totalLength;
    }

    /**
     * Joins a list of tags into a single comma-separated string.
     *
     * @param tags the list of tags to join
     * @return a string containing all tags separated by commas
     */
    public static String joinTagsList(List<String> tags) {
        return String.join(",", tags);
    }
}