package iainpalangkarayarepository.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentResponse {

    private String title;

    private String url;

    private Instant createdAt;

    private Instant updatedAt;

}
