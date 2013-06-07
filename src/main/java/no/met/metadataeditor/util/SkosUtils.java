package no.met.metadataeditor.util;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * A utility to parse the GCMD SKOS taxonomy.
 */
public final class SkosUtils {

    public enum LEVEL {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN;

        @Override
        public String toString() {
            switch (this) {
                case TWO:
                    return "2";
                case THREE:
                    return "3";    
                case FOUR:
                    return "4";
                case FIVE:
                    return "5";
                case SIX:
                    return "6";
                case SEVEN:
                    return "7";
                default:
                    throw new IllegalArgumentException();
            }
        }
        
        public int toInt(){
            return Integer.parseInt(toString());
        }
    }

    private SkosUtils() {
    }

    private static String createQuery(int endLevel) {
        if (endLevel < 3 && endLevel > 7) {
            throw new IllegalArgumentException("End level should be betweem 3 and 7");
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ");
        queryBuilder.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        queryBuilder.append("SELECT DISTINCT ");
        StringBuilder resultVarBuilder = new StringBuilder();
        for (int i = 0; i < endLevel; i++) {
            String var = "?lbl" + String.valueOf(i + 1) + " ";
            resultVarBuilder.append(var);
        }
        queryBuilder.append(resultVarBuilder.toString());
        queryBuilder.append("WHERE {");
        for (int i = 0; i < endLevel; i++) {
            String broader = "?object" + String.valueOf(i) + " skos:broader ?object" + String.valueOf(i + 1) + ". ";
            String prefLabel = "?object" + String.valueOf(i + 1) + " skos:prefLabel ?lbl" + String.valueOf(i + 1) + ". ";
            queryBuilder.append(broader);
            queryBuilder.append(prefLabel);
        }
        String filter = "FILTER  regex (?lbl" + String.valueOf(endLevel) + ", \"Science Keywords\", \"i\")";
        queryBuilder.append(filter);
        queryBuilder.append("}");
        return queryBuilder.toString();
    }

    /**
     * Get SKOS keywords
     * @param model model
     * @param startLevel starting point of SKOS hierarchy (e.g.,startLevelL = 2  "SOLID EARTH" from gets Science Keywords > Earth Science > SOLID EARTH)
     * @param endLevel endLevel of SKOS hierarchy (e.g., LEVEL 3 gets Science Keywords > Earth Science > SOLID EARTH)
     * @return a map of the outer most keyword and its hierarchy
     */
    public static Map<String, String> getSkos(Model model, LEVEL startLevel, LEVEL endLevel) {
        Objects.requireNonNull(model, "Model can not be null");
        if (startLevel.toInt() >= endLevel.toInt()) {
            throw new IllegalArgumentException("Start level should be less than end level");
        }

        Map<String, String> skosClassification = new HashMap<>();        
        Query sparqlQuery = QueryFactory.create(createQuery(endLevel.toInt()));
        // Execute the sparqlQuery and obtain results
        QueryExecution qe = QueryExecutionFactory.create(sparqlQuery, model);
        ResultSet results = qe.execSelect();
        List<String> varMNames = null;
        while (results.hasNext()) {
            QuerySolution row = results.next();
            if (varMNames == null) {
                varMNames = new ArrayList<>();
                for (Iterator it = row.varNames(); it.hasNext();) {
                    varMNames.add((String) it.next());
                }
                Collections.reverse(varMNames);
            }
            String key = null;
            StringBuilder hierachiyBuilder = new StringBuilder();
            //start create string from the start level. 
            for (int i = startLevel.toInt(); i < varMNames.size(); i++) {
                RDFNode node = row.get(varMNames.get(i));
                String  value = StringUtils.capitalize(node.asLiteral().getValue().toString().toLowerCase());
                hierachiyBuilder.append(value);
                if (i != varMNames.size() - 1) {
                    hierachiyBuilder.append(" > ");
                }
                if (i == varMNames.size() - 1) {
                    key = node.asLiteral().getValue().toString();
                }
            }
            skosClassification.put(key, hierachiyBuilder.toString());
        }
        qe.close();
        return skosClassification;
    }

    /**
     * Get all the SKOS keyword hierarchy
     * @param inputStream 
     * @return a map of the outer most keyword and its hierarchy
     */
    public static Map<String, String> getAllSkos(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "Stream can not be null");
        Model model = getModel(inputStream);
        //since GCMD Taxonomy contains 80% outer most leaf at level 5 and remaining 20% at level 7. Hence calling getSkos
        //with level 5 and 7 gets all the leafs
        Map<String, String> allSkos = getSkos(model, LEVEL.TWO, LEVEL.FIVE);
        Map<String, String> skosLevel7 = getSkos(model, LEVEL.TWO, LEVEL.SEVEN);
        allSkos.putAll(skosLevel7);
        model.close();
        return allSkos;
    }

    /**
     * Get the model 
     * @param inputStream
     * @return model
     */
    public static Model getModel(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "Stream can not be null");
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, "");
        return model;
    }
}
