package org.nohope;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 */
public class ParserTest {

    public static class Ternary<A, B, C> {

    }

    public static Type doGetType(String input) {
        CharStream stream = new ANTLRInputStream(input);
        TokenStream tokenStream = new CommonTokenStream(new ParametrizedTypeLexer(stream));

        ParametrizedTypeParser parser = new ParametrizedTypeParser(tokenStream);
        parser.removeErrorListeners();
        parser.setErrorHandler(new BailErrorStrategy());
        parser.setBuildParseTree(true);

        return parser.type().value.build();
    }

    @Test
    public void testParser() {
        String type = "java.util.Map<java.util.List<java.lang.Integer>,java.util.Set<java.lang.String>>";

        assertEquals(new TypeReference<Map<List<Integer>, Set<String>>>() {
        }.getType(), doGetType(type));
    }

    @Test
    public void testTernary() {

        String type = "org.nohope.ParserTest$Ternary<java.util.List<java.lang.String>,java.util.Map<java.lang.Integer,java.util.Set<java.lang.Object>>,java.lang.String>";

        assertEquals(new TypeReference<Ternary<List<String>, Map<Integer, Set<Object>>, String>>() {
        }.getType(), doGetType(type));

    }
}
