package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Directory;
import com.mycompany.myapp.repository.DirectoryRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DirectoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DirectoryResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUM_FILES = 1;
    private static final Integer UPDATED_NUM_FILES = 2;

    private static final Boolean DEFAULT_IS_DIR = false;
    private static final Boolean UPDATED_IS_DIR = true;

    private static final String DEFAULT_VIRTUAL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_VIRTUAL_PATH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/directories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DirectoryRepository directoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDirectoryMockMvc;

    private Directory directory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createEntity(EntityManager em) {
        Directory directory = new Directory()
            .uuid(DEFAULT_UUID)
            .parentUuid(DEFAULT_PARENT_UUID)
            .name(DEFAULT_NAME)
            .tenantId(DEFAULT_TENANT_ID)
            .createDate(DEFAULT_CREATE_DATE)
            .userName(DEFAULT_USER_NAME)
            .numFiles(DEFAULT_NUM_FILES)
            .isDir(DEFAULT_IS_DIR)
            .virtualPath(DEFAULT_VIRTUAL_PATH);
        return directory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Directory createUpdatedEntity(EntityManager em) {
        Directory directory = new Directory()
            .uuid(UPDATED_UUID)
            .parentUuid(UPDATED_PARENT_UUID)
            .name(UPDATED_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .numFiles(UPDATED_NUM_FILES)
            .isDir(UPDATED_IS_DIR)
            .virtualPath(UPDATED_VIRTUAL_PATH);
        return directory;
    }

    @BeforeEach
    public void initTest() {
        directory = createEntity(em);
    }

    @Test
    @Transactional
    void createDirectory() throws Exception {
        int databaseSizeBeforeCreate = directoryRepository.findAll().size();
        // Create the Directory
        restDirectoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isCreated());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate + 1);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testDirectory.getParentUuid()).isEqualTo(DEFAULT_PARENT_UUID);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirectory.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testDirectory.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testDirectory.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testDirectory.getNumFiles()).isEqualTo(DEFAULT_NUM_FILES);
        assertThat(testDirectory.getIsDir()).isEqualTo(DEFAULT_IS_DIR);
        assertThat(testDirectory.getVirtualPath()).isEqualTo(DEFAULT_VIRTUAL_PATH);
    }

    @Test
    @Transactional
    void createDirectoryWithExistingId() throws Exception {
        // Create the Directory with an existing ID
        directory.setId(1L);

        int databaseSizeBeforeCreate = directoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDirectories() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get all the directoryList
        restDirectoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(directory.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].parentUuid").value(hasItem(DEFAULT_PARENT_UUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].numFiles").value(hasItem(DEFAULT_NUM_FILES)))
            .andExpect(jsonPath("$.[*].isDir").value(hasItem(DEFAULT_IS_DIR.booleanValue())))
            .andExpect(jsonPath("$.[*].virtualPath").value(hasItem(DEFAULT_VIRTUAL_PATH)));
    }

    @Test
    @Transactional
    void getDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        // Get the directory
        restDirectoryMockMvc
            .perform(get(ENTITY_API_URL_ID, directory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(directory.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.parentUuid").value(DEFAULT_PARENT_UUID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.numFiles").value(DEFAULT_NUM_FILES))
            .andExpect(jsonPath("$.isDir").value(DEFAULT_IS_DIR.booleanValue()))
            .andExpect(jsonPath("$.virtualPath").value(DEFAULT_VIRTUAL_PATH));
    }

    @Test
    @Transactional
    void getNonExistingDirectory() throws Exception {
        // Get the directory
        restDirectoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory
        Directory updatedDirectory = directoryRepository.findById(directory.getId()).get();
        // Disconnect from session so that the updates on updatedDirectory are not directly saved in db
        em.detach(updatedDirectory);
        updatedDirectory
            .uuid(UPDATED_UUID)
            .parentUuid(UPDATED_PARENT_UUID)
            .name(UPDATED_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .numFiles(UPDATED_NUM_FILES)
            .isDir(UPDATED_IS_DIR)
            .virtualPath(UPDATED_VIRTUAL_PATH);

        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDirectory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDirectory.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirectory.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDirectory.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testDirectory.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testDirectory.getNumFiles()).isEqualTo(UPDATED_NUM_FILES);
        assertThat(testDirectory.getIsDir()).isEqualTo(UPDATED_IS_DIR);
        assertThat(testDirectory.getVirtualPath()).isEqualTo(UPDATED_VIRTUAL_PATH);
    }

    @Test
    @Transactional
    void putNonExistingDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, directory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(directory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDirectoryWithPatch() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory using partial update
        Directory partialUpdatedDirectory = new Directory();
        partialUpdatedDirectory.setId(directory.getId());

        partialUpdatedDirectory.uuid(UPDATED_UUID).parentUuid(UPDATED_PARENT_UUID).tenantId(UPDATED_TENANT_ID);

        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDirectory.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
        assertThat(testDirectory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirectory.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDirectory.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testDirectory.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testDirectory.getNumFiles()).isEqualTo(DEFAULT_NUM_FILES);
        assertThat(testDirectory.getIsDir()).isEqualTo(DEFAULT_IS_DIR);
        assertThat(testDirectory.getVirtualPath()).isEqualTo(DEFAULT_VIRTUAL_PATH);
    }

    @Test
    @Transactional
    void fullUpdateDirectoryWithPatch() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();

        // Update the directory using partial update
        Directory partialUpdatedDirectory = new Directory();
        partialUpdatedDirectory.setId(directory.getId());

        partialUpdatedDirectory
            .uuid(UPDATED_UUID)
            .parentUuid(UPDATED_PARENT_UUID)
            .name(UPDATED_NAME)
            .tenantId(UPDATED_TENANT_ID)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .numFiles(UPDATED_NUM_FILES)
            .isDir(UPDATED_IS_DIR)
            .virtualPath(UPDATED_VIRTUAL_PATH);

        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDirectory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDirectory))
            )
            .andExpect(status().isOk());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
        Directory testDirectory = directoryList.get(directoryList.size() - 1);
        assertThat(testDirectory.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testDirectory.getParentUuid()).isEqualTo(UPDATED_PARENT_UUID);
        assertThat(testDirectory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirectory.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testDirectory.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testDirectory.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testDirectory.getNumFiles()).isEqualTo(UPDATED_NUM_FILES);
        assertThat(testDirectory.getIsDir()).isEqualTo(UPDATED_IS_DIR);
        assertThat(testDirectory.getVirtualPath()).isEqualTo(UPDATED_VIRTUAL_PATH);
    }

    @Test
    @Transactional
    void patchNonExistingDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, directory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(directory))
            )
            .andExpect(status().isBadRequest());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDirectory() throws Exception {
        int databaseSizeBeforeUpdate = directoryRepository.findAll().size();
        directory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDirectoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(directory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Directory in the database
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDirectory() throws Exception {
        // Initialize the database
        directoryRepository.saveAndFlush(directory);

        int databaseSizeBeforeDelete = directoryRepository.findAll().size();

        // Delete the directory
        restDirectoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, directory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Directory> directoryList = directoryRepository.findAll();
        assertThat(directoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
