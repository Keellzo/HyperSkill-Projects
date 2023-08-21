package sorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(final String[] args) {
        String dataType = "word";
        String sortingType = "natural";
        String inputFile = null;
        String outputFile = null;

        // Replace the default scanner for standard input with a more flexible one
        Scanner scanner = null;
        PrintWriter writer = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-sortingType":
                    if (i + 1 < args.length && ("natural".equals(args[i + 1]) || "byCount".equals(args[i + 1]))) {
                        sortingType = args[i + 1];
                        i++;  // skip the next argument
                    } else {
                        System.out.println("No sorting type defined!");
                        return;
                    }
                    break;
                case "-dataType":
                    if (i + 1 < args.length && ("long".equals(args[i + 1]) || "line".equals(args[i + 1]) || "word".equals(args[i + 1]))) {
                        dataType = args[i + 1];
                        i++;  // skip the next argument
                    } else {
                        System.out.println("No data type defined!");
                        return;
                    }
                    break;
                case "-inputFile":
                    if (i + 1 < args.length) {
                        inputFile = args[i + 1];
                        i++;
                    }
                    break;
                case "-outputFile":
                    if (i + 1 < args.length) {
                        outputFile = args[i + 1];
                        i++;
                    }
                    break;
                default:
                    System.out.println("\"" + args[i] + "\" is not a valid parameter. It will be skipped.");
            }
        }

        // Handle the inputFile
        if (inputFile != null) {
            try {
                scanner = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("Error: Input file not found.");
                return;
            }
        } else {
            scanner = new Scanner(System.in);
        }

        // Handle the outputFile
        if (outputFile != null) {
            try {
                writer = new PrintWriter(new File(outputFile));
            } catch (FileNotFoundException e) {
                System.out.println("Error: Cannot create or write to output file.");
                return;
            }
        }

        switch (dataType) {
            case "long":
                handleLongs(scanner, sortingType, writer);
                break;
            case "line":
                handleLines(scanner, sortingType, writer);
                break;
            case "word":
                handleWords(scanner, sortingType, writer);
                break;
            default:
                System.out.println("Unknown data type: " + dataType);
                break;
        }

        if (inputFile != null && scanner != null) {
            scanner.close();
        }
    }

    private static void handleLongs(Scanner scanner, String sortingType, PrintWriter writer) {
        List<Long> numbers = new ArrayList<>();
        while (scanner.hasNext()) {
            if (scanner.hasNextLong()) {
                numbers.add(scanner.nextLong());
            } else {
                output(writer, "\"" + scanner.next() + "\" is not a long. It will be skipped.");
            }
        }
        processData(numbers, sortingType, "numbers", writer);
    }

    private static void handleLines(Scanner scanner, String sortingType, PrintWriter writer) {
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }
        processData(lines, sortingType, "lines", writer);
    }

    private static void handleWords(Scanner scanner, String sortingType, PrintWriter writer) {
        List<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            words.add(scanner.next());
        }
        processData(words, sortingType, "words", writer);
    }

    private static <T extends Comparable<T>> void processData(List<T> data, String sortingType, String dataType, PrintWriter writer) {
        output(writer, "Total " + dataType + ": " + data.size() + ".");
        if ("natural".equals(sortingType)) {
            Collections.sort(data);
            StringBuilder sb = new StringBuilder("Sorted data: ");
            for (T entry : data) {
                sb.append(entry).append(" ");
            }
            output(writer, sb.toString());
        } else if ("byCount".equals(sortingType)) {
            Map<T, Integer> frequencyMap = new HashMap<>();
            for (T entry : data) {
                frequencyMap.put(entry, frequencyMap.getOrDefault(entry, 0) + 1);
            }

            List<Map.Entry<T, Integer>> entryList = new ArrayList<>(frequencyMap.entrySet());
            entryList.sort((e1, e2) -> {
                int cmp = Integer.compare(e1.getValue(), e2.getValue());
                if (cmp == 0) {
                    return e1.getKey().compareTo(e2.getKey());
                }
                return cmp;
            });

            for (Map.Entry<T, Integer> entry : entryList) {
                int percentage = (int) Math.round((double) entry.getValue() / data.size() * 100);
                output(writer, entry.getKey() + ": " + entry.getValue() + " time(s), " + percentage + "%");
            }
        }
    }

    private static void output(PrintWriter writer, String message) {
        if (writer == null) {
            System.out.println(message);
        } else {
            writer.println(message);
        }
    }
}

