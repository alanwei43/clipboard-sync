package net.alanwei.tools.models;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class LocalClipboardData {
    private LocalClipboardData() {
    }

    public static LocalClipboardData invalid() {
        LocalClipboardData result = new LocalClipboardData();
        result.type = ClipboardType.Invalid;
        return result;
    }

    public static LocalClipboardData init(String data) {
        LocalClipboardData result = new LocalClipboardData();
        result.setType(ClipboardType.String);
        result.setData(data.getBytes());
        return result;
    }

    private ClipboardType type;
    private byte[] data;

    public String getStringData() {
        if (this.type == ClipboardType.String) {
            return new String(this.data, StandardCharsets.UTF_8);
        }
        return "";
    }
}
