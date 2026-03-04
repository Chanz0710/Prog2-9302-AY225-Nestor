import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
Product Category Profitability Analysis
Scenario:
Company wants to know which category is most profitable.

Requirements:
Group by product category

Compute:
Total sales per category
Average sale per category

Identify:
Most profitable category
Least profitable category
 */

public class CategoryProfitabilityAnalysis {

    // Inner class to hold one row
    static class Game {
        String title;
        String genre;
        double totalSales;

        Game(String title, String genre, double totalSales) {
            this.title      = title;
            this.genre      = genre;
            this.totalSales = totalSales;
        }
    }

    // Helper: parse a double safely
    static double parseDouble(String s) {
        try {
            s = s.trim();
            return (s.isEmpty() || s.equals("N/A")) ? 0.0 : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Split a CSV line respecting quoted fields
    // Handles cases like: "Title with, comma",Genre,Publisher
    static String[] splitCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        fields.add(sb.toString().trim());
        return fields.toArray(new String[0]);
    }

    // Load and parse the CSV file
    static List<Game> loadCSV(String filePath) throws IOException {
        List<Game> games = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        if (lines.isEmpty()) throw new IOException("CSV file is empty.");

        // Detect column positions from header row
        String[] headers = splitCSVLine(lines.get(0));
        Map<String, Integer> colIndex = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            colIndex.put(headers[i].toLowerCase(), i);
        }

        int titleIdx = colIndex.getOrDefault("title",       0);
        int genreIdx = colIndex.getOrDefault("genre",       2);
        int salesIdx = colIndex.getOrDefault("total_sales", 6);

        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            String[] cols = splitCSVLine(line);
            if (cols.length <= Math.max(genreIdx, salesIdx)) continue;

            String title = cols[titleIdx].trim();
            String genre = cols[genreIdx].trim();
            double sales = parseDouble(cols[salesIdx]);

            if (!genre.isEmpty() && !genre.contains("\"")) {
                games.add(new Game(title, genre, sales));
            }
        }
        return games;
    }

    // Run the profitability analysis
    static void analyze(List<Game> games) {

        // Group sales by genre
        Map<String, List<Double>> salesByGenre = new LinkedHashMap<>();
        for (Game g : games) {
            salesByGenre.computeIfAbsent(g.genre, k -> new ArrayList<>()).add(g.totalSales);
        }

        // Compute total and average per genre
        Map<String, double[]> stats = new TreeMap<>();
        for (Map.Entry<String, List<Double>> entry : salesByGenre.entrySet()) {
            List<Double> vals = entry.getValue();
            double total = 0;
            for (double v : vals) total += v;
            double avg = total / vals.size();
            stats.put(entry.getKey(), new double[]{total, avg, vals.size()});
        }

        // Print results table
        System.out.println("\n+--------------------------------------------------------------+");
        System.out.println("|      PRODUCT CATEGORY PROFITABILITY ANALYSIS                 |");
        System.out.println("|      Dataset: Video Game Sales 2024                          |");
        System.out.println("+--------------------------------------------------------------+\n");

        System.out.printf("%-22s %12s %12s %6s%n", "Genre (Category)", "Total Sales", "Avg Sale", "Count");
        System.out.println("-".repeat(56));

        // Sort by total sales descending
        List<Map.Entry<String, double[]>> sorted = new ArrayList<>(stats.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue()[0], a.getValue()[0]));

        for (Map.Entry<String, double[]> e : sorted) {
            double[] v = e.getValue();
            System.out.printf("%-22s %10.2fM %10.2fM %6.0f%n", e.getKey(), v[0], v[1], v[2]);
        }
        System.out.println("-".repeat(56));

        // Most / Least profitable
        Map.Entry<String, double[]> most  = sorted.get(0);
        Map.Entry<String, double[]> least = sorted.get(sorted.size() - 1);

        System.out.println();
        System.out.printf("MOST  Profitable Category : %-20s (%.2fM total sales)%n", most.getKey(),  most.getValue()[0]);
        System.out.printf("LEAST Profitable Category : %-20s (%.2fM total sales)%n", least.getKey(), least.getValue()[0]);
        System.out.println("\nTotal games analysed : " + games.size());
        System.out.println("Total genres found   : " + stats.size());
    }

    public static void main(String[] args) {
        String csvPath = args.length > 0 ? args[0] : "vgchartz-2024.csv";
        System.out.println("Loading dataset from: " + csvPath);
        try {
            List<Game> games = loadCSV(csvPath);
            analyze(games);
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
    }
}