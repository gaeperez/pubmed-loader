package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "affiliation")
public class AffiliationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
    @Basic
    @Column(name = "hash", unique = true, length = 256)
    private String hash;

    @ManyToMany(mappedBy = "affiliations")
    private Set<AuthorEntity> authors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffiliationEntity that = (AffiliationEntity) o;
        return Objects.equals(name.toLowerCase(), that.name.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }
}
