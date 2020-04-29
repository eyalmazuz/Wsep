package Domain.Spelling;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.*;

public class Spellchecker {

    private static JLanguageTool langTool = new JLanguageTool(new BritishEnglish());

    public static List<String> getSuggestions(String message) {
        try {
            List<RuleMatch> matches = langTool.check(message);
            List<String> suggestions = new ArrayList<>();
            for (RuleMatch match: matches)
                suggestions.addAll(match.getSuggestedReplacements());
            return suggestions;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
