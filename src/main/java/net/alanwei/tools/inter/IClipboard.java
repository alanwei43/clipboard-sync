package net.alanwei.tools.inter;

import net.alanwei.tools.models.ClipboardResult;

public interface IClipboard {
    ClipboardResult get();
    boolean set(ClipboardResult result);
}
