package net.alanwei.tools.inter;

import net.alanwei.tools.models.LocalClipboardData;

public interface ILocalClipboard {
    LocalClipboardData get();
    boolean set(LocalClipboardData result);
}
