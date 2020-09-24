package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "author")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "last_name", nullable = false, columnDefinition = "TEXT")
    private String lastName;
    @Basic
    @Column(name = "fore_name", nullable = false, columnDefinition = "TEXT")
    private String foreName;
    @Basic
    @Column(name = "initials", nullable = false, length = 100)
    private String initials;
    @Basic
    @Column(name = "hash", unique = true, length = 256)
    private String hash;

    @ManyToMany(mappedBy = "authors")
    private Set<DocumentEntity> documents;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "author_affiliation",
            joinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "affiliation_id", referencedColumnName = "id", nullable = false))
    private Set<AffiliationEntity> affiliations;
}
