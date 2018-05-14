import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ls {
    private List<File> work;

    Ls(File file) {
        work = new ArrayList<>();
        File[] list = file.listFiles();
        if (file.isDirectory()) {
            if (list == null) throw new AssertionError();
            Collections.addAll(this.work, list);
        } else if (file.isFile()) this.work.add(file);
        Collections.sort(work);
    }

    private String isRWX(File file) {
        String res = "";
        if (file.canRead()) res += 'r';
        else res += '-';
        if (file.canWrite()) res += 'w';
        else res += '-';
        if (file.canExecute()) res += 'x';
        else res += '-';
        return res;
    }

    private String rwxToBit(File file) {
        String res = "";
        if (file.canRead()) res += '1';
        else res += '0';
        if (file.canWrite()) res += '1';
        else res += '0';
        if (file.canExecute()) res += '1';
        else res += '0';
        return res;
    }

    private String toNormal(long s) {
        StringBuilder size = new StringBuilder();
        Formatter fmt = new Formatter(size);
        if (s > 1024 * 1024 * 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Gb", (float) s / (1024 * 1024 * 1024));
        } else if (s > 1024 * 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Mb", (float) s / (1024 * 1024 * 1024));
        } else if (s > 1024) {
            fmt.format(Locale.ENGLISH, "%.1f Kb", (float) s / 1024);
        } else fmt.format(Locale.ENGLISH, "%.1f b", (float) s);
        return size.toString();
    }

    private long size(File file) {
        long s = 0;
        if (file.isFile()) s = file.length();
        else {
            for (File b : file.listFiles()) {
                if (file.isFile()) s += b.length();
                else s += size(b);
            }
        }
        return s;
    }

    List<String> ls(boolean l, boolean h, boolean r) throws IOException {
        List<String> res = new ArrayList<>();
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        for (File i : work) {
            StringBuilder s = new StringBuilder();
            Formatter fmt = new Formatter(s);
            s.append(i.getName());
            if (l && h) {
                fmt.format(" " + isRWX(i) + "%8s " + sim.format(i.lastModified()), toNormal(size(i)));
            } else if (l) {
                fmt.format(" " + rwxToBit(i) + "%12s " + sim.format(i.lastModified()), size(i));
            }
            res.add(s.toString());
        }
        if (r) Collections.reverse(res);
        return res;
    }

    String Data(List<String> list) {
        int n = list.size();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < n; i++) {
            res.append(list.get(i));
            if (i < n - 1)
                res.append("\n");
        }
        return res.toString();
    }

    void output(String o, List<String> list) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(o);
        fileOutputStream.write(Data(list).getBytes());
    }


    public static void main(String[] args) throws Exception {
        Ls st = new Ls(new File(args[args.length - 1]));
        List<String> list = Arrays.asList(args);
        if (args[0].compareTo("ls") == 0) {
            boolean l = list.contains("-l");
            boolean h = list.contains("-h");
            boolean r = list.contains("-r");
            List<String> res = st.ls(l, h, r);
            if (!list.contains("-o")) {
                System.out.print(st.Data(res));
            } else {
                if (list.size() == list.indexOf("-o") + 3) {
                    String name = list.get(list.indexOf("-o") + 1);
                    st.output(name, st.ls(l, h, r));
                    System.out.print(name);
                }
                st.output("output", st.ls(l, h, r));
                System.out.println("output");
            }
        } else {
            throw new AssertionError();
        }
    }
}