package um.playlist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import um.playlist.service.VideoService;
import um.playlist.model.Video;
import java.util.Optional;
import static org.mockito.Mockito.*;

@WebMvcTest(VideoRestController.class)
class VideoRestControllerTest {
    @Autowired MockMvc mvc;
    @MockBean VideoService service;

    @Test
    void getNotFound() throws Exception {
        when(service.find(999L)).thenReturn(Optional.empty());
        mvc.perform(get("/api/videos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOk() throws Exception {
        Video v = new Video();
        v.setId(1L); v.setTitle("t"); v.setUrl("u");
        when(service.find(1L)).thenReturn(Optional.of(v));
        mvc.perform(get("/api/videos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("t"));
    }
}
