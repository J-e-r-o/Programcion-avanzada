package um.playlist.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import um.playlist.repository.VideoRepository;
import um.playlist.model.Video;

import java.util.Optional;

class VideoServiceTest {
    VideoRepository repo = mock(VideoRepository.class);
    VideoService svc = new VideoService(repo);

    @Test
    void saveAndFind() {
        Video v = new Video();
        v.setId(1L); v.setTitle("x");
        when(repo.save(any())).thenReturn(v);
        when(repo.findById(1L)).thenReturn(Optional.of(v));

        Video saved = svc.save(v);
        assertEquals("x", saved.getTitle());
        assertTrue(svc.find(1L).isPresent());
        verify(repo, times(1)).save(any());
    }
}

