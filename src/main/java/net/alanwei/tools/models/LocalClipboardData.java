package net.alanwei.tools.models;

import lombok.Getter;
import net.alanwei.tools.Configurations;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Getter
public class LocalClipboardData {
    private ClipboardType type;
    private byte[] data;
    private long timeStamp;
    private String sourceId;

    public LocalClipboardData() {
        this(Configurations.LOCAL_HOST_SOURCE_ID, System.currentTimeMillis() / 1000);
    }

    public LocalClipboardData(String sid, long ts) {
        this.sourceId = sid;
        this.timeStamp = ts;
    }

    public LocalClipboardData(String txt) {
        this();
        this.type = ClipboardType.String;
        this.data = txt.getBytes(StandardCharsets.UTF_8);
    }

    public LocalClipboardData(ClipboardType t, byte[] data) {
        this();
        this.type = t;
        this.data = data;
    }

    public static LocalClipboardData invalid() {
        LocalClipboardData clipboardData = new LocalClipboardData(ClipboardType.Invalid, null);
        return clipboardData;
    }

    public String getStringData() {
        if (this.type == ClipboardType.String) {
            return new String(this.data, StandardCharsets.UTF_8);
        }
        return "";
    }

    public boolean areEqual(LocalClipboardData clipboardData) {
        if (clipboardData == null) {
            return false;
        }
        if (this.data == null || clipboardData.data == null) {
            //如果两个剪贴板都没数据也认为不同
            return false;
        }
        if (this.data.length != clipboardData.data.length) {
            return false;
        }
        for (int index = 0; index < this.data.length; index++) {
            if (this.data[index] != clipboardData.data[index]) {
                return false;
            }
        }
        return true;
    }

    public NetworkClipboardData toNetworkClipboard() {
        NetworkClipboardData networkClipboardData = new NetworkClipboardData();
        networkClipboardData.setType(this.getType().toString());
        networkClipboardData.setSourceId(this.sourceId);
        networkClipboardData.setTimerStamp(this.timeStamp);
        String base64Data = "";
        if (this.data != null) {
            base64Data = Base64.getEncoder().encodeToString(this.data);
        }
        networkClipboardData.setBase64Data(base64Data);
        return networkClipboardData;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
