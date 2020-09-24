package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "mesh_term")
public class MeshTermEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "descriptor_name", nullable = false, unique = true, length = 500)
    private String descriptorName;
    @Basic
    @Column(name = "qualifier_name")
    private String qualifierName;

    @ManyToMany(mappedBy = "meshTerms")
    private Set<DocumentEntity> documents;
}
