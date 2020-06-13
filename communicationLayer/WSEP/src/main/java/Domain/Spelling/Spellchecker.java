package Domain.Spelling;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Spellchecker {

    private static SpellChecker checker;

    static {
        try {
            checker = new SpellChecker(new SpellDictionaryHashMap(new File("src/main/java/Domain/Spelling/english.0")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static void main(String[] args){
//
//        Spellchecker checker = new Spellchecker();
//
//        ArrayList<String> b= checker.getSuggestions("appple");
//
//        for (String s: b){
//            System.out.println(b);
//        }
//    }


    public static ArrayList<String> getSuggestions(String word) {
        List<com.swabunga.spell.engine.Word> sugs = checker.getSuggestions(word, 5);

        ArrayList<String> res = new ArrayList<>();
        for (com.swabunga.spell.engine.Word s: sugs){
            res.add(s.getWord());
        }

        return res;

    }
}
