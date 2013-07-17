package com.generate.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

/**
 * User: dorid
 * Date: 13-7-16
 * Time: 16:47
 */
public class FileUtil {

    protected static Log logger = LogFactory.getLog(FileUtil.class);

    public static void writeToFile(String file, String content) throws IOException {
        FileUtils.writeStringToFile(new File(file), content);
        logger.info("write to file: " + file);
    }
}
