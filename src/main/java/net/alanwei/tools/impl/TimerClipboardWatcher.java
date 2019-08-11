package net.alanwei.tools.impl;

import net.alanwei.tools.Util;
import net.alanwei.tools.inter.ILocalClipboard;
import net.alanwei.tools.inter.IClipboardWatcher;
import net.alanwei.tools.inter.INetworkClipboard;
import net.alanwei.tools.models.LocalClipboardData;
import net.alanwei.tools.models.ClipboardType;
import net.alanwei.tools.models.NetworkClipboardData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TimerClipboardWatcher implements IClipboardWatcher {
    @Autowired
    private ILocalClipboard localClipboard;
    private LocalClipboardData previousClipboardData;

    private Timer timer;
    private List<Function<LocalClipboardData, Integer>> watchers = new ArrayList<>();

    private void init() {
        if (timer != null) {
            return;
        }
        timer = new Timer(TimerClipboardWatcher.class.getSimpleName());

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalClipboardData result = localClipboard.get();
                if (previousClipboardData == null || result.areEqual(previousClipboardData)) {
                    previousClipboardData = result;
                    return;
                }
                previousClipboardData = result;
                List<Function<LocalClipboardData, Integer>> exitWatchers = watchers.stream().parallel().filter(w -> Objects.equals(w.apply(result), 0)).collect(Collectors.toList());

                while (exitWatchers.size() > 0) {
                    Function<LocalClipboardData, Integer> first = exitWatchers.get(0);
                    watchers.remove(first);
                    exitWatchers.remove(first);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000L, 1000L);
    }

    @Override
    public void watch(Function<LocalClipboardData, Integer> callback) {
        if (callback == null) {
            throw new Error("callback is null");
        }
        this.watchers.add(callback);
        this.init();
    }
}
