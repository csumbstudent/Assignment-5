/**
 * Created by Chris on 10/1/2015.
 */
import javax.swing.*;
import java.awt.*;

public class Phase1 {
    static final int NUM_CARD_IMAGES = 57;
    static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];
    static final String[] VALID_VALUES = {"A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "J", "K", "Q", "X"};
    static final String[] VALID_SUITS = {"C", "D", "H", "S"};
    static void loadCardIcons(){
        int arrayPosition = 0;
        for(String value : Phase1.VALID_VALUES)
            for(String suit : Phase1.VALID_SUITS){
                Phase1.icon[arrayPosition] = new ImageIcon("../images/" + value + suit + ".gif");
                arrayPosition++;
            }
        Phase1.icon[arrayPosition] = new ImageIcon("../images/BK.gif");

    }
    public static void main(String[] args){
        loadCardIcons();

        JFrame frmMyWindow = new JFrame("Card Room");
        frmMyWindow.setSize(1150, 650);
        frmMyWindow.setLocationRelativeTo(null);
        frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);
        frmMyWindow.setLayout(layout);
        JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
        for(int i = 0; i < NUM_CARD_IMAGES; i++)
            labels[i] = new JLabel(icon[i]);
        for(int i = 0; i < NUM_CARD_IMAGES; i++)
            frmMyWindow.add(labels[i]);
        frmMyWindow.setVisible(true);
    }
}
