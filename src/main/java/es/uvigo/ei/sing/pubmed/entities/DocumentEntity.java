package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "document", indexes = {@Index(columnList = "journal_id", name = "fk_document_journal_id"),
        @Index(columnList = "external_id,doi", name = "uniqueness")})
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "external_id", nullable = false, length = 25)
    private String externalId;
    @Basic
    @Column(name = "doi")
    private String doi;
    @Basic
    @Column(name = "title", columnDefinition = "TEXT")
    private String title;
    @Basic
    @Column(name = "body", columnDefinition = "TEXT")
    private String body;
    @Basic
    @Column(name = "language", length = 25)
    private String language;
    @Basic
    @Column(name = "received_date")
    private LocalDate receivedDate;
    @Basic
    @Column(name = "accepted_date")
    private LocalDate acceptedDate;
    @Basic
    @Column(name = "entrez_date")
    private LocalDate entrezDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "journal_id", referencedColumnName = "id", nullable = false)
    private JournalEntity journal;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_mesh_term", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "mesh_term_id", referencedColumnName = "id", nullable = false))
    private Set<MeshTermEntity> meshTerms;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_funding", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "funding_id", referencedColumnName = "id", nullable = false))
    private Set<FundingEntity> fundings;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_author", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false))
    private Set<AuthorEntity> authors;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_keyword", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "keyword_id", referencedColumnName = "id", nullable = false))
    private Set<KeywordEntity> keywords;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_publication_type", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "publication_type_id", referencedColumnName = "id", nullable = false))
    private Set<PublicationTypeEntity> publicationTypes;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_reference", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "reference_id", referencedColumnName = "id", nullable = false))
    private Set<ReferenceEntity> references;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "document_substance", schema = "grant_support",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "substance_id", referencedColumnName = "id", nullable = false))
    private Set<SubstanceEntity> substances;

    @ManyToMany
    @JoinTable(name = "query_document",
            joinColumns = @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "query_id", referencedColumnName = "id", nullable = false))
    private Set<QueryEntity> queries = new HashSet<>();
}
