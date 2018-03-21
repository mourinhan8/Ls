import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Ls {
    public List<File> work;

    public Ls(File file) {
        work = new ArrayList<>();
        File[] list = file.listFiles();
        if (file.isDirectory()) {
            if (list == null) throw new AssertionError();
            Collections.addAll(this.work, list);
        }
        else if (file.isFile()) this.work.add(file);
    }

    public String isRWX(File file) {
        String res = "";
        if (file.canRead()) res += 'r';
        else res += '-';
        if (file.canWrite()) res += 'w';
        else res += '-';
        if (file.canExecute()) res += 'x';
        else res += '-';
        return res;
    }

    public String rwxToBit(File file) {
        String res = "";
        if (file.canRead()) res += '1';
        else res += '0';
        if (file.canWrite()) res += '1';
        else res += '0';
        if (file.canExecute()) res += '1';
        else res+= '0';
        return res;
    }

    public String toNormal(long s) {
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

    public long size(File file) {
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

    public List<List<String>> ls(boolean l, boolean h, boolean r) throws IOException {
        List<List<String>> res = new ArrayList<>();
        String pattern = "dd.MM.yyyy HH:mm:ss";
        SimpleDateFormat  sim = new SimpleDateFormat(pattern);
        for (File i : work) {
            List<String> s = new ArrayList<>();
            s.add(i.getName());
            if (l && h) {
                s.add(isRWX(i));
                StringBuilder str = new StringBuilder();
                Formatter fmt = new Formatter(str);
                fmt.format("%8s", toNormal(size(i)));
                s.add(str.toString());
                s.add(sim.format(i.lastModified()));
            } else if (l) {
                s.add(rwxToBit(i));
                StringBuilder str = new StringBuilder();
                Formatter fmt = new Formatter(str);
                fmt.format("%12s", size(i));
                s.add(str.toString());
                s.add(sim.format(i.lastModified()));
            } else if (h) throw new  IOException("-l?");
            res.add(s);
        }
        if (r) Collections.reverse(res);
        return res;
    }

    public void output(String o, List<List<String>> list) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(o);
        for (List<String> i : list) {
            for (int j = 0; j < i.size(); j++) {
                fileOutputStream.write(i.get(j).getBytes());
                if (j < i.size() - 1) fileOutputStream.write(" ".getBytes());
            }
            fileOutputStream.write("\n".getBytes());
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Write your command as type: ls [-l] [-h] [-r] [-o output.file] directory_or_file");
        String cmd = sc.nextLine();
        String[] subs = cmd.split("\\s");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, subs);
        int n = list.size();
        Ls st = new Ls(new File(list.get(n - 1)));
        String regex = "(.*/)*.+\\.(?i)(txt)$";
        if (cmd.charAt(0) == 'l' && cmd.charAt(1) == 's') {
            boolean l = cmd.contains("-l");
            boolean h = cmd.contains("-h");
            boolean r = cmd.contains("-r");
            if (!cmd.contains("-o")) System.out.print(st.ls(l, h, r));
            else {
                String name = list.get(list.indexOf("-o") + 1);
                if (!name.matches(regex)) throw new IOException("You need to input file's name");
                else {
                    st.output(name, st.ls(l, h, r));

                    System.out.println(name);
                }
            }
        }
        else {
            throw new IOException("No Data, run the program again");
        }
    }
}
