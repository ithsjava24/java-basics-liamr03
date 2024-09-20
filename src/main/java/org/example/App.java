package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] prices = new int[24];
        char choice;

        do {
            System.out.println("""
                
                Elpriser
                ========
                1. Inmatning
                2. Min, Max och Medel
                3. Sortera
                4. Bästa Laddningstid (4h)
                5. Visualisering
                e. Avsluta
                """);
            System.out.print("Välj ett alternativ: \n");

            choice = scanner.next().toLowerCase().charAt(0);

            if (choice == 'e') {
                System.out.println("Programmet avslutas.");
                break;
            }

            switch (choice) {
                case '1' -> inputPrices(scanner, prices);
                case '2' ->
                    calculateMinMaxAverage(prices);
                case '3' ->
                    sortPrices(prices);
                case '4' ->
                    findBestChargingTime(prices);
                case '5' ->
                    Visualisering(prices);
                default -> System.out.println("Ogiltigt val. Försök igen.");
            }

            System.out.println();
        } while (true);
        scanner.close();
    }

    public static void inputPrices(Scanner scanner, int[] prices) {
        System.out.println("Ange elpriserna för varje timme (i hela ören):");
        for (int i = 0; i < 24; i++) {
            System.out.print("Pris för intervallet " + String.format("%02d", i) + ":00 - " + String.format("%02d", (i + 1) % 24) + ":00: \n");
            prices[i] = scanner.nextInt();
        }
        System.out.println("Inmatningen är klar.");
    }

    public static void calculateMinMaxAverage(int[] prices) {
        if (prices.length == 0) {
            System.out.println("Inga priser inmatade.");
            return;
        }

        int min = prices[0], max = prices[0], sum = 0;
        int minHour = 0, maxHour = 0;

        for (int i = 0; i < prices.length; i++) {
            sum += prices[i];
            if (prices[i] < min) {
                min = prices[i];
                minHour = i;
            }
            if (prices[i] > max) {
                max = prices[i];
                maxHour = i;
            }
        }

        double average = sum / 24.0;

        System.out.print("Lägsta pris: " + String.format("%02d-%02d", minHour, (minHour + 1) % 24) + ", " + min + " öre/kWh\n");
        System.out.print("Högsta pris: " + String.format("%02d-%02d", maxHour, (maxHour + 1) % 24) + ", " + max + " öre/kWh\n" );
        System.out.print("Medelpris: " + String.format(Locale.forLanguageTag("sv-SE"),"%.2f", average) + " öre/kWh\n");
    }

    public static void sortPrices(int[] prices) {
        // Create an array of index-value pairs and sort it by value
        Integer[] indices = new Integer[24];
        for (int i = 0; i < indices.length; i++) indices[i] = i;

        Arrays.sort(indices, (i1, i2) -> Integer.compare(prices[i2], prices[i1]));

        System.out.print("Priserna har sorterats.\n");
        for (int i : indices) {
            // Check if the hour is 23, then print 23-24 instead of 23-00
            if (i == 23) {
                System.out.print(String.format("23-24 %d öre", prices[i])+ "\n");
            } else {
                System.out.print(String.format("%02d-%02d %d öre", i, (i + 1) % 24, prices[i]) + "\n");
            }
        }
    }

    public static void findBestChargingTime(int[] prices) {
        if (prices.length < 4) {
            System.out.println("För få priser för att beräkna bästa laddningstid.");
            return;
        }

        int bestStart = 0;
        int minSum = Integer.MAX_VALUE;

        for (int i = 0; i <= prices.length - 4; i++) {
            int currentSum = prices[i] + prices[i + 1] + prices[i + 2] + prices[i + 3];
            if (currentSum < minSum) {
                minSum = currentSum;
                bestStart = i;
            }
        }

        double average = minSum / 4.0;

        System.out.print("Påbörja laddning klockan " + String.format("%02d", bestStart) + "\n");
        System.out.println("Medelpris 4h: " + String.format("%.1f", average) + " öre/kWh" + "\n");
    }

    public static void Visualisering(int[] prices){
        int maxPrice = Arrays.stream(prices).max().orElse(1);
        int minPrice = Arrays.stream(prices).min().orElse(0);
        final int HEIGHT = 6;
        final float DIFFERENCE = (maxPrice - minPrice) / (HEIGHT - 1f);

        System.out.println("Visualisering av elpriser:");

        int maxRowLength = 3 + 1 + (prices.length * 3);

        for (int i = HEIGHT; i > 0; i--) {
            StringBuilder row = new StringBuilder();
            int lowerBound = (i == HEIGHT) ? maxPrice : (int) (maxPrice - (HEIGHT - i) * DIFFERENCE);

            if (i == HEIGHT) {
                row.append(String.format("%3d|", maxPrice));
            } else if (i == 1) {
                row.append(String.format("%3d|", minPrice));
            } else {
                row.append("   |");
            }

            for (int price : prices) {
                if (price >= lowerBound) {
                    row.append("  x");
                } else {
                    row.append("  ");
                }
            }
            while (row.length() < maxRowLength - 1){
                row.append(" ");
            }
            if (i != 1){
                row.append(" ");
            }

            System.out.println(row);
        }

        System.out.print("   |------------------------------------------------------------------------\n");
        System.out.print("   | 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23\n");
    }
}


