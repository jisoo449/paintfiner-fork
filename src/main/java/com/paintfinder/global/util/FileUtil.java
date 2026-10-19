package com.paintfinder.global.util;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUtil {

    /**
     * URL을 받아 MultipartFile로 변환하는 메서드
     *
     * @param imageUrl 변환할 이미지 URL
     * @return MultipartFile
     * @throws IOException URL로부터 데이터를 읽을 수 없을 때 발생
     */
    public static MultipartFile convertUrlToMultipartFile(String imageUrl) throws IOException {
        // 1. URL 연결
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.connect();

        // 2. InputStream으로 데이터 읽기
        InputStream inputStream = connection.getInputStream();
        byte[] imageBytes = readInputStreamToByteArray(inputStream);

        // 3. 파일명 가져오기
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        String contentType = connection.getContentType();

        // 4. FileItem을 사용하여 MultipartFile 변환
        FileItem fileItem = new DiskFileItem("file", contentType, false, fileName, imageBytes.length, null);
        try (OutputStream os = fileItem.getOutputStream()) {
            os.write(imageBytes);
        }

        return new CommonsMultipartFile(fileItem);
    }

    /**
     * InputStream을 바이트 배열로 변환하는 유틸리티 메서드
     *
     * @param inputStream 변환할 InputStream
     * @return 바이트 배열
     * @throws IOException 읽기 오류 발생 시
     */
    private static byte[] readInputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[4096]; // 4KB씩 읽기
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}
