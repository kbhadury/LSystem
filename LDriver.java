import java.awt.Color;
import java.util.Random;

public class LDriver{
    public static void main(String[] args){
        LSystem sys = new LSystem();
        Random rand = new Random();
        sys.getInput();
        //draw(x, y, direction, width, color
        for(int i = 0; i < 7; ++i){
            int x = rand.nextInt(500)-250;
            int y = rand.nextInt(100)-50;
            sys.draw(x, y, 90, 2, new Color(1,150,150));
        }
    }
}
