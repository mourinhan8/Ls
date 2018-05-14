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
        s.append("Directory1 111").append(String.format("%12s", "19717")).append(" 21.03.2018 14:26:08");
        work.add(s.toString());

        work.add("Directory2 111" + String.format("%12s", "52319") + " 21.03.2018 14:25:55");

        work.add("Directory3 111" + String.format("%12s", "45323") + " 21.03.2018 14:25:50");

        StringBuilder s3 = new StringBuilder();
        s3.append("File 111").append(String.format("%12s", "17011")).append(" 20.03.2018 23:23:45");
        work.add(s3.toString());

        work.add("File1 111" + String.format("%12s", "13737") + " 20.03.2018 23:23:45");
        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, false, false));
        work.clear();
        work.add(s3.toString());
        st = new Ls(new File("Directory/File"));
        assertEquals(work, st.ls(true, false, false));

        work.clear();
        work.add("Directory1 rwx" + String.format("%8s", "19.3 Kb") + " 21.03.2018 14:26:08");

        work.add("Directory2 rwx" + String.format("%8s", "51.1 Kb") + " 21.03.2018 14:25:55");

        work.add("Directory3 rwx" + String.format("%8s", "44.3 Kb") + " 21.03.2018 14:25:50");

        work.add("File rwx" + String.format("%8s", "16.6 Kb") + " 20.03.2018 23:23:45");

        work.add("File1 rwx" + String.format("%8s", "13.4 Kb") + " 20.03.2018 23:23:45");

        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(true, true, false));
        Collections.reverse(work);
        assertEquals(work, st.ls(true, true, true));
        work.clear();
        work.add("File1 rwx" + String.format("%8s", "13.4 Kb") + " 20.03.2018 23:23:45");
        st = new Ls(new File("Directory/File1"));
        assertEquals(work, st.ls(true, true, false));

        work.clear();
        work.add("Directory1");
        work.add("Directory2");
        work.add("Directory3");
        work.add("File");
        work.add("File1");
        st = new Ls(new File("Directory"));
        assertEquals(work, st.ls(false, true, false));

        work.clear();

        work.add("File");
        st = new Ls(new File("Directory\\File"));
        assertEquals(work, st.ls(false, true, false));

    }

    @Test
    public void Data() throws IOException {
        Ls st = new Ls(new File("Directory"));
        assertEquals("Directory1\nDirectory2\nDirectory3\nFile\nFile1",
                st.Data(st.ls(false, false, false)));
        assertEquals("File1\nFile\nDirectory3\nDirectory2\nDirectory1",
                st.Data(st.ls(false, false, true)));
        assertEquals("Directory1\nDirectory2\nDirectory3\nFile\nFile1",
                st.Data(st.ls(false, true, false)));
        assertEquals("File1 111       13737 20.03.2018 23:23:45\n" +
                        "File 111       17011 20.03.2018 23:23:45\n" +
                        "Directory3 111       45323 21.03.2018 14:25:50\n" +
                        "Directory2 111       52319 21.03.2018 14:25:55\n" +
                        "Directory1 111       19717 21.03.2018 14:26:08",
                st.Data(st.ls(true, false, true)));
        assertEquals("Directory1 rwx 19.3 Kb 21.03.2018 14:26:08\n" +
                "Directory2 rwx 51.1 Kb 21.03.2018 14:25:55\n" +
                "Directory3 rwx 44.3 Kb 21.03.2018 14:25:50\n" +
                "File rwx 16.6 Kb 20.03.2018 23:23:45\n" +
                "File1 rwx 13.4 Kb 20.03.2018 23:23:45", st.Data(st.ls(true, true, false)));
    }

    @Test
    public void output() throws Exception {
        Ls st = new Ls(new File("Directory"));
        st.output("output.txt", st.ls(true, true, false));
        StringBuilder str = new StringBuilder();
        FileReader fr = new FileReader("output.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            str.append(line);
            str.append("\n");
        }
        assertEquals("Directory1 rwx 19.3 Kb 21.03.2018 14:26:08\n" +
                "Directory2 rwx 51.1 Kb 21.03.2018 14:25:55\n" +
                "Directory3 rwx 44.3 Kb 21.03.2018 14:25:50\n" +
                "File rwx 16.6 Kb 20.03.2018 23:23:45\n"+
                "File1 rwx 13.4 Kb 20.03.2018 23:23:45\n", str.toString());
        Ls tb = new Ls(new File("Directory"));
        tb.output("output.txt", tb.ls(false, true, false));
        StringBuilder str1 = new StringBuilder();
        FileReader fr1 = new FileReader("output.txt");
        BufferedReader br1 = new BufferedReader(fr1);
        String line1;
        while ((line1 = br1.readLine()) != null) {
            str1.append(line1);
            str1.append("\n");
        }
        assertEquals("Directory1\nDirectory2\nDirectory3\nFile\nFile1\n", str1.toString());
        Ls ts = new Ls(new File("Directory"));
        ts.output("output.txt", ts.ls(true, false, true));
        StringBuilder str2 = new StringBuilder();
        FileReader fr2 = new FileReader("output.txt");
        BufferedReader br2 = new BufferedReader(fr2);
        String line2;
        while ((line2 = br2.readLine()) != null) {
            str2.append(line2);
            str2.append("\n");
        }
        assertEquals("File1 111       13737 20.03.2018 23:23:45\n" +
                "File 111       17011 20.03.2018 23:23:45\n" +
                "Directory3 111       45323 21.03.2018 14:25:50\n" +
                "Directory2 111       52319 21.03.2018 14:25:55\n" +
                "Directory1 111       19717 21.03.2018 14:26:08\n"
                , str2.toString());

        Ls tn = new Ls(new File("Directory\\Directory3"));
        tn.output("output.txt", tn.ls(true, true, false));
        StringBuilder str3 = new StringBuilder();
        FileReader fr3 = new FileReader("output.txt");
        BufferedReader br3 = new BufferedReader(fr3);
        String line3;
        while ((line3 = br3.readLine()) != null) {
            str3.append(line3);
            str3.append("\n");
        }
        assertEquals("Directory rwx 25.9 Kb 21.03.2018 01:45:36\n" +
                "File rwx 18.3 Kb 20.03.2018 23:23:45\n", str3.toString());
    }
}
