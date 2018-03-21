import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LsTest {
    @Test
    public void ls() throws IOException {
        List<List<String>> work = new ArrayList<>();
        List<String> s = new ArrayList<>();
        s.add("Directory1");
        work.add(s);

        List<String> s1 = new ArrayList<>();
        s1.add("Directory2");
        work.add(s1);

        List<String> s2 = new ArrayList<>();
        s2.add("Directory3");
        work.add(s2);

        List<String> s3 = new ArrayList<>();
        s3.add("File");
        work.add(s3);

        List<String> s4 = new ArrayList<>();
        s4.add("File1");
        work.add(s4);

        Ls st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(false, false, false));
        Collections.reverse(work);
        assertEquals(work, st.ls(false, false, true));
        work.clear();
        List<String> s5 = new ArrayList<>();
        s5.add("Directory");
        work.add(s5);
        List<String> s6 = new ArrayList<>();
        s6.add("File");
        work.add(s6);
        st = new Ls(new File("Directory/Directory2"));
        assertEquals(work, st.ls(false, false, false));

        work.clear();
        s.clear();
        s.add("Directory1");
        s.add("111");
        s.add(String.format("%12s", "19717"));
        s.add("21.03.2018 14:26:08");
        work.add(s);

        s1.clear();
        s1.add("Directory2");
        s1.add("111");
        s1.add(String.format("%12s", "52319"));
        s1.add("21.03.2018 14:25:55");
        work.add(s1);

        s2.clear();
        s2.add("Directory3");
        s2.add("111");
        s2.add(String.format("%12s", "45323"));
        s2.add("21.03.2018 14:25:50");
        work.add(s2);

        s3.clear();
        s3.add("File");
        s3.add("111");
        s3.add(String.format("%12s", "17011"));
        s3.add("20.03.2018 23:23:45");
        work.add(s3);

        s4.clear();
        s4.add("File1");
        s4.add("111");
        s4.add(String.format("%12s", "13737"));
        s4.add("20.03.2018 23:23:45");
        work.add(s4);
        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, false, false));
        work.clear();
        work.add(s3);
        st = new Ls(new File("Directory/File"));
        assertEquals(work, st.ls(true, false, false));

        work.clear();
        s.clear();
        s.add("Directory1");
        s.add("rwx");
        s.add(String.format("%8s", "19.3 Kb"));
        s.add("21.03.2018 14:26:08");
        work.add(s);

        s1.clear();
        s1.add("Directory2");
        s1.add("rwx");
        s1.add(String.format("%8s", "51.1 Kb"));
        s1.add("21.03.2018 14:25:55");
        work.add(s1);

        s2.clear();
        s2.add("Directory3");
        s2.add("rwx");
        s2.add(String.format("%8s", "44.3 Kb"));
        s2.add("21.03.2018 14:25:50");
        work.add(s2);

        s3.clear();
        s3.add("File");
        s3.add("rwx");
        s3.add(String.format("%8s", "16.6 Kb"));
        s3.add("20.03.2018 23:23:45");
        work.add(s3);

        s4.clear();
        s4.add("File1");
        s4.add("rwx");
        s4.add(String.format("%8s", "13.4 Kb"));
        s4.add("20.03.2018 23:23:45");
        work.add(s4);

        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, true, false));
        Collections.reverse(work);
        assertEquals(work, st.ls(true, true, true));
        work.clear();
        work.add(s4);
        st = new Ls(new File("Directory/File1"));
        assertEquals(work, st.ls(true, true, false));
    }
    @Test
    public void output() throws Exception{
        Ls st = new Ls(new File("Directory"));
        st.output("output.txt", st.ls(true, true, false));
        List<List<String>> work = new ArrayList();
        work = (ArrayList) st.ls(true, true, false);
        StringBuilder str = new StringBuilder();
        FileInputStream file = new  FileInputStream(new File("output.txt"));
        int c = file.read();
        while (c != -1) {
            str.append((char) c);
            c = file.read();
        }
        assertEquals(work, st.ls(true, true, false));
    }
}
