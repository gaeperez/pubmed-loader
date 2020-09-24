package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "journal", indexes = @Index(columnList = "issn,isbn,name", name = "uniqueness", unique = true))
public class JournalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 500)
    private String name;
    @Basic
    @Column(name = "issn")
    private String issn;
    @Basic
    @Column(name = "isbn")
    private String isbn;
    @Basic
    @Column(name = "abbreviation", length = 400)
    private String abbreviation;
    @Basic
    @Column(name = "volume", length = 200)
    private String volume;
    @Basic
    @Column(name = "issue", length = 100)
    private String issue;
    @Basic
    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "journal")
    private Set<DocumentEntity> documents = new HashSet<>();
}
