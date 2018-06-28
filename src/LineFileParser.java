import java.io.*;
import java.util.ArrayList;

public class LineFileParser{

    public static ArrayList<YahooQuestion> testQuestions = new ArrayList<>();

    public static void ReadFile (String fileName)
    {
        InputStream ins = null; // raw byte-stream
        Reader r = null; // cooked reader
        BufferedReader br = null; // buffered for readLine()
        try {
            String s;
            ins = new FileInputStream(fileName);
            r = new InputStreamReader(ins, "UTF-8"); // leave charset out for default
            br = new BufferedReader(r);

                s=br.readLine();
                while (!s.equals("")) {
                    String parts[] = s.split("\t", 2);
                    testQuestions.add(new YahooQuestion(parts[1], (parts[0])));
                    s=br.readLine();
                }

        } catch (Exception e) {
            System.err.println(e.getMessage()); // handle exception
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (r != null) {
                try {
                    r.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (Throwable t) { /* ensure close happens */ }
            }
        }

    }

}
