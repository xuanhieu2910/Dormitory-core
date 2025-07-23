package teamit.hust.ktxcdshustbe.utility;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import teamit.hust.ktxcdshustbe.exception.FileExtensionException;
import teamit.hust.ktxcdshustbe.exception.FileIsNullException;
import teamit.hust.ktxcdshustbe.exception.FileSizeException;

import java.io.*;
import java.util.*;

@Log4j2
public class FileUtil {

    public final static String EXT_PDF = "pdf";
    public final static String EXT_OFFICE = "xls,xlsx,doc,docx,ppt";
    public static final String FOLDER_NAME_PARENT = "resources";
    public static final String FOLDER_NAME_IMAGE = "upload_image";
    public static final String FOLDER_NAME_FILE = "upload_file";
    public static final String FOLDER_NAME_REPORT ="report";
    public static final String SEPARATOR = "/";
    public static String pathReturn = "";
    private static final StringBuilder builder = new StringBuilder();
    private static final String CREATE_FILE_WIN = "copy con";
    private static final String CREATE_FILE_UNIX = "touch";

    // Save file if success then return file path, else return null
    public static Map<String, String> saveFiles(MultipartFile[] uploadedFile) {
        Map<String, String> outputList = new LinkedHashMap<>();
        for (MultipartFile multipartFile : uploadedFile) {
            if (multipartFile.isEmpty()) {
                return new LinkedHashMap<>();
            }
            String mimeType = multipartFile.getContentType().split("/")[0];
            if ("image".contains(mimeType)) {
                outputList.put(saveImage(multipartFile), multipartFile.getOriginalFilename());
            } else {
                outputList.put(saveFile(multipartFile), multipartFile.getOriginalFilename());
            }
        }
        return outputList;
    }

    // Save file if success then return file path, else return null
    public static String saveFile(MultipartFile uploadedFile) {
        return save(uploadedFile, FOLDER_NAME_FILE);
    }

    // Save file if success then return file path, else return null
    public static String saveImage(MultipartFile uploadedFile) {
        return save(uploadedFile, FOLDER_NAME_IMAGE);
    }

    private static String save(MultipartFile uploadedFile, String folderName) {
        try {
            return save(uploadedFile.getInputStream(), uploadedFile.getOriginalFilename(), folderName);
        } catch (IOException e) {
            log.error("Save file error", e);
            return null;
        }
    }

    public static String getFolderInfo() {
        builder.setLength(0);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        builder.append(year).append(month).append(day);
        return builder.toString();
    }

    public static String getTimeInfo() {
        builder.setLength(0);
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        builder.append(hour).append(minute).append(second);
        return builder.toString();
    }

    public static String save(InputStream inputStream, String fileName, String folderName) {
        String todayFolder = DateUtil.getTodayFolder();
        String fileId = generateFileId();

        String folder = buildFolderUpload(folderName);
        File inFiles = new File(folder);
        if (!inFiles.exists() && !inFiles.mkdirs()) {
            log.error("Can't create folder");
        }
        File file = new File(folder + File.separator + fileId + "." + FilenameUtils.getExtension(fileName));
        try {
            if (file.exists()) {
                file.delete();
            }
            FileUtils.copyInputStreamToFile(inputStream, file);
            return SEPARATOR + folderName + SEPARATOR + todayFolder + SEPARATOR + fileId + "." + FilenameUtils.getExtension(fileName);
        } catch (IOException e) {
            log.error("Save file error", e);
            return null;
        }
    }


    public static FileInputStream getInputStream(String filePath) {
        try {
            return new FileInputStream(new File(filePath));
        } catch (FileNotFoundException ignored) {

        }
        return null;
    }

    public static String getFilePathFromDatabase(String databaseFilePath) {
        return SEPARATOR + FOLDER_NAME_PARENT + SEPARATOR + databaseFilePath;
    }

    // Get only file name
    public static String getFilenameFromFilePath(String databaseFilePath) {
        if (StringUtils.isBlank(databaseFilePath)) {
            return "";
        }
        return databaseFilePath.substring(databaseFilePath.lastIndexOf(SEPARATOR) + 1);
    }

    // Create file id (unique)
    public static String generateFileId() {
        return DateUtil.getCurrentDateStr();
    }

