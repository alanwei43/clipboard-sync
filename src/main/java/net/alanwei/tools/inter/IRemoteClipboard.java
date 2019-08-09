package net.alanwei.tools.inter;

import net.alanwei.tools.models.LocalClipboardData;

import java.util.function.Function;

public interface IRemoteClipboard {
    void subscribe(Function<LocalClipboardData, Integer> subscriber);

    void push(LocalClipboardData data);
}
