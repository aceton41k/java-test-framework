package template.framework.api.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@With
public class Post {
    private Integer id;
    private String title;
    private String message;
    private String date;
}
