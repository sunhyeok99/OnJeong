package com.a503.onjeong.global.util;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("파일 테스트")
@SpringBootTest
class S3UtilTest {

    @Autowired
    private S3Util s3Util;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    public void before() {
        s3Util = new S3Util(amazonS3);
    }

    @DisplayName("파일 업로드 테스트")
    @Test
    public void uploadFile() throws IOException {

        // Given
        String beforeFileName = "MockFile.jpg";
        MockMultipartFile file = getMockMultipartFile();

        // When
        String afterFileName = s3Util.uploadFile(file);

        // Then
        assertNotEquals(beforeFileName, afterFileName);
    }

    @DisplayName("파일 삭제 테스트")
    @Test
    public void deleteFile() throws Exception {

        // Given
        MockMultipartFile file = getMockMultipartFile();

        // When
        String afterFileName = s3Util.uploadFile(file);
        s3Util.deleteFile(afterFileName);

        // Then
        assertNull(s3Util.getFile(afterFileName));
    }

    @DisplayName("파일 조회 테스트")
    @Test
    public void getFile() throws IOException {

        // Given
        String afterFileName = s3Util.uploadFile(getMockMultipartFile());

        // When
        /* 실제 getFile() 반환값은 S3 URL이므로, 테스트 과정에서는 Mock URL을 사용.
        S3내에 파일 존재가 확인 된다면 새로운 Mock URL 반환하도록 설정 */
        when(amazonS3.getUrl(any(), eq(afterFileName)))
                .thenReturn(new URL("https://example.com/mock-url"));  // 파일 이름에 대한 Mock URL 설정
        String filePath = s3Util.getFile(afterFileName);

        // Then
        assertEquals("/mock-url", filePath);
    }

    private static MockMultipartFile getMockMultipartFile() {

        return new MockMultipartFile(
                "file",
                "MockFile.jpg",
                "image/jpeg",
                new byte[0]
        );
    }
}