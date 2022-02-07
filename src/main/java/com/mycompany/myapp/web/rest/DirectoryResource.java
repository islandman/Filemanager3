package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Directory;
import com.mycompany.myapp.repository.DirectoryRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Directory}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);

    private static final String ENTITY_NAME = "directory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DirectoryRepository directoryRepository;

    public DirectoryResource(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    /**
     * {@code POST  /directories} : Create a new directory.
     *
     * @param directory the directory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new directory, or with status {@code 400 (Bad Request)} if the directory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/directories")
    public ResponseEntity<Directory> createDirectory(@RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to save Directory : {}", directory);
        if (directory.getId() != null) {
            throw new BadRequestAlertException("A new directory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Directory result = directoryRepository.save(directory);
        return ResponseEntity
            .created(new URI("/api/directories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /directories/:id} : Updates an existing directory.
     *
     * @param id the id of the directory to save.
     * @param directory the directory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directory,
     * or with status {@code 400 (Bad Request)} if the directory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the directory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/directories/{id}")
    public ResponseEntity<Directory> updateDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Directory directory
    ) throws URISyntaxException {
        log.debug("REST request to update Directory : {}, {}", id, directory);
        if (directory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Directory result = directoryRepository.save(directory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /directories/:id} : Partial updates given fields of an existing directory, field will ignore if it is null
     *
     * @param id the id of the directory to save.
     * @param directory the directory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated directory,
     * or with status {@code 400 (Bad Request)} if the directory is not valid,
     * or with status {@code 404 (Not Found)} if the directory is not found,
     * or with status {@code 500 (Internal Server Error)} if the directory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/directories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Directory> partialUpdateDirectory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Directory directory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Directory partially : {}, {}", id, directory);
        if (directory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, directory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!directoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Directory> result = directoryRepository
            .findById(directory.getId())
            .map(existingDirectory -> {
                if (directory.getUuid() != null) {
                    existingDirectory.setUuid(directory.getUuid());
                }
                if (directory.getParentUuid() != null) {
                    existingDirectory.setParentUuid(directory.getParentUuid());
                }
                if (directory.getName() != null) {
                    existingDirectory.setName(directory.getName());
                }
                if (directory.getTenantId() != null) {
                    existingDirectory.setTenantId(directory.getTenantId());
                }
                if (directory.getCreateDate() != null) {
                    existingDirectory.setCreateDate(directory.getCreateDate());
                }
                if (directory.getUserName() != null) {
                    existingDirectory.setUserName(directory.getUserName());
                }
                if (directory.getNumFiles() != null) {
                    existingDirectory.setNumFiles(directory.getNumFiles());
                }
                if (directory.getIsDir() != null) {
                    existingDirectory.setIsDir(directory.getIsDir());
                }
                if (directory.getVirtualPath() != null) {
                    existingDirectory.setVirtualPath(directory.getVirtualPath());
                }

                return existingDirectory;
            })
            .map(directoryRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, directory.getId().toString())
        );
    }

    /**
     * {@code GET  /directories} : get all the directories.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of directories in body.
     */
    @GetMapping("/directories")
    public ResponseEntity<List<Directory>> getAllDirectories(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Directories");
        Page<Directory> page = directoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /directories/:id} : get the "id" directory.
     *
     * @param id the id of the directory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the directory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/directories/{id}")
    public ResponseEntity<Directory> getDirectory(@PathVariable Long id) {
        log.debug("REST request to get Directory : {}", id);
        Optional<Directory> directory = directoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(directory);
    }

    /**
     * {@code DELETE  /directories/:id} : delete the "id" directory.
     *
     * @param id the id of the directory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/directories/{id}")
    public ResponseEntity<Void> deleteDirectory(@PathVariable Long id) {
        log.debug("REST request to delete Directory : {}", id);
        directoryRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
