package net.alanwei.tools.models;

import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class ClipboardResult {
    private ClipboardResult() {
    }

    public static ClipboardResult invalid() {
        ClipboardResult result = new ClipboardResult();
        result.type = ClipboardType.Invalid;
        return result;
    }

    public static ClipboardResult init(String data) {
        ClipboardResult result = new ClipboardResult();
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
