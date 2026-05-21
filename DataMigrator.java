import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataMigrator {
    public static void main(String[] args) {
        String inputFile = "orders.csv";
        String outputFile = "errors.txt";

        List<String> errorRecords = new ArrayList<>();

        System.out.println("Starting");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) {
                    continue;
                }

                if (line.trim().isEmpty()) {
                    System.out.println("Line " + lineNumber + ": Skipped empty row.");
                    continue;
                }

                String[] columns = line.split(",");
                if (columns.length < 3) {
                    System.out.println("Line " + lineNumber + ": Skipped corrupted data (missing columns) -> " + line);
                    continue;
                }

                if (columns[2].equalsIgnoreCase("ERROR")) {
                    errorRecords.add(line);
                    System.out.println("Line " + lineNumber + ": Found ERROR record -> " + columns[1]);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Critical Error: Source file not found: " + inputFile);
            return;
        } catch (IOException e) {
            System.err.println("Critical Error during file reading: " + e.getMessage());
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (String errorLine : errorRecords) {
                bw.write(errorLine);
                bw.newLine();
            }
            System.out.println("Pipeline finished successfully. Saved " + errorRecords.size() + " records to " + outputFile);

        } catch (IOException e) {
            System.err.println("Error while writing to output file: " + e.getMessage());
        }
    }
}