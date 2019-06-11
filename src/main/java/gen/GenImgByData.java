package gen;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import utils.Constant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class GenImgByData {

    final static Logger logger = Logger.getLogger(GenImgByData.class);

    private Properties prop;

    public GenImgByData(Properties prop, String fileName) throws Exception {

        this.prop = prop;

        List<Map<String, String>> mapList = new ArrayList<>();
        logger.info("Reading file");

        String regex = Pattern.quote(prop.getProperty("file.header.regex"));

        LineIterator it = FileUtils.lineIterator(new File(fileName), StandardCharsets.UTF_8.name());
        it.hasNext();
        String headerStr = it.nextLine().replace(Constant.Regex.QUOTE, "");
        String[] headerArr = headerStr.split(regex);
        try {
            while (it.hasNext()) {
                String line = it.nextLine().replace(Constant.Regex.QUOTE, "");
                String[] lineArr = line.split(regex,-1);
                if (lineArr.length == headerArr.length) {
                    LinkedHashMap<String, String> map = new LinkedHashMap<>();
                    for (int i = 0; i < headerArr.length - 1; i++) {
                        map.put(headerArr[i].trim(), lineArr[i].trim());
                    }
                    mapList.add(map);

                } else {
                    logger.error("Line Data Error: " + lineArr[0] + " is not mapping with header");
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            it.close();
        }

        logger.info("Reading file complete!!");

        logger.info("Gen Image start");

        String fDataId = prop.getProperty("field.data.id");
        String fFileName = prop.getProperty("field.file.name");
        String fFolderType = prop.getProperty("field.folder.type");
        String fDataDetail = prop.getProperty("field.data.detail");
        String fFileType = prop.getProperty("field.file.type");
        String fFolderPrimary = prop.getProperty("field.folder.primary");
        String fFolderSecondary = prop.getProperty("field.folder.secondary");

        mapList.stream()
//                .filter(o -> o.get(fFileType).equalsIgnoreCase(Constant.TypeFile.PNG))
                .forEach(o -> {
                    try {

                        File imgData = getFile(o, fFileType, fFileName
                                , fFolderType, fFolderPrimary, fFolderSecondary);

                        if (imgData != null){
//                                && !imgData.exists()) {
                            logger.debug("Gen Image RefId " + o.get(fDataId) + " to : " + imgData.getAbsolutePath());

                            String data = o.get(fDataDetail).replaceAll("\\s","");
                            BufferedImage image;
                            byte[] imageByte = Base64.getDecoder().decode(data);

                            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                            image = ImageIO.read(bis);
                            bis.close();

                            ImageIO.write(image, FilenameUtils.getExtension(imgData.getName()), imgData);
                        }else {
                            throw new Exception();
                        }

                    } catch (Exception e) {
//                        e.printStackTrace();
                        logger.error("Error RefId " + o.get(fDataId)+", Error Data " + o.get(fDataDetail) + " : " + e);
                    }

                });
        logger.info("Gen Images complete!!");
    }

    private File getFile(Map<String, String> o, String fFileType, String fFileName,
                         String fFolderType, String fFolderPrimary, String fFolderSecondary) {

//        String imgType = o.get(fFileType).toLowerCase();
        String imgType = Constant.TypeFile.JPG.toLowerCase();
        String imgName = o.get(fFileName) + "." + imgType;
        String folder = null;

        if (!StringUtils.isEmpty(o.get(fFolderPrimary))) {
            folder = casePS(o.get(fFolderPrimary)) + o.get(fFolderType) + File.separator;
        } else if (!StringUtils.isEmpty(o.get(fFolderSecondary))) {
            folder = casePS(o.get(fFolderSecondary)) + Constant.TypeFile.SECONDARY + File.separator;
        }

        File file = StringUtils.isEmpty(folder) ? null : new File(folder + imgName);
        if (file != null)
            file.getParentFile().mkdirs();

        return file;
    }

    private String casePS(String ps) {
        String folder = prop.getProperty("file.output.folder");
        if (ps.contains(Constant.TypeFile.PASS)) {
            folder += Constant.TypeFile.PASS + File.separator;
        } else {
            folder += Constant.TypeFile.FAIL + File.separator;
        }
        return folder;
    }


}
