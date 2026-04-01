package ru.yarsu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calc = new Calculator();

        System.out.println("Input: +, -, *, /, %, ^, '()'. Input 'exit' to leave.");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input)) break;
            if (input.trim().isEmpty()) continue;

            try {
                String rpn = calc.transformToRPN(input);
                System.out.println("RPN: " + rpn);

                double result = calc.calculateRPN(rpn);
                if (result == (long) result) {
                    System.out.println("Result: " + (long) result);
                } else {
                    System.out.println("Result: " + result);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}