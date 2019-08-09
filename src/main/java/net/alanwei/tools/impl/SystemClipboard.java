package net.alanwei.tools.impl;

import net.alanwei.tools.inter.IClipboard;
import net.alanwei.tools.models.ClipboardResult;

public class SystemClipboard implements IClipboard {
    @Override
    public ClipboardResult get() {
        return null;
    }

    @Override
    public boolean set() {
        return false;
    }
}
