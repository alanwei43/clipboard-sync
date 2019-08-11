package net.alanwei.tools.impl;

import net.alanwei.tools.Util;
import net.alanwei.tools.inter.ILocalClipboard;
import net.alanwei.tools.models.LocalClipboardData;
import net.alanwei.tools.models.ClipboardType;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class LocalSystemClipboard implements ILocalClipboard {
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    @Override
    public LocalClipboardData get() {
        try {
            Transferable transferable = this.clipboard.getContents(null);
            if (transferable == null) {
                return LocalClipboardData.invalid();
            }
            if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String value = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                return LocalClipboardData.string(value);
            }
            if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                Image image = (Image) transferable.getTransferData(DataFlavor.imageFlavor);
                byte[] data = this.imageToBytes(image, "jpg");
                return LocalClipboardData.builder().type(ClipboardType.Image).data(data).build();
            }
        } catch (Throwable ex) {
        }
        return LocalClipboardData.invalid();
    }

    @Override
    public boolean set(LocalClipboardData result) {
        Util.log("update clipboard type: " + result.getType().toString());
        if (result.getData() != null) {
            Util.log("update clipboard length: " + result.getData().length);
        }
        if (result.getType().equals(ClipboardType.String)) {
            StringSelection data = new StringSelection(new String(result.getData(), StandardCharsets.UTF_8));
            this.clipboard.setContents(data, data);
            return true;
        }
        if (result.getType().equals(ClipboardType.Image)) {
            byte[] bytes = result.getData();
            Image image = this.bytesToImage(bytes);
            this.clipboard.setContents(new ImageTransferable(image), null);
        }
        return false;
    }


    /**
     * image to bytes
     * ref: https://www.mkyong.com/java/how-to-convert-byte-to-bufferedimage-in-java/
     *
     * @param image
     * @param type
     * @return
     */
    private byte[] imageToBytes(Image image, String type) {
        try {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.getGraphics();
            g.drawImage(image, 0, 0, null);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bi, type, out);
            out.flush();
            byte[] data = out.toByteArray();
            out.close();
            return data;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * bytes to image
     *
     * @param data
     * @return
     */
    private Image bytesToImage(byte[] data) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(in);
            return image;
        } catch (Throwable ex) {
        }
        return null;
    }

    /**
     * ref: https://stackoverflow.com/questions/7834768/setting-images-to-clipboard-java
     */
    private static class ImageTransferable implements Transferable {
        private Image image;

        public ImageTransferable(Image image) {
            this.image = image;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (this.isDataFlavorSupported(flavor)) {
                return image;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor == DataFlavor.imageFlavor;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{DataFlavor.imageFlavor};
        }
    }

}
