package com.suora.microStatus.tweet;

import com.suora.microStatus.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TweetRepository extends CrudRepository<Tweet, UUID> {
    List<Tweet> findAllByUser(User user, Pageable pageable);
}
