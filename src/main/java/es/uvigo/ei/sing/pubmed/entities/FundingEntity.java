package es.uvigo.ei.sing.pubmed.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "funding", indexes = @Index(columnList = "name,agency", name = "name_agency_index"))
public class FundingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;
    @Basic
    @Column(name = "agency", nullable = false, columnDefinition = "TEXT")
    private String agency;
    @Basic
    @Column(name = "hash", unique = true)
    private String hash;
    @Basic
    @Column(name = "country")
    private String country;
    @Basic
    @Column(name = "acronym", length = 50)
    private String acronym;

    @ManyToMany(mappedBy = "fundings")
    private Set<DocumentEntity> documents;
}
