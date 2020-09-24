package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "reference")
public class ReferenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;
    @Basic
    @Column(name = "name", length = 1000)
    private String name;

    @ManyToMany(mappedBy = "references")
    private Set<DocumentEntity> documents;
}
