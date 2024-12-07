import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class EquationProcessor {

    public static void main(String[] args) {
    	
        String filePath = "example.txt";

        if (validateXMLFile(filePath)) {
            System.out.println("The file is valid.");
            processSection1(filePath);
        } else {
            System.out.println("The file is not valid.");
        }
    }

    public static boolean validateXMLFile(String filePath) {
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

    public static boolean processSection1(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            boolean isInInfixSection = false;
            boolean isInPostfixSection = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("<section>")) {
                    continue;
                }

                if (line.equals("</section>")) {
                    isInInfixSection = false;
                    isInPostfixSection = false;
                    continue;
                }

                if (line.equals("<infix>")) {
                    isInInfixSection = true;
                    continue;
                }

                if (line.equals("</infix>")) {
                    isInInfixSection = false;
                    continue;
                }

                if (line.equals("<postfix>")) {
                    isInPostfixSection = true;
                    continue;
                }

                if (line.equals("</postfix>")) {
                    isInPostfixSection = false;
                    continue;
                }

                if (isInInfixSection || isInPostfixSection) {
                    processEquation(line, isInInfixSection);
                }
            }

            return true; // Assume success for now
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void processEquation(String equation, boolean isInInfix) {
        System.out.println("Processing " + (isInInfix ? "Infix" : "Postfix") + " Equation: " + equation);

        try {
            String result = isInInfix ? infixToPostfix(equation) : postfixToPrefix(equation);
            System.out.println((isInInfix ? "Postfix" : "Prefix") + ": " + result);
            evaluateExpression(result, isInInfix);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (Character.isDigit(c)) {
                // Keep appending digits to form a multi-digit number
                StringBuilder number = new StringBuilder();
                while (i < infix.length() && (Character.isDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    number.append(infix.charAt(i));
                    i++;
                }
                postfix.append(number.toString()).append(" ");
                i--;  // Adjust the index to reprocess the current character
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.pop(); // Pop the '('
            } else if (isOperator(c)) {
                while (!stack.isEmpty() && getPrecedence(stack.peek()) >= getPrecedence(c)) {
                    if (c != '^' && c != '/' && stack.peek() == '/') {
                        break;
                    }
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }

    private static String postfixToPrefix(String postfix) {
        Stack<String> stack = new Stack<>();

        for (String token : postfix.split("\\s+")) {
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && Character.isDigit(token.charAt(1)))) {
                stack.push(token);
            } else if (isOperator(token.charAt(0))) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid postfix expression - insufficient operands");
                }

                String operand2 = stack.pop();
                String operand1 = stack.pop();
                String result = token + " " + operand1 + " " + operand2;
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression - more than one result");
        }

        return stack.pop();
    }

    private static double evaluatePrefixExpression(String prefix) {
        Stack<Double> stack = new Stack<>();

        // Split the prefix expression into tokens
        String[] tokens = prefix.split("\\s+");

        // Process the tokens in reverse order
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && Character.isDigit(token.charAt(1)))) {
                // If the token is a number, push it onto the stack
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                // If the token is an operator, pop two operands from the stack, perform the operation, and push the result
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid prefix expression - insufficient operands");
                }
                double operand1 = stack.pop();
                double operand2 = stack.pop();
                double result = performOperation(operand1, operand2, token.charAt(0));
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid prefix expression - more than one result");
        }

        // The final result is in the stack
        return stack.pop();
    }


    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '^';
    }

    private static int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0; // for operands or parentheses
        }
    }

    private static void evaluateExpression(String expression, boolean isInInfix) {
        try {
            if (isInInfix) {
                System.out.println("Result: " + evaluatePostfixExpression(expression));
            } else {
                System.out.println("Result: " + evaluatePrefixExpression(expression));
            }
            printCustomMessage();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static double evaluatePostfixExpression(String postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix.split("\\s+")) {
            if (Character.isDigit(token.charAt(0)) || (token.length() > 1 && Character.isDigit(token.charAt(1)))) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result = performOperation(operand1, operand2, token.charAt(0));
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid postfix expression");
        }

        return stack.pop();
    }

    private static double performOperation(double operand1, double operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new IllegalArgumentException("Division by zero");
                }
                return operand1 / operand2;
            case '^':
                return Math.pow(operand1, operand2);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    public static void printCustomMessage() {
        String message = "-------------------------------------";
        System.out.println(message);
    }
    private static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}


