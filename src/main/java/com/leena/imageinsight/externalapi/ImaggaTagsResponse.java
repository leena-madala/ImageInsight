package com.leena.imageinsight.externalapi;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ImaggaTagsResponse {

    private Result result;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private List<TagConfidence> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TagConfidence {
        private double confidence;
        private Tag tag;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String en;
    }
}