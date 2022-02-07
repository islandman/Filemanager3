package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "directory_uuid")
    private String directoryUuid;

    @Column(name = "name")
    private String name;

    @Column(name = "remote_path")
    private String remotePath;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "sizz")
    private Long sizz;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "user_name")
    private String userName;

    @Lob
    @Column(name = "content")
    private byte[] content;

    @Column(name = "content_content_type")
    private String contentContentType;

    @Column(name = "virtual_path")
    private String virtualPath;

    @Column(name = "is_dir")
    private Boolean isDir;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public File id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public File uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDirectoryUuid() {
        return this.directoryUuid;
    }

    public File directoryUuid(String directoryUuid) {
        this.setDirectoryUuid(directoryUuid);
        return this;
    }

    public void setDirectoryUuid(String directoryUuid) {
        this.directoryUuid = directoryUuid;
    }

    public String getName() {
        return this.name;
    }

    public File name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemotePath() {
        return this.remotePath;
    }

    public File remotePath(String remotePath) {
        this.setRemotePath(remotePath);
        return this;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public File tenantId(Long tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getContentType() {
        return this.contentType;
    }

    public File contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSizz() {
        return this.sizz;
    }

    public File sizz(Long sizz) {
        this.setSizz(sizz);
        return this;
    }

    public void setSizz(Long sizz) {
        this.sizz = sizz;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public File createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return this.userName;
    }

    public File userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getContent() {
        return this.content;
    }

    public File content(byte[] content) {
        this.setContent(content);
        return this;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentContentType() {
        return this.contentContentType;
    }

    public File contentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
        return this;
    }

    public void setContentContentType(String contentContentType) {
        this.contentContentType = contentContentType;
    }

    public String getVirtualPath() {
        return this.virtualPath;
    }

    public File virtualPath(String virtualPath) {
        this.setVirtualPath(virtualPath);
        return this;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public Boolean getIsDir() {
        return this.isDir;
    }

    public File isDir(Boolean isDir) {
        this.setIsDir(isDir);
        return this;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "File{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", directoryUuid='" + getDirectoryUuid() + "'" +
            ", name='" + getName() + "'" +
            ", remotePath='" + getRemotePath() + "'" +
            ", tenantId=" + getTenantId() +
            ", contentType='" + getContentType() + "'" +
            ", sizz=" + getSizz() +
            ", createDate='" + getCreateDate() + "'" +
            ", userName='" + getUserName() + "'" +
            ", content='" + getContent() + "'" +
            ", contentContentType='" + getContentContentType() + "'" +
            ", virtualPath='" + getVirtualPath() + "'" +
            ", isDir='" + getIsDir() + "'" +
            "}";
    }
}
