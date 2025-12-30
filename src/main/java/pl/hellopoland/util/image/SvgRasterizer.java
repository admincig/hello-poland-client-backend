package pl.hellopoland.util.image;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class SvgRasterizer {

    public static BufferedImage toBufferedImage(byte[] svgBytes, float width, float height) {
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgBytes));

        BufferedImageTranscoder t = new BufferedImageTranscoder();
        t.addTranscodingHint(ImageTranscoder.KEY_WIDTH, width);
        t.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, height);

        try {
            t.transcode(input, null);
            return t.getBufferedImage();
        } catch (Exception e) {
            throw new RuntimeException("Failed to rasterize SVG", e);
        }
    }

    private static class BufferedImageTranscoder extends ImageTranscoder {
        private BufferedImage image;

        @Override
        public BufferedImage createImage(int w, int h) {
            return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        }

        @Override
        public void writeImage(BufferedImage img, TranscoderOutput out) {
            this.image = img;
        }

        public BufferedImage getBufferedImage() {
            return image;
        }
    }
}
