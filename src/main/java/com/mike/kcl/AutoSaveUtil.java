package com.mike.kcl;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class AutoSaveUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void saveData(Object data, String filePath) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), data);
    }

    public static <T> T loadData(String filePath, Class<T> clazz) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return mapper.readValue(file, clazz);
        }
        return null;
    }
}
