package org.eddie.tools.geocaching.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides statistics for a given text
 */
public class TextStatistics {

    //approx 31.8% of all words
    public static final Set<String> GERMAN_MOST_COMMON_WORDS = new HashSet<>(Arrays.asList(new String[]{
            "DIE", "DER", "UND", "IN", "ZU", "DEN", "DAS", "NICHT", "VON", "SIE", "IST", "DES", "SICH",
            "MIT", "DEM", "DASS", "ER", "ES", "EIN", "ICH", "AUF", "SO", "EINE", "AUCH", "ALS", "AN", "NACH", "WIE", "IM", "FÜR"
    }));

    //approx 15.3% of all words
    public static final Set<String> GERMAN_SECOND_MOST_COMMON_WORDS = new HashSet<>(Arrays.asList(new String[]{
            "MAN", "ABER", "AUS", "DURCH", "WENN", "NUR", "WAR", "NOCH", "WERDEN", "BEI", "HAT", "WIR", "WAS", "WIRD", "SEIN",
            "EINEN", "WELCHE", "SIND", "ODER", "ZUR", "UM", "HABEN", "EINER", "MIR", "ÜBER", "IHM", "DIESE", "EINEM", "IHR",
            "UNS", "DA", "ZUM", "KANN", "DOCH", "VOR", "DIESER", "MICH", "IHN", "DU", "HATTE", "SEINE", "MEHR", "AM",
            "DENN", "NUN", "UNTER", "SEHR", "SELBST", "SCHON", "HIER", "BIS", "HABE", "IHRE", "DANN", "IHNEN", "SEINER",
            "ALLE", "WIEDER", "MEINE", "ZEIT", "GEGEN", "VOM", "GANZ", "EINZELNEN", "WO", "MUSS", "OHNE", "EINES", "KÖNNEN", "SEI"
    }));

    //in this order: the (6.42%), of (4.02%), and (3.15%), to (2.37%), a (2.09%), in (1.78%), that (1.24%), is (1.03%), I (0.95%), it (0.93%)
    public static final List<String> ENGLISH_MOST_COMMON_WORDS = Arrays.asList(new String[]{
            "THE", "OF", "AND", "TO", "A", "IN", "THAT", "IS", "", "IT", "FOR", "AS", "WITH", "WAS", "HIS", "HE", "BE",
            "NOT", "BY", "BUT", "HAVE", "YOU", "WHICH", "ARE", "ON", "OR", "HER", "HAD", "AT", "FROM", "THIS", "MY", "THEY",
            "ALL", "THEIR", "AN", "SHE", "HAS", "WERE", "ME", "BEEN", "HIM", "ONE", "SO", "IF", "WILL", "THERE", "WHO", "NO",
            "WE", "WHEN", "WHAT", "YOUR", "MORE", "WOULD", "THEM", "SOME", "THAN", "MAY", "UPON", "ITS", "OUT", "INTO", "OUR",
            "THESE", "MAN", "UP", "DO", "LIKE", "SHALL", "GREAT", "NOW", "SUCH", "SHOULD", "OTHER", "ONLY", "ANY", "THEN", "ABOUT",
            "THOSE", "CAN", "MADE", "WELL", "OLD", "MUST", "US", "SAID", "TIME", "EVEN", "NEW", "COULD", "VERY", "MUCH", "OWN",
            "MOST", "MIGHT", "FIRST", "AFTER", "YET", "TWO"
    });

    //Data not yet used
    //Die häufigsten Wörter im Englischen
    //
    //the (6.42%), of (4.02%), and (3.15%), to (2.37%), a (2.09%), in (1.78%), that (1.24%), is (1.03%), I (0.95%), it (0.93%), for, as, with, was, his, he, be, not, by, but, have, you, which, are, on (0.48 %), or, her, had, at, from, this, my, they, all, their, an, she, has, were, me, been, him, one, so, if, will, there, who, no, we (0.26 %), when, what, your, more, would, them, some, than, may, upon, its, out, into, our, these, man, up, do, like, shall, great, now, such, should, other (0.13%), only, any, then, about, those, can, made, well, old, must, us, said, time, even, new, could, very, much, own, most, might, first, after, yet, two (0.10%)
    //
    //Zweibuchstabige Wörter im Deutschen
    //
    //mit 'a':	ab, am, an, da, ja
    //mit 'e':	eh, er, es, je
    //mit 'i':	im, in
    //mit 'o':	ob, so, wo
    //mit 'u':	du, um, zu, nu
    //
    //Zweibuchstabige Wörter im Englischen
    //
    //mit 'a':	an, at, as, am
    //mit 'e':	he, be, me, we
    //mit 'i':	in, is, it, if
    //mit 'o':	on, or, to, of, do, go, no, so
    //mit 'u':	up, us
    //mit 'y':	by, my

    public static final Comparator<SnippetStats> SNIPPETSTAT_COMPARATOR_PERCENTAGE_DESCENDING = (s1, s2) -> {
        if (s1==s2) {
            return 0;
        }
        if (s1==null || s2==null) {
            return (s1==null?-1:1);
        }
        if (s1.getPercentage()==s2.getPercentage()) {
            return 0;
        }
        return s1.getPercentage()>s2.getPercentage()?-1:1;
    };


    public static final TextStatistics ENGLISH = new TextStatistics("ENGLISH",
            "A:8.17|B:1.49|C:2.78|D:4.25|E:12.70|F:2.23|G:2.02|H:6.09|I:6.98|J:0.15|K:0.77|L:4.03|M:2.41|N:6.75|O:7.51|P:1.93|Q:0.10|R:5.99|S:6.33|T:9.06|U:2.76|V:0.98|W:2.36|X:0.15|Y:1.97|Z:0.07",
            "TH:3.15|HE:2.51|AN:1.72|IN:1.69|ER:1.54|RE:1.48|ON:1.45|ES:1.45|TI:1.28|AT:1.24",
            "THE:3.53|ING:1.11|AND:1.02|ION:0.75|TIO:0.75|ENT:0.73|ERE:0.69|HER:0.68|ATE:0.66|VER:0.64",
            "LL:0.55|TT:0.53|SS:0.41|EE:0.39|PP:0.26|OO:0.23|RR:0.18|FF:0.14|CC:0.12|DD:0.10"
    );

    public static final TextStatistics GERMAN = new TextStatistics("GERMAN",
            "A:6.51|B:1.89|C:3.06|D:5.08|E:17.41|F:1.66|G:3.01|H:4.76|I:7.55|J:0.27|K:1.21|L:3.44|M:2.53|N:9.78|O:2.51|P:0.79|Q:0.02|R:7.00|S:7.89|T:6.15|U:4.35|V:0.67|W:1.89|X:0.03|Y:0.04|Z:1.13",
            "ER:4.09|EN:4.00|CH:2.42|DE:2.27|EI:1.93|ND:1.87|TE:1.85|IN:1.68|IE:1.63|GE:1.47",
            "ICH:1.15|EIN:1.08|UND:1.05|DER:0.97|NDE:0.83|SCH:0.65|DIE:0.64|DEN:0.62|END:0.60|CHT:0.60",
            "SS:0.76|NN:0.43|LL:0.42|EE:0.23|MM:0.23|TT:0.23|RR:0.15|DD:0.13|FF:0.12|AA:0.08"
    );

    private final String text;

    public static class SnippetStats {
        private final String text;
        private final String snippet;
        private final int count;
        private final double percentage;

        //for preefined stat creation, format needs to be "<snippet>;<percentage>";
        SnippetStats(String textFormat) {
            this.text = "";
            this.count = -1;
            String[] tokens = textFormat.split(":");
            this.snippet = tokens[0];
            this.percentage = Double.parseDouble(tokens[1])/100d;
        }

        public SnippetStats(String text, String snippet, int count, double percentage) {
            this.text = text;
            this.snippet = snippet;
            this.count = count;
            this.percentage = percentage;
        }

        public String getText() {
            return text;
        }

        public String getSnippet() {
            return snippet;
        }

        public int getCount() {
            return count;
        }

        public double getPercentage() {
            return percentage;
        }

        public String toString() {
            return getSnippet()+":"+String.format("%.1f",getPercentage()*100)+"%"+(getCount()>0?"("+getCount()+")":"");
        }
    }

    private static class StatHolder {
        final Map<String,SnippetStats> statMap = new HashMap<>();
        final List<SnippetStats> statList = new ArrayList<>();

        public String toString() {
            return toString(statList.size()+1);
        }

        public String toString(int maxLength) {
            int cnt = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            boolean first = true;
            for(SnippetStats ss : statList) {
                if (!first) {
                    sb.append(",");
                }
                first = false;
                sb.append(ss);
                cnt++;
                if (cnt>=maxLength) {
                    break;
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }

    //single char statistics
    private StatHolder singleChar;
    private StatHolder doubleChar;
    private StatHolder tripleChar;
    private StatHolder doubleSameChar;

    private Set<Character> markedChars;

    public TextStatistics(String text) {
        this(text,null);
    }

    private TextStatistics(String text, Set<Character> markedChars) {
        this.text = text;
        this.singleChar = createStat(this.text,1,null);
        this.doubleChar = createStat(this.text,2,null);
        this.tripleChar = createStat(this.text,3,null);
        this.doubleSameChar = createStat(this.text,2,s -> s.charAt(0)==s.charAt(1));
        this.markedChars = markedChars ==null?null:new HashSet<>(markedChars);
    }

    private TextStatistics(String name, String singleCharStat, String doubleCharStat, String tripleCharStat, String doubleSameCharStat) {
        this.text = name;
        this.singleChar = createStat(singleCharStat);
        this.doubleChar = createStat(doubleCharStat);
        this.tripleChar = createStat(tripleCharStat);
        this.doubleSameChar = createStat(doubleSameCharStat);
    }

    public TextStatistics replaceChars(Character ... chars) {
        return replaceChars(Utils.toMap(new HashMap<>(), (Object[]) chars));
    }

    public TextStatistics replaceChars(Map<Character,Character> map) {
        Set<Character> newSafeSet = new HashSet<>();
        if (this.markedChars !=null) {
            newSafeSet.addAll(this.markedChars);
        }
        newSafeSet.addAll(map.values());
        return new TextStatistics(replaceChars(this.text,map),newSafeSet);
    }

    public static String replaceChars(String text, Map<Character,Character> map) {

        //add entries to the map so replacement will not create ambiguity
        Set<Character> targetsToBeHandled = new HashSet<>(map.values());
        targetsToBeHandled.removeAll(map.keySet());
        Set<Character> usableSources = new HashSet<>(map.keySet());
        usableSources.removeAll(map.values());

        Map<Character,Character> realMap = new HashMap<>(map);
        Iterator<Character> usableSourceIt = usableSources.iterator();
        for(char target : targetsToBeHandled) {
            realMap.put(target,usableSourceIt.next());
        }


        StringBuilder sb = new StringBuilder();
        for(char c : text.toCharArray()) {
            if (realMap!=null && realMap.containsKey(c)) {
                sb.append(realMap.get(c));
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static boolean equalsPattern(String pattern, String text) {
        if (pattern.length()!=text.length()) {
            return false;
        }
        Map<Character,Character> repl = new HashMap<>();
        Set<Character> found = new HashSet<>();
        int idx = 0;
        for(char r : text.toCharArray()) {
            char e = pattern.charAt(idx++);
            if (e=='.') {
                continue;
            }
            if (Character.isLowerCase(e)) {
                if (repl.containsKey(e)) {
                    if (r!=repl.get(e)) {
                        return false;
                    }
                }
                else {
                    if (found.contains(r)) {
                        return false;
                    }
                }
                repl.put(e,r);
            }
            else {
                if (r!=e || repl.values().contains(r)) {
                    return false;
                }
                found.add(r);
            }
        }
        return true;
    }


    public String getText() {
        return text;
    }

    public String getMarkedText(boolean upperLowerStyle) {
        StringBuilder sb = new StringBuilder();
        for(char c : this.text.toCharArray()) {
            if (this.markedChars !=null && this.markedChars.contains(c)) {
                sb.append(upperLowerStyle?Character.toUpperCase(c):c);
            }
            else {
                sb.append(upperLowerStyle?Character.toLowerCase(c):".");
            }
        }
        return sb.toString();
    }

    public String matchedText(String token) {
        String allmarked = markedChars.stream().map(c -> ""+c).collect(Collectors.joining(""));
        String marked = getMarkedText(true);
        StringBuilder sb = new StringBuilder();
        int mlength = 0;
        for(int idx = 0;idx<marked.length();idx++) {
            if (idx+token.length()<=marked.length()) {
                String pattern = marked.substring(idx, idx + token.length()) + allmarked;
                if (equalsPattern(pattern, token + allmarked)) {
                    mlength = token.length();
                }
            }
            if (mlength>0) {
                char c = token.charAt(token.length()-mlength);
                sb.append(this.markedChars.contains(c)?Character.toUpperCase(c):Character.toLowerCase(c));
            }
            else {
                sb.append(".");
            }
            mlength--;
        }
        return sb.toString();
    }

    public String toString() {
        return "Text:"+getText()+"\n"+
                (this.markedChars ==null?"":"STxt:"+ getMarkedText(false)+"\n")+
                "Single:"+this.singleChar+"\n"+
                "Double:"+this.doubleChar.toString(10)+"\n"+
                "Triple:"+this.tripleChar.toString(10)+"\n"+
                "DoubleSameChar:"+this.doubleSameChar.toString(10);
    }

    private static StatHolder createStat(String statText, int snippetLength, Predicate<String> condition) {
        Map<String, Integer> countMap = new HashMap<>();

        int total = 0;
        for(int idx=0;idx<statText.length()+1-snippetLength;idx++) {
            String key = statText.substring(idx,idx+snippetLength);
            if (condition!=null && !condition.test(key)) {
                continue;
            }
            total++;
            if (countMap.containsKey(key)) {
                countMap.put(key,countMap.get(key)+1);
            }
            else {
                countMap.put(key,1);
            }
        }
        StatHolder holder = new StatHolder();

        for(Map.Entry<String,Integer> entry : countMap.entrySet()) {
            SnippetStats stat = new SnippetStats(statText,""+entry.getKey(),entry.getValue(),((double)entry.getValue())/(double)total);
            holder.statMap.put(entry.getKey(),stat);
            holder.statList.add(stat);
        }
        holder.statList.sort(SNIPPETSTAT_COMPARATOR_PERCENTAGE_DESCENDING);
        return holder;
    }

    private static StatHolder createStat(String parseText) {
        StatHolder holder = new StatHolder();
        for(String token : parseText.split("\\|")) {
            if (token.isEmpty()) {
                continue;
            }
            SnippetStats stat = new SnippetStats(token);
            holder.statMap.put(stat.getSnippet(),stat);
            holder.statList.add(stat);
        }
        holder.statList.sort(SNIPPETSTAT_COMPARATOR_PERCENTAGE_DESCENDING);
        return holder;
    }
}
