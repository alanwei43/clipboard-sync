package net.alanwei.tools.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RemoteClipboardData {
    private static final String LOCAL_HOST_SOURCE_ID = UUID.randomUUID().toString();
    private String sourceHostId;
    private long timerStamp;
    private String type;
    private String base64Data;

    public static String getLocalHostSourceId() {
        return LOCAL_HOST_SOURCE_ID;
    }
}
