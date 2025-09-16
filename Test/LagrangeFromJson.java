import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LagrangeFromJson {

    // Function to perform Lagrange Interpolation at x = 0 (constant term)
    public static double lagrangeConstant(double[] x, double[] y) {
        int n = x.length;
        double result = 0.0;

        for (int i = 0; i < n; i++) {
            double term = y[i];
            for (int j = 0; j < n; j++) {
                if (j != i) {
                    term *= (0 - x[j]) / (x[i] - x[j]); // evaluate at x=0
                }
            }
            result += term;
        }

        return result;
    }

    // Function to process one JSON file
    public static void processJsonFile(String filename) {
        JSONParser parser = new JSONParser();

        try {
            // Step 1: Read JSON file
            Object obj = parser.parse(new FileReader(filename));
            JSONObject jsonObject = (JSONObject) obj;

            // Step 2: Extract data points (ignore "keys" metadata)
            jsonObject.remove("keys");

            int n = jsonObject.size();
            double[] x = new double[n];
            double[] y = new double[n];

            int idx = 0;
            for (Object keyObj : jsonObject.keySet()) {
                String keyStr = (String) keyObj;
                int xi = Integer.parseInt(keyStr);

                JSONObject point = (JSONObject) jsonObject.get(keyStr);

                String baseStr = (String) point.get("base");
                String valStr = (String) point.get("value");

                int base = Integer.parseInt(baseStr);
                int yi = Integer.parseInt(valStr, base); // convert using base

                x[idx] = xi;
                y[idx] = yi;
                idx++;
            }

            // Step 3: Use Lagrange interpolation to compute constant term
            double c = lagrangeConstant(x, y);

            // Step 4: Print result
            System.out.println("File: " + filename);
            System.out.println("The constant term c is: " + c);
            System.out.println("---------------------------------");

        } catch (IOException | ParseException e) {
            System.out.println("Error processing file: " + filename);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide at least one JSON filename as an argument.");
            return;
        }

        // Process each JSON file provided in command line
        for (String filename : args) {
            processJsonFile(filename);
        }
    }
}
