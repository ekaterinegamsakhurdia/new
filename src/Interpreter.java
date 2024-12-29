import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
//    todo
//    has no else
//    doesn't look at the package and imports, only looks at func main
//    if stuff after main - will give errors
//    var x = 10 - only supported assignment
//    no ( ) in expressions
//    supports only integer variables
//    fmt.Scanln(&n) - doesn't check if n exists or not

    private final Map<String, Integer> state = new HashMap<>(); // Variable storage


    public int parse_expr(String expr) {
//        System.out.println(expr);
        expr = expr.trim();
        if (expr.contains("+")) {
            int i = expr.indexOf("+");
            return parse_expr(expr.substring(0, i)) + parse_expr(expr.substring((i+1)));
        }
//        -_2
        else if (expr.contains("-") && !expr.startsWith("-")) {
            int i = expr.indexOf("-");
            return parse_expr(expr.substring(0, i)) - parse_expr(expr.substring((i+1)));
        }
        else if (expr.contains("/")) {
            int i = expr.indexOf("/");
            return parse_expr(expr.substring(0, i)) / parse_expr(expr.substring((i+1)));
        }
        else if (expr.contains("*")) {
            int i = expr.indexOf("*");
            return parse_expr(expr.substring(0, i)) * parse_expr(expr.substring((i+1)));
        }
        else if (expr.contains("%")) {
            int i = expr.indexOf("%");
            return parse_expr(expr.substring(0, i)) % parse_expr(expr.substring((i+1)));
        }
//        -_1
        else if (expr.startsWith("-")) {
            return - parse_expr(expr.substring(1));
        }
        else if (state.get(expr) != null)
            return state.get(expr);
        else
            return Integer.parseInt(expr);
    }

    public boolean parse_cond(String cond) {
//        first is checked or/and, ==/<= so the order of execution is done right
        if (cond.contains("&&")) {
            int i = cond.indexOf("&&");
            return parse_cond(cond.substring(0, i)) && parse_cond(cond.substring((i+2)));
        }
        else if (cond.contains("||")) {
            int i = cond.indexOf("||");
            return parse_cond(cond.substring(0, i)) || parse_cond(cond.substring((i+2)));
        }
        else if (cond.startsWith("!")) {
            return ! parse_cond(cond.substring(1));
        }
        else if (cond.contains(">=")) {
            int i = cond.indexOf(">=");
            return parse_expr(cond.substring(0, i)) >= parse_expr(cond.substring((i+2)));
        }
        else if (cond.contains(">")) {
            int i = cond.indexOf(">");
            return parse_expr(cond.substring(0, i)) > parse_expr(cond.substring((i+1)));
        }
        if (cond.contains("<=")) {
            int i = cond.indexOf("<=");
            return parse_expr(cond.substring(0, i)) <= parse_expr(cond.substring((i+2)));
        }
        else if (cond.contains("<")) {
            int i = cond.indexOf("<");
            return parse_expr(cond.substring(0, i)) < parse_expr(cond.substring((i+1)));
        }
        else if (cond.contains("==")) {
            int i = cond.indexOf("==");
            return parse_expr(cond.substring(0, i)) == parse_expr(cond.substring((i+2)));
        }
        else if (cond.contains("!=")) {
            int i = cond.indexOf("!=");
            return parse_expr(cond.substring(0, i)) != parse_expr(cond.substring((i+2)));
        }

        throw new RuntimeException("Unsupported expression");
    }

    public void execute(String stm) {
//        go lang ignores all ; at the end of the statement x=7;;; allowed in go
        stm = stm.trim();
        while (stm.endsWith(";"))
            stm = stm.substring(0, stm.length() - 1);

//            ignore comments
        if (stm.startsWith("//")) {
            return;
        }

//        read user input
//        fmt.Scanln(&n)
        else if (stm.startsWith("fmt.Scanln(&")) {
            String name = stm.substring(12, stm.length()-1);
            Scanner scanner = new Scanner(System.in);
            int num = Integer.parseInt(scanner.nextLine().trim());
            this.state.put(name, num);
        }

//        fmt.Println("...") or fmt.Println(...)
        else if (stm.startsWith("fmt.Println")) {
//            print string
            if (stm.startsWith("fmt.Println(\"")) {
//                remove at start fmt.Println(", and at end ")
                String content = stm.substring(13, stm.length()-2);
                System.out.println(content);
            }
//            print expression
            else {
                String expr = stm.substring(12, stm.length()-1);
                System.out.println(parse_expr(expr));
            }
        }
//      declaring variables
        else if (stm.startsWith("var")) {
            int i = stm.indexOf('=');
            String name = stm.substring(3, i).trim();
            String expr = stm.substring(i+1).trim();

            program(name + "  " + expr);
            this.state.put(name, parse_expr(expr));
        }
//        changing variables
        else if (!stm.contains("if") && !stm.contains("for") && stm.contains("=")) {
            int i = stm.indexOf('=');
            String name = stm.substring(0, i).trim();
            String expr = stm.substring(i+1).trim();

            program(name + "  " + expr);
            this.state.put(name, parse_expr(expr));
        }

        else if (stm.startsWith("if")) {
            int start = stm.indexOf('{');
            int end = stm.lastIndexOf('}');
//             cond - after if before {
            String cond = stm.substring(2, start).trim();
//            sts - after first { all the way to the code minus the last }
            String sts = stm.substring(start + 1, stm.length()-1).trim();
            boolean b = parse_cond(cond);
//            System.out.println("-- " + cond + "    " + sts);
            if (b) {
                program(sts);
            }
        }

        else if (stm.startsWith("for")) {
            int start = stm.indexOf('{');
            int end = stm.lastIndexOf('}');
//             cond - after for before {
            String cond = stm.substring(3, start).trim();
//            sts - after first { all the way to the code minus the last }
            String sts = stm.substring(start + 1, stm.length()-1).trim();
            boolean b = parse_cond(cond);
//            System.out.println("-- " + cond + "    " + sts);

//          if cond run sts and run while again
//          c`.pr = sts, c.pr
            if (b) {
                program(sts);
                program(stm);
            }
        }

    }



    //    runs code statement by statement
    public void program(String code) {
        String[] stms = getStatement(code);

        if (stms[0] == null) return;

//        System.out.println(stms[0]);
//        System.out.println("//////////");
        execute(stms[0]);
        program(stms[1]);
    }


    //    returns nextStatement, rest_code
    public String[] getStatement(String code) {
        if (code == null || code.trim().equals("")) return new String[] {null, null};

        // handles one line statements
        code = code.trim();
        String[] lines = code.split("\n");
        String first_line = lines.length > 0 ? lines[0].trim() : code;

        if (    first_line.startsWith("//") ||
                first_line.contains("fmt.Println") || first_line.contains("fmt.Scanln") ||
                first_line.contains("var") ||
                (!first_line.contains("if") && !first_line.contains("for") && first_line.contains("="))) {
            String stm = code.split("\n")[0];
            if (code.length() == stm.length()) return new String[]{stm, null};

            return  new String[]{stm, code.substring(stm.length() + 1)};
        }

        // for statements which have statements inside, like if
        int begin = 0;
        int end = 0;
        char[] codeInChar = code.toCharArray();

        // end of statement is reached when number of { and } match
        for (char c : codeInChar) {
            if (begin != 0 && begin == end)
                break;

            if (c == '{')
                begin++;
            else if (c == '}') {
                end++;
            }
        }

        if (begin == 0 || begin != end) return new String[] {null, null};

        int index_of_last_end = 0;

//        find index of last }
        for (char c : codeInChar) {
            if (end == 0) break;
            if (c == '}') end--;
            index_of_last_end++;
        }

        String stm = code.substring(0, index_of_last_end);

//        if last statement is reached rest is set to null
        if (code.length() == stm.length()) return new String[]{stm, null};

        return new String[]{stm, code.substring(stm.length() + 1)};
    }



    public Map<String, Integer>  runCode(String fileName){
        // Read the contents of the file
        String code = null;
        try {
            code = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Clean the code by replacing multiple spaces and newlines with a single space
//        String cleanedCode = code.replaceAll("\\s+", " ");
//        find function main and run it

        int i = code.indexOf("func main()");
        if (i == -1) {
            throw new RuntimeException("function main is undeclared in the main package");
            //The code runs the main part, if it's not declaired throws an error
        }
        else {
            code = code.substring(i + 11);
            int start = code.indexOf("{");
            int end = code.lastIndexOf("}");
            code = code.substring(start + 1, end).trim();
        }
        program(code);
        return this.state;
    }


    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();



        Map<String, Integer>  c = interpreter.runCode("Largest_digit.go");

    }
}