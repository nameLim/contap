package com.project.contap.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.amazonaws.services.s3.AmazonS3Client;
import com.project.contap.model.HashTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    // 버킷 이름 동적 할당
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketUrl;

    private final Map<String, String> s3Url2userUrl = new HashMap<>();

    public static String upload(ImageService imageService, MultipartFile multipartFile, String dirName, String oldProfile) throws IOException {

        // File 형식으로 변경
        File file = imageService.convertMultipartFileToFile(multipartFile).orElseThrow(
                () -> new ContapException(ErrorCode.ERROR_CONVERT_FILE) //파일 변환 중 에러가 발생하였습니다.
        );

        // 업로드
        String uploadImageUrl = imageService.uploadToS3(file, dirName);
        
        //기존 파일 삭제
        if(oldProfile != null && oldProfile.contains(imageService.bucketUrl)) {
            oldProfile = oldProfile.substring(imageService.bucketUrl.length()+1);
            imageService.amazonS3Client.deleteObject(imageService.bucket, oldProfile);
        }

        return uploadImageUrl;
    }

    private Optional<File> convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convertFile = new File(multipartFile.getOriginalFilename());
        if(convertFile.createNewFile()) {

            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(multipartFile.getBytes());
            }
            catch (Exception e) {
                throw new ContapException(ErrorCode.ERROR_CONVERT_FILE); //파일 변환 중 에러가 발생하였습니다.
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    //S3로 파일 업로드 및 로컬 위치 파일 삭제
    private String uploadToS3(File file, String dirName) {
        String uploadImageUrl = "";
        String fileName = "";
        try {
             fileName =  "" +UUID.randomUUID() + System.currentTimeMillis() + file.getName();
             uploadImageUrl = putToS3(file, dirName + "/" + fileName);
        } finally {
            removeNewFile(file);
        }

        return uploadImageUrl;
    }

    //S3로 업로드
    private String putToS3(File file, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
        throw new ContapException(ErrorCode.ERROR_DELETE_FILE);
    }

}
