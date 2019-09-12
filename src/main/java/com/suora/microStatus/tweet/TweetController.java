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

    @GetMapping("/tweets")
    public Iterable<Tweet> findAllTweets() {
        return tweetRepository.findAll();
    }

    @GetMapping("/tweets/{id}")
    public ResponseEntity<Tweet> findTweetById(@PathVariable UUID id) {
        return ResponseEntity.of(tweetRepository.findById(id));
    }

    @GetMapping("/users/{userId}/tweets")
    public Iterable<Tweet> findTweetsByUser(@PathVariable UUID userId, Pageable pageable) {
        return tweetRepository.findAllByUser(
            userRepository.findById(userId).orElseThrow(NotFoundException::new),
            pageable);
    }

    @PostMapping("/tweets")
    @RolesAllowed(User.ROLE_USER)
    public Tweet postTweet(@RequestBody NewTweet tweet, @AuthenticationPrincipal User user) {
        return tweetRepository.save(tweet.buildTweetModelForUser(user));
    }

    @GetMapping("/currentUser/tweets")
    @RolesAllowed(User.ROLE_USER)
    public Iterable<Tweet> myTweets(@AuthenticationPrincipal User user, Pageable pageable) {
        return tweetRepository.findAllByUser(user, pageable);
    }

    @Data
    @NoArgsConstructor
    private static class NewTweet {
        @NonNull
        private String text;

        Tweet buildTweetModelForUser(User user) {
            return new Tweet(UUID.randomUUID(), user, Instant.now(), text);
        }
    }
}
