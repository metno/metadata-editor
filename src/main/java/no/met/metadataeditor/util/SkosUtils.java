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

/**
 * A utility to parse the GMCD SKOS taxonomy.
 */
public final class SkosUtils {

    public enum LEVEL {
        THREE, FOUR, FIVE, SIX, SEVEN;

        @Override
        public String toString() {
            switch (this) {
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
    }

    private SkosUtils() {
    }

    private static String createQuery(int level) {
        if (level < 3 && level > 7) {
            throw new IllegalArgumentException("Level should be betweem 3 and 7");
        }

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("PREFIX skos: <http://www.w3.org/2004/02/skos/core#> ");
        queryBuilder.append("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        queryBuilder.append("SELECT DISTINCT ");
        StringBuilder resultVarBuilder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            String var = "?lbl" + String.valueOf(i + 1) + " ";
            resultVarBuilder.append(var);
        }
        queryBuilder.append(resultVarBuilder.toString());
        queryBuilder.append("WHERE {");
        for (int i = 0; i < level; i++) {
            String broader = "?object" + String.valueOf(i) + " skos:broader ?object" + String.valueOf(i + 1) + ". ";
            String prefLabel = "?object" + String.valueOf(i + 1) + " skos:prefLabel ?lbl" + String.valueOf(i + 1) + ". ";
            queryBuilder.append(broader);
            queryBuilder.append(prefLabel);
        }
        String filter = "FILTER  regex (?lbl" + String.valueOf(level) + ", \"Science Keywords\", \"i\")";
        queryBuilder.append(filter);
        queryBuilder.append("}");
        return queryBuilder.toString();
    }

    public static Map<String, String> getSkos(Model model, LEVEL level) {
        if (model == null) {
            throw new IllegalArgumentException("Model can not be null");
        }

        Map<String, String> skosClassification = new HashMap<>();
        Query sparqlQuery = QueryFactory.create(createQuery(Integer.parseInt(level.toString())));
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
            for (int i = 0; i < varMNames.size(); i++) {
                RDFNode node = row.get(varMNames.get(i));
                hierachiyBuilder.append(node.asLiteral().getValue().toString());
                if (i != varMNames.size() - 1) {
                    hierachiyBuilder.append(" > ");
                }
                if (i == varMNames.size() - 1) {
                    key = node.asLiteral().getValue().toString();
                }
            }
            skosClassification.put(key, hierachiyBuilder.toString());
        }
        return skosClassification;
    }

    public static Map<String, String> getAllSkos(InputStream inputStream) {

        if (inputStream == null) {
            throw new IllegalArgumentException("Stream can not be null");
        }
        Model model = getModel(inputStream);
        Map<String, String> allSkos = getSkos(model, LEVEL.FIVE);
        Map<String, String> skosLevel7 = getSkos(model, LEVEL.SEVEN);
        allSkos.putAll(skosLevel7);

        return allSkos;
    }

    public static Model getModel(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("Stream can not be null");
        }
        Model model = ModelFactory.createDefaultModel();
        model.read(inputStream, "");
        return model;
    }
}
