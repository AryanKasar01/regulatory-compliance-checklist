
package com.student.checklist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ComplianceControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void homepageFileContainsApplicationTitle() throws Exception {
        ClassPathResource resource =
                new ClassPathResource("static/index.html");

        String html = StreamUtils.copyToString(
                resource.getInputStream(),
                StandardCharsets.UTF_8
        );

        assertTrue(html.contains("Regulatory Compliance Checklist"));
    }

    @Test
    void recordsCanBeListed() throws Exception {
        mvc.perform(get("/api/records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").exists());
    }

    @Test
    void recordCanBeCreated() throws Exception {
        mvc.perform(post("/api/records")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test control\",\"owner\":\"QA\",\"status\":\"Pending\",\"dueDate\":\"2026-10-30\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test control"));
    }
}
	