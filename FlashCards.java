
package flash.cards;

import java.util.Collections;
import java.util.Vector;

/**
 *
 * @author prashish
 */
public class FlashCards {

    FlashCore fl = new FlashCore("test.txt");

    
    public Vector<String> GetWords() {
        Vector<String> li = new Vector();
        li = fl.GetList();      // get wordlist from FlashCore Class
        Collections.sort(li);   // sort vector and then return

        return li;
    }
    
    public String GetMeaning( String word ) {
        String wd = fl.getSecondValue(word);    // get second value of stored csv
        return wd;
    }
    
    // Add Words with meaning
    public boolean AddWord(String word, String meaning ) {
        fl.insertInfile(word + ";" + meaning + "\n");
        return true;
    }
    
    // Add Words with meaning and extra info
    public boolean AddWord(String word, String meaning, String info ) {
        fl.insertInfile(word + ";" + meaning + ";" + info + "\n");
        return true;
    }
    
    //Delete Word from the list
    public boolean DeleteWord ( String word ) {
        fl.deletedata(word);
        return true;
    }
}
