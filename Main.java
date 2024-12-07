import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        String filePath = "example.txt";

        if (validateXMLFile(filePath)) {
            System.out.println("The file is valid.");
        } else {
            System.out.println("The file is not valid.");
        }
    }

    private static boolean validateXMLFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Stack<String> stack = new Stack<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("<equation>")) {
                    // Check for opening and closing tags for equations
                    if (line.endsWith("</equation>")) {
                        continue;
                    } else {
                        // Multi-line equation case
                        stack.push("equation");
                    }
                }

                if (line.startsWith("<")) {
                    String tagName = getTagName(line);

                    if (tagName.startsWith("/")) {
                        // Closing tag
                        if (stack.isEmpty() || !tagName.substring(1).equals(stack.pop())) {
                            return false;
                        }
                    } else {
                        // Opening tag
                        stack.push(tagName);
                    }
                }
            }

            // If the stack is empty, all tags were properly closed
            return stack.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String getTagName(String line) {
        int start = line.indexOf('<') + 1;
        int end = line.indexOf('>', start);
        return line.substring(start, end);
    }
    
}