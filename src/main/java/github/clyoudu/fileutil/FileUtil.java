package github.clyoudu.fileutil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Create by IntelliJ IDEA
 *
 * @author chenlei
 * @dateTime 2019/7/29 10:01
 * @description FileUtil
 */
@Slf4j
public class FileUtil {

    private static final String SEPARATOR_LINE = "\n";
    private static final String DOUBLE_SLASH = "//";
    private static final String DOUBLE_BACK_SLASH = "\\\\";
    private static final String SLASH = "/";
    private static final String BACK_SLASH = "\\";

    private FileUtil() {
    }

    /**
     * 读取操作系统文件
     *
     * @param absoluteFilePath 文件绝对路径
     * @return String
     */
    public static String readOsFile(String absoluteFilePath) {
        if (StringUtils.isBlank(absoluteFilePath)) {
            throw emptyFilePath();
        }

        File file = new File(absoluteFilePath);
        if (!file.exists()) {
            throw fileNotExists(absoluteFilePath);
        }

        return readFile(file);
    }

    /**
     * 读取文件
     *
     * @param file 文件
     * @return String
     */
    public static String readFile(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return readFile(is);
        } catch (IOException e) {
            throw readFileError(e);
        }
    }

    /**
     * 读取输入流
     *
     * @param inputStream 输入流
     * @return String
     */
    public static String readFile(InputStream inputStream) {
        try (InputStreamReader isr = new InputStreamReader(inputStream); BufferedReader br = new BufferedReader(isr)) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            inputStream.close();
            return StringUtils.join(lines, SEPARATOR_LINE);
        } catch (IOException e) {
            throw readFileError(e);
        }
    }

    /**
     * 保存操作系统文件
     *
     * @param absoluteFilePath 文件绝对路径
     * @param content          文件内容
     * @param append           是否追加
     * @return
     */
    public static void saveOsFile(String absoluteFilePath, String content, boolean append) {
        if (StringUtils.isBlank(absoluteFilePath)) {
            throw emptyFilePath();
        }

        File file = new File(absoluteFilePath);
        if (!file.exists()) {
            try {
                if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                    throw createFileError(file.getParent());
                }
                if (!file.createNewFile()) {
                    throw createFileError(absoluteFilePath);
                }
            } catch (IOException e) {
                throw createFileError(absoluteFilePath);
            }
        }

        try (OutputStream os = new FileOutputStream(file, append); OutputStreamWriter osw = new OutputStreamWriter(os); BufferedWriter bw = new BufferedWriter(osw)) {
            if (StringUtils.isNotBlank(content)) {
                bw.write(content);
            }
        } catch (IOException e) {
            log.error("Write os file error", e);
            throw new RuntimeException("Write os file error", e);
        }
    }

    /**
     * 读取classpath文件
     * @param classpathFile classpath文件路径
     * @return
     */
    public static String readClasspathFile(String classpathFile) {
        if (StringUtils.isBlank(classpathFile)) {
            throw emptyFilePath();
        }

        InputStream inputStream = null;
        try {
            inputStream = new ClassPathResource(classpathFile).getInputStream();
        } catch (IOException e) {
            log.error("Read classpath file error", e);
            throw new RuntimeException("Read classpath file error", e);
        }

        return readFile(inputStream);
    }

    /**
     * 删除操作系统文件
     * @param absoluteFilePath 文件绝对路径
     */
    public static boolean deleteOsFile(String absoluteFilePath) {
        if (StringUtils.isBlank(absoluteFilePath)) {
            throw emptyFilePath();
        }
        File file = new File(absoluteFilePath);

        if (!file.exists()) {
            throw fileNotExists(absoluteFilePath);
        }

        try {
            Files.delete(file.toPath());
            return true;
        } catch (IOException e) {
            log.error("Delete file[{}] error", absoluteFilePath, e);
            throw new RuntimeException("Delete file[" + absoluteFilePath + "]error.", e);
        }
    }

    public static String formatFilePath(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }

        while (filePath.contains(DOUBLE_SLASH) || filePath.contains(DOUBLE_BACK_SLASH)) {
            filePath = filePath.replaceAll(DOUBLE_SLASH, SLASH).replaceAll(Matcher.quoteReplacement(DOUBLE_BACK_SLASH), Matcher.quoteReplacement(BACK_SLASH));
        }

        return filePath;
    }

    private static RuntimeException emptyFilePath() {
        return new RuntimeException("Empty file path.");
    }

    private static RuntimeException fileNotExists(String filePath) {
        log.error("File[" + filePath + "] not exists.");
        return new RuntimeException("File[" + filePath + "] not exists.");
    }

    private static RuntimeException createFileError(String filePath) {
        log.error("Create file[" + filePath + "] error.");
        return new RuntimeException("Create file[" + filePath + "] error.");
    }

    private static RuntimeException readFileError(Exception e) {
        log.error("Read file error", e);
        return new RuntimeException("Read file error", e);
    }

    public static String getParentDir(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        if (filePath.lastIndexOf('/') < 0) {
            log.error("File path [" + filePath + "] has no parent dir.");
            return null;
        }
        if (filePath.lastIndexOf('/') == 0) {
            return "/";
        }
        return filePath.substring(0, filePath.lastIndexOf('/'));
    }
}
