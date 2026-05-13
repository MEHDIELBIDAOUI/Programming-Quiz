package com.example.programmingquiz_bidaoui.utils;

import android.util.Log;

import com.example.programmingquiz_bidaoui.firebase.FirestoreManager;
import com.example.programmingquiz_bidaoui.models.Question;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {

    public static void seedQuestions(FirestoreManager dbManager) {
        dbManager.getDb().collection("questions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Re-seed if database has missing questions (increased threshold to 290 for more C++ challenges)
            if (queryDocumentSnapshots.size() < 290) {
                Log.d("DatabaseSeeder", "Not enough questions found, seeding database...");
                List<Question> questions = new ArrayList<>();

                // --------- JAVA ---------
                // Java Easy
                questions.add(new Question("j_e_1", "What is the extension of java code files?", ".js", ".txt", ".java",
                        ".cpp", 3, "Java", "Facile"));
                questions.add(new Question("j_e_2", "Who invented Java?", "James Gosling", "Guido van Rossum",
                        "Dennis Ritchie", "Bjarne Stroustrup", 1, "Java", "Facile"));
                questions.add(new Question("j_e_3",
                        "Which component is used to compile, debug and execute the java programs?", "JRE", "JIT", "JDK",
                        "JVM", 3, "Java", "Facile"));
                questions.add(new Question("j_e_4", "Which statement is true about Java?",
                        "Java is a sequence-dependent programming language",
                        "Java is a code dependent programming language",
                        "Java is a platform-dependent programming language",
                        "Java is a platform-independent programming language", 4, "Java", "Facile"));
                questions.add(new Question("j_e_5", "Which keyword is used to access the features of a package?",
                        "import", "package", "extends", "export", 1, "Java", "Facile"));
                questions.add(new Question("j_e_6", "What is the default value of a boolean variable?", "true", "false",
                        "null", "not defined", 2, "Java", "Facile"));
                questions.add(new Question("j_e_7", "What operator is used to compare two values?", "><", "=", "==",
                        "<>", 3, "Java", "Facile"));
                questions.add(new Question("j_e_8", "Which method must be implemented by all threads?", "start()",
                        "run()", "stop()", "main()", 2, "Java", "Facile"));

                // Java Easy Code Challenge
                String javaCodeE = "int a = 10;\n" +
                        "int b = 20;\n" +
                        "System.out.println(a + b);";
                questions.add(new Question("j_c_e", "What is the output of this code?\n\n" + javaCodeE, "10", "20",
                        "30", "Error", 3, "Java", "Facile", true));

                String javaCodeE2 = "String s = \"Java\";\n" +
                        "System.out.println(s.length());";
                questions.add(new Question("j_c_e2", "What is the output of this code?\n\n" + javaCodeE2, "3", "4", "5",
                        "Error", 2, "Java", "Facile", true));

                String javaCodeE3 = "int[] nums = {1, 2, 3, 4};\n" +
                        "System.out.println(nums[0]);";
                questions.add(new Question("j_c_e3", "What is the output?\n\n" + javaCodeE3, "0", "1", "2",
                        "ArrayIndexOutOfBounds", 2, "Java", "Facile", true));

                String javaCodeE4 = "int x = 5;\n" +
                        "System.out.println(x > 2 ? \"A\" : \"B\");";
                questions.add(new Question("j_c_e4", "What is the output?\n\n" + javaCodeE4, "A", "B", "5", "Error", 1,
                        "Java", "Facile", true));

                questions.add(new Question("j_c_e5", "What is the output?\n\nSystem.out.println(10 + 20 + \"Java\");",
                        "1020Java", "30Java", "Error", "Java30", 2, "Java", "Facile", true));

                questions.add(new Question("j_c_e6", "What is the output?\n\nchar c = 'A';\nSystem.out.println(c + 1);",
                        "B", "66", "A1", "65", 2, "Java", "Facile", true));

                questions.add(new Question("j_c_e7", "What is the output?\n\nint x = 5;\nx += 10 * 2;", "30", "25", "20",
                        "Error", 2, "Java", "Facile", true));

                // Java Medium
                questions.add(new Question("j_m_1", "What is the default value of a local variable?", "null", "0",
                        "Depends on data type", "Not assigned", 4, "Java", "Moyen"));
                questions.add(new Question("j_m_2", "Which method can be used to find the length of a string?",
                        "getSize()", "length()", "size()", "len()", 2, "Java", "Moyen"));
                questions.add(new Question("j_m_3", "Which of these are selection statements in Java?", "break",
                        "continue", "for()", "if()", 4, "Java", "Moyen"));
                questions.add(new Question("j_m_4", "What is the size of boolean variable?", "8 bit", "16 bit",
                        "32 bit", "not precisely defined", 4, "Java", "Moyen"));
                questions.add(new Question("j_m_5", "Which of these interfaces handle sequences?", "Set", "List", "Map",
                        "Collection", 2, "Java", "Moyen"));
                questions.add(
                        new Question("j_m_6", "Which exception is thrown when divide by zero statement is executed?",
                                "NullPointerException", "NumberFormatException", "ArithmeticException",
                                "IndexOutOfBoundsException", 3, "Java", "Moyen"));
                questions.add(new Question("j_m_7", "What is the output of Math.floor(3.6)?", "3", "3.0", "4", "4.0", 2,
                        "Java", "Moyen"));
                questions.add(new Question("j_m_8", "Which of these are direct subclasses of the Throwable class?",
                        "RuntimeException and Error", "Exception and VirtualMachineError", "Error and Exception",
                        "IOException", 3, "Java", "Moyen"));

                // Medium Code Challenges
                String javaCode1 = "public class Test {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int x = 5;\n" +
                        "        System.out.println(x++ + ++x);\n" +
                        "    }\n" +
                        "}";
                questions.add(new Question("j_c_1", "What is the output of this code?\n\n" + javaCode1, "10", "12",
                        "11", "13", 2, "Java", "Moyen", true));

                String javaCode2 = "int[] arr = {1, 2, 3};\n" +
                        "System.out.println(arr[3]);";
                questions.add(new Question("j_c_2", "What is the result of executing this code block?\n\n" + javaCode2,
                        "3", "0", "ArrayIndexOutOfBoundsException", "Compilation Error", 3, "Java", "Moyen", true));

                String javaCode3 = "String s1 = new String(\"Hi\");\n" +
                        "String s2 = new String(\"Hi\");\n" +
                        "System.out.println(s1 == s2);";
                questions.add(new Question("j_c_3", "What does this code output?\n\n" + javaCode3, "true", "false",
                        "Compilation Error", "null", 2, "Java", "Moyen", true));

                String javaCode4 = "String s = \"apple\";\n" +
                        "s.replace('p', 'b');\n" +
                        "System.out.println(s);";
                questions.add(new Question("j_c_4", "What is the output? (Strings are immutable)\n\n" + javaCode4,
                        "apple", "abble", "appleb", "Error", 1, "Java", "Moyen", true));

                questions.add(new Question("j_c_5",
                        "What is the output?\n\nint[] a = {1};\nint[] b = a;\nb[0] = 2;\nSystem.out.println(a[0]);", "1",
                        "2", "0", "Error", 2, "Java", "Moyen", true));

                questions.add(new Question("j_c_6",
                        "What is the output?\n\nString s = \"123\";\ns.concat(\"456\");\nSystem.out.println(s);",
                        "123456", "123", "456", "Error", 2, "Java", "Moyen", true));

                questions.add(new Question("j_c_7", "What is the output?\n\nSystem.out.println(Math.min(Double.MIN_VALUE, 0.0d));",
                        "0.0", "Double.MIN_VALUE", "Negative Infinity", "Error", 1, "Java", "Moyen", true));

                // Java Hard
                questions.add(new Question("j_d_1", "Which of these keywords is used to make a class?", "class",
                        "struct", "int", "None of these", 1, "Java", "Difficile"));
                questions.add(new Question("j_d_2", "Which of these cannot be used for a variable name in Java?",
                        "identifier & keyword", "identifier", "keyword", "none of these", 3, "Java", "Difficile"));
                questions.add(
                        new Question("j_d_3", "What is the return type of the hashCode() method in the Object class?",
                                "int", "Object", "long", "void", 1, "Java", "Difficile"));
                questions.add(new Question("j_d_4", "Which package contains the Random class?", "java.util",
                        "java.lang", "java.awt", "java.io", 1, "Java", "Difficile"));
                questions.add(new Question("j_d_5", "Which method keyword is used to refer to the current object?",
                        "this", "super", "abstract", "catch", 1, "Java", "Difficile"));
                questions.add(new Question("j_d_6", "What is the use of the transient keyword?",
                        "to make variable static", "to make variable final", "to prevent serialization",
                        "to prevent overriding", 3, "Java", "Difficile"));
                questions.add(new Question("j_d_7", "Which of the following is not an OOPS concept in Java?",
                        "Polymorphism", "Inheritance", "Compilation", "Encapsulation", 3, "Java", "Difficile"));
                questions.add(new Question("j_d_8",
                        "If we put a return statement in a try block with a finally block, what happens?", "Error",
                        "finally executes after try", "finally is skipped", "immediate termination", 2, "Java",
                        "Difficile"));

                // Java Hard Code Challenge
                String javaCodeD = "Runnable r = () -> System.out.print(\"Go \");\n" +
                        "Thread t = new Thread(r);\n" +
                        "t.start();";
                questions.add(new Question("j_c_d", "What is the result when this runs successfully?\n\n" + javaCodeD,
                        "Go", "Go ", "Nothing", "Compilation Error", 2, "Java", "Difficile", true));

                String javaCodeD2 = "try {\n" +
                        "   int a = 5/0;\n" +
                        "} catch (Exception e) {\n" +
                        "   System.out.print(\"A\");\n" +
                        "} finally {\n" +
                        "   System.out.print(\"B\");\n" +
                        "}";
                questions.add(new Question("j_c_d2", "What will be printed to the console?\n\n" + javaCodeD2, "A", "B",
                        "AB", "Error", 3, "Java", "Difficile", true));

                String javaCodeD3 = "List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));\n" +
                        "list.remove(1);\n" +
                        "System.out.println(list);";
                questions.add(new Question("j_c_d3", "What is the resulting output?\n\n" + javaCodeD3, "[1, 2, 3]",
                        "[1, 3]", "[2, 3]", "[1, 2]", 2, "Java", "Difficile", true));

                String javaCodeD4 = "Integer a = 100, b = 100;\n" +
                        "Integer c = 200, d = 200;\n" +
                        "System.out.print((a == b) + \" \" + (c == d));";
                questions.add(new Question("j_c_d4", "What is the output? (Integer caching)\n\n" + javaCodeD4,
                        "true true", "false false", "true false", "false true", 3, "Java", "Difficile", true));

                questions.add(new Question("j_c_d5", "What is the result?\n\ntry {\n  return 1;\n} finally {\n  return 2;\n}",
                        "1", "2", "Compilation Error", "1 then 2", 2, "Java", "Difficile", true));

                questions.add(new Question("j_c_d6", "What is the output?\n\nThread t = new Thread(() -> System.out.print(\"A\"));\nt.run();",
                        "A", "Nothing", "Exception", "Depends on OS", 1, "Java", "Difficile", true));

                questions.add(new Question("j_c_d7", "What happens?\n\nSet<Integer> set = new TreeSet<>();\nset.add(null);",
                        "Adds null", "Skips null", "NullPointerException", "Compilation Error", 3, "Java", "Difficile", true));

                questions.add(new Question("j_c_d8", "What is the output?\n\nSystem.out.println(1.0 / 0.0);",
                        "ArithmeticException", "Infinity", "NaN", "0.0", 2, "Java", "Difficile", true));

                // --------- PYTHON ---------
                // Python Easy
                questions.add(new Question("p_e_1", "What is the maximum possible length of an identifier in Python?",
                        "79 characters", "31 characters", "63 characters", "None of these", 4, "Python", "Facile"));
                questions.add(new Question("p_e_2", "Who developed Python Programming Language?", "Wick van Rossum",
                        "Rasmus Lerdorf", "Guido van Rossum", "Niene Stom", 3, "Python", "Facile"));
                questions.add(new Question("p_e_3", "What is the correct file extension for Python files?", ".pyth",
                        ".pt", ".py", ".pyt", 3, "Python", "Facile"));
                questions
                        .add(new Question("p_e_4", "Which method can be used to return a string in upper case letters?",
                                "upperCase()", "toUpperCase()", "upper()", "uppercase()", 3, "Python", "Facile"));
                questions.add(new Question("p_e_5", "What does len() do?", "Gets the length", "Gets the last element",
                        "Finds max", "Finds min", 1, "Python", "Facile"));
                questions.add(new Question("p_e_6", "Which of the following is a mutable data type?", "Tuple", "String",
                        "List", "Integer", 3, "Python", "Facile"));
                questions.add(new Question("p_e_7",
                        "How do you create a variable with a numeric floating-point value 2.8?", "x = 2.8",
                        "x = float(2.8)", "Both are correct", "x = double(2.8)", 3, "Python", "Facile"));
                questions.add(new Question("p_e_8", "Which of these is a valid Python data structure?", "List", "Array",
                        "Vector", "Sequence", 1, "Python", "Facile"));

                // Python Easy Code Challenge
                String pyCodeE = "x = \"Hello\"\n" +
                        "print(x[0])";
                questions.add(new Question("p_c_e", "What does this code print?\n\n" + pyCodeE, "H", "e", "Hello",
                        "Error", 1, "Python", "Facile", true));

                String pyCodeE2 = "x = 5\n" +
                        "y = \"10\"\n" +
                        "print(x + y)";
                questions.add(new Question("p_c_e2", "What does this code print?\n\n" + pyCodeE2, "15", "510",
                        "TypeError", "105", 3, "Python", "Facile", true));

                String pyCodeE3 = "colors = [\"red\", \"blue\"]\n" +
                        "colors.append(\"green\")\n" +
                        "print(len(colors))";
                questions.add(new Question("p_c_e3", "What does this code print?\n\n" + pyCodeE3, "1", "2", "3",
                        "Error", 3, "Python", "Facile", true));

                String pyCodeE4 = "print(\"a\" * 3)";
                questions.add(new Question("p_c_e4", "What is the output?\n\n" + pyCodeE4, "a3", "aaa", "a,a,a",
                        "Error", 2, "Python", "Facile", true));

                questions.add(new Question("p_c_e5", "What is the output?\n\nprint(2 + 2 * 2)", "8", "6", "4", "Error", 2,
                        "Python", "Facile", true));

                questions.add(new Question("p_c_e6", "What is the output?\n\nprint(\"Python\"[::-1])", "P", "nohtyP",
                        "nohty", "Error", 2, "Python", "Facile", true));

                questions.add(new Question("p_c_e7", "What is the value of x?\n\nx = 1; y = 2; x, y = y, x", "1", "2",
                        "None", "Error", 2, "Python", "Facile", true));

                // Python Medium
                questions.add(new Question("p_m_1", "What is the correct syntax to output 'Hello World' in Python?",
                        "echo 'Hello World'", "p('Hello World')", "printf('Hello World')", "print('Hello World')", 4,
                        "Python", "Moyen"));
                questions.add(new Question("p_m_2", "How do you insert COMMENTS in Python code?",
                        "/*This is a comment*/", "//This is a comment", "#This is a comment",
                        "<!--This is a comment-->", 3, "Python", "Moyen"));
                questions.add(new Question("p_m_3", "Which one is NOT a legal variable name?", "my_var", "_myvar",
                        "my-var", "myvar", 3, "Python", "Moyen"));
                questions.add(new Question("p_m_4", "How do you create a variable with the numeric value 5?",
                        "x = int(5)", "x = 5", "Both are correct", "None", 3, "Python", "Moyen"));
                questions.add(new Question("p_m_5", "How do you create a function in Python?", "function myFunc():",
                        "def myFunc():", "create myFunc():", "void myFunc():", 2, "Python", "Moyen"));
                questions.add(new Question("p_m_6", "How to get the data type of an object?", "type(obj)",
                        "typeof(obj)", "getType(obj)", "datatype(obj)", 1, "Python", "Moyen"));
                questions.add(
                        new Question("p_m_7", "Which method removes whitespace from beginning and end of a string?",
                                "trim()", "strip()", "ptrim()", "len()", 2, "Python", "Moyen"));
                questions.add(new Question("p_m_8", "Which collection is ordered, changeable, and allows duplicates?",
                        "List", "Tuple", "Dictionary", "Set", 1, "Python", "Moyen"));

                // Python Medium Code Challenge
                String pyCode1 = "def myFunc(x):\n" +
                        "    return x * 2\n" +
                        "print(myFunc(3))";
                questions.add(new Question("p_c_1", "What is the output of this Python code?\n\n" + pyCode1, "3", "6",
                        "x * 2", "Error", 2, "Python", "Moyen", true));

                String pyCode2 = "d = {\"a\":1, \"b\":2}\n" +
                        "print(\"a\" in d)";
                questions.add(new Question("p_c_m2", "What is the output?\n\n" + pyCode2, "True", "False", "1", "Error",
                        1, "Python", "Moyen", true));

                String pyCode3 = "for i in range(3):\n" +
                        "  if i == 1:\n" +
                        "    continue\n" +
                        "  print(i, end=\"\")";
                questions.add(new Question("p_c_m3", "What is the output?\n\n" + pyCode3, "012", "12", "02", "0,1,2", 3,
                        "Python", "Moyen", true));

                String pyCodeM4 = "x = [1, 2, 3]\n" +
                        "y = x.copy()\n" +
                        "y.append(4)\n" +
                        "print(len(x))";
                questions.add(new Question("p_c_m4", "What is the output?\n\n" + pyCodeM4, "3", "4", "2", "Error", 1,
                        "Python", "Moyen", true));

                questions.add(new Question("p_c_m5", "What is the output?\n\nprint([i for i in range(5) if i % 2 == 0])",
                        "[0, 2, 4]", "[2, 4]", "[0, 1, 2, 3, 4]", "[0, 2]", 1, "Python", "Moyen", true));

                questions.add(new Question("p_c_m6", "What is the output?\n\nd = {1: \"A\", 2: \"B\"}\nprint(d.get(3, \"C\"))",
                        "None", "Error", "C", "KeyError", 3, "Python", "Moyen", true));

                questions.add(new Question("p_c_m7", "What is len(l1)?\n\nl1 = [1, 2]; l2 = l1 + [3]", "2", "3", "1",
                        "Error", 1, "Python", "Moyen", true));

                // Python Hard
                questions.add(new Question("p_d_1", "What is the output of print(2 ** 3)?", "6", "8", "9", "5", 2,
                        "Python", "Difficile"));
                questions.add(new Question("p_d_2", "Which of these collections defines a LIST?",
                        "{\"name\": \"apple\"}", "(\"apple\", \"banana\")", "[\"apple\", \"banana\"]",
                        "{\"apple\", \"banana\"}", 3, "Python", "Difficile"));
                questions.add(new Question("p_d_3", "How do you start writing a while loop in Python?", "while (x > y)",
                        "while x > y:", "while x > y {", "x > y while {", 2, "Python", "Difficile"));
                questions.add(new Question("p_d_4", "Which statement is used to stop a loop?", "stop", "exit", "return",
                        "break", 4, "Python", "Difficile"));
                questions.add(new Question("p_d_5", "What is polymorphism in Python?", "Data hiding",
                        "Multi-form functions", "Encapsulation", "Multiple inheritance", 2, "Python", "Difficile"));
                questions.add(new Question("p_d_6", "Which statement is used to create an empty class?", "pass",
                        "empty", "void", "null", 1, "Python", "Difficile"));
                questions.add(new Question("p_d_7", "What is a lambda function in Python?", "Built-in function",
                        "Anonymous function", "Recursive function", "Decorator", 2, "Python", "Difficile"));
                questions.add(new Question("p_d_8", "What does the __init__ method do in Python?", "Initializes a list",
                        "Acts as a destructor", "Acts as a constructor", "Imports a module", 3, "Python", "Difficile"));

                // Python Hard Code Challenge
                String pyCodeD = "def x():\n" +
                        "   yield 1\n\n" +
                        "print(next(x()))";
                questions.add(new Question("p_c_d", "What is the output?\n\n" + pyCodeD, "None", "1",
                        "Generator Object", "Error", 2, "Python", "Difficile", true));

                String pyCodeD2 = "x = [1, 2, 3]\n" +
                        "y = x\n" +
                        "y.append(4)\n" +
                        "print(x)";
                questions.add(new Question("p_c_d2", "What is the output?\n\n" + pyCodeD2, "[1, 2, 3]", "[4, 1, 2, 3]",
                        "[1, 2, 3, 4]", "TypeError", 3, "Python", "Difficile", true));

                String pyCodeD3 = "print(bool(\"False\"))";
                questions.add(new Question("p_c_d3", "What is the output?\n\n" + pyCodeD3, "False", "True", "None",
                        "TypeError", 2, "Python", "Difficile", true));

                String pyCodeD4 = "print(1 and 2)";
                questions.add(new Question("p_c_d4", "What is the output?\n\n" + pyCodeD4, "True", "1", "2", "False", 3,
                        "Python", "Difficile", true));

                String pyCodeD5 = "print(type(lambda x: x))";
                questions.add(new Question("p_c_d5", "What is the output?\n\n" + pyCodeD5, "<class 'function'>",
                        "<class 'lambda'>", "<class 'object'>", "Error", 1, "Python", "Difficile", true));

                String pyCodeD6 = "def f(a, b=[]):\n" +
                        "    b.append(a)\n" +
                        "    return b\n" +
                        "print(f(1), f(2))";
                questions.add(new Question("p_c_d6", "What is the output? (Mutable default arguments)\n\n" + pyCodeD6,
                        "[1] [2]", "[1, 2] [1, 2]", "[1] [1, 2]", "Error", 2, "Python", "Difficile", true));

                questions.add(new Question("p_c_d7", "What is the output?\n\nprint(round(0.5) - round(-0.5))", "1", "0",
                        "-1", "Error", 2, "Python", "Difficile", true));

                questions.add(new Question("p_c_d8", "What is the output?\n\ndef f(x=[]):\n  x.append(1)\n  return x\nf()\nprint(f())",
                        "[1]", "[1, 1]", "[]", "Error", 2, "Python", "Difficile", true));

                questions.add(new Question("p_c_d9", "What is the type?\n\nprint(type(1 / 1))", "int", "float", "number",
                        "Error", 2, "Python", "Difficile", true));

                questions.add(new Question("p_c_d10", "What is the value of b?\n\na = [1, 2, 3]; b = a; a = [4, 5, 6]",
                        "[1, 2, 3]", "[4, 5, 6]", "None", "Error", 1, "Python", "Difficile", true));

                // --------- WEB ---------
                // Web Easy
                questions.add(new Question("w_e_1", "What does HTML stand for?", "Hyper Text Markup Language",
                        "Home Tool Markup Language", "Hyperlinks and Text Markup Language",
                        "Hyper Tool Markup Language", 1, "Web", "Facile"));
                questions.add(new Question("w_e_2", "Choose the correct HTML element for the largest heading:",
                        "<heading>", "<h6>", "<head>", "<h1>", 4, "Web", "Facile"));
                questions.add(new Question("w_e_3", "What is the correct HTML element for inserting a line break?",
                        "<br>", "<break>", "<lb>", "<b>", 1, "Web", "Facile"));
                questions.add(new Question("w_e_4", "What does CSS stand for?", "Cascading Style Sheets",
                        "Creative Style Sheets", "Computer Style Sheets", "Colorful Style Sheets", 1, "Web", "Facile"));
                questions.add(new Question("w_e_5", "Correct HTML element for inserting an image?",
                        "<img alt=\"image\">", "<image src=\"image.gif\">", "<img src=\"image.gif\" alt=\"img\">",
                        "<picture src=\"image.gif\">", 3, "Web", "Facile"));
                questions.add(new Question("w_e_6", "How to write a comment in HTML?", "// comment", "<!-- comment -->",
                        "/* comment */", "# comment", 2, "Web", "Facile"));
                questions.add(new Question("w_e_7", "Choose the correct HTML element to define important text.",
                        "<strong>", "<b>", "<i>", "<important>", 1, "Web", "Facile"));
                questions.add(new Question("w_e_8", "Which CSS property is used to change the background color?",
                        "bgcolor", "color", "background-color", "bg-color", 3, "Web", "Facile"));

                // Web Easy Code Challenge
                String webCodeE = "<p id=\"wow\">Hello</p>\n" +
                        "<script>\n" +
                        "  document.getElementById(\"wow\").innerHTML = \"World\";\n" +
                        "</script>";
                questions.add(new Question("w_c_e", "What text appears on the screen?\n\n" + webCodeE, "Hello", "World",
                        "HelloWorld", "Nothing", 2, "Web", "Facile", true));

                String webCodeE2 = "<script>\n" +
                        "  var x = 5;\n" +
                        "  var y = 2;\n" +
                        "  console.log(x % y);\n" +
                        "</script>";
                questions.add(new Question("w_c_e2", "What prints to the console?\n\n" + webCodeE2, "2.5", "2", "1",
                        "0", 3, "Web", "Facile", true));

                String webCodeE3 = "<button onclick=\"this.innerHTML='Ouch!'\">Click</button>";
                questions.add(new Question("w_c_e3", "What happens when you click this button?\n\n" + webCodeE3,
                        "Browser crashes", "Opens alert box", "Text changes to Ouch!", "Navigates away", 3, "Web",
                        "Facile", true));

                String webCodeE4 = "console.log(2 + 2 + \"2\");";
                questions.add(new Question("w_c_e4", "What is logged to the console?\n\n" + webCodeE4, "42", "6", "222",
                        "Error", 1, "Web", "Facile", true));

                questions.add(new Question("w_c_e5", "What is the output?\n\nconsole.log(typeof NaN);", "NaN", "number",
                        "undefined", "Error", 2, "Web", "Facile", true));

                questions.add(new Question("w_c_e6", "What is the output?\n\nconsole.log(2 == \"2\");", "true", "false",
                        "TypeError", "undefined", 1, "Web", "Facile", true));

                questions.add(new Question("w_c_e7", "What is the output?\n\nconsole.log(2 === \"2\");", "true", "false",
                        "TypeError", "undefined", 2, "Web", "Facile", true));

                // Web Medium
                questions.add(new Question("w_m_1", "Which HTML attribute is used to define inline styles?", "class",
                        "style", "styles", "font", 2, "Web", "Moyen"));
                questions.add(new Question("w_m_2", "Which is the correct CSS syntax?", "body:color=black;",
                        "{body:color=black;}", "body {color: black;}", "{body;color:black;}", 3, "Web", "Moyen"));
                questions.add(new Question("w_m_3", "Inside which HTML element do we put the JavaScript?", "<js>",
                        "<scripting>", "<javascript>", "<script>", 4, "Web", "Moyen"));
                questions.add(new Question("w_m_4", "Where is the correct place to insert a JavaScript?",
                        "The <body> section", "The <head> section", "Both the <head> and <body>", "None", 3, "Web",
                        "Moyen"));
                questions.add(new Question("w_m_5", "How do you make a list with square items?", "list-type: square;",
                        "list-style-type: square;", "ul {square}", "type: square", 2, "Web", "Moyen"));
                questions.add(new Question("w_m_6", "How to select all p elements inside a div?", "div p", "div.p",
                        "div+p", "p div", 1, "Web", "Moyen"));
                questions.add(new Question("w_m_7", "How do you add a background-color targeting all <h1> elements?",
                        "all.h1 {bg-color:white;}", "h1.all {bg:white;}", "h1 {background-color:white;}",
                        "h1 {bgcolor:white;}", 3, "Web", "Moyen"));
                questions.add(new Question("w_m_8", "In JavaScript, how do you call a function named 'myFunction'?",
                        "call myFunction()", "call fn myFunction()", "myFunction()", "execute myFunction()", 3, "Web",
                        "Moyen"));

                // Web Medium Code Challenge
                String webCode1 = "<script>\n" +
                        "  let a = 10;\n" +
                        "  if (a === \"10\") {\n" +
                        "     console.log(\"Yes\");\n" +
                        "  } else {\n" +
                        "     console.log(\"No\");\n" +
                        "  }\n" +
                        "</script>";
                questions.add(new Question("w_c_1", "What is printed in the browser console?\n\n" + webCode1, "Yes",
                        "No", "Error", "Undefined", 2, "Web", "Moyen", true));

                String webCodeM2 = "let arr = [1, 2, 3];\n" +
                        "let newArr = arr.map(x => x * 2);\n" +
                        "console.log(newArr[1]);";
                questions.add(new Question("w_c_m2", "What is the output?\n\n" + webCodeM2, "2", "4", "6", "undefined",
                        2, "Web", "Moyen", true));

                String webCodeM3 = "console.log(typeof null);";
                questions.add(new Question("w_c_m3", "What does the console log?\n\n" + webCodeM3, "null", "undefined",
                        "object", "string", 3, "Web", "Moyen", true));

                String webCodeM4 = "let x = [1, 2, 3];\n" +
                        "let y = [...x];\n" +
                        "y[0] = 10;\n" +
                        "console.log(x[0]);";
                questions.add(new Question("w_c_m4", "What is the output?\n\n" + webCodeM4, "10", "1", "undefined",
                        "Error", 2, "Web", "Moyen", true));

                questions.add(new Question("w_c_m5", "What is the output?\n\nconsole.log([1, 2] + [3, 4]);", "1,2,3,4",
                        "1,23,4", "[1,2,3,4]", "Error", 2, "Web", "Moyen", true));

                questions.add(new Question("w_c_m6", "What is the output?\n\nlet x = 1;\n{ let x = 2; }\nconsole.log(x);",
                        "1", "2", "undefined", "ReferenceError", 1, "Web", "Moyen", true));

                questions.add(new Question("w_c_m7", "What is the output?\n\nconsole.log(!\"Hello\");", "true", "false",
                        "\"Hello\"", "Error", 2, "Web", "Moyen", true));

                // Web Hard
                questions.add(new Question("w_d_1",
                        "What is the correct syntax for referring to an external script called 'xxx.js'?",
                        "<script name='xxx.js'>", "<script src='xxx.js'>", "<script href='xxx.js'>",
                        "<script alt='xxx.js'>", 2, "Web", "Difficile"));
                questions.add(new Question("w_d_2", "How do you write 'Hello World' in an alert box?",
                        "alertBox('Hello World');", "msg('Hello World');", "alert('Hello World');",
                        "msgBox('Hello World');", 3, "Web", "Difficile"));
                questions.add(
                        new Question("w_d_3", "How do you create a function in JavaScript?", "function:myFunction()",
                                "function = myFunction()", "function myFunction()", "None", 3, "Web", "Difficile"));
                questions.add(new Question("w_d_4", "How to write an IF statement in JavaScript?", "if i = 5 then",
                        "if i == 5 then", "if (i == 5)", "if i = 5", 3, "Web", "Difficile"));
                questions.add(new Question("w_d_5", "How does a FOR loop start in JS?", "for (i <= 5; i++)",
                        "for (i = 0; i <= 5; i++)", "for i = 1 to 5", "for (i = 5)", 2, "Web", "Difficile"));
                questions.add(new Question("w_d_6", "Which event occurs when the user clicks?", "onchange",
                        "onmouseclick", "onclick", "onmouseover", 3, "Web", "Difficile"));
                questions.add(new Question("w_d_7", "How do you round 7.25 to the nearest integer in JS?",
                        "Math.round(7.25)", "Math.rnd(7.25)", "rnd(7.25)", "round(7.25)", 1, "Web", "Difficile"));
                questions.add(new Question("w_d_8", "Which CSS property changes the text color of an element?",
                        "fgcolor", "color", "text-color", "font-color", 2, "Web", "Difficile"));

                // Web Hard Code Challenge
                String webCodeD = "const obj = { v: 10 };\n" +
                        "Object.freeze(obj);\n" +
                        "obj.v = 20;\n" +
                        "console.log(obj.v);";
                questions.add(new Question("w_c_d", "What does the console log (non-strict mode)?\n\n" + webCodeD, "20",
                        "undefined", "10", "TypeError", 3, "Web", "Difficile", true));

                String webCodeD2 = "console.log(0.1 + 0.2 === 0.3);";
                questions.add(new Question("w_c_d2", "What is the output?\n\n" + webCodeD2, "true", "false",
                        "undefined", "TypeError", 2, "Web", "Difficile", true));

                String webCodeD3 = "setTimeout(() => console.log(1), 0);\n" +
                        "console.log(2);";
                questions.add(new Question("w_c_d3", "What is logged to the console first?\n\n" + webCodeD3, "1 then 2",
                        "2 then 1", "2 then Error", "Nothing", 2, "Web", "Difficile", true));

                String webCodeD4 = "console.log(1 + \"2\" + 3);";
                questions.add(new Question("w_c_d4", "What is the output?\n\n" + webCodeD4, "6", "123", "15", "Error",
                        2, "Web", "Difficile", true));

                questions.add(new Question("w_c_d5", "What is the output?\n\nconsole.log(0.1 + 0.2 === 0.3);", "true",
                        "false", "undefined", "Error", 2, "Web", "Difficile", true));

                questions.add(new Question("w_c_d6",
                        "What is the output?\n\n(function(){\n  var a = b = 3;\n})();\nconsole.log(typeof b);",
                        "undefined", "number", "Error", "ReferenceError", 2, "Web", "Difficile", true));

                questions.add(new Question("w_c_d7", "What is the output?\n\nconsole.log(typeof Array);", "object",
                        "function", "Array", "Error", 2, "Web", "Difficile", true));

                questions.add(new Question("w_c_d8", "What is the output?\n\nconsole.log([] == ![]);", "true", "false",
                        "TypeError", "undefined", 1, "Web", "Difficile", true));

                // --------- C++ ---------
                // C++ Easy
                questions.add(new Question("cpp_e_1", "What is the extension of C++ code files?", ".cpp", ".c", ".cxx", ".cp", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_2", "Who created C++?", "Bjarne Stroustrup", "Dennis Ritchie", "James Gosling", "Guido van Rossum", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_3", "Which operator is used for input in C++?", ">>", "<<", ">", "<", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_4", "Which operator is used for output in C++?", "<<", ">>", "<", ">", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_5", "Which of these is a correct comment in C++?", "// Comment", "/* Comment */", "Both", "# Comment", 3, "C++", "Facile"));
                questions.add(new Question("cpp_e_6", "What is the correct syntax for 'Hello World'?", "cout << \"Hello World\";", "print(\"Hello World\");", "echo \"Hello World\";", "printf(\"Hello World\");", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_7", "Which data type is used to create a variable for text?", "string", "String", "text", "txt", 1, "C++", "Facile"));
                questions.add(new Question("cpp_e_8", "How do you start a for loop in C++?", "for (int i=0; i<5; i++)", "for i from 1 to 5", "foreach (i in 5)", "for (i < 5)", 1, "C++", "Facile"));

                String cppCodeE1 = "int a = 5, b = 10;\ncout << a + b;";
                questions.add(new Question("cpp_c_e1", "What is the output?\n\n" + cppCodeE1, "15", "510", "Error", "5 10", 1, "C++", "Facile", true));

                String cppCodeE2 = "int x = 10;\nif (x > 5) cout << \"Yes\";\nelse cout << \"No\";";
                questions.add(new Question("cpp_c_e2", "What is the output?\n\n" + cppCodeE2, "Yes", "No", "10", "Error", 1, "C++", "Facile", true));

                questions.add(new Question("cpp_c_e3", "What is the output?\n\nint x = 10;\nx++;\ncout << x;", "10", "11", "12", "Error", 2, "C++", "Facile", true));

                questions.add(new Question("cpp_c_e4", "What is the output?\n\ncout << (5 > 2 && 3 < 1);", "1", "0", "true", "false", 2, "C++", "Facile", true));

                questions.add(new Question("cpp_c_e5", "What is the output?\n\nint x = 5;\nx *= 2;\ncout << x;", "5", "10", "7", "Error", 2, "C++", "Facile", true));

                questions.add(new Question("cpp_c_e6", "What is the output?\n\ncout << (true || false);", "1", "0", "true", "false", 1, "C++", "Facile", true));

                // C++ Medium
                questions.add(new Question("cpp_m_1", "What is a pointer in C++?", "A variable that stores address", "A variable that stores value", "A type of loop", "A class", 1, "C++", "Moyen"));
                questions.add(new Question("cpp_m_2", "Which header file is used for vectors?", "<vector>", "<list>", "<array>", "<set>", 1, "C++", "Moyen"));
                questions.add(new Question("cpp_m_3", "How do you access the memory address of variable x?", "&x", "*x", "x&", "addr(x)", 1, "C++", "Moyen"));
                questions.add(new Question("cpp_m_4", "Which keyword is used to define a class in C++?", "class", "struct", "Both", "None", 3, "C++", "Moyen"));
                questions.add(new Question("cpp_m_5", "What is the size of an int in C++ (usually)?", "4 bytes", "2 bytes", "8 bytes", "1 byte", 1, "C++", "Moyen"));
                questions.add(new Question("cpp_m_6", "Which of these is used to allocate memory dynamically?", "new", "malloc", "Both", "alloc", 3, "C++", "Moyen"));
                questions.add(new Question("cpp_m_7", "Which visibility modifier is default for a class?", "private", "public", "protected", "none", 1, "C++", "Moyen"));
                questions.add(new Question("cpp_m_8", "Which of these is a sequence container?", "vector", "set", "map", "unordered_map", 1, "C++", "Moyen"));

                String cppCodeM1 = "int x = 5;\nint *p = &x;\ncout << *p;";
                questions.add(new Question("cpp_c_m1", "What is the output?\n\n" + cppCodeM1, "5", "Address of x", "Error", "Pointer", 1, "C++", "Moyen", true));

                String cppCodeM2 = "vector<int> v = {1, 2};\nv.push_back(3);\ncout << v.size();";
                questions.add(new Question("cpp_c_m2", "What is the output?\n\n" + cppCodeM2, "2", "3", "1", "Error", 2, "C++", "Moyen", true));

                questions.add(new Question("cpp_c_m3", "What is the output?\n\nint a = 5;\nint &b = a;\nb = 10;\ncout << a;", "5", "10", "Address", "Error", 2, "C++", "Moyen", true));

                questions.add(new Question("cpp_c_m4", "What is the output?\n\nint x = 5;\ncout << sizeof(x++);\ncout << x;", "45", "46", "Error", "Depends on OS", 1, "C++", "Moyen", true));

                questions.add(new Question("cpp_c_m5", "What is the output?\n\nint x = 5;\nint y = x++;\ncout << y << x;", "55", "56", "66", "Error", 2, "C++", "Moyen", true));

                questions.add(new Question("cpp_c_m6", "What is the output?\n\nstring s = \"ABC\";\ns += 'D';\ncout << s.length();", "3", "4", "5", "Error", 2, "C++", "Moyen", true));

                questions.add(new Question("cpp_c_m7", "What is the output?\n\nint a[2] = {10, 20};\nint *p = a;\ncout << *(p + 1);", "10", "20", "Address", "Error", 2, "C++", "Moyen", true));

                // C++ Hard
                questions.add(new Question("cpp_d_1", "What is RAII in C++?", "Resource Acquisition Is Initialization", "Random Access Is Important", "Runtime Access Is Illegal", "None", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_2", "Which of these is a smart pointer in C++11?", "unique_ptr", "shared_ptr", "Both", "auto_ptr", 3, "C++", "Difficile"));
                questions.add(new Question("cpp_d_3", "What is a virtual function?", "Function that can be overridden", "Function that cannot be used", "A static function", "A private function", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_4", "What is the purpose of 'std::move'?", "To cast to rvalue reference", "To move a file", "To move memory", "To delete an object", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_5", "Which of these is used for multiple inheritance?", "class A : public B, public C", "class A : B + C", "class A < B, C", "None", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_6", "What is the time complexity of std::map insertion?", "O(log N)", "O(1)", "O(N)", "O(N log N)", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_7", "Which keyword is used for exception handling?", "try-catch", "if-else", "for-while", "None", 1, "C++", "Difficile"));
                questions.add(new Question("cpp_d_8", "What is a friend function?", "A function that has access to private members", "A function in another class", "A public function", "None", 1, "C++", "Difficile"));

                String cppCodeD1 = "template <typename T>\nT add(T a, T b) { return a + b; }\ncout << add(1, 2);";
                questions.add(new Question("cpp_c_d1", "What is the output?\n\n" + cppCodeD1, "3", "12", "Error", "Compilation Fail", 1, "C++", "Difficile", true));

                String cppCodeD2 = "int x = 5;\nauto f = [=](){ return x + 1; };\ncout << f();";
                questions.add(new Question("cpp_c_d2", "What is the output?\n\n" + cppCodeD2, "6", "5", "Error", "0", 1, "C++", "Difficile", true));

                String cppCodeD3 = "struct A { virtual void f() { cout << \"A\"; } };\nstruct B : A { void f() { cout << \"B\"; } };\nA* obj = new B();\nobj->f();";
                questions.add(new Question("cpp_c_d3", "What is the output? (Polymorphism)\n\n" + cppCodeD3, "A", "B", "AB", "Error", 2, "C++", "Difficile", true));

                questions.add(new Question("cpp_c_d4", "What is the output?\n\nint x = 10;\nauto f = [&](){ x = 20; };\nf();\ncout << x;", "10", "20", "Error", "0", 2, "C++", "Difficile", true));

                questions.add(new Question("cpp_c_d5", "What is the output?\n\ncout << (1 << 3);", "1", "3", "8", "4", 3, "C++", "Difficile", true));

                questions.add(new Question("cpp_c_d6", "What is the output?\n\nint x = 10;\nauto f = [x](int y){ return x + y; };\ncout << f(5);", "10", "15", "5", "Error", 2, "C++", "Difficile", true));

                questions.add(new Question("cpp_c_d7", "What is the output?\n\ncout << (10 ^ 3);", "7", "13", "9", "Error", 3, "C++", "Difficile", true));

                questions.add(new Question("cpp_c_d8", "What is the output?\n\nclass A { public: static int x; };\nint A::x = 5;\ncout << A::x;", "5", "0", "Error", "Pointer", 1, "C++", "Difficile", true));

                // --------- PHP ---------
                // PHP Easy
                questions.add(new Question("php_e_1", "How do PHP scripts start?", "<?php", "<script>", "<php", "<?", 1, "PHP", "Facile"));
                questions.add(new Question("php_e_2", "How do you write a variable in PHP?", "$var", "var var", "int var", "@var", 1, "PHP", "Facile"));
                questions.add(new Question("php_e_3", "Which operator is used for string concatenation?", ".", "+", ",", "&", 1, "PHP", "Facile"));
                questions.add(new Question("php_e_4", "Which keyword outputs text in PHP?", "echo", "print", "Both", "output", 3, "PHP", "Facile"));
                questions.add(new Question("php_e_5", "What is the extension for PHP files?", ".php", ".ph", ".html", ".p", 1, "PHP", "Facile"));
                questions.add(new Question("php_e_6", "How do you add a comment in PHP?", "//", "/* */", "#", "All of these", 4, "PHP", "Facile"));
                questions.add(new Question("php_e_7", "PHP is a ... side language.", "Server", "Client", "Both", "None", 1, "PHP", "Facile"));
                questions.add(new Question("php_e_8", "Which of these is a correct way to start a PHP statement?", "echo 'Hello';", "echo 'Hello'", "printf Hello", "print('Hello')", 1, "PHP", "Facile"));

                String phpCodeE1 = "$x = 5;\n$y = 10;\necho $x + $y;";
                questions.add(new Question("php_c_e1", "What is the output?\n\n" + phpCodeE1, "15", "510", "Error", "$x+$y", 1, "PHP", "Facile", true));

                String phpCodeE2 = "$s = \"PHP\";\necho strlen($s);";
                questions.add(new Question("php_c_e2", "What is the output?\n\n" + phpCodeE2, "3", "4", "PHP", "Error", 1, "PHP", "Facile", true));

                questions.add(new Question("php_c_e3", "What is the output?\n\n$x = \"5\";\necho $x . 5;", "55", "10", "Error", "5.5", 1, "PHP", "Facile", true));

                questions.add(new Question("php_c_e4", "What is the output?\n\n$a = true;\n$b = false;\necho ($a || $b);", "1", "true", "0", "false", 1, "PHP", "Facile", true));

                questions.add(new Question("php_c_e5", "What is the output?\n\n$x = 10;\n$y = \"10\";\necho ($x == $y);", "1", "0", "true", "false", 1, "PHP", "Facile", true));

                questions.add(new Question("php_c_e6", "What is the output?\n\n$a = \"Hello\";\necho \"{$a} World\";", "Hello World", "{$a} World", "Error", "Hello", 1, "PHP", "Facile", true));

                // PHP Medium
                questions.add(new Question("php_m_1", "What are associative arrays in PHP?", "Arrays with named keys", "Arrays with numeric keys", "Fixed size arrays", "None", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_2", "Which superglobal is used to collect form data (POST)?", "$_POST", "$_GET", "$_REQUEST", "$_SESSION", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_3", "What is the difference between include and require?", "require stops on error, include doesn't", "include stops on error, require doesn't", "No difference", "None", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_4", "How do you start a session in PHP?", "session_start()", "start_session()", "session_begin()", "new session()", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_5", "Which function counts elements in an array?", "count()", "size()", "len()", "length()", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_6", "How do you define a constant in PHP?", "define()", "const", "Both", "None", 3, "PHP", "Moyen"));
                questions.add(new Question("php_m_7", "Which function is used to send a cookie?", "setcookie()", "cookie()", "new cookie()", "sendcookie()", 1, "PHP", "Moyen"));
                questions.add(new Question("php_m_8", "What does 'unset()' do?", "Destroys a variable", "Creates a variable", "Sets variable to null", "None", 1, "PHP", "Moyen"));

                String phpCodeM1 = "$a = [\"x\" => 1];\necho $a[\"x\"];";
                questions.add(new Question("php_c_m1", "What is the output?\n\n" + phpCodeM1, "1", "x", "Error", "Array", 1, "PHP", "Moyen", true));

                String phpCodeM2 = "$x = \"5\";\n$y = 5;\necho ($x == $y ? \"Yes\" : \"No\");";
                questions.add(new Question("php_c_m2", "What is the output?\n\n" + phpCodeM2, "Yes", "No", "5", "Error", 1, "PHP", "Moyen", true));

                questions.add(new Question("php_c_m3", "What is the output?\n\n$a = [1, 2];\n$b = [3, 4];\necho count($a + $b);", "2", "4", "Error", "1", 1, "PHP", "Moyen", true));

                questions.add(new Question("php_c_m4", "What is the output?\n\necho \"5\" + 5;", "10", "55", "Error", "5", 1, "PHP", "Moyen", true));

                questions.add(new Question("php_c_m5", "What is the output?\n\n$x = 5;\necho ++$x + $x++;", "10", "11", "12", "13", 3, "PHP", "Moyen", true));

                questions.add(new Question("php_c_m6", "What is the output?\n\n$a = [1];\narray_push($a, 2);\necho $a[1];", "1", "2", "Error", "null", 2, "PHP", "Moyen", true));

                questions.add(new Question("php_c_m7", "What is the output?\n\necho (false ? \"A\" : (true ? \"B\" : \"C\"));", "A", "B", "C", "Error", 2, "PHP", "Moyen", true));

                // PHP Hard
                questions.add(new Question("php_d_1", "What are Traits in PHP?", "A mechanism for code reuse", "A type of class", "A type of interface", "None", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_2", "Which operator is used for Null Coalescing?", "??", "?:", "?", "::", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_3", "What is PDO in PHP?", "PHP Data Objects", "PHP Data Orientation", "PHP Database Option", "None", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_4", "Which keyword is used for namespaces?", "namespace", "use", "Both", "package", 3, "PHP", "Difficile"));
                questions.add(new Question("php_d_5", "What is a 'Closure' in PHP?", "An anonymous function", "A private class", "A type of loop", "None", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_6", "How do you implement an interface?", "class MyClass implements MyInterface", "class MyClass extends MyInterface", "class MyClass uses MyInterface", "None", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_7", "What is the use of the 'static' keyword in a function?", "To keep variable value across calls", "To make function public", "To stop execution", "None", 1, "PHP", "Difficile"));
                questions.add(new Question("php_d_8", "Which function is used to convert an array to JSON?", "json_encode()", "json_decode()", "to_json()", "None", 1, "PHP", "Difficile"));

                String phpCodeD1 = "$f = function($x) use (&$y) { return $x + $y; };\n$y = 10;\necho $f(5);";
                questions.add(new Question("php_c_d1", "What is the output?\n\n" + phpCodeD1, "15", "5", "Error", "10", 1, "PHP", "Difficile", true));

                String phpCodeD2 = "echo 1 <=> 2;";
                questions.add(new Question("php_c_d2", "What is the output? (Spaceship operator)\n\n" + phpCodeD2, "-1", "1", "0", "Error", 1, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d3", "What is the output?\n\n$x = 10;\nfunction f() { global $x; $x = 20; }\nf();\necho $x;", "10", "20", "Error", "0", 2, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d4", "What is the output?\n\n$x = null;\necho $x ?? \"Default\";", "null", "Default", "Error", "0", 2, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d5", "What is the output?\n\necho (1 == \"1\" && 1 === \"1\" ? \"Yes\" : \"No\");", "Yes", "No", "Error", "1", 2, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d6", "What is the output?\n\n$x = \"1\";\n$x++;\necho gettype($x);", "string", "integer", "double", "Error", 2, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d7", "What is the output?\n\necho (012);", "12", "10", "0", "Error", 2, "PHP", "Difficile", true));

                questions.add(new Question("php_c_d8", "What is the output?\n\n$a = \"b\";\n$b = \"c\";\necho $$a;", "b", "c", "Error", "a", 2, "PHP", "Difficile", true));

                for (Question q : questions) {
                    dbManager.getDb().collection("questions").document(q.getId()).set(q);
                }
                Log.d("DatabaseSeeder", "Seeding complete with full dataset.");
            } else {
                Log.d("DatabaseSeeder", "Questions already exist.");
            }
        });
    }
}
