package org.tokio.spring.resources.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileHelper {

    private FileHelper() {}

    /**
     * Amount the segment path given over current working directory,
     * if is null, then return the current working directory
     *
     * @param segmentPath fragment of path for amount over current working directory
     * @return path with segment given or directory working
     */
    public static Path getCurrentWorking(@Nullable String segmentPath){
        return Optional.ofNullable(segmentPath)
                .map(StringUtils::stripToNull)
                .map(FileSystems.getDefault()::getPath)
                .orElseGet(()->FileSystems.getDefault().getPath(StringUtils.EMPTY));

    }
    /**
     * Method that create the directories if not exits
     * @param path directory to builder
     * @return true if was possible the created, otherwise false
     */
    public static boolean createWorkingIfNotExits(@NonNull Path path) throws IOException {
        if( Files.notExists(path)){
            Files.createDirectories(path);
            // verifica
            if(!Files.isDirectory(path)){
                Files.delete(path);
            }
            return true;
        }
        return false;
    }

    /**
     * Method that delete the directory or file if exits
     * @param path directory to delete
     * @return true if was possible the deleted, otherwise false
     */
    public static boolean deleteWorkIfNotExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }

}
