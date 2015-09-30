package Interface;
import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class GetWebcamImage {
    public static BufferedImage captureFrame() {
        final OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        try {
            grabber.start();
            IplImage img = grabber.grab();
            if (img != null) {
                return img.getBufferedImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}