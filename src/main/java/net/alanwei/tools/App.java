package net.alanwei.tools;

import net.alanwei.tools.inter.IClipboardWatcher;
import org.springframework.context.ApplicationContext;

import java.util.Objects;

/**
 * Hello world!
 */

public class App {
    public static void main(String[] args) {
        System.out.println("version: 0.0.1");
        ApplicationContext cxt = Configurations.getContext();
        IClipboardWatcher watcher = cxt.getBean(IClipboardWatcher.class);
        watcher.watch(r -> {
            if (Objects.equals(r.getStringData(), "exit")) {
                return 0;
            }
            return 1;
        });
    }
}
