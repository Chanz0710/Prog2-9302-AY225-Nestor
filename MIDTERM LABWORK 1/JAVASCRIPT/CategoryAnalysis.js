/**
 * MIDTERM-LAB-1: Product Category Profitability Analysis
 * Course: Prog2-9302
 * Language: JavaScript (Node.js)
 * Dataset: Video Game Sales 2024 (vgchartz-2024.csv)
 *
 * Run: node categoryAnalysis.js
 *      node categoryAnalysis.js path/to/vgchartz-2024.csv
 */

const fs   = require("fs");
const path = require("path");

// Known valid genres from the dataset
const VALID_GENRES = new Set([
  "Action", "Action-Adventure", "Adventure", "Fighting", "Misc",
  "Music", "Platform", "Puzzle", "Racing", "Role-Playing",
  "Shooter", "Simulation", "Sports", "Strategy", "MMO",
  "Party", "Visual Novel", "Sandbox", "Education", "Board Game"
]);

// Parse CSV respecting quoted fields (handles commas inside titles)
function parseCSV(filePath) {
  const raw   = fs.readFileSync(filePath, "utf-8");
  const lines = raw.trim().split("\n");

  // Parse header
  const headers = splitLine(lines[0]);

  return lines.slice(1)
    .filter(line => line.trim())
    .map(line => {
      const cols = splitLine(line);
      const obj  = {};
      headers.forEach((h, i) => {
        obj[h.toLowerCase()] = (cols[i] || "").trim();
      });
      return obj;
    });
}

// Split a CSV line respecting quoted fields
function splitLine(line) {
  const fields = [];
  let cur = "", inQuotes = false;
  for (const c of line) {
    if (c === '"') { inQuotes = !inQuotes; }
    else if (c === ',' && !inQuotes) { fields.push(cur.trim()); cur = ""; }
    else { cur += c; }
  }
  fields.push(cur.trim());
  return fields;
}

// Safe number parser
function toNum(val) {
  const n = parseFloat(val);
  return isNaN(n) ? 0 : n;
}

// Main analysis
function analyze(csvPath) {
  console.log(`\nLoading dataset: ${csvPath}\n`);
  const rows = parseCSV(csvPath);

  // Group total_sales by genre (only valid genres)
  const grouped = {};
  rows.forEach(row => {
    const genre = row["genre"];
    const sales = toNum(row["total_sales"]);
    if (!genre || !VALID_GENRES.has(genre)) return;

    if (!grouped[genre]) grouped[genre] = { totalSales: 0, count: 0 };
    grouped[genre].totalSales += sales;
    grouped[genre].count      += 1;
  });

  // Compute averages and sort by total sales descending
  const results = Object.entries(grouped)
    .map(([genre, data]) => ({
      genre,
      totalSales: data.totalSales,
      avgSale:    data.totalSales / data.count,
      count:      data.count,
    }))
    .sort((a, b) => b.totalSales - a.totalSales);

  // Print table
  console.log("+--------------------------------------------------------------+");
  console.log("|      PRODUCT CATEGORY PROFITABILITY ANALYSIS                 |");
  console.log("|      Dataset: Video Game Sales 2024                          |");
  console.log("+--------------------------------------------------------------+\n");

  console.log(`${"Genre (Category)".padEnd(22)} ${"Total Sales".padStart(12)} ${"Avg Sale".padStart(12)} ${"Count".padStart(6)}`);
  console.log("-".repeat(56));

  results.forEach(r => {
    console.log(
      `${r.genre.padEnd(22)} ${(r.totalSales.toFixed(2) + "M").padStart(12)} ${(r.avgSale.toFixed(2) + "M").padStart(12)} ${String(r.count).padStart(6)}`
    );
  });

  console.log("-".repeat(56));

  const most  = results[0];
  const least = results[results.length - 1];

  console.log(`\nMOST  Profitable Category : ${most.genre.padEnd(22)} (${most.totalSales.toFixed(2)}M total sales)`);
  console.log(`LEAST Profitable Category : ${least.genre.padEnd(22)} (${least.totalSales.toFixed(2)}M total sales)`);
  console.log(`\nTotal games analysed : ${rows.length}`);
  console.log(`Total genres found   : ${results.length}`);
}

// Entry point
const csvFile = process.argv[2] || path.join(__dirname, "vgchartz-2024.csv");
analyze(csvFile);