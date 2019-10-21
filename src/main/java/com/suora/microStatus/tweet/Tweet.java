package com.suora.microStatus.tweet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.suora.microStatus.user.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Tweet {
    @Id
    @NonNull
    private final UUID id;

    @ManyToOne
    @JsonIgnore
    @NonNull
    private final User user;

    @NonNull
    private final Instant createdAt;

    @javax.persistence.Lob
    @NonNull
    private final String text;

    @JsonProperty("user")
    public UUID getUserId() {
        return user.getId();
    }
}
