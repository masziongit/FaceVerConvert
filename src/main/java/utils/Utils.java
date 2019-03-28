package utils;



import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class Utils  {

    private static String ROOT = "base64_test"+File.separator;

    public static void main(String[] args) throws UnsupportedEncodingException {

        String signatureKey = "bioauthB74908CD6BDF4789802192C26931D308";
        String hmacSha1 = null;
        try {
            String message = "15522944777649757219";
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(
                    signatureKey.getBytes("UTF-8"), "HmacSHA1");
            mac.init(spec);
            byte[] byteHMAC = mac.doFinal(message.getBytes("UTF-8"));
            hmacSha1 = byteArray2String(byteHMAC);

            System.out.println(hmacSha1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String byteArray2String(byte[] bs) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            String inTmp = null;
            String text = Integer.toHexString(bs[i]);
            if (text.length() >= 2) {
                inTmp = text.substring(text.length() - 2, text.length());
            } else {
                char[] array = new char[2];
                Arrays.fill(array, 0, 2 - text.length(), '0');
                System.arraycopy(text.toCharArray(), 0, array,
                        2 - text.length(), text.length());
                inTmp = new String(array);
            }
            sb.append(inTmp);
        }
        return sb.toString().toUpperCase();
    }

    public static void encoder(String imagePath,String base64) {
        String base64Image = "";
        File file = new File(ROOT+imagePath);
        try (FileInputStream imageInFile = new FileInputStream(file)) {

            byte imageData[] = new byte[(int) file.length()];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);
            File base64File = new File(ROOT+base64);
            FileUtils.writeByteArrayToFile(base64File, base64Image.getBytes());

            System.out.println(base64File.getAbsoluteFile());

        } catch (FileNotFoundException e) {
            System.out.println("Image not found " + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading the Image " + ioe);
        }
    }

    public static void decoder(String base64Path,String img) {
        try {
            byte[] b = IOUtils.toByteArray(new FileInputStream(new File(ROOT+base64Path)));
            String data = new String(b).replaceAll("\\s","").trim();
            BufferedImage image;
            byte[] imageByte = Base64.getDecoder().decode(data);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
            File file = new File(ROOT+img+".jpg");
            ImageIO.write(image, FilenameUtils.getExtension(file.getName()), file);

            System.out.println(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
