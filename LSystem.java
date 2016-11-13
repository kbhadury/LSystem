import gpdraw.*;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.awt.Color;

public class LSystem{
    String vars, consts = "+-[]'\"~`*"; //Each var/const is just one char, initialize with defaults
    RuleObj[] rules;
    double angle, length;
    String start;
    int level;
    boolean varsDraw = true; //Test if variables draw by default

    SketchPad canvas;
    DrawingTool pen;

    /* Get info about L-system from user:
     * Variables, constants, start pattern, rules, turning angle, forward length, recursion level
     */
    public void getInput(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("---Welcome to the L-System Renderer!---");

        //Variables are one alpha char each
        System.out.print("Enter variables (one letter each, i.e. X Y): ");
        vars = parseCharInput(scanner.nextLine(),false);

        //Constants are one char each (may/may not be alpha)
        System.out.println("Recognized constants are + - [ ] ' \" ~ ` *");
        System.out.print("Enter constants F and M if you'll need them (i.e. F M): ");
        consts += parseCharInput(scanner.nextLine(),true);

        //Starting pattern
        System.out.print("Enter starting pattern: ");
        start = scanner.next();
        scanner.nextLine(); //Consume extra input

        //Var1=rule1, var2=rule2
        System.out.print("Enter rules (i.e. X=X+Y): ");
        rules = parseRuleInput(scanner.nextLine());

        //Default turning angle
        System.out.print("Enter turning angle: ");
        angle = scanner.nextDouble();
        scanner.nextLine();

        //Determines how far the pen moves on a "forward" instruction
        System.out.print("Enter length in pixels: ");
        length = scanner.nextDouble();
        scanner.nextLine();

        do{
            System.out.print("Enter recursion level between 0 and 10: "); //0 = draw start
            level = scanner.nextInt();
            scanner.nextLine();
        } while(level < 0 || level > 10);

        canvas = new SketchPad(500,500);
        pen = new DrawingTool(canvas);
    }

    public void draw(int x, int y, double dir, int width, Color color){
        initPen(x, y, dir, width, color);
        drawStr(start, level);
    }

    public void initPen(int x, int y, double dir, int width, Color color){
        pen.up();
        pen.move(x,y);
        pen.setDirection(dir);
        pen.down();

        //Set color and thickness
        pen.setColor(color);
        pen.setWidth(width);
    }

    /*Draw the system based on the starting rule and recursion level*/
    public void drawStr(String str, int level){
        ArrayDeque<Double> stack = new ArrayDeque<Double>(); //For ] and [ operations

        for(int i=0; i<str.length(); ++i){
            char c = str.charAt(i);

            //Perform appropriate action
            if(consts.indexOf(c) != -1){
                if(c == '['){ //Save pen and length info to stack
                    stack.push(length);
                    stack.push(pen.getXPos());
                    stack.push(pen.getYPos());
                    stack.push(pen.getDirection());
                    stack.push((double)pen.getWidth());
                } else if(c == ']'){ //Restore angle and position from stack
                    double width = stack.pop();
                    double dir = stack.pop();
                    double y = stack.pop();
                    double x = stack.pop();
                    length = stack.pop();
                    pen.up();
                    pen.move(x,y);
                    pen.setDirection(dir);
                    pen.setWidth((int)width);
                    pen.down();
                } else {
                    doConstAction(c);
                }
            } else if(vars.indexOf(c) != -1){
                if(level == 0){ //No more recursion, execute directly
                    doVarAction();
                } else { //Replace with rule and recurse
                    String rule = getRule(c);
                    drawStr(rule,level-1);
                }
            } else {
                System.out.println("Unknown character in drawStr: " + c);
                System.exit(0);
            }
        }
    }

    private void doConstAction(char cons){ //Apparently const is a keyword even though it's not used
        switch(cons){
            //Movement
            case '+': 
            pen.turnRight(angle); break;
            case '-':
            pen.turnLeft(angle); break;
            case 'F':
            pen.forward(length); break;
            case 'M':
            pen.up(); pen.forward(length); pen.down(); break;

            //Pen
            case '*':
            changePenColor(); break;
            case '"':
            pen.setWidth(Math.min(100, pen.getWidth()+1)); break;
            case '\'':
            pen.setWidth(Math.max(1, pen.getWidth()-1)); break;

            //Length
            case '~':
            length = length*2; break;
            case '`':
            length = length/2; break;

            default: 
            //This should really never happen
            System.out.println("Unknown character in doConstAction: " + cons);
            System.exit(0);
        }
    }

    private void doVarAction(){
        if(varsDraw){
            pen.forward(length);
            changePenColor();
        }
        //Otherwise do nothing
    }

    /*Given a variable, return its corresponding rule*/
    private String getRule(char var){
        int varIndex = vars.indexOf(var);
        if(varIndex == -1){
            System.out.println("Unknown character in getRule: " + var);
            System.exit(0);
            return null;
        } else {
            return rules[varIndex].getRule();
        }
    }

    /*Retrieve single characters separated by whitespace from a string and
     * place them into a String.  If checkAlpha is enabled, varsDraw will
     * be updated accordingly
     * Does NOT check for duplicate characters
     * Does NOT check that each character is immediately followed by a space, but will
     * print an error later if a rule tries to assign a multi-char variable
     * 
     * Ex. A B C -> ABC
     */
    private String parseCharInput(String input, boolean checkAlpha){
        input = input.trim();

        //Check for correct format
        for(int i=0; i<input.length(); ++i){
            if(i%2 == 0 && input.charAt(i) == ' '){
                System.out.println("Each variable or constant can only be one character");
                System.exit(0);
            }
            else if(i%2 != 0 && input.charAt(i) != ' '){
                System.out.println("Each variable or constant can only be one character");
                System.exit(0);
            }
        }
        String result = input.replaceAll(" ",""); //Remove all whitespace

        //Update varsDraw
        if(checkAlpha && result.matches(".*[A-Za-z].*")) varsDraw = false;

        return result;
    }

    /*Parse a set of rules separated by commas and place them into an array which
     * aligns with the "vars" array.  Rules are entered in the format
     * X=X+X-X, Y=X+Y
     * with variables being a single character
     */
    private RuleObj[] parseRuleInput(String input){
        RuleObj[] result = new RuleObj[vars.length()];
        String[] splitInput = input.split(", *"); //i.e. ["X=X+X-X","Y=X+Y"]

        //For each rule in the input string
        for(int i=0; i<splitInput.length; ++i){
            String rule = splitInput[i];

            //Check formatting
            rule = rule.replace(" ",""); //Remove extra spaces
            if(!rule.matches("[A-Za-z]=.*")){
                System.out.println("Badly formatted rule: " + rule + "\nExample: 'Y=X+Y-Y'");
                System.exit(0);
            }

            //Parse rule
            char var = rule.charAt(0); //Get variable
            int varIndex = vars.indexOf(var);
            if(varIndex == -1){
                System.out.println("Unmatched rule for var " + var);
                System.exit(0);
            } else {
                if(result[varIndex] == null) result[varIndex] = new RuleObj(); //Make sure it's initialized
                result[varIndex].addRule(rule.substring(2)); //Get string after "="
            }
        }
        return result;
    }

    //Just a fun method for changing the pen color
    private void changePenColor(){
        int r = (pen.getColor().getRed()); //r == 1 means delta == 1, else delta == -1
        int g = (pen.getColor().getGreen());
        int b = (pen.getColor().getBlue());
        int delta;

        if(r==1) delta = 1;
        else delta = -1;

        //Oscillate through colors, step by 1 or -1
        if(b > 254){
            delta = -1;
            r = 0;
        } else if(b < 50){
            delta = 1;
            r = 1;
        }

        b += delta;
        pen.setColor(new Color(r,b,b));
    }
}
