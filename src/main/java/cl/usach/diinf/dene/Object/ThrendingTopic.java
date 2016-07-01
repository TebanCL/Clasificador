
package cl.usach.diinf.dene.Object;


/**
 *
 * @author Teban
 */
public class ThrendingTopic {
    private String word;
    private int counter;
    
    public ThrendingTopic(String w){
        word = w;
        counter = 1;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the counter
     */
    public int getCounter() {
        return counter;
    }

    /**
     * @param counter the counter to set
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
    
    /**
     * Increase counter
     */
    public void increaseCounter(){
        this.counter++;
    }
}
