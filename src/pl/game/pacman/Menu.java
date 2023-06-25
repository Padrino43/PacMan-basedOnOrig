package pl.game.pacman;


import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Menu extends JFrame {
    private JPanel pOptions;
    private JLabel photoLabel;
    private JButton game;
    private JButton highScore;
    private JButton exit;
    private ScoreBoard scoreboard;
    private Image photo;



    public Menu() {
        super("Pacman");
        setSize(600,400);
        setLayout(new GridLayout(2,1));
        setBackground(Color.BLACK);
        setResizable(false);

        ImageIcon temp = new ImageIcon("src/img/main_menu_bg.jpg");
        photo = temp.getImage().getScaledInstance(getWidth(),getHeight()/2,Image.SCALE_SMOOTH);
        photoLabel = new JLabel(new ImageIcon(photo));

        photoLabel.setBackground(Color.BLACK);
        photoLabel.setForeground(Color.BLACK);
        add(photoLabel);

        pOptions = new JPanel();
        pOptions.setLayout(new FlowLayout(FlowLayout.CENTER,30,35));
        pOptions.setBackground(Color.BLACK);

        game = new JButton();
        game.setPreferredSize(new Dimension(180,50));
        game.setIcon(new ImageIcon("src/img/startgame.png"));
        game.addActionListener(e -> {
            game();
        });

        highScore = new JButton();
        highScore.setPreferredSize(new Dimension(180,50));
        highScore.setIcon(new ImageIcon("src/img/HighScoreButton.png"));
        highScore.addActionListener(e -> {
            scoreboard.setVisible(true);
        });

        exit = new JButton();
        exit.setPreferredSize(new Dimension(180,50));
        exit.setIcon(new ImageIcon("src/img/ExitButton.png"));
        exit.addActionListener(e -> {
            closeSaveFile();
            System.exit(0);
        });

        pOptions.add(game);
        pOptions.add(highScore);
        pOptions.add(exit);

        add(pOptions);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        openSaveFile();
    }

    public void game()
    {
        JSlider width = new JSlider(JSlider.HORIZONTAL,10,100,11);
        JSlider height = new JSlider(JSlider.HORIZONTAL,10,100,11);
        JLabel lb = new JLabel("Wybrana wielkosc planszy = w-"+width.getValue()+" h-"+height.getValue());
        width.setPaintTicks(true);
        width.setPaintLabels(true);
        width.setMajorTickSpacing(30);
        width.setMinorTickSpacing(10);
        width.addChangeListener(e -> lb.setText("Wybrana wielkosc planszy = w-"+width.getValue()+" h-"+height.getValue()));
        height.setPaintTicks(true);
        height.setPaintLabels(true);
        height.setMajorTickSpacing(30);
        height.setMinorTickSpacing(10);
        height.addChangeListener(e -> lb.setText("Wybrana wielkosc planszy = w-"+width.getValue()+" h-"+height.getValue()));

        Object[] info = {lb,width,height};
        int temp=JOptionPane.showConfirmDialog(null,info,"Rozpocznij gre", JOptionPane.OK_CANCEL_OPTION);
        if (temp == JOptionPane.OK_OPTION) {
            new Game(height.getValue(), width.getValue(), this,scoreboard);
            this.dispose();
        }

    }
    private void openSaveFile(){
        try {

            ObjectInputStream o = new ObjectInputStream(
                    new FileInputStream("src/pl/game/pacman/ScoreBoardSerializable.bin")
            );
            scoreboard = (ScoreBoard) o.readObject();
            o.close();

        } catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
    }
    private void closeSaveFile(){
        try
        {
            ObjectOutputStream o =
                    new ObjectOutputStream(
                            new FileOutputStream("src/pl/game/pacman/ScoreBoardSerializable.bin")
            );
            o.writeObject(scoreboard);
            o.close();

        } catch (Exception ex)
        {
            ex.getMessage();
        }
    }



}
