package nl.ajax.alert.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    private String id;
    private UUID userToken;
    private String email;
    private String matchAgainst;

    public Subscription(String email, String matchAgainst) {
        this.email = email;
        this.matchAgainst = matchAgainst;
    }

    @PrePersist
    private void generateId() {
        this.id = email + "-" + matchAgainst;
        this.userToken = UUID.randomUUID();
    }
}