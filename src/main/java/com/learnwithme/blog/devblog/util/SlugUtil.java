package com.learnwithme.blog.devblog.util;
import java.text.Normalizer;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utility class for generating URL-friendly slugs from text
 */
public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    private SlugUtil() {
        // Utility class with private constructor
    }

    /**
     * Convert a string to a URL-friendly slug
     * @param input the input string
     * @return the slug
     */
    public static String toSlug(String input) {
        if (input == null) {
            return "";
        }

        // Normalize the string (convert accented characters, etc.)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Replace whitespace with hyphens
        String noWhitespace = WHITESPACE.matcher(normalized).replaceAll("-");

        // Remove all non-Latin characters
        String noSpecialChars = NONLATIN.matcher(noWhitespace).replaceAll("");

        // Convert to lowercase and trim
        return noSpecialChars.toLowerCase(Locale.ENGLISH)
                .replaceAll("-{2,}", "-")  // Remove duplicated hyphens
                .replaceAll("^-|-$", "");  // Remove leading and trailing hyphens
    }
}