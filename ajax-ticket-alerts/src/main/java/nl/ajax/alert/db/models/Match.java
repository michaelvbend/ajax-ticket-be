package nl.ajax.alert.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    private String id;
    private String homeTeam;
    private String awayTeam;
    private boolean soldOut;
    private String matchLink;
    private LocalDateTime lastModified;

    @PrePersist
    private void generateId() {
        if (homeTeam != null && awayTeam != null) {
            this.id = homeTeam + "-" + awayTeam;
        }
    }

}