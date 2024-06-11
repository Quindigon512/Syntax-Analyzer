import java.nio.charset.StandardCharsets;
import java.io.*;

import java.util.*;
import java.util.stream.*;

public class Test {
    // Credit "WhiteFang34" of https://stackoverflow.com/users/653230/whitefang34
    // via https://stackoverflow.com/a/5762502/6286797
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    // End copyrighted material
    
    public static class ProcessResult {
        public String stdout;
        public String stderr;
        public int exitValue;
        public ProcessResult() {}
        public ProcessResult(String stdout, String stderr, int exitValue) {
            this.stdout = stdout;
            this.stderr = stderr;
            this.exitValue = exitValue;
        }
    }

    public static String read(InputStream s) throws Exception {
        return new BufferedReader(
            new InputStreamReader(s, StandardCharsets.UTF_8)
        ).lines()
        .collect(Collectors.joining("\n"));
    }
    public static String read(String s) throws Exception {
        return read(new FileInputStream(s));
    }

    public static ProcessResult exec(String[] command) throws Exception {
        System.out.println("Running command " + Arrays.toString(command));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(command);
        Process shellP = builder.start();
        
        String stdout = read(shellP.getInputStream());
        String stderr = read(shellP.getErrorStream());
        shellP.waitFor();
        
        return new ProcessResult(stdout, stderr, shellP.exitValue());
    }

    public static void run(String[] command) throws Exception {
        ProcessResult result = exec(command);
        System.out.print(result.stdout);
        System.err.print(result.stderr);
        if (result.exitValue != 0) {
            throw new Exception("Process exited with non-zero code " + result.exitValue);
        }
    }

    public static int getFirstDifference(String left, String right) throws Exception {
        int firstStop = left.length() < right.length() ? left.length() : right.length();
        for (int i = 0; i < firstStop; i++) {
            if (left.charAt(i) != right.charAt(i)) {
                return i;
            }
        }
        
        if (left.length() != right.length()) {
            return firstStop;
        }
        // Both strings are the same. Shouldn't happen in this program...
        throw new Exception("Strings are the same.");
    }

    public static String getSubstringEllipsis(String s, int start, int end) {
        // if (s.indexOf('\n', start) != -1) {
        //     // Break on lines if present, but not if that would give an empty output
        //     int n = s.indexOf('\n', start);
        //     if (n != start) {
        //         s = s.substring(0, n);
        //     } else {
        //         while ()
        //     }
        // }

        if (end >= s.length()) {
            // Swallow IndexOutOfBoundsException
            end = s.length();
        }    

        String suffix = "";
        if (end < s.length()) {
            // If we will truncate the string, make room for an ellipsis.
            end -= 3;
            suffix = "...";
        }

        return s.substring(start, end) + suffix;
    }

    public static void main(String[] args) throws Exception {
        String[][] testNames = new String[][] {
            {"fail_01.minc", "output_fail_01.txt"},
            {"fail_02.minc", "output_fail_02.txt"},
            {"fail_03a.minc", "output_fail_03a.txt"},
            {"fail_03b.minc", "output_fail_03b.txt"},
            {"fail_04.minc", "output_fail_04.txt"},
            {"fail_05a.minc", "output_fail_05a.txt"},
            {"fail_05b.minc", "output_fail_05b.txt"},
            {"fail_06.minc", "output_fail_06.txt"},
            {"fail_07.minc", "output_fail_07.txt"},
            {"fail_08a.minc", "output_fail_08a.txt"},
            {"fail_08b.minc", "output_fail_08b.txt"},
            {"fail_09.minc", "output_fail_09.txt"},
            {"fail_10.minc", "output_fail_10.txt"},
            {"succ_01.minc", "output_succ_01.txt"},
            {"succ_02.minc", "output_succ_02.txt"},
            {"succ_03.minc", "output_succ_03.txt"},
            {"succ_04.minc", "output_succ_04.txt"},
            {"succ_05.minc", "output_succ_05.txt"},
            {"succ_06.minc", "output_succ_06.txt"},
            {"succ_07.minc", "output_succ_07.txt"},
            {"succ_08.minc", "output_succ_08.txt"},
            {"succ_09.minc", "output_succ_09.txt"},
            {"succ_10.minc", "output_succ_10.txt"},
        };

        for (String[] inputOutput : testNames) {
            String inputName = inputOutput[0];
            String expectedName = inputOutput[1];
			//String inputName = "../samples/" + inputOutput[0];
            //String expectedName = "../samples/" + inputOutput[1];

            ProcessResult result = exec(new String[] {"java", "Program", inputName});
            String observed = result.stdout;
            String expected = read(expectedName);
            if (!observed.equals(expected)) {
                System.out.println("Test on " + inputName + " expecting " + expectedName + " failed.; Got: ");
                System.out.print(observed);
                System.err.print(result.stderr);

                System.out.println();
                System.out.println();
                System.out.println();
                int firstDifference = getFirstDifference(observed, expected);
                System.out.println("Strings are identical for first " + firstDifference + " characters:");
                System.out.println(observed.substring(0, firstDifference));
                System.out.println();
                System.out.println();
                System.out.println("After that, compare [" + ANSI_BLACK_BACKGROUND + ANSI_CYAN + " the expected solution from " + expectedName + ANSI_RESET +
                    "] with [" + ANSI_WHITE_BACKGROUND + ANSI_RED + "the result your code produced:" + ANSI_RESET + "]");
                System.out.println("[" + ANSI_BLACK_BACKGROUND + ANSI_CYAN + getSubstringEllipsis(expected, firstDifference, firstDifference + 75) + ANSI_RESET + "]");
                System.out.println("[" + ANSI_WHITE_BACKGROUND + ANSI_RED + getSubstringEllipsis(observed, firstDifference, firstDifference + 75) + ANSI_RESET + "]");
                System.out.println();
                break;
            } else {
                System.out.println("Test " + inputOutput[0] + " ok");
            }

        }
    }
}

