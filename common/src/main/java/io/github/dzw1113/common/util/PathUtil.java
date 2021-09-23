package io.github.dzw1113.common.util;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/23 10:28
 **/
public class PathUtil {
    
    private static final String userDir = System.getProperty("user.dir");
    
    private static final String tmpDir = System.getProperty("java.io.tmpdir");
    
    private static final Map<String, File> map = new HashMap<>();
    
    public static File getFile(String fileName) {
        if (map.containsKey(fileName)) {
            return map.get(fileName);
        }
        File file = getFile(fileName, PathUtil.class);
        if (!file.exists()) {
            map.put(fileName, file);
        }
        return file;
    }
    
    public static File getFileByClassLoader(String fileName, Class cls) {
        if (map.containsKey(fileName)) {
            return map.get(fileName);
        }
        File file = getFile(fileName, cls);
        if (!file.exists()) {
            map.put(fileName, file);
        }
        return file;
    }
    
    private static File getFile(String fileName, Class cls) {
        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }
        try {
            URL url = cls.getClassLoader().getResource(fileName);
            file = new File(url.toURI());
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        file = new File(userDir + File.separator + fileName);
        if (file.exists()) {
            return file;
        }
        file = new File(tmpDir + fileName);
        if (file.exists()) {
            return file;
        }
        return null;
    }
    
    
}
