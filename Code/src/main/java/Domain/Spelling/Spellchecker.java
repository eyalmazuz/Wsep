package Domain.Spelling;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.*;

public class Spellchecker {

    private static JLanguageTool langTool = new JLanguageTool(new BritishEnglish());

    public static String getSuggestions(String message) {
        try {
            List<RuleMatch> matches = langTool.check(message);
            Map<String, String> suggestions = new HashMap<>();
            for (RuleMatch match: matches)
                suggestions.put(message.substring(match.getFromPos(), match.getToPos()), match.getSuggestedReplacements().get(0));
            String result = "";
            for (Map.Entry<String, String> suggestion : suggestions.entrySet())
                result += "Maybe switch \"" + suggestion.getKey() + "\" with \"" + suggestion.getValue() + "\"?";
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(getSuggestions("appple"));

    }
}