    public static String buildFolderUpload(String folderName) {
        String todayFolder = DateUtil.getTodayFolder();
        String folderSave = PropertiesUtil.getProperty("vn.cpa.static.location.upload");
        pathReturn = "";
        pathReturn = pathReturn + File.separator + folderName
                + File.separator + todayFolder
                + File.separator;
        // in project
        return folderSave
                + File.separator + folderName
                + File.separator + todayFolder
                + File.separator;
    }


    public static String getFileExtFromFileName(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static boolean isPdfFileExt(String fileName) {
        return StringUtils.equalsIgnoreCase(getFileExtFromFileName(fileName), EXT_PDF);
    }

    public static boolean isOfficeFileExt(String fileName) {
        return StringUtils.contains(EXT_OFFICE, getFileExtFromFileName(fileName).toLowerCase());
    }

    public static boolean isAcceptFileType(String fileName) {
        return isAcceptFileType(fileName, PropertiesUtil.getProperty("accept_file_types"));
    }

    public static boolean isAcceptAudioFileType(String fileName) {
        return isAcceptFileType(fileName, PropertiesUtil.getProperty("accept_file_types_audio"));
    }


    public static boolean isAcceptFileTypeImage(String fileName) {
        return isAcceptFileType(fileName, PropertiesUtil.getProperty("accept_image_file_types"));
    }

    public static boolean isAcceptFileType(String fileName, String acceptTypes) {
        if (StringUtils.isBlank(acceptTypes) || StringUtils.isBlank(fileName)) {
            return false;
        }
        List<String> fileTypeList = Arrays.asList(acceptTypes.split(","));
        return fileTypeList.contains(getFileExtFromFileName(fileName).toLowerCase());
    }


    public static String getAcceptFilePDFString() {
        return PropertiesUtil.getProperty("accept_file_types_pdf");
    }


    public static boolean checkSizeFile(MultipartFile file) {
        Long sizeFileByte = file.getSize();
        double sizeFileMb = (sizeFileByte) / Math.pow(1024, 2);
        return sizeFileMb <= Double.parseDouble(PropertiesUtil.getProperty("spring.servlet.multipart.max-file-size").substring(0, 2));
    }

    public static boolean checkSizeFileImage(MultipartFile file) {
        Long sizeFileByte = file.getSize();
        double sizeFileMb = (sizeFileByte) / Math.pow(1024, 2);
        return sizeFileMb <= Double.parseDouble(PropertiesUtil.getProperty("max-size-upload-image").substring(0, 2));
    }

    public static void checkFileImages(MultipartFile file) {
        if (file.isEmpty()){
            throw new FileIsNullException();
        }
        if( Arrays.stream(Constants.FILE_IMAGES).noneMatch(x->x.equals(FilenameUtils.getExtension(file.getOriginalFilename()).toUpperCase()))){
            throw new FileExtensionException();
        }
        if (!FileUtil.checkSizeFileImage(file)){
            throw new FileSizeException();
        };
    }


    public static byte[] convertStringToByteWithBase64(String pathFilePhysical) {
        return Base64.getDecoder().decode(pathFilePhysical);
    }

    public static File createFileSampleAsset(String nameFile) throws IOException {
        // Define the file path
        File file = new File(nameFile);
        if(!file.exists()) {
            System.out.println("creating file");
            executeCreateFileCommand(nameFile);
        }
        return file;
    }

    public static String executeCreateFileCommand(String file) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String osName = System.getProperty("os.name").toLowerCase();
        String[] cmdArray = null;
        if (osName.contains("win")) {
            file = CREATE_FILE_WIN + " " + file;
            cmdArray = new String[]{"cmd.exe", "/c", file};
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            file = CREATE_FILE_UNIX + " " + file + " && chmod 751 " + file;
            cmdArray = new String[]{"/bin/bash", "-c", file};
        }
        log.info("Cmd: " + Arrays.toString(cmdArray));
        processBuilder.command(cmdArray);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("File created successfully: " + file);
            } else {
                System.out.println("Failed to create file. Exit code: " + exitCode);
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createFolder(String folder){
        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdirs();// Create directories if they don't exist
            log.info("Create folder " + folder + " success!");
        }
    }
}
