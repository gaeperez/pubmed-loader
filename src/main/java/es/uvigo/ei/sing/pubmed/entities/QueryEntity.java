package es.uvigo.ei.sing.pubmed.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "query", indexes = {@Index(columnList = ("value,type"), name = "value_name_UNIQUE")})
public class QueryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "value", nullable = false, length = 500)
    private String value;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "enum")
    private QueryType type;
    @Basic
    @Column(name = "ret_max", nullable = false)
    private int retMax;
    @Basic
    @Column(name = "current_page", nullable = false)
    private int currentPage;
    @Basic
    @Column(name = "limit_pages", nullable = false)
    private int limitPages;
    @Enumerated(EnumType.STRING)
    @Column(name = "sort_mode", nullable = false, columnDefinition = "enum")
    private SortMode sortMode;
    @Basic
    @Column(name = "suspended")
    private boolean isSuspended;
    @Basic
    @Column(name = "created")
    private LocalDateTime created;
    @Basic
    @Column(name = "updated")
    private LocalDateTime updated;
    @Basic
    @Column(name = "times_executed")
    private int timesExecuted;
    @Basic
    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "query_document",
            joinColumns = @JoinColumn(name = "query_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false))
    private Set<DocumentEntity> documents = new HashSet<>();

    public void incrementTimesExecuted() {
        this.timesExecuted++;
    }

    // Allowed values to the type in the database
    private enum QueryType {
        QUERY,
        IDS
    }

    public int getRetStart() {
        return this.currentPage * this.retMax;
    }

    // Allowed values to the sort mode
    @AllArgsConstructor
    private enum SortMode {
        JOURNAL("journal"),
        PUBDATE("pub+date"),
        MOSTRECENT("most+recent"),
        RELEVANCE("relevance"),
        TITLE("title"),
        AUTHOR("author");

        private String value;

        public String toString() {
            return this.value;
        }
    }
}
