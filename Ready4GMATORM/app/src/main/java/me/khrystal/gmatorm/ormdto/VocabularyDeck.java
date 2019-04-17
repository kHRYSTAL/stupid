package me.khrystal.gmatorm.ormdto;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

import me.khrystal.gmatorm.greendao.DaoSession;
import me.khrystal.gmatorm.greendao.VocabularyCategoryDao;
import me.khrystal.gmatorm.greendao.VocabularyDeckDao;
import me.khrystal.gmatorm.greendao.VocabularyFlashcardDao;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
@Entity(nameInDb = "vocabularydeck", createInDb = false)
public class VocabularyDeck extends DBElement {

    private static final long serialVersionUID = 7710496601172511629L;

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

    @OrderBy
    @Property(nameInDb = "order")
    private int order;

    @Unique
    @Property(nameInDb = "uuid")
    private String uuid;

    @Property(nameInDb = "is_active")
    private boolean is_active = true;

    @Property(nameInDb = "category_id")
    private long category_id;

    @ToOne(joinProperty = "category_id")
    private VocabularyCategory category; // 与VocabularyCategory一对一关系

    @ToMany
    @JoinEntity(entity = VocabularyDeckFlashcard.class, sourceProperty = "deck_id", targetProperty = "flashcard_id")
    private List<VocabularyFlashcard> flashcards;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1856098116)
    private transient VocabularyDeckDao myDao;

    @Generated(hash = 222292925)
    public VocabularyDeck(Long id, boolean isNew, String creation_date, String modified_data, String create_at,
            String updated_at, String client_creation_date, String title, String resource_uri, int order, String uuid,
            boolean is_active, long category_id) {
        this.id = id;
        this.isNew = isNew;
        this.creation_date = creation_date;
        this.modified_data = modified_data;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.client_creation_date = client_creation_date;
        this.title = title;
        this.resource_uri = resource_uri;
        this.order = order;
        this.uuid = uuid;
        this.is_active = is_active;
        this.category_id = category_id;
    }

    @Generated(hash = 1362530265)
    public VocabularyDeck() {
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

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public long getCategory_id() {
        return this.category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 546702196)
    public VocabularyCategory getCategory() {
        long __key = this.category_id;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyCategoryDao targetDao = daoSession.getVocabularyCategoryDao();
            VocabularyCategory categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1310853638)
    public void setCategory(@NotNull VocabularyCategory category) {
        if (category == null) {
            throw new DaoException("To-one property 'category_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.category = category;
            category_id = category.getId();
            category__resolvedKey = category_id;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 701744669)
    public List<VocabularyFlashcard> getFlashcards() {
        if (flashcards == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyFlashcardDao targetDao = daoSession.getVocabularyFlashcardDao();
            List<VocabularyFlashcard> flashcardsNew = targetDao._queryVocabularyDeck_Flashcards(id);
            synchronized (this) {
                if (flashcards == null) {
                    flashcards = flashcardsNew;
                }
            }
        }
        return flashcards;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 982924541)
    public synchronized void resetFlashcards() {
        flashcards = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 801445632)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVocabularyDeckDao() : null;
    }
}
