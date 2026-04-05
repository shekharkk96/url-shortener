package com.shekhar.url_shortener.repository;

import com.shekhar.url_shortener.entity.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortUrl(String shortUrl);
}
