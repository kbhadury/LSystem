import java.util.ArrayList;
import java.util.Random;

public class RuleObj{
    private ArrayList<String> rules;
    private int ruleCount;
    private Random random;
    
    public RuleObj(){
        rules = new ArrayList<String>();
        ruleCount = 0;
        random = new Random();
    }
    
    //Add a rule to the list
    public void addRule(String rule){
        rules.add(rule);
        ++ruleCount;
    }
    
    //Choose a rule randomly.  Each rule has an even chance of being chosen.
    public String getRule(){
        int choice = random.nextInt(ruleCount);
        return rules.get(choice);
    }
}
