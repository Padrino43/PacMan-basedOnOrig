package pl.game.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pacman implements Runnable {

    private int posX;
    private int posY;
    private Image pacmanImageOpenL;
    private Image pacmanImageClosedL;
    private Image pacmanImageOpenU;
    private Image pacmanImageClosedU;
    private Image pacmanImageOpenR;
    private Image pacmanImageClosedR;
    private Image pacmanImageOpenD;
    private Image pacmanImageClosedD;
    private Image mainPacmanImageOpen;
    private Image mainPacmanImageClosed;
    private Image mainImage;

    private int maxPoints;
    private int lives;
    private int points;
    private int move;
    private JTable board;
    private Board board1;
    private boolean moving;
    private int speed;
    private Game game;

    public Pacman(JTable board,Game game,Board board1){
        renderImages();
        this.board=board;
        this.board1=board1;
        speed=1;
        do{
            posX=(int)(Math.random()*board.getRowCount());
            posY=(int)(Math.random()*board.getColumnCount());
        }while((int)board.getValueAt(posX,posY)==50);
        board.setValueAt(this,posX,posY);
        board.repaint();
        lives = 3;
        move = 1;
        points = 0;
        moving=false;
        this.game=game;
        setMove(move);
        maxPoints= board1.getPointsCounter();
        System.out.println(maxPoints);
    }

    private void renderImages() {

            ImageIcon temp = new ImageIcon("src/gameImg/pacman/open/1.png");
            pacmanImageOpenU = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
            temp = new ImageIcon("src/gameImg/pacman/closed/1.png");
            pacmanImageClosedU = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);

            temp = new ImageIcon("src/gameImg/pacman/open/2.png");
            pacmanImageOpenR = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
            temp = new ImageIcon("src/gameImg/pacman/closed/2.png");
            pacmanImageClosedR = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);

            temp = new ImageIcon("src/gameImg/pacman/open/3.png");
            pacmanImageOpenD = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
            temp = new ImageIcon("src/gameImg/pacman/closed/3.png");
            pacmanImageClosedD = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);

            temp = new ImageIcon("src/gameImg/pacman/open/4.png");
            pacmanImageOpenL = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
            temp = new ImageIcon("src/gameImg/pacman/closed/4.png");
            pacmanImageClosedL = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
        mainPacmanImageOpen=pacmanImageOpenU;
        mainPacmanImageClosed=pacmanImageOpenU;
        mainImage=mainPacmanImageClosed;

    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public void setX(int posX) {
        this.posX = posX;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

    public void setMove(int move) {
        this.move = move;
        if(move==1){
            mainPacmanImageOpen=pacmanImageOpenU;
            mainPacmanImageClosed=pacmanImageClosedU;
        } else if (move==2) {
            mainPacmanImageOpen=pacmanImageOpenR;
            mainPacmanImageClosed=pacmanImageClosedR;
        } else if (move==3) {
            mainPacmanImageOpen=pacmanImageOpenD;
            mainPacmanImageClosed=pacmanImageClosedD;
        }
        else {
            mainPacmanImageOpen=pacmanImageOpenL;
            mainPacmanImageClosed=pacmanImageClosedL;
        }
    }
    public Image getPacmanImage() {
        return mainImage;
    }
    public void addPoint(){
        points+=20;
        game.points.setText("Points: "+points);
    }
    public void firstSetMaxPoints(int temp){
        maxPoints=temp;
    }
    public void setMaxPoints(int temp){
        maxPoints+=temp;
    }
    public void setPoints(int temp){
        points+=temp;
        game.points.setText("Points: "+points);
    }

    public int getScore() {
        return points;
    }
    public int getLives(){
        return lives;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void currentPosRemove(){
    board.setValueAt(0,posX,posY);
}
    public void reSpawn(){
    do{
        posX=(int)(Math.random()*board.getRowCount());
        posY=(int)(Math.random()*board.getColumnCount());
    }while(!board.getValueAt(posX,posY).equals(0));
    board.setValueAt(this,posX,posY);
    board.repaint();
    }
    public void pacmanDead(){
            lives--;
            game.health.setText("Lives: "+lives);
    }
        public boolean getMove(){
            return moving;
        }
    public int getMaxPoints(){
        return maxPoints;
    }


    @Override
    public void run() {
        while(!Thread.interrupted()){

                try {
                    mainImage=mainPacmanImageClosed;
                    board.repaint();
                    Thread.sleep(200);
                    mainImage=mainPacmanImageOpen;
                    board.repaint();


                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.getMessage();
                }

            }


    }
}
