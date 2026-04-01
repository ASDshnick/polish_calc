package ru.yarsu;

import java.util.*;

public class Calculator {

    private final Map<Character, Integer> priority = new HashMap<>();
    private final Set<Character> operators = new HashSet<>();

    public Calculator() {
        // Уровень 1
        priority.put('+', 1);
        priority.put('-', 1);
        // Уровень 2
        priority.put('*', 2);
        priority.put('/', 2);
        priority.put('%', 2);
        // Уровень 3 (Степень)
        priority.put('^', 3);

        operators.addAll(priority.keySet());
    }

    public String transformToRPN(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty string");
        }

        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder();
        String cleanS = s.replaceAll("\\s+", "");

        for (int i = 0; i < cleanS.length(); i++) {
            char ch = cleanS.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                result.append(ch);
                // Добавляем пробел, если следующее не цифра и не точка
                if (i + 1 >= cleanS.length() || !Character.isDigit(cleanS.charAt(i + 1)) && cleanS.charAt(i + 1) != '.') {
                    result.append(' ');
                }
            } else if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(stack.pop()).append(' ');
                }
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Wrong number of brackets: too many ')'");
                }
                stack.pop();
            } else if (operators.contains(ch)) {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    char topOp = stack.peek();
                    if (operators.contains(topOp)) {
                        int currentPriority = priority.get(ch);
                        int topPriority = priority.get(topOp);

                        boolean shouldPop;
                        if (ch == '^') {
                            // Правоассоциативный: выгружаем только если строго больше
                            shouldPop = (topPriority > currentPriority);
                        } else {
                            // Левоассоциативный: выгружаем если больше или равен
                            shouldPop = (topPriority >= currentPriority);
                        }

                        if (shouldPop) {
                            result.append(stack.pop()).append(' ');
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                stack.push(ch);
            } else {
                throw new IllegalArgumentException("Illegal symbol: " + ch);
            }
        }

        while (!stack.isEmpty()) {
            char op = stack.pop();
            if (op == '(' || op == ')') {
                throw new IllegalArgumentException("Wrong number of brackets");
            }
            result.append(op).append(' ');
        }

        return result.toString().trim();
    }

    public double calculateRPN(String rpn) {
        if (rpn == null || rpn.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty RPN");
        }

        Stack<Double> stack = new Stack<>();
        String[] tokens = rpn.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }

            try {
                double value = Double.parseDouble(token);
                stack.push(value);
            } catch (NumberFormatException e) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Illegal string: not enough operands for " + token);
                }

                double b = stack.pop();
                double a = stack.pop();
                double res;
                char op = token.charAt(0);

                switch (op) {
                    case '+':
                        res = a + b;
                        break;

                    case '-':
                        res = a - b;
                        break;

                    case '*':
                        res = a * b;
                        break;

                    case '/':
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        res = a / b;
                        break;

                    case '%':
                        if (b == 0) {
                            throw new ArithmeticException("Remainder of the division by zero");
                        }
                        res = a % b;
                        break;

                    case '^':
                        res = Math.pow(a, b);
                        break;

                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }
                stack.push(res);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Wrong string: too many operands");
        }

        return stack.pop();
    }

    // Удобный метод для полного цикла
    public double evaluate(String expression) {
        String rpn = transformToRPN(expression);
        return calculateRPN(rpn);
    }
}