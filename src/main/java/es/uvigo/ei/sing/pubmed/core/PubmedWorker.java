package es.uvigo.ei.sing.pubmed.core;

import es.uvigo.ei.sing.pubmed.core.eutils.EntrezFetch;
import es.uvigo.ei.sing.pubmed.core.eutils.EntrezSearch;
import es.uvigo.ei.sing.pubmed.core.eutils.io.InputStreamParser;
import es.uvigo.ei.sing.pubmed.entities.*;
import es.uvigo.ei.sing.pubmed.services.*;
import es.uvigo.ei.sing.pubmed.utils.AppConstants;
import es.uvigo.ei.sing.pubmed.utils.AppFunctions;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

@Setter
@Log4j2
@Component
@Scope("prototype")
class PubmedWorker implements Callable<DocumentEntity> {

    private final AffiliationService affiliationService;
    private final AuthorService authorService;
    private final FundingService fundingService;
    private final JournalService journalService;
    private final KeywordService keywordService;
    private final MeshTermService meshTermService;
    private final PublicationTypeService publicationTypeService;
    private final ReferenceService referenceService;
    private final SubstanceService substanceService;

    // Synchronized variables among threads
    private Map<String, DocumentEntity> mapDocumentEntities;
    private Map<String, AuthorEntity> mapAuthorEntities;
    private Map<String, AffiliationEntity> mapAffiliationEntities;
    private Map<String, FundingEntity> mapFundingEntities;
    private Map<String, PublicationTypeEntity> mapPublicationTypeEntities;
    private Map<String, SubstanceEntity> mapSubstanceEntities;
    private Map<String, MeshTermEntity> mapMeshTermEntities;
    private Map<String, KeywordEntity> mapKeywordEntities;
    private Map<String, ReferenceEntity> mapReferenceEntities;
    private Map<String, JournalEntity> mapJournalEntities;

    private String savedID;
    private boolean done = false;

    @Autowired
    public PubmedWorker(AffiliationService affiliationService, AuthorService authorService,
                        FundingService fundingService, JournalService journalService, KeywordService keywordService,
                        MeshTermService meshTermService, PublicationTypeService publicationTypeService,
                        ReferenceService referenceService, SubstanceService substanceService) {
        this.affiliationService = affiliationService;
        this.authorService = authorService;
        this.fundingService = fundingService;
        this.journalService = journalService;
        this.keywordService = keywordService;
        this.meshTermService = meshTermService;
        this.publicationTypeService = publicationTypeService;
        this.referenceService = referenceService;
        this.substanceService = substanceService;
    }

