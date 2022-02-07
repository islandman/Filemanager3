package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.File;
import com.mycompany.myapp.repository.FileRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FileResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FileResourceIT {

    private static final String DEFAULT_UUID = "AAAAAAAAAA";
    private static final String UPDATED_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTORY_UUID = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTORY_UUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMOTE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_REMOTE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_TENANT_ID = 1L;
    private static final Long UPDATED_TENANT_ID = 2L;

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_SIZZ = 1L;
    private static final Long UPDATED_SIZZ = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CONTENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CONTENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CONTENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CONTENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_VIRTUAL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_VIRTUAL_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DIR = false;
    private static final Boolean UPDATED_IS_DIR = true;

    private static final String ENTITY_API_URL = "/api/files";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFileMockMvc;

    private File file;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createEntity(EntityManager em) {
        File file = new File()
            .uuid(DEFAULT_UUID)
            .directoryUuid(DEFAULT_DIRECTORY_UUID)
            .name(DEFAULT_NAME)
            .remotePath(DEFAULT_REMOTE_PATH)
            .tenantId(DEFAULT_TENANT_ID)
            .contentType(DEFAULT_CONTENT_TYPE)
            .sizz(DEFAULT_SIZZ)
            .createDate(DEFAULT_CREATE_DATE)
            .userName(DEFAULT_USER_NAME)
            .content(DEFAULT_CONTENT)
            .contentContentType(DEFAULT_CONTENT_CONTENT_TYPE)
            .virtualPath(DEFAULT_VIRTUAL_PATH)
            .isDir(DEFAULT_IS_DIR);
        return file;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static File createUpdatedEntity(EntityManager em) {
        File file = new File()
            .uuid(UPDATED_UUID)
            .directoryUuid(UPDATED_DIRECTORY_UUID)
            .name(UPDATED_NAME)
            .remotePath(UPDATED_REMOTE_PATH)
            .tenantId(UPDATED_TENANT_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .sizz(UPDATED_SIZZ)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .virtualPath(UPDATED_VIRTUAL_PATH)
            .isDir(UPDATED_IS_DIR);
        return file;
    }

    @BeforeEach
    public void initTest() {
        file = createEntity(em);
    }

    @Test
    @Transactional
    void createFile() throws Exception {
        int databaseSizeBeforeCreate = fileRepository.findAll().size();
        // Create the File
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isCreated());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate + 1);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getUuid()).isEqualTo(DEFAULT_UUID);
        assertThat(testFile.getDirectoryUuid()).isEqualTo(DEFAULT_DIRECTORY_UUID);
        assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFile.getRemotePath()).isEqualTo(DEFAULT_REMOTE_PATH);
        assertThat(testFile.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testFile.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
        assertThat(testFile.getSizz()).isEqualTo(DEFAULT_SIZZ);
        assertThat(testFile.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFile.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testFile.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testFile.getContentContentType()).isEqualTo(DEFAULT_CONTENT_CONTENT_TYPE);
        assertThat(testFile.getVirtualPath()).isEqualTo(DEFAULT_VIRTUAL_PATH);
        assertThat(testFile.getIsDir()).isEqualTo(DEFAULT_IS_DIR);
    }

    @Test
    @Transactional
    void createFileWithExistingId() throws Exception {
        // Create the File with an existing ID
        file.setId(1L);

        int databaseSizeBeforeCreate = fileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFiles() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get all the fileList
        restFileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(file.getId().intValue())))
            .andExpect(jsonPath("$.[*].uuid").value(hasItem(DEFAULT_UUID)))
            .andExpect(jsonPath("$.[*].directoryUuid").value(hasItem(DEFAULT_DIRECTORY_UUID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].remotePath").value(hasItem(DEFAULT_REMOTE_PATH)))
            .andExpect(jsonPath("$.[*].tenantId").value(hasItem(DEFAULT_TENANT_ID.intValue())))
            .andExpect(jsonPath("$.[*].contentType").value(hasItem(DEFAULT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].sizz").value(hasItem(DEFAULT_SIZZ.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)))
            .andExpect(jsonPath("$.[*].contentContentType").value(hasItem(DEFAULT_CONTENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(Base64Utils.encodeToString(DEFAULT_CONTENT))))
            .andExpect(jsonPath("$.[*].virtualPath").value(hasItem(DEFAULT_VIRTUAL_PATH)))
            .andExpect(jsonPath("$.[*].isDir").value(hasItem(DEFAULT_IS_DIR.booleanValue())));
    }

    @Test
    @Transactional
    void getFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        // Get the file
        restFileMockMvc
            .perform(get(ENTITY_API_URL_ID, file.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(file.getId().intValue()))
            .andExpect(jsonPath("$.uuid").value(DEFAULT_UUID))
            .andExpect(jsonPath("$.directoryUuid").value(DEFAULT_DIRECTORY_UUID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.remotePath").value(DEFAULT_REMOTE_PATH))
            .andExpect(jsonPath("$.tenantId").value(DEFAULT_TENANT_ID.intValue()))
            .andExpect(jsonPath("$.contentType").value(DEFAULT_CONTENT_TYPE))
            .andExpect(jsonPath("$.sizz").value(DEFAULT_SIZZ.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME))
            .andExpect(jsonPath("$.contentContentType").value(DEFAULT_CONTENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.content").value(Base64Utils.encodeToString(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.virtualPath").value(DEFAULT_VIRTUAL_PATH))
            .andExpect(jsonPath("$.isDir").value(DEFAULT_IS_DIR.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFile() throws Exception {
        // Get the file
        restFileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file
        File updatedFile = fileRepository.findById(file.getId()).get();
        // Disconnect from session so that the updates on updatedFile are not directly saved in db
        em.detach(updatedFile);
        updatedFile
            .uuid(UPDATED_UUID)
            .directoryUuid(UPDATED_DIRECTORY_UUID)
            .name(UPDATED_NAME)
            .remotePath(UPDATED_REMOTE_PATH)
            .tenantId(UPDATED_TENANT_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .sizz(UPDATED_SIZZ)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .virtualPath(UPDATED_VIRTUAL_PATH)
            .isDir(UPDATED_IS_DIR);

        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFile.getDirectoryUuid()).isEqualTo(UPDATED_DIRECTORY_UUID);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getRemotePath()).isEqualTo(UPDATED_REMOTE_PATH);
        assertThat(testFile.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testFile.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFile.getSizz()).isEqualTo(UPDATED_SIZZ);
        assertThat(testFile.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFile.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFile.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFile.getVirtualPath()).isEqualTo(UPDATED_VIRTUAL_PATH);
        assertThat(testFile.getIsDir()).isEqualTo(UPDATED_IS_DIR);
    }

    @Test
    @Transactional
    void putNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, file.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .uuid(UPDATED_UUID)
            .contentType(UPDATED_CONTENT_TYPE)
            .sizz(UPDATED_SIZZ)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .virtualPath(UPDATED_VIRTUAL_PATH);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFile.getDirectoryUuid()).isEqualTo(DEFAULT_DIRECTORY_UUID);
        assertThat(testFile.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFile.getRemotePath()).isEqualTo(DEFAULT_REMOTE_PATH);
        assertThat(testFile.getTenantId()).isEqualTo(DEFAULT_TENANT_ID);
        assertThat(testFile.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFile.getSizz()).isEqualTo(UPDATED_SIZZ);
        assertThat(testFile.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFile.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFile.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFile.getVirtualPath()).isEqualTo(UPDATED_VIRTUAL_PATH);
        assertThat(testFile.getIsDir()).isEqualTo(DEFAULT_IS_DIR);
    }

    @Test
    @Transactional
    void fullUpdateFileWithPatch() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeUpdate = fileRepository.findAll().size();

        // Update the file using partial update
        File partialUpdatedFile = new File();
        partialUpdatedFile.setId(file.getId());

        partialUpdatedFile
            .uuid(UPDATED_UUID)
            .directoryUuid(UPDATED_DIRECTORY_UUID)
            .name(UPDATED_NAME)
            .remotePath(UPDATED_REMOTE_PATH)
            .tenantId(UPDATED_TENANT_ID)
            .contentType(UPDATED_CONTENT_TYPE)
            .sizz(UPDATED_SIZZ)
            .createDate(UPDATED_CREATE_DATE)
            .userName(UPDATED_USER_NAME)
            .content(UPDATED_CONTENT)
            .contentContentType(UPDATED_CONTENT_CONTENT_TYPE)
            .virtualPath(UPDATED_VIRTUAL_PATH)
            .isDir(UPDATED_IS_DIR);

        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFile))
            )
            .andExpect(status().isOk());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
        File testFile = fileList.get(fileList.size() - 1);
        assertThat(testFile.getUuid()).isEqualTo(UPDATED_UUID);
        assertThat(testFile.getDirectoryUuid()).isEqualTo(UPDATED_DIRECTORY_UUID);
        assertThat(testFile.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFile.getRemotePath()).isEqualTo(UPDATED_REMOTE_PATH);
        assertThat(testFile.getTenantId()).isEqualTo(UPDATED_TENANT_ID);
        assertThat(testFile.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
        assertThat(testFile.getSizz()).isEqualTo(UPDATED_SIZZ);
        assertThat(testFile.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFile.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testFile.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testFile.getContentContentType()).isEqualTo(UPDATED_CONTENT_CONTENT_TYPE);
        assertThat(testFile.getVirtualPath()).isEqualTo(UPDATED_VIRTUAL_PATH);
        assertThat(testFile.getIsDir()).isEqualTo(UPDATED_IS_DIR);
    }

    @Test
    @Transactional
    void patchNonExistingFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, file.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(file))
            )
            .andExpect(status().isBadRequest());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFile() throws Exception {
        int databaseSizeBeforeUpdate = fileRepository.findAll().size();
        file.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFileMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(file)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the File in the database
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFile() throws Exception {
        // Initialize the database
        fileRepository.saveAndFlush(file);

        int databaseSizeBeforeDelete = fileRepository.findAll().size();

        // Delete the file
        restFileMockMvc
            .perform(delete(ENTITY_API_URL_ID, file.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<File> fileList = fileRepository.findAll();
        assertThat(fileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
