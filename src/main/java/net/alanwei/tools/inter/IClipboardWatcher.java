package net.alanwei.tools.inter;

import net.alanwei.tools.models.LocalClipboardData;

import java.util.function.Function;

public interface IClipboardWatcher {
    void watch(Function<LocalClipboardData, Integer> callback);
}
