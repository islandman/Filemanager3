package com.mycompany.myapp.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Task entity.\n@author The JHipster team.
 */
@Schema(description = "Task entity.\n@author The JHipster team.")
@Entity
@Table(name = "directory")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "parent_uuid")
    private String parentUuid;

    @Column(name = "name")
    private String name;

    @Column(name = "tenant_id")
    private Long tenantId;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "num_files")
    private Integer numFiles;

    @Column(name = "is_dir")
    private Boolean isDir;

    @Column(name = "virtual_path")
    private String virtualPath;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Directory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Directory uuid(String uuid) {
        this.setUuid(uuid);
        return this;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getParentUuid() {
        return this.parentUuid;
    }

    public Directory parentUuid(String parentUuid) {
        this.setParentUuid(parentUuid);
        return this;
    }

    public void setParentUuid(String parentUuid) {
        this.parentUuid = parentUuid;
    }

    public String getName() {
        return this.name;
    }

    public Directory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public Directory tenantId(Long tenantId) {
        this.setTenantId(tenantId);
        return this;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Instant getCreateDate() {
        return this.createDate;
    }

    public Directory createDate(Instant createDate) {
        this.setCreateDate(createDate);
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getUserName() {
        return this.userName;
    }

    public Directory userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getNumFiles() {
        return this.numFiles;
    }

    public Directory numFiles(Integer numFiles) {
        this.setNumFiles(numFiles);
        return this;
    }

    public void setNumFiles(Integer numFiles) {
        this.numFiles = numFiles;
    }

    public Boolean getIsDir() {
        return this.isDir;
    }

    public Directory isDir(Boolean isDir) {
        this.setIsDir(isDir);
        return this;
    }

    public void setIsDir(Boolean isDir) {
        this.isDir = isDir;
    }

    public String getVirtualPath() {
        return this.virtualPath;
    }

    public Directory virtualPath(String virtualPath) {
        this.setVirtualPath(virtualPath);
        return this;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Directory)) {
            return false;
        }
        return id != null && id.equals(((Directory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Directory{" +
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", parentUuid='" + getParentUuid() + "'" +
            ", name='" + getName() + "'" +
            ", tenantId=" + getTenantId() +
            ", createDate='" + getCreateDate() + "'" +
            ", userName='" + getUserName() + "'" +
            ", numFiles=" + getNumFiles() +
            ", isDir='" + getIsDir() + "'" +
            ", virtualPath='" + getVirtualPath() + "'" +
            "}";
    }
}
