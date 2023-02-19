package chatserver;

import java.io.IOException;

class ChangeLanguage{
    static String randomizer(String message) throws IOException {
        String[] FirstGrouplanguages = {"it", "fr", "pt", "es", "ro","ca", "de", "nl", "sv"};
        // String[] SecondGrouplanguages = {"sm", "ko", "te", "th", "ug", "vi", "ru"};
        // String[] ThirdGrouplanguages = {"ar", "hy", "hi", "fa", "ht", "af"};

        String tracer = "en";

        String translated = message;
        for (int i = 0; i < 1; i++) {
            for (String lang : FirstGrouplanguages) {
                System.out.println("From: " + tracer + " to: " + lang);
                translated = Translator.translate(tracer, lang, translated);
                System.out.println("Translated: " + translated);
                tracer = lang;
                //out.println(ret);
            }
            // for (String lang : SecondGrouplanguages) {
            //     System.out.println("From: " + tracer + " to: " + lang);
            //     translated = Translator.translate(tracer, lang, translated);
            //     System.out.println("Translatede: " + translated);
            //     tracer = lang;
            //     //out.println(ret);
            // }
            // for (String lang : ThirdGrouplanguages) {
            //     System.out.println("From: " + tracer + " to: " + lang);
            //     translated = Translator.translate(tracer, lang, translated);
            //     System.out.println("Translatede: " + translated);
            //     tracer = lang;
            //     //out.println(ret);
            // }
        }
        String ret = Translator.translate(tracer, "en", translated);
        return ret;
    }


}
