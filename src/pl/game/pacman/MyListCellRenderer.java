package pl.game.pacman;

import javax.swing.*;
import java.awt.*;

public class MyListCellRenderer extends JLabel implements ListCellRenderer {

    public MyListCellRenderer(){
        setOpaque(true);
    }
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setText((String)value);
        setBackground(Color.BLACK);
        setFont(new Font(Font.SERIF, Font.ITALIC,25));
        if(index==0)
            setForeground(new Color(255,215,0));
        else if (index == 1)
            setForeground(new Color(192,192,192));
        else if (index == 2)
            setForeground(new Color(205, 127, 50));
        else setForeground(Color.WHITE);


        return this;
    }
}
