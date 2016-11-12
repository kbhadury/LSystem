import java.awt.Color;
public class LDriver{
    public static void main(String[] args){
        LSystem sys = new LSystem();
        sys.getInput();
        sys.draw(-250, -50, 90, 2, new Color(1,150,150), 1);
    }
}
