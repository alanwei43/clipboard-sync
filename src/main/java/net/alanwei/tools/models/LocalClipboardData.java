package net.alanwei.tools.models;

import lombok.Builder;
import lombok.Getter;
import net.alanwei.tools.Configurations;
import net.alanwei.tools.Util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Builder
public class LocalClipboardData {
    private ClipboardType type;
    private byte[] data;
    private long timeStamp;

    public static LocalClipboardData invalid() {
        return LocalClipboardData.builder().type(ClipboardType.Invalid).build();
    }

    public static LocalClipboardData string(String txt) {
        return LocalClipboardData.builder().type(ClipboardType.String).data(txt.getBytes(StandardCharsets.UTF_8)).build();
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
        networkClipboardData.setTimeStamp(this.timeStamp);
        networkClipboardData.setBase64Data(Util.toBase64(this.data));
        networkClipboardData.setSourceId(Configurations.LOCAL_HOST_SOURCE_ID);
        return networkClipboardData;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
