package net.alanwei.tools.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClipboardResult {
    private ClipboardType type;
    private byte[] data;
}
