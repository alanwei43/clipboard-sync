package net.alanwei.tools;

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
                String currentClipboard = getClipboard();
                if (Objects.equals(previousClipboard, currentClipboard)) {
                    return;
                }
                previousClipboard = currentClipboard;
                System.out.println(currentClipboard);
            }
        };
        timer.scheduleAtFixedRate(task, 1000L, 1000L);
    }

    public static String getClipboard() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(null);
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                return value;
            }
            if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                BufferedImage data = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                System.out.println(data.getClass().getTypeName());
            }
        } catch (Throwable ex) {

        }
        return "";
    }
}
