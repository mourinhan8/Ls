import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LsTest {
    @Test
    public void ls() throws IOException {
        List<String> work = new ArrayList<>();
        work.add("Directory1");

        work.add("Directory2");

        work.add("Directory3");

        work.add("File");

        work.add("File1");

        Ls st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(false, false, false));
        Collections.reverse(work);
        assertEquals(work, st.ls(false, false, true));
        work.clear();
        work.add("Directory");
        work.add("File");
        st = new Ls(new File("Directory/Directory2"));
        assertEquals(work, st.ls(false, false, false));
        StringBuilder s = new StringBuilder();
        work.clear();
        s.append("Directory1 111" + String.format("%12s", "19717") + " 21.03.2018 14:26:08");
        work.add(s.toString());

        StringBuilder s1 = new StringBuilder();
        s1.append("Directory2 111" + String.format("%12s", "52319") + " 21.03.2018 14:25:55");
        work.add(s1.toString());

        StringBuilder s2 = new StringBuilder();
        s2.append("Directory3 111" + String.format("%12s", "45323") + " 21.03.2018 14:25:50");
        work.add(s2.toString());

        StringBuilder s3 = new StringBuilder();
        s3.append("File 111" + String.format("%12s", "17011") + " 20.03.2018 23:23:45");
        work.add(s3.toString());

        StringBuilder s4 = new StringBuilder();
        s4.append("File1 111" + String.format("%12s", "13737") + " 20.03.2018 23:23:45");
        work.add(s4.toString());
        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, false, false));
        work.clear();
        work.add(s3.toString());
        st = new Ls(new File("Directory/File"));
        assertEquals(work, st.ls(true, false, false));

        work.clear();
        StringBuilder s5 = new StringBuilder();
        s5.append("Directory1 rwx" + String.format("%8s", "19.3 Kb") + " 21.03.2018 14:26:08");
        work.add(s5.toString());

        StringBuilder s6 = new StringBuilder();
        s6.append("Directory2 rwx" + String.format("%8s", "51.1 Kb") + " 21.03.2018 14:25:55");
        work.add(s6.toString());

        StringBuilder s7 = new StringBuilder();
        s7.append("Directory3 rwx" + String.format("%8s", "44.3 Kb") + " 21.03.2018 14:25:50" );
        work.add(s7.toString());

        StringBuilder s8 = new StringBuilder();
        s8.append("File rwx" + String.format("%8s", "16.6 Kb") + " 20.03.2018 23:23:45");
        work.add(s8.toString());

        StringBuilder s9 = new StringBuilder();
        s9.append("File1 rwx" + String.format("%8s", "13.4 Kb") + " 20.03.2018 23:23:45");
        work.add(s9.toString());

        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, true, false));
        Collections.reverse(work);
        assertEquals(work, st.ls(true, true, true));
        work.clear();
        work.add(s9.toString());
        st = new Ls(new File("Directory/File1"));
        assertEquals(work, st.ls(true, true, false));
    }
    @Test
    public void output() throws Exception{
        Ls st = new Ls(new File("Directory"));
        st.output("output.txt", st.ls(true, true, false));
        List<String> work = new ArrayList();
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
