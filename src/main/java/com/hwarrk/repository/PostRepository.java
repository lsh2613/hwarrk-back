package com.hwarrk.repository;

import com.hwarrk.entity.Post;
import com.hwarrk.entity.Project;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByProject(Project project);
}
