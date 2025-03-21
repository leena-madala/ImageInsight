package com.leena.imageinsight.externalapi;

import com.leena.imageinsight.externalapi.ImaggaTagsResponse.Result;
import com.leena.imageinsight.externalapi.ImaggaTagsResponse.TagConfidence;
import com.leena.imageinsight.externalapi.ImaggaTagsResponse.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ImaggaTagsResponseTest {

    @Test
    public void testImaggaTagsResponseCreationWithResult() {
        Tag tag = new Tag("cat");
        TagConfidence tagConfidence = new TagConfidence(0.9, tag);
        Result result = new Result(List.of(tagConfidence));
        ImaggaTagsResponse response = new ImaggaTagsResponse(result);
        assertNotNull(response.getResult());
        assertEquals(1, response.getResult().getTags().size());
        assertEquals("cat", response.getResult().getTags().get(0).getTag().getEn());
    }

    @Test
    public void testImaggaTagsResponseEmptyResult() {
        Result result = new Result(List.of());
        ImaggaTagsResponse response = new ImaggaTagsResponse(result);
        assertNotNull(response.getResult());
        assertTrue(response.getResult().getTags().isEmpty());
    }

    @Test
    public void testTagConfidenceCreation() {
        Tag tag = new Tag("dog");
        TagConfidence tagConfidence = new TagConfidence(0.8, tag);
        assertNotNull(tagConfidence.getTag());
        assertEquals(0.8, tagConfidence.getConfidence());
        assertEquals("dog", tagConfidence.getTag().getEn());
    }

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("bird");
        assertNotNull(tag);
        assertEquals("bird", tag.getEn());
    }
}