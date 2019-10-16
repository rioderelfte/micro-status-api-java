package com.suora.microStatus.tweet;

import com.suora.microStatus.NotFoundException;
import com.suora.microStatus.user.User;
import com.suora.microStatus.user.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TweetController {
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/tweets")
    public Iterable<Tweet> allTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/tweets/{id}")
    public ResponseEntity<Tweet> findTweetById(@PathVariable UUID id) {
        return ResponseEntity.of(tweetRepository.findById(id));
    }

    @GetMapping("/users/{userId}/tweets")
    public Iterable<Tweet> tweetsForUser(
        @PathVariable UUID userId,
        Pageable pageable
    ) {
        return tweetRepository.findAllByUser(
            userRepository.findById(userId)
                          .orElseThrow(NotFoundException::new),
            pageable
        );
    }

    @PostMapping("/tweets")
    @RolesAllowed(User.ROLE_USER)
    public Tweet addTweet(
        @RequestBody NewTweet tweet,
        @AuthenticationPrincipal User user
    ) {
        var savedTweet = tweetRepository.save(tweet.buildTweetModelForUser(user));
        simpMessagingTemplate.convertAndSend("/queue/tweets", savedTweet);
        return savedTweet;
    }

    @GetMapping("/currentUser/tweets")
    @RolesAllowed(User.ROLE_USER)
    public Iterable<Tweet> tweetsForCurrentUser(
        @AuthenticationPrincipal User user,
        Pageable pageable
    ) {
        return tweetRepository.findAllByUser(user, pageable);
    }

    @Data
    @NoArgsConstructor
    private static class NewTweet {
        @NonNull
        private String text;

        Tweet buildTweetModelForUser(User user) {
            return new Tweet(
                UUID.randomUUID(), user, Instant.now(), text
            );
        }
    }
}
