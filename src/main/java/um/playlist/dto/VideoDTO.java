package um.playlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VideoDTO {
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "La url es obligatoria")
    @Size(max = 500)
    private String url;

    private Integer likes;
    private Boolean favorite;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }
    public Boolean getFavorite() { return favorite; }
    public void setFavorite(Boolean favorite) { this.favorite = favorite; }
}
