package net.alanwei.tools.impl;

import net.alanwei.tools.inter.IClipboard;
import net.alanwei.tools.models.ClipboardResult;
import net.alanwei.tools.models.ClipboardType;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.nio.charset.StandardCharsets;

public class SystemClipboard implements IClipboard {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Override
    public ClipboardResult get() {
        try {
            Transferable transferable = this.clipboard.getContents(null);
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                return ClipboardResult.init(value);
            }
            return ClipboardResult.invalid();
        } catch (Throwable ex) {
            return ClipboardResult.invalid();
        }
    }

    @Override
    public boolean set(ClipboardResult result) {
        if (result.getType().equals(ClipboardType.String)) {
            StringSelection data = new StringSelection(new String(result.getData(), StandardCharsets.UTF_8));
            this.clipboard.setContents(data, data);
            return true;
        }
        return false;
    }
}
