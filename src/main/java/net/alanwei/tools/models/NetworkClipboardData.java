package net.alanwei.tools.models;

import lombok.Getter;
import lombok.Setter;
import net.alanwei.tools.Util;

import java.util.Base64;
import java.util.Objects;

@Getter
@Setter
public class NetworkClipboardData {
    //TODO 接收网络剪贴板时判断是否来自本机, 如果是本机发送的网络剪切板忽略掉
    private String sourceId;
    //TODO 使用最新的网络剪切板
    private long timeStamp;
    private String type;
    private String base64Data;

    public LocalClipboardData toLocalClipboard() {
        ClipboardType t = ClipboardType.Invalid;
        if (Objects.equals(this.type.toLowerCase(), ClipboardType.String.toString().toLowerCase())) {
            t = ClipboardType.String;
        }
        if (Objects.equals(this.type.toLowerCase(), ClipboardType.Image.toString().toLowerCase())) {
            t = ClipboardType.Image;
        }

        LocalClipboardData clipboardData = LocalClipboardData.builder().type(t).data(Util.fromBase64(this.base64Data)).build();
        clipboardData.setTimeStamp(this.timeStamp);
        return clipboardData;
    }

    @Override
    public String toString() {
        return this.toLocalClipboard().toString();
    }
}
