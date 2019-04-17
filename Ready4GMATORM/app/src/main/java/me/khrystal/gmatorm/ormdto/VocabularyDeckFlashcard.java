package me.khrystal.gmatorm.ormdto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * usage: 多对多关联关系中间表 仅在内部做关联关系使用 外部不显式调用
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
@Entity(nameInDb = "vocabularydeckflashcard", createInDb = false)
public class VocabularyDeckFlashcard extends DBElement {
    private static final long serialVersionUID = -3164357870981780726L;

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

    @Unique
    @Property(nameInDb = "uuid")
    private String uuid;

    @OrderBy
    @Property(nameInDb = "order")
    private String order;

    @Property(nameInDb = "deck_id")
    private Long deck_id;

    @Property(nameInDb = "flashcard_id")
    private Long flashcard_id;

    @Generated(hash = 133100775)
    public VocabularyDeckFlashcard(Long id, boolean isNew, String creation_date,
            String modified_data, String create_at, String updated_at,
            String client_creation_date, String uuid, String order, Long deck_id,
            Long flashcard_id) {
        this.id = id;
        this.isNew = isNew;
        this.creation_date = creation_date;
        this.modified_data = modified_data;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.client_creation_date = client_creation_date;
        this.uuid = uuid;
        this.order = order;
        this.deck_id = deck_id;
        this.flashcard_id = flashcard_id;
    }

    @Generated(hash = 90805767)
    public VocabularyDeckFlashcard() {
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

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getDeck_id() {
        return this.deck_id;
    }

    public void setDeck_id(Long deck_id) {
        this.deck_id = deck_id;
    }

    public Long getFlashcard_id() {
        return this.flashcard_id;
    }

    public void setFlashcard_id(Long flashcard_id) {
        this.flashcard_id = flashcard_id;
    }
}
