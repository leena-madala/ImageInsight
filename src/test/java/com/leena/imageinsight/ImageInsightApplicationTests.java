package com.leena.imageinsight;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "IMAGGA_API_URL=http://dummy-url",
        "IMAGGA_API_KEY=dummy-key"
})
class ImageInsightApplicationTests {

    @Test
    void contextLoads() {
    }

}
