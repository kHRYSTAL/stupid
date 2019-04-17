package me.khrystal.gmatorm.ormdto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import me.khrystal.gmatorm.greendao.DaoSession;
import me.khrystal.gmatorm.greendao.VocabularyGeneralDataDao;
import me.khrystal.gmatorm.greendao.VocabularyFlashcardDao;

/**
 * usage:
 * author: kHRYSTAL
 * create time: 19/4/16
 * update time:
 * email: 723526676@qq.com
 */
@Entity(nameInDb = "vocabularyflashcard", createInDb = false)
public class VocabularyFlashcard extends DBElement {
    private static final long serialVersionUID = 8840288520023320751L;

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

    @Property(nameInDb = "resource_uri")
    private String resource_uri;

    @Property(nameInDb = "word")
    private String word;

    @Property(nameInDb = "pronunciation")
    private String pronunciation;

    @Property(nameInDb = "definition")
    private String definition;

    @Property(nameInDb = "context_sentence")
    private String context_sentence;

    @Property(nameInDb = "not_to_be_confused_with")
    private String not_to_be_confused_with;

    @Property(nameInDb = "helpful_hints")
    private String helpful_hints;

    @Property(nameInDb = "difficulty")
    private Integer difficulty;

    @Property(nameInDb = "uuid")
    private String uuid;

    @Property(nameInDb = "is_pending_sync")
    private Integer is_pending_sync;

    @Property(nameInDb = "status")
    private Integer status;

    @OrderBy
    @Property(nameInDb = "order")
    private String order;

    private long group_1_id;

    @ToMany(referencedJoinProperty = "id")
    private List<VocabularyGeneralData> group_1;

    @ToMany(referencedJoinProperty = "id")
    private List<VocabularyGeneralData> group_2;

    @ToMany(referencedJoinProperty = "id")
    private List<VocabularyGeneralData> group_3;

    @ToMany(referencedJoinProperty = "id")
    private List<VocabularyGeneralData> part_of_speech;

    @ToMany(referencedJoinProperty = "id")
    private List<VocabularyGeneralData> connotation;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1032906703)
    private transient VocabularyFlashcardDao myDao;

    @Generated(hash = 1257640953)
    public VocabularyFlashcard(Long id, boolean isNew, String creation_date,
            String modified_data, String create_at, String updated_at,
            String client_creation_date, String resource_uri, String word,
            String pronunciation, String definition, String context_sentence,
            String not_to_be_confused_with, String helpful_hints,
            Integer difficulty, String uuid, Integer is_pending_sync,
            Integer status, String order) {
        this.id = id;
        this.isNew = isNew;
        this.creation_date = creation_date;
        this.modified_data = modified_data;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.client_creation_date = client_creation_date;
        this.resource_uri = resource_uri;
        this.word = word;
        this.pronunciation = pronunciation;
        this.definition = definition;
        this.context_sentence = context_sentence;
        this.not_to_be_confused_with = not_to_be_confused_with;
        this.helpful_hints = helpful_hints;
        this.difficulty = difficulty;
        this.uuid = uuid;
        this.is_pending_sync = is_pending_sync;
        this.status = status;
        this.order = order;
    }

    @Generated(hash = 1329542333)
    public VocabularyFlashcard() {
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

    public String getResource_uri() {
        return this.resource_uri;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPronunciation() {
        return this.pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getDefinition() {
        return this.definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getContext_sentence() {
        return this.context_sentence;
    }

    public void setContext_sentence(String context_sentence) {
        this.context_sentence = context_sentence;
    }

    public String getNot_to_be_confused_with() {
        return this.not_to_be_confused_with;
    }

    public void setNot_to_be_confused_with(String not_to_be_confused_with) {
        this.not_to_be_confused_with = not_to_be_confused_with;
    }

    public String getHelpful_hints() {
        return this.helpful_hints;
    }

    public void setHelpful_hints(String helpful_hints) {
        this.helpful_hints = helpful_hints;
    }

    public Integer getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getIs_pending_sync() {
        return this.is_pending_sync;
    }

    public void setIs_pending_sync(Integer is_pending_sync) {
        this.is_pending_sync = is_pending_sync;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 637311636)
    public List<VocabularyGeneralData> getGroup_1() {
        if (group_1 == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyGeneralDataDao targetDao = daoSession
                    .getVocabularyGeneralDataDao();
            List<VocabularyGeneralData> group_1New = targetDao
                    ._queryVocabularyFlashcard_Group_1(id);
            synchronized (this) {
                if (group_1 == null) {
                    group_1 = group_1New;
                }
            }
        }
        return group_1;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1746620057)
    public synchronized void resetGroup_1() {
        group_1 = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 679677502)
    public List<VocabularyGeneralData> getGroup_2() {
        if (group_2 == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyGeneralDataDao targetDao = daoSession
                    .getVocabularyGeneralDataDao();
            List<VocabularyGeneralData> group_2New = targetDao
                    ._queryVocabularyFlashcard_Group_2(id);
            synchronized (this) {
                if (group_2 == null) {
                    group_2 = group_2New;
                }
            }
        }
        return group_2;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 560340625)
    public synchronized void resetGroup_2() {
        group_2 = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1740220440)
    public List<VocabularyGeneralData> getGroup_3() {
        if (group_3 == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyGeneralDataDao targetDao = daoSession
                    .getVocabularyGeneralDataDao();
            List<VocabularyGeneralData> group_3New = targetDao
                    ._queryVocabularyFlashcard_Group_3(id);
            synchronized (this) {
                if (group_3 == null) {
                    group_3 = group_3New;
                }
            }
        }
        return group_3;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1485274262)
    public synchronized void resetGroup_3() {
        group_3 = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 60285083)
    public List<VocabularyGeneralData> getPart_of_speech() {
        if (part_of_speech == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyGeneralDataDao targetDao = daoSession
                    .getVocabularyGeneralDataDao();
            List<VocabularyGeneralData> part_of_speechNew = targetDao
                    ._queryVocabularyFlashcard_Part_of_speech(id);
            synchronized (this) {
                if (part_of_speech == null) {
                    part_of_speech = part_of_speechNew;
                }
            }
        }
        return part_of_speech;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1474957510)
    public synchronized void resetPart_of_speech() {
        part_of_speech = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 521470610)
    public List<VocabularyGeneralData> getConnotation() {
        if (connotation == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            VocabularyGeneralDataDao targetDao = daoSession
                    .getVocabularyGeneralDataDao();
            List<VocabularyGeneralData> connotationNew = targetDao
                    ._queryVocabularyFlashcard_Connotation(id);
            synchronized (this) {
                if (connotation == null) {
                    connotation = connotationNew;
                }
            }
        }
        return connotation;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1522203414)
    public synchronized void resetConnotation() {
        connotation = null;
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
    @Generated(hash = 706025415)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getVocabularyFlashcardDao() : null;
    }
}
