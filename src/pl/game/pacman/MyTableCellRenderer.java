package pl.game.pacman;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class MyTableCellRenderer extends DefaultTableCellRenderer {

    private Image ipellet;
    private Image upgrade;
    private Pacman pacman;
    private Game game;

    public MyTableCellRenderer(Pacman pacman,Game game){
        this.pacman=pacman;
        this.game=game;
        ImageIcon temp = new ImageIcon("src/gameImg/point.png");
        ipellet = temp.getImage().getScaledInstance(7,7,Image.SCALE_SMOOTH);
        temp = new ImageIcon("src/gameImg/pacman/upgrade.png");
        upgrade = temp.getImage().getScaledInstance(14,14,Image.SCALE_SMOOTH);
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component temp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if(value.equals(50)) {
            temp.setBackground(Color.BLUE);
            temp.setForeground(Color.BLUE);
            setIcon(null);
        }  else if (value.equals(pacman)) {
            temp.setBackground(Color.BLACK);
            temp.setForeground(Color.BLACK);
           setIcon(new ImageIcon(pacman.getPacmanImage()));

        } else if (value instanceof Monster) {
            Monster monster = (Monster)value;
            int id = monster.getMonsterId();
            temp.setBackground(Color.BLACK);
            temp.setForeground(Color.BLACK);
            setIcon(new ImageIcon(monster.getImage()));
        } else if (value.equals(1)) {
            temp.setBackground(null);
            temp.setForeground(null);
            setIcon(new ImageIcon(ipellet));
        } else if (value.equals(5)||value.equals(6)||value.equals(7)||value.equals(8)||value.equals(9)) {
            temp.setBackground(null);
            temp.setForeground(null);
            setIcon(new ImageIcon(upgrade));
        } else {
            setIcon(null);
            temp.setBackground(Color.BLACK);
            temp.setForeground(Color.BLACK);
        }
        return this;
    }





}
