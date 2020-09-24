package es.uvigo.ei.sing.pubmed.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppConstants {

    // UI constants
    public static final String STR_ERROR_INPUTS = "Please, provide the mandatory input parameter to start the " +
            "application: \"path/to/configuration\".";
    public static final String STR_ERROR_RETRIEVAL = "An error has occurred during the query retrieval \"{}\". Please, try again later. ERROR: {}";
    public static final String STR_ERROR_UNEXPECTED = "Unexpected error during the retrieval. ERROR: {}";
    public static final String STR_ERROR_PUBMED_ID = "Discarding the following PMIDs: \"{}\" because they not exist in PubMed.";
    public static final String STR_ERROR_PUBMED_SLEEP = "The PubmedWorker {} can not sleep.";
    public static final String STR_ERROR_SAVING_DOCS = "An error has occurred during saving documents... See error: %s...";

    public static final String STR_INFO_WAIT = "Waiting {} seconds to start the next query...";
    public static final String STR_INFO_QUERY = "Retrieving from QUERY {}...";
    public static final String STR_INFO_IDS = "Retrieving from IDS {}...";
    public static final String STR_INFO_SAVING_DOCS = "Saving a subset of %s documents...";
    public static final String STR_INFO_NOT_SAVING_DOCS = "There are no new documents to save...";
    public static final String STR_INFO_RETRIEVAL = "Starting the retrieval of document {}...";
    public static final String STR_INFO_SAVING = "Saving a {} in the database...";
    public static final String STR_WORKER_INFO_RETRIEVAL = "The document with PMID {} was retrieved without errors...";

    public static final String STR_WARN_MODE = "The input query {} has an unrecognised mode mode {}. Please, use one of the following modes in your input file: QUERY or IDS.";
    public static final String STR_WARN_LINE = "The input line {} has an unsupported structure. Please, revise the line and try again (e.x. IDS\t12345678).";
    public static final String STR_WARN_INTEGER = "The parameter {} with a value {} cannot be cast to an integer. Skipping this value...";
    public static final String STR_WARN_INTEGER_DEFAULT = "The parameter {} with a value {} cannot be cast to an integer. Setting a default value of {}...";
    public static final String STR_WORKER_WARN_DOCTYPE = "The parsing for book documents {}, is not already supported. Skipping document with PMID {}...";
    public static final String STR_WARN_NO_IDS = "There are no more IDs for the input query. Skipping the query {}...";

    public static final String STR_CONFIG_ERROR_INTEGER = "The value \"{}\" in properties file, must be a number.";
    public static final String STR_CONFIG_ERROR_STRING = "The value \"{}\" in properties file, must be a valid String. Please, provide one of these values: {}. Using by default {}.";
    public static final String STR_CONFIG_ERROR_INPUT = "The value path \"{}\" in properties file, has an invalid value. Please, revise it and try again. ERROR: {}";
    public static final String STR_WORKER_WARN_DOCUMENT = "The document with PMID {} cannot be parsed. Skipping the document... ERROR: {}";
    public static final String STR_ERROR_SLEEP = "The main thread cannot go to sleep. Skipping...";

    // Integer constants
    public static final int APP_SECONDS_SUCCESS = 60;
    public static final int APP_SECONDS_ERROR = 180;
    public static final int APP_SECONDS_LINES = 120;

    // String constants
    public static final String MODE_RET_QUERY = "QUERY";
    public static final String MODE_RET_IDS = "IDS";

    public static final String PUBMED_RETTYPE_DOCSUM = "docsum";
    public static final String PUBMED_RETTYPE_UILIST = "uilist";
    public static final String PUBMED_RETTYPE_FULL = "full";
    public static final String PUBMED_RETTYPE_MEDLINE = "medline";
    public static final String PUBMED_RETTYPE_ABSTRACT = "abstract";
    public static final String PUBMED_RETTYPE_XML = "xml";
    public static final String PUBMED_RETMODE_XML = "xml";
    public static final String PUBMED_RETMODE_TEXT = "text";
    public static final String PUBMED_RETMODE_ASN = "asn.1";

    // Pubmed eutils constants (https://dataguide.nlm.nih.gov/eutilities/utilities.html#esearch)
    public static final String PUBMED_DATABASE = "pubmed";
    // abstract, docsum, full, medline, uilist, xml
    public static final String PUBMED_RET_TYPE = "xml";
    // asn.1, text, xml
    public static final String PUBMED_RET_MODE = "xml";
    public static final String PUBMED_ENCODING = "UTF-8";
    public static final int PUBMED_TIMEOUT = 180;
    // Sort mode
    public static final String PUBMED_SORT_RELEVANCE = "relevance";
    public static final String PUBMED_SORT_AUTHOR = "author";
    public static final String PUBMED_SORT_JOURNAL = "journal";
    public static final String PUBMED_SORT_MOSTRECENT = "most+recent";
    public static final String PUBMED_SORT_PUBDATE = "pub+date";
    public static final String PUBMED_SORT_TITLE = "title";

    // Pubmed XML Retrieval
    public static final String XML_PUBMED_ARTICLE = "<PubmedArticle>";
    // E.x. https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id=26866230&retmode=xml
    public static final String XML_PUBMED_BOOK = "<PubmedBookArticle>";

    public static final String XML_PMID = "<PMID";
    public static final String XML_TITLE = "<ArticleTitle";
    public static final String XML_ABSTRACT_START = "<Abstract>";
    public static final String XML_ABSTRACT_END = "</Abstract>";
    public static final String XML_ABSTRACT = "<AbstractText";
    public static final String XML_DATE_RECEIVED_START = "<PubMedPubDate PubStatus=\"received\">";
    public static final String XML_DATE_ACCEPTED_START = "<PubMedPubDate PubStatus=\"accepted\">";
    public static final String XML_DATE_ENTREZ_START = "<PubMedPubDate PubStatus=\"entrez\">";
    public static final String XML_DATE_END = "</PubMedPubDate>";
    public static final String XML_DATE_YEAR = "<Year>";
    public static final String XML_DATE_MONTH = "<Month>";
    public static final String XML_DATE_DAY = "<Day>";
    public static final String XML_LANGUAGE = "<Language>";
    public static final String XML_DOI = "<ELocationID EIdType=\"doi\"";

    public static final String XML_TYPE = "<PublicationType ";

    public static final String XML_MESH_START = "<MeshHeading>";
    public static final String XML_MESH_END = "</MeshHeading>";
    public static final String XML_MESH_QUALNAME = "<QualifierName ";
    public static final String XML_MESH_QUALNAME_MAJOR = "MajorTopicYN=\"Y\">";
    public static final String XML_MESH_DESCNAME = "<DescriptorName ";

    public static final String XML_SUBSTANCE_START = "<Chemical>";
    public static final String XML_SUBSTANCE_END = "</Chemical>";
    public static final String XML_SUBSTANCE_REGNUMBER = "<RegistryNumber";
    public static final String XML_SUBSTANCE_NAME = "<NameOfSubstance ";

    public static final String XML_KEYWORD_NAME = "<Keyword ";

    public static final String XML_REFERENCE_START = "<Reference>";
    public static final String XML_REFERENCE_END = "</Reference>";
    public static final String XML_REFERENCE_CITATION = "<Citation>";
    public static final String XML_REFERENCE_PMID = "<ArticleId ";

    public static final String XML_JOURNAL_MEDLINE_START = "<MedlineJournalInfo>";
    public static final String XML_JOURNAL_MEDLINE_END = "</MedlineJournalInfo>";
    public static final String XML_JOURNAL_TITLE = "<Title";
    public static final String XML_JOURNAL_COUNTRY = "<Country";
    public static final String XML_JOURNAL_ABBR = "<ISOAbbreviation";
    public static final String XML_JOURNAL_ISSN = "<ISSN IssnType";
    public static final String XML_JOURNAL_ISBN = "<ISBN";
    public static final String XML_JOURNAL_VOLUME = "<Volume";
    public static final String XML_JOURNAL_ISSUE = "<Issue";

    public static final String XML_AUTHOR_START = "<Author ";
    public static final String XML_AUTHOR_END = "</Author>";
    public static final String XML_AUTHOR_LASTNAME = "<LastName";
    public static final String XML_AUTHOR_FORENAME = "<ForeName";
    public static final String XML_AUTHOR_INITIALS = "<Initials";
    public static final String XML_AUTHOR_AFFILIATION = "<Affiliation>";
    public static final String XML_AUTHOR_GROUP = "<CollectiveName>";

    public static final String XML_GRANT_START = "<Grant>";
    public static final String XML_GRANT_END = "</Grant>";
    public static final String XML_GRANT_ID = "<GrantID";
    public static final String XML_GRANT_AGENCY = "<Agency";
    public static final String XML_GRANT_COUNTRY = "<Country";
    public static final String XML_GRANT_ACRONYM = "<Acronym";

    // Regex constants
    public static final String XML_REGEX_REMOVE_TAGS = "^<.*?>|</\\w*?>$";
    public static final String XML_REGEX_EMAILS = "([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))\\.*";
}
