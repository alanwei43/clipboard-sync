package net.alanwei.tools.impl;

import net.alanwei.tools.inter.IRemoteClipboard;
import net.alanwei.tools.models.LocalClipboardData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RabbitMqRemoteClipboard implements IRemoteClipboard {
    private List<Function<LocalClipboardData, Integer>> subscribers = new ArrayList<>();

    @Override
    public void subscribe(Function<LocalClipboardData, Integer> subscriber) {
        this.subscribers.add(subscriber);
    }

    @Override
    public void push(LocalClipboardData data) {

    }
}
