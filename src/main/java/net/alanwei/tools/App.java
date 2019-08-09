package net.alanwei.tools;

import net.alanwei.tools.impl.SystemClipboard;
import net.alanwei.tools.inter.IClipboard;
import net.alanwei.tools.models.ClipboardResult;
import net.alanwei.tools.models.ClipboardType;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Hello world!
 */
public class App {
    static String previousClipboard = "";

    public static void main(String[] args) throws Throwable {

        Timer timer = new Timer("timer");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                IClipboard clipboard = new SystemClipboard();
                ClipboardResult result = clipboard.get();
                if (result.getType().equals(ClipboardType.String)) {
                    String current = result.getStringData();
                    if (Objects.equals(current, previousClipboard)) {
                        return;
                    }
                    previousClipboard = current;
                    System.out.println(current);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000L, 1000L);
    }
}