    @Override
    public DocumentEntity call() {
        // Document variables
        DocumentEntity toRet;
        DocumentEntity documentEntity = new DocumentEntity();

        try {
            log.info(AppConstants.STR_INFO_RETRIEVAL, savedID);

            EntrezSearch search = new EntrezSearch();
            search.setIds(savedID);

            // Motor to obtain the texts from IDs
            EntrezFetch fetch = new EntrezFetch(search);
            // Set parameters
            fetch.setDatabase(AppConstants.PUBMED_DATABASE);
            fetch.setRetType(AppConstants.PUBMED_RET_TYPE);
            fetch.setRetMode(AppConstants.PUBMED_RET_MODE);
            fetch.setTimeout(AppConstants.PUBMED_TIMEOUT);
            fetch.setEncoding(AppConstants.PUBMED_ENCODING);

            fetch.doQuery(new InputStreamParser() {
                @Override
                public void parseFrom(int start) {
                }// do nothing

                @Override
                public void parseTo(int end) {
                }// do nothing

                @Override
                public void parseInput(InputStream is) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        String line;

                        // Document variables
                        String cleanLine;
                        String documentPMID = "", documentTitle = "", documentBody = "", documentDOI = "";
                        String authorLastName = "", authorForeName = "", authorInitials = "";
                        String fundingName = "", fundingAgency = "", fundingCountry = "", fundingAcronym = "";
                        String substanceRegNumber = "", substanceName = "";
                        String meshTermDescName = "", meshTermQualName = "";
                        String referenceCitation = "", referencePMID = "";
                        int year = 1900, month = 1, day = 1;
                        boolean isValid = true;
                        boolean isBody = false;
                        boolean isFunding = false;
                        boolean isJournal = false;
                        boolean isAuthor = false;
                        boolean isSubstance = false;
                        boolean isMeshTerm = false;
                        boolean isDateReceived = false;
                        boolean isDateAccepted = false;
                        boolean isDateEntrez = false;
                        boolean isReference = false;

                        // Entities
                        JournalEntity journalEntity = new JournalEntity();
                        String mapJournalKey = "";
                        Set<AuthorEntity> authorEntities = new HashSet<>();
                        Set<AffiliationEntity> affiliationEntities = new HashSet<>();
                        Set<FundingEntity> fundingEntities = new HashSet<>();
                        Set<PublicationTypeEntity> publicationTypeEntities = new HashSet<>();
                        Set<SubstanceEntity> substanceEntities = new HashSet<>();
                        Set<MeshTermEntity> meshTermEntities = new HashSet<>();
                        Set<KeywordEntity> keywordEntities = new HashSet<>();
                        Set<ReferenceEntity> referenceEntities = new HashSet<>();

                        while ((line = br.readLine()) != null) {
                            line = line.trim();

                            // Unescape special characters
                            line = StringEscapeUtils.unescapeXml(line);

                            // Delete XML tags
                            cleanLine = line.replaceAll(AppConstants.XML_REGEX_REMOVE_TAGS, "");

                            // Get properties
                            if (line.contains(AppConstants.XML_PUBMED_BOOK)) {
                                // Parse only the journals, not the books
                                // TODO: 19/02/2019 Enable this parsing if it is interesting in the future
                                log.warn(AppConstants.STR_WORKER_WARN_DOCTYPE, line, documentPMID);
                                isValid = false;
                                break;
                            } else if (line.contains(AppConstants.XML_PMID) && documentPMID.isEmpty()) {
                                // The document PMID is one of the first values of the XML file.
                                // Once set, do not allow to set it again
                                documentPMID = cleanLine;
                                documentEntity.setExternalId(documentPMID);
                            } else if (line.contains(AppConstants.XML_JOURNAL_ISSN)) {
                                journalEntity.setIssn(cleanLine);
                                mapJournalKey += cleanLine;
                            } else if (line.contains(AppConstants.XML_JOURNAL_ISBN)) {
                                journalEntity.setIsbn(cleanLine);
                                mapJournalKey += cleanLine;
                            } else if (line.contains(AppConstants.XML_JOURNAL_VOLUME)) {
                                journalEntity.setVolume(cleanLine);
                            } else if (line.contains(AppConstants.XML_JOURNAL_ISSUE)) {
                                journalEntity.setIssue(cleanLine);
                            } else if (line.contains(AppConstants.XML_JOURNAL_TITLE)) {
                                journalEntity.setName(cleanLine);
                                mapJournalKey += cleanLine;
                            } else if (line.contains(AppConstants.XML_JOURNAL_ABBR)) {
                                journalEntity.setAbbreviation(cleanLine);
                            } else if (line.contains(AppConstants.XML_TITLE)) {
                                // Multiline titles
                                documentTitle += documentTitle.isEmpty() ? cleanLine.trim() : " " + cleanLine.trim();
                            } else if (line.contains(AppConstants.XML_DOI)) {
                                documentDOI = cleanLine;
                            } else if (line.contains(AppConstants.XML_ABSTRACT_START)) {
                                isBody = true;
                            } else if (line.contains(AppConstants.XML_ABSTRACT_END)) {
                                isBody = false;
                            } else if (line.contains(AppConstants.XML_ABSTRACT) && isBody) {
                                // Concatenate abstracts with sections
                                documentBody += documentBody.isEmpty() ? cleanLine.trim() : " " + cleanLine.trim();
                            } else if (line.contains(AppConstants.XML_AUTHOR_START)) {
                                isAuthor = true;
                            } else if (line.contains(AppConstants.XML_AUTHOR_END)) {
                                // Set isAuthor to false
                                isAuthor = false;

                                // Check if author is in the database
                                boolean exist;
                                AuthorEntity authorEntity;
                                String mapKey = authorLastName + authorForeName + authorInitials;

                                // Synchronized this to ensure all threads read the same variable
                                String authorHash = AppFunctions.doHash(mapKey);
                                Optional<AuthorEntity> possibleAuthor = authorService.findByHash(authorHash);
                                synchronized (mapAuthorEntities) {
                                    if (mapAuthorEntities.containsKey(authorHash)) {
                                        authorEntity = mapAuthorEntities.get(authorHash);

                                        exist = true;
                                    } else if (possibleAuthor.isPresent()) {
                                        authorEntity = possibleAuthor.get();

                                        exist = true;
                                    } else {
                                        // Add author
                                        authorEntity = new AuthorEntity();
                                        authorEntity.setLastName(authorLastName);
                                        authorEntity.setForeName(authorForeName);
                                        authorEntity.setInitials(authorInitials);
                                        authorEntity.setAffiliations(affiliationEntities);
                                        authorEntity.setHash(authorHash);

                                        exist = false;
                                    }

                                    if (exist) {
                                        // Check if there are new affiliations for this author
                                        Set<AffiliationEntity> newAffiliations = authorService
                                                .obtainNewAffiliations(authorEntity.getAffiliations(), affiliationEntities);

                                        // Add new affiliations to the author
                                        if (!newAffiliations.isEmpty())
                                            for (AffiliationEntity newAffiliation : newAffiliations)
                                                authorEntity.getAffiliations().add(newAffiliation);
                                    }

                                    // Add author
                                    mapAuthorEntities.put(authorHash, authorEntity);
                                    authorEntities.add(authorEntity);
                                }

                                // Clear variables
                                authorLastName = "";
                                authorForeName = "";
                                authorInitials = "";
                                affiliationEntities = new HashSet<>();
                            } else if ((line.contains(AppConstants.XML_AUTHOR_LASTNAME) ||
                                    line.contains(AppConstants.XML_AUTHOR_GROUP)) && isAuthor) {
                                // Consider the collective group as an author last name in the database
                                authorLastName = cleanLine;
                            } else if (line.contains(AppConstants.XML_AUTHOR_FORENAME) && isAuthor) {
                                authorForeName = cleanLine;
                            } else if (line.contains(AppConstants.XML_AUTHOR_INITIALS) && isAuthor) {
                                authorInitials = cleanLine;
                            } else if (line.contains(AppConstants.XML_AUTHOR_AFFILIATION) && isAuthor) {
                                // Get affiliation and delete possible emails
                                String affiliationName = cleanLine.replaceAll(AppConstants.XML_REGEX_EMAILS, "").trim();
                                // If the name starts with a letter or number plus a whitespace (e.g. a ESEI, b CINBIO...)
                                if (affiliationName.matches("^([a-z0-9]\\s+).*$"))
                                    affiliationName = affiliationName.substring(2);

                                // Check if affiliation is in the database
                                AffiliationEntity affiliationEntity;

                                String affiliationHash = AppFunctions.doHash(affiliationName);
                                Optional<AffiliationEntity> possibleAffiliation = affiliationService.findByHash(affiliationHash);
                                synchronized (mapAffiliationEntities) {
                                    if (mapAffiliationEntities.containsKey(affiliationHash)) {
                                        affiliationEntity = mapAffiliationEntities.get(affiliationHash);
                                    } else if (possibleAffiliation.isPresent()) {
                                        affiliationEntity = possibleAffiliation.get();
                                    } else {
                                        affiliationEntity = new AffiliationEntity();
                                        affiliationEntity.setName(affiliationName);
                                        affiliationEntity.setHash(affiliationHash);
                                    }

                                    // Add affiliation
                                    affiliationEntities.add(affiliationEntity);
                                    mapAffiliationEntities.put(affiliationHash, affiliationEntity);
                                }
                            } else if (line.contains(AppConstants.XML_LANGUAGE)) {
                                documentEntity.setLanguage(cleanLine);
                            } else if (line.contains(AppConstants.XML_GRANT_START)) {
                                isFunding = true;
                            } else if (line.contains(AppConstants.XML_GRANT_END)) {
                                // Set isFunding to false
                                isFunding = false;

                                // Check if grant is in the database
                                FundingEntity fundingEntity;
                                String mapKey = fundingName + fundingAgency;

                                String fundingHash = AppFunctions.doHash(mapKey);
                                Optional<FundingEntity> possibleFunding = fundingService.findByHash(fundingHash);
                                synchronized (mapFundingEntities) {
                                    if (mapFundingEntities.containsKey(fundingHash)) {
                                        fundingEntity = mapFundingEntities.get(fundingHash);
                                    } else if (possibleFunding.isPresent()) {
                                        fundingEntity = possibleFunding.get();
                                    } else {
                                        fundingEntity = new FundingEntity();
                                        fundingEntity.setName(fundingName);
                                        fundingEntity.setAgency(fundingAgency);
                                        fundingEntity.setCountry(fundingCountry);
                                        fundingEntity.setAcronym(fundingAcronym);
                                        fundingEntity.setHash(fundingHash);
                                    }

                                    // Add funding
                                    fundingEntities.add(fundingEntity);
                                    mapFundingEntities.put(fundingHash, fundingEntity);
                                }

                                // Clear variables
                                fundingName = "";
                                fundingAgency = "";
                                fundingCountry = "";
                            } else if (line.contains(AppConstants.XML_GRANT_ID) && isFunding) {
                                fundingName = cleanLine.replaceAll("\\s", "");
                            } else if (line.contains(AppConstants.XML_GRANT_AGENCY) && isFunding) {
                                fundingAgency = cleanLine;
                            } else if (line.contains(AppConstants.XML_GRANT_COUNTRY) && isFunding) {
                                fundingCountry = cleanLine;
                            } else if (line.contains(AppConstants.XML_GRANT_ACRONYM) && isFunding) {
                                fundingAcronym = cleanLine;
                            } else if (line.contains(AppConstants.XML_TYPE)) {
                                // Check if PubType is in the database
                                PublicationTypeEntity publicationTypeEntity;

                                Optional<PublicationTypeEntity> possiblePutType = publicationTypeService.findByName(cleanLine);
                                synchronized (mapPublicationTypeEntities) {
                                    if (mapPublicationTypeEntities.containsKey(cleanLine)) {
                                        publicationTypeEntity = mapPublicationTypeEntities.get(cleanLine);
                                    } else if (possiblePutType.isPresent()) {
                                        publicationTypeEntity = possiblePutType.get();
                                    } else {
                                        publicationTypeEntity = new PublicationTypeEntity();
                                        publicationTypeEntity.setName(cleanLine);
                                    }

                                    // Add PubType
                                    publicationTypeEntities.add(publicationTypeEntity);
                                    mapPublicationTypeEntities.put(cleanLine, publicationTypeEntity);
                                }
                            } else if (line.contains(AppConstants.XML_JOURNAL_MEDLINE_START)) {
                                isJournal = true;
                            } else if (line.contains(AppConstants.XML_JOURNAL_MEDLINE_END)) {
                                isJournal = false;
                            } else if (line.contains(AppConstants.XML_JOURNAL_COUNTRY) && isJournal) {
                                journalEntity.setCountry(cleanLine);
                            } else if (line.contains(AppConstants.XML_SUBSTANCE_START)) {
                                isSubstance = true;
                            } else if (line.contains(AppConstants.XML_SUBSTANCE_END)) {
                                // Set isSubstance to false
                                isSubstance = false;

                                // Check if substance is in the database
                                SubstanceEntity substanceEntity;
                                String mapKey = substanceRegNumber + substanceName;

                                Optional<SubstanceEntity> possibleSubstance = substanceService.findByName(substanceName);
                                synchronized (mapSubstanceEntities) {
                                    if (mapSubstanceEntities.containsKey(mapKey)) {
                                        substanceEntity = mapSubstanceEntities.get(mapKey);
                                    } else if (possibleSubstance.isPresent()) {
                                        substanceEntity = possibleSubstance.get();
                                    } else {
                                        substanceEntity = new SubstanceEntity();
                                        substanceEntity.setName(substanceName);
                                        substanceEntity.setRegistryNumber(substanceRegNumber);
                                    }

                                    // Add substance
                                    substanceEntities.add(substanceEntity);
                                    mapSubstanceEntities.put(mapKey, substanceEntity);
                                }

                                // Clear variables
                                substanceName = "";
                                substanceRegNumber = "";
                            } else if (line.contains(AppConstants.XML_SUBSTANCE_REGNUMBER) && isSubstance) {
                                substanceRegNumber = cleanLine;
                            } else if (line.contains(AppConstants.XML_SUBSTANCE_NAME) && isSubstance) {
                                substanceName = cleanLine;
                            } else if (line.contains(AppConstants.XML_MESH_START)) {
                                isMeshTerm = true;
                            } else if (line.contains(AppConstants.XML_MESH_END)) {
                                // Set isMeshTerm to false
                                isMeshTerm = false;

                                // Check if mesh term is in the database
                                MeshTermEntity meshTermEntity;

                                Optional<MeshTermEntity> possibleMesh = meshTermService.findByDescriptorName(meshTermDescName);
                                synchronized (mapMeshTermEntities) {
                                    if (mapMeshTermEntities.containsKey(meshTermDescName)) {
                                        meshTermEntity = mapMeshTermEntities.get(meshTermDescName);
                                    } else if (possibleMesh.isPresent()) {
                                        meshTermEntity = possibleMesh.get();
                                    } else {
                                        meshTermEntity = new MeshTermEntity();
                                        meshTermEntity.setDescriptorName(meshTermDescName);
                                        meshTermEntity.setQualifierName(meshTermQualName);
                                    }

                                    // Add MeshTerm
                                    meshTermEntities.add(meshTermEntity);
                                    mapMeshTermEntities.put(meshTermDescName, meshTermEntity);
                                }

                                // Clear variables
                                meshTermDescName = "";
                                meshTermQualName = "";
                            } else if (line.contains(AppConstants.XML_MESH_DESCNAME) && isMeshTerm) {
                                meshTermDescName = cleanLine;
                            } else if (line.contains(AppConstants.XML_MESH_QUALNAME) && isMeshTerm) {
                                // Save only major qualifier or save a minor if there is no other
                                if ((line.contains(AppConstants.XML_MESH_QUALNAME_MAJOR))
                                        || (!line.contains(AppConstants.XML_MESH_QUALNAME_MAJOR) && meshTermQualName.isEmpty()))
                                    meshTermQualName = cleanLine;
                            } else if (line.contains(AppConstants.XML_KEYWORD_NAME)) {
                                // Check if keyword is empty (due to a bad composition of the XML file)
                                if (!cleanLine.isEmpty()) {
                                    // Check if keyword is in the database
                                    KeywordEntity keywordEntity;

                                    Optional<KeywordEntity> possibleKeyword = keywordService.findByName(cleanLine);
                                    synchronized (mapKeywordEntities) {
                                        if (mapKeywordEntities.containsKey(cleanLine)) {
                                            keywordEntity = mapKeywordEntities.get(cleanLine);
                                        } else if (possibleKeyword.isPresent()) {
                                            keywordEntity = possibleKeyword.get();
                                        } else {
                                            keywordEntity = new KeywordEntity();
                                            keywordEntity.setName(cleanLine);
                                        }

                                        // Add keyword
                                        keywordEntities.add(keywordEntity);
                                        mapKeywordEntities.put(cleanLine, keywordEntity);
                                    }
                                }
                            } else if (line.contains(AppConstants.XML_DATE_RECEIVED_START)) {
                                isDateReceived = true;
                            } else if (line.contains(AppConstants.XML_DATE_ACCEPTED_START)) {
                                isDateAccepted = true;
                            } else if (line.contains(AppConstants.XML_DATE_ENTREZ_START)) {
                                isDateEntrez = true;
                            } else if (line.contains(AppConstants.XML_DATE_END)) {
                                // Save corresponding date
                                LocalDate date = LocalDate.of(year, month, day);

                                if (isDateReceived) {
                                    documentEntity.setReceivedDate(date);
                                    isDateReceived = false;
                                } else if (isDateAccepted) {
                                    documentEntity.setAcceptedDate(date);
                                    isDateAccepted = false;
                                } else if (isDateEntrez) {
                                    documentEntity.setEntrezDate(date);
                                    isDateEntrez = false;
                                }

                                // Clear variables
                                day = 1;
                                month = 1;
                                year = 1900;
                            } else if (line.contains(AppConstants.XML_DATE_DAY)
                                    && (isDateReceived || isDateAccepted || isDateEntrez)) {
                                try {
                                    day = Integer.parseInt(cleanLine);
                                } catch (NumberFormatException e) {
                                    day = 1;
                                    log.warn(AppConstants.STR_WARN_INTEGER_DEFAULT, AppConstants.XML_JOURNAL_VOLUME, cleanLine, day);
                                }
                            } else if (line.contains(AppConstants.XML_DATE_MONTH)
                                    && (isDateReceived || isDateAccepted || isDateEntrez)) {
                                try {
                                    month = Integer.parseInt(cleanLine);
                                } catch (NumberFormatException e) {
                                    month = 1;
                                    log.warn(AppConstants.STR_WARN_INTEGER_DEFAULT, AppConstants.XML_JOURNAL_VOLUME, cleanLine, month);
                                }
                            } else if (line.contains(AppConstants.XML_DATE_YEAR)
                                    && (isDateReceived || isDateAccepted || isDateEntrez)) {
                                try {
                                    year = Integer.parseInt(cleanLine);
                                } catch (NumberFormatException e) {
                                    year = 1900;
                                    log.warn(AppConstants.STR_WARN_INTEGER_DEFAULT, AppConstants.XML_JOURNAL_VOLUME, cleanLine, year);
                                }
                            } else if (line.contains(AppConstants.XML_REFERENCE_START)) {
                                isReference = true;
                            } else if (line.contains(AppConstants.XML_REFERENCE_END)) {
                                // Set isReference to false
                                isReference = false;

                                // Check if keyword is in the database
                                ReferenceEntity referenceEntity;

                                Optional<ReferenceEntity> possibleReference = referenceService.findByExternalId(referencePMID);
                                synchronized (mapReferenceEntities) {
                                    if (mapReferenceEntities.containsKey(referencePMID)) {
                                        referenceEntity = mapReferenceEntities.get(referencePMID);
                                    } else if (possibleReference.isPresent()) {
                                        referenceEntity = possibleReference.get();
                                    } else {
                                        referenceEntity = new ReferenceEntity();
                                        referenceEntity.setName(referenceCitation);
                                        referenceEntity.setExternalId(referencePMID);
                                    }

                                    // Add reference
                                    referenceEntities.add(referenceEntity);
                                    mapReferenceEntities.put(referencePMID, referenceEntity);
                                }

                                // Clear variables
                                referenceCitation = "";
                                referencePMID = "";
                            } else if (line.contains(AppConstants.XML_REFERENCE_CITATION) && isReference) {
                                referenceCitation = cleanLine;
                            } else if (line.contains(AppConstants.XML_REFERENCE_PMID) && isReference) {
                                referencePMID = cleanLine;
                            }
                        }

                        if (isValid) {
                            // Check if keyword is in the database
                            Optional<JournalEntity> possibleJournal = journalService.findByNameAndIsbnAndIssn(
                                    journalEntity.getName(), journalEntity.getIsbn(), journalEntity.getIssn());
                            synchronized (mapJournalEntities) {
                                if (mapJournalEntities.containsKey(mapJournalKey))
                                    journalEntity = mapJournalEntities.get(mapJournalKey);
                                else if (possibleJournal.isPresent())
                                    journalEntity = possibleJournal.get();

                                mapJournalEntities.put(mapJournalKey, journalEntity);
                            }

                            synchronized (mapDocumentEntities) {
                                String mapKey = documentPMID + documentDOI;
                                if (!mapDocumentEntities.containsKey(mapKey)) {
                                    // Assign dependencies to document entity
                                    documentEntity.setTitle(documentTitle);
                                    documentEntity.setBody(documentBody);
                                    documentEntity.setDoi(documentDOI);
                                    documentEntity.setAuthors(authorEntities);
                                    documentEntity.setFundings(fundingEntities);
                                    documentEntity.setKeywords(keywordEntities);
                                    documentEntity.setPublicationTypes(publicationTypeEntities);
                                    documentEntity.setReferences(referenceEntities);
                                    documentEntity.setSubstances(substanceEntities);
                                    documentEntity.setMeshTerms(meshTermEntities);
                                    documentEntity.setJournal(journalEntity);

                                    mapDocumentEntities.put(mapKey, documentEntity);
                                }
                            }

                            // Close buffered reader
                            br.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // Document must be considered
            if (documentEntity.getExternalId() != null)
                toRet = documentEntity;
            else
                toRet = null;

            log.info(AppConstants.STR_WORKER_INFO_RETRIEVAL, savedID);
        } catch (Exception e) {
            log.warn(AppConstants.STR_WORKER_WARN_DOCUMENT, savedID, e);

            // Set document to null (do not consider this document)
            toRet = null;
        }

        try {
            // Wait before doing the next request
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.warn(AppConstants.STR_ERROR_PUBMED_SLEEP, Thread.currentThread());
        }

        return toRet;
    }
}
