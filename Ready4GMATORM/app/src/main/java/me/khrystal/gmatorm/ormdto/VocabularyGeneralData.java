package me.khrystal.gmatorm.ormdto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
@Entity(nameInDb = "vocabularygeneraldata", createInDb = false)
public class VocabularyGeneralData extends DBElement {

    private static final long serialVersionUID = -3801845631989330760L;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "is_new")
    private boolean isNew;

    @Property(nameInDb = "creation_date")
    private String creation_date;

    @Property(nameInDb = "modified_data")
    private String modified_data;

    @Property(nameInDb = "created_at")
    private String create_at;

    @Property(nameInDb = "updated_at")
    private String updated_at;

    @Property(nameInDb = "client_creation_date")
    private String client_creation_date;

    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "resource_uri")
    private String resource_uri;

    @Property(nameInDb = "type")
    private Integer type;

    @Property(nameInDb = "uuid")
    private String uuid;

    @Generated(hash = 823076132)
    public VocabularyGeneralData(Long id, boolean isNew, String creation_date,
            String modified_data, String create_at, String updated_at,
            String client_creation_date, String title, String resource_uri,
            Integer type, String uuid) {
        this.id = id;
        this.isNew = isNew;
        this.creation_date = creation_date;
        this.modified_data = modified_data;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.client_creation_date = client_creation_date;
        this.title = title;
        this.resource_uri = resource_uri;
        this.type = type;
        this.uuid = uuid;
    }

    @Generated(hash = 1044211902)
    public VocabularyGeneralData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIsNew() {
        return this.isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getCreation_date() {
        return this.creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public String getModified_data() {
        return this.modified_data;
    }

    public void setModified_data(String modified_data) {
        this.modified_data = modified_data;
    }

    public String getCreate_at() {
        return this.create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return this.updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClient_creation_date() {
        return this.client_creation_date;
    }

    public void setClient_creation_date(String client_creation_date) {
        this.client_creation_date = client_creation_date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource_uri() {
        return this.resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
