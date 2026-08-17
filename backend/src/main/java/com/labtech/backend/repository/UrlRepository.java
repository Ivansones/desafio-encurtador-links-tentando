package com.labtech.backend.repository;

import com.labtech.backend.entity.Url;
import com.labtech.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository<Url,Long> {

    List<Url> findAllByUserId( Long userId );
    List<Url> findAllByUserEmail( String email );
    List<Url> findByShortCode(String shortCode);
    void deleteByShortCode(String shortCode);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    UPDATE Url u
    SET u.shortCode = :newShortCode,
        u.link = :link
    WHERE u.shortCode = :oldShortCode
""")
    int urlUpdate(
            @Param("oldShortCode") String oldShortCode,
            @Param("newShortCode") String newShortCode,
            @Param("link") String link
    );
}

