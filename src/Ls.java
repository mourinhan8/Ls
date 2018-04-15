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
            s.append(i.getName());
            if (l && h) {
                s.append(" " + isRWX(i) + " " + String.format("%8s", toNormal(size(i)) + " " + sim.format(i.lastModified())));
            } else if (l) {
                s.append(" " + rwxToBit(i)  + String.format("%12s", size(i)) + " " + sim.format(i.lastModified()));
            } else if (h) throw new IOException("-l?");
            res.add(s.toString());
        }
        if (r) Collections.reverse(res);
        return res;
    }

    protected void output(String o, List<String> list) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(o);
        StringBuilder s = new StringBuilder();
        for (String i : list)
            s.append(i + "\n");
        fileOutputStream.write(s.toString().getBytes());
    }

    public static void main(String[] args) throws Exception {
        Ls st = new Ls(new File(args[args.length - 1]));
        List<String> list = Arrays.asList(args);
        String regex = "(.*/)*.+\\.(?i)(txt)$";
        if (args[0].compareTo("l") == 0 && args[1].compareTo("s") == 0) {
            boolean l = list.contains("-l");
            boolean h = list.contains("-h");
            boolean r = list.contains("-r");
            if (!list.contains("-o")) System.out.print(st.ls(l, h, r));
            else {
                String name = list.get(list.indexOf("-o") + 1);
                if (!name.matches(regex)) throw new IOException("You need to input correct file's name");
                else {
                    st.output(name, st.ls(l, h, r));
                    System.out.println(name);
                }
            }
        } else {
            throw new IOException("No Data, run the program again");
        }
    }
}