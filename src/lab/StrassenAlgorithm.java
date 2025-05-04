package lab;

import java.io.*;
import java.util.*;

public class StrassenAlgorithm {

    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter input file path: ");
        String inputFilePath = console.nextLine();
        File inputFile = new File(inputFilePath);

        String baseName = inputFile.getName();
        int dotIndex = baseName.lastIndexOf(".");
        if (dotIndex > 0) {
            baseName = baseName.substring(0, dotIndex);
        }
        String outputFilePath = baseName + "_output.txt";

        Scanner scanner = new Scanner(inputFile);
        PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath));

        while (scanner.hasNextInt()) {
            int size = scanner.nextInt();
            int[][] A = new int[size][size];
            int[][] B = new int[size][size];

            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    A[i][j] = scanner.nextInt();

            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    B[i][j] = scanner.nextInt();

            long start, end;
            
            start = System.nanoTime();
            int[][] resultOrdinary = ordinaryMultiply(A, B);
            end = System.nanoTime();
            long timeOrdinary = end - start;

            start = System.nanoTime();
            int[][] resultStrassen = strassenMultiply(A, B);
            end = System.nanoTime();
            long timeStrassen = end - start;

            writer.println("Matrix Size: " + size + " x " + size);
            writer.println("Ordinary Multiplication Result:");
            printMatrix(writer, resultOrdinary);
            writer.println("Strassen Multiplication Result:");
            printMatrix(writer, resultStrassen);
            writer.println("Time (Ordinary): " + timeOrdinary + " ns");
            writer.println("Time (Strassen): " + timeStrassen + " ns");
            writer.println();
        }

        scanner.close();
        writer.close();
        console.close();
    }

    public static int[][] ordinaryMultiply(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                for (int k = 0; k < n; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    public static int[][] strassenMultiply(int[][] A, int[][] B) {
        int n = A.length;
        if (n == 1) {
            int[][] C = { { A[0][0] * B[0][0] } };
            return C;
        }

        int newSize = n / 2;
        int[][][] sub = new int[8][newSize][newSize];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                sub[0][i][j] = A[i][j];
                sub[1][i][j] = A[i][j + newSize];
                sub[2][i][j] = A[i + newSize][j];
                sub[3][i][j] = A[i + newSize][j + newSize];
                sub[4][i][j] = B[i][j];
                sub[5][i][j] = B[i][j + newSize];
                sub[6][i][j] = B[i + newSize][j];
                sub[7][i][j] = B[i + newSize][j + newSize];
            }
        }

        int[][] M1 = strassenMultiply(add(sub[0], sub[3]), add(sub[4], sub[7]));
        int[][] M2 = strassenMultiply(add(sub[2], sub[3]), sub[4]);
        int[][] M3 = strassenMultiply(sub[0], subtract(sub[5], sub[7]));
        int[][] M4 = strassenMultiply(sub[3], subtract(sub[6], sub[4]));
        int[][] M5 = strassenMultiply(add(sub[0], sub[1]), sub[7]);
        int[][] M6 = strassenMultiply(subtract(sub[2], sub[0]), add(sub[4], sub[5]));
        int[][] M7 = strassenMultiply(subtract(sub[1], sub[3]), add(sub[6], sub[7]));

        int[][] C = new int[n][n];
        for (int i = 0; i < newSize; i++) {
            for (int j = 0; j < newSize; j++) {
                C[i][j] = M1[i][j] + M4[i][j] - M5[i][j] + M7[i][j];
                C[i][j + newSize] = M3[i][j] + M5[i][j];
                C[i + newSize][j] = M2[i][j] + M4[i][j];
                C[i + newSize][j + newSize] = M1[i][j] - M2[i][j] + M3[i][j] + M6[i][j];
            }
        }
        return C;
    }

    public static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = A[i][j] + B[i][j];
        return result;
    }

    public static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[i][j] = A[i][j] - B[i][j];
        return result;
    }

    public static void printMatrix(PrintWriter writer, int[][] matrix) {
        for (int[] row : matrix) {
            for (int val : row)
                writer.print(val + " ");
            writer.println();
        }
    }
} 
