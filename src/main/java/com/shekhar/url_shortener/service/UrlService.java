package com.shekhar.url_shortener.service;

import com.shekhar.url_shortener.entity.ShortUrl;
import com.shekhar.url_shortener.repository.UrlRepository;
import com.shekhar.url_shortener.util.Base62;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UrlService {

    private final UrlRepository repository;

    public String shortenUrl(String originalUrl) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setOriginalUrl(originalUrl);
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setClickCount(0L);

        ShortUrl savedUrl = repository.save(shortUrl);

        String code = Base62.encode(savedUrl.getId());
        savedUrl.setShortUrl(code);
        repository.save(savedUrl);
        return code;
    }

    public String getOriginalUrl(String code) {
        ShortUrl shortUrl = repository.findByShortUrl(code)
                .orElseThrow(() -> new RuntimeException("URL not found"));

        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        repository.save(shortUrl);

        return shortUrl.getOriginalUrl();
    }

}
