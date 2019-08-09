package net.alanwei.tools;

import net.alanwei.tools.inter.IClipboardWatcher;

import java.util.Objects;

/**
 * Hello world!
 */

public class App {

    public static void main(String[] args) throws Throwable {
        IClipboardWatcher watcher = Configurations.getContext().getBean(IClipboardWatcher.class);
        watcher.watch(r -> {
            System.out.println(r.getStringData());
            if (Objects.equals(r.getStringData(), "exit")) {
                return 0;
            }
            return 1;
        });
    }
}
