package net.alanwei.tools.impl;

import net.alanwei.tools.Util;
import net.alanwei.tools.inter.ILocalClipboard;
import net.alanwei.tools.models.LocalClipboardData;
import net.alanwei.tools.models.ClipboardType;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.nio.charset.StandardCharsets;

@Service
public class LocalSystemClipboard implements ILocalClipboard {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Override
    public LocalClipboardData get() {
        try {
            Transferable transferable = this.clipboard.getContents(null);
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                LocalClipboardData clipboardData = LocalClipboardData.string(value);
                return clipboardData;
            }
            return LocalClipboardData.invalid();
        } catch (Throwable ex) {
            return LocalClipboardData.invalid();
        }
    }

    @Override
    public boolean set(LocalClipboardData result) {
        Util.log("update clipboard: " + result.getStringData());
        if (result.getType().equals(ClipboardType.String)) {
            StringSelection data = new StringSelection(new String(result.getData(), StandardCharsets.UTF_8));
            this.clipboard.setContents(data, data);
            return true;
        }
        return false;
    }
}
