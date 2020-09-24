package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "substance", indexes = @Index(columnList = "name,registry_number", name = "uniqueness", unique = true))
public class SubstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 500)
    private String name;
    @Basic
    @Column(name = "registry_number")
    private String registryNumber;

    @ManyToMany(mappedBy = "substances")
    private Set<DocumentEntity> documents;
}
