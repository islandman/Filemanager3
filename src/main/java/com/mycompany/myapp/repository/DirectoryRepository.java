package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Directory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Directory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {}
