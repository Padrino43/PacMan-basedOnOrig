package pl.game.pacman;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Monster implements Runnable {
    public static ArrayList<Monster> ghosts = new ArrayList<>();
    private static int counter = 0;
    private int id;
    private int posX;
    private int posY;
    private Image up;
    private Image right;
    private Image down;
    private Image left;
    private Image main;
    private JTable board;
    private Board boardNext;
    private Pacman pacman;
    private int move;
    private Game game;
    private Runnable upgrades;
    private static int moveSpeed;
    private int earlierMove;
    protected boolean exist;



    public Monster(JTable board, Pacman pacman,Game game, Board boardNext) {
        ghosts.add(this);
        this.game=game;
        this.board = board;
        this.pacman = pacman;
        this.boardNext=boardNext;
        exist=true;

        do {
            posX = (int) (Math.random() * board.getRowCount());
            posY = (int) (Math.random() * board.getColumnCount());
        } while (board.getValueAt(posX, posY).equals(50));
        board.setValueAt(this, posX, posY);
        board.repaint();
        id = counter;
        counter++;
        move=1;
        createImages();
        main=up;
        upgrades = () -> {
            int temp1,temp2;
            while(exist) {
                switch ((int)(Math.random()*4)) {
                    case 0 -> {
                        if (!board.getValueAt(posX, posY).equals(1)) {
                            switch ((int) (Math.random() * 5)) {
                                case 0 -> {
                                    do {
                                        temp1 = (int) (Math.random() * board.getRowCount());
                                        temp2 = (int) (Math.random() * board.getColumnCount());
                                    } while (!board.getValueAt(temp1, temp2).equals(0));
                                    board.setValueAt(9, temp1, temp2);}
                                case 1 -> {
                                    do {
                                    temp1 = (int) (Math.random() * board.getRowCount());
                                    temp2 = (int) (Math.random() * board.getColumnCount());
                                } while (!board.getValueAt(temp1, temp2).equals(0));
                                    board.setValueAt(8, temp1, temp2);}
                                case 2 -> {
                                    do {
                                        temp1 = (int) (Math.random() * board.getRowCount());
                                        temp2 = (int) (Math.random() * board.getColumnCount());
                                    } while (!board.getValueAt(temp1, temp2).equals(0));
                                    board.setValueAt(7, temp1, temp2);}
                                case 3 -> {
                                    do {
                                        temp1 = (int) (Math.random() * board.getRowCount());
                                        temp2 = (int) (Math.random() * board.getColumnCount());
                                    } while (!board.getValueAt(temp1, temp2).equals(0));
                                    board.setValueAt(6, temp1, temp2);}
                                case 4 -> {
                                    do {
                                        temp1 = (int) (Math.random() * board.getRowCount());
                                        temp2 = (int) (Math.random() * board.getColumnCount());
                                    } while (!board.getValueAt(temp1, temp2).equals(0));
                                    board.setValueAt(5, temp1, temp2);}
                            }
                            board.repaint();
                            earlierMove=0;
                        }
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };
        moveSpeed=1200;
        earlierMove = 0;
    }
    public static void upgrade1(){
        new Thread(()-> {
            try {
                moveSpeed=800;
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.getMessage();
            }
            moveSpeed=1200;
        }).start();
    }
    public static void upgrade3(){
        new Thread(()-> {
            try {
                moveSpeed=2000;
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.getMessage();
            }
            moveSpeed=1200;
        }).start();
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    private void createImages(){
    ImageIcon temp = new ImageIcon("src/gameImg/monster/1.png");
    up = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
    temp = new ImageIcon("src/gameImg/monster/2.png");
    right = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
    temp = new ImageIcon("src/gameImg/monster/3.png");
    down = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
    temp = new ImageIcon("src/gameImg/monster/4.png");
    left = temp.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
    }

    public Image getImage(){
        return main;
    }

    public int getMonsterId() {
        return id;
    }
    public void setExist(){
        exist=false;
    }

    @Override
    public void run() {
        int possibleMove;
        Thread t = new Thread(upgrades);
        t.start();
        while (exist) {
                try {
                    Thread.sleep(moveSpeed);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            possibleMove = 20;
            while (possibleMove == 20) {
                switch ((int) (Math.random() * 4)) {
                    case 0 -> {
                        synchronized (board.getValueAt(posX, posY - 1)) {
                            if (!board.getValueAt(posX, posY - 1).equals(50) && !(board.getValueAt(posX, posY - 1) instanceof Monster)) {
                                if(board.getValueAt(posX, posY - 1).equals(pacman)){
                                    pacman.currentPosRemove();
                                    pacman.pacmanDead();
                                    pacman.reSpawn();
                                    game.checkWin();

                                }
                                possibleMove = 0;move=4;main=left;
                            }
                        }

                    }
                    case 1 -> {
                        synchronized (board.getValueAt(posX + 1, posY)) {
                            if (!board.getValueAt(posX + 1, posY).equals(50) && !(board.getValueAt(posX + 1, posY) instanceof Monster)) {
                                if(board.getValueAt(posX + 1, posY).equals(pacman)){
                                    pacman.currentPosRemove();
                                    pacman.pacmanDead();
                                    pacman.reSpawn();
                                    game.checkWin();
                                }
                                possibleMove = 1;move=3; main=down;
                            }
                        }

                    }
                    case 2 -> {
                        synchronized (board.getValueAt(posX - 1, posY)) {
                            if (!board.getValueAt(posX - 1, posY).equals(50) && !(board.getValueAt(posX - 1, posY) instanceof Monster)) {
                                if(board.getValueAt(posX - 1, posY).equals(pacman)){
                                    pacman.currentPosRemove();
                                    pacman.pacmanDead();
                                    pacman.reSpawn();
                                    game.checkWin();
                                }
                                possibleMove = 2;move=1;main=up;
                            }
                        }

                    }
                    case 3 -> {
                        synchronized (board.getValueAt(posX, posY + 1)) {
                            if (!board.getValueAt(posX, posY + 1).equals(50) && !(board.getValueAt(posX, posY + 1) instanceof Monster)) {
                                if(board.getValueAt(posX, posY+1).equals(pacman)){
                                    pacman.currentPosRemove();
                                    pacman.pacmanDead();
                                    pacman.reSpawn();
                                    game.checkWin();
                                }
                                possibleMove = 3;move=2;main=right;
                            }
                        }

                    }

                }
                switch (possibleMove) {

                    //A
                    case 0 -> {
                        if (!board.getValueAt(posX, posY - 1).equals(50) && !(board.getValueAt(posX, posY - 1) instanceof Monster)) {
                            board.setValueAt(earlierMove, posX, posY);
                            if (!board.getValueAt(posX, posY - 1).equals(0)) {
                                earlierMove = (int)board.getValueAt(posX, posY - 1);
                            } else earlierMove = 0;
                            board.setValueAt(this, posX, posY - 1);
                            posY -= 1;
                            board.repaint();
                        }
                    }
                    //S
                    case 1 -> {
                        if (!board.getValueAt(posX + 1, posY).equals(50) && !(board.getValueAt(posX + 1, posY) instanceof Monster)) {
                            board.setValueAt(earlierMove, posX, posY);
                            if (!board.getValueAt(posX + 1, posY).equals(0)) {
                                earlierMove = (int)board.getValueAt(posX + 1, posY);
                            } else earlierMove = 0;
                            board.setValueAt(this, posX + 1, posY);
                            posX += 1;
                            board.repaint();
                        }
                    }

                    //W
                    case 2 -> {
                        if (!board.getValueAt(posX - 1, posY).equals(50) && !(board.getValueAt(posX - 1, posY) instanceof Monster)) {

                            board.setValueAt(earlierMove, posX, posY);
                           if (!board.getValueAt(posX - 1, posY).equals(0)) {
                                earlierMove = (int)board.getValueAt(posX - 1, posY);
                            } else {earlierMove = 0;}
                            board.setValueAt(this, posX - 1, posY);
                            posX -= 1;
                            board.repaint();
                        }

                    }
                    //D
                    case 3 -> {
                        if (!board.getValueAt(posX, posY + 1).equals(50) && !(board.getValueAt(posX, posY + 1) instanceof Monster)) {

                            board.setValueAt(earlierMove, posX, posY);
                            if (!board.getValueAt(posX, posY + 1).equals(0)) {
                                earlierMove = (int)board.getValueAt(posX, posY + 1);
                            } else earlierMove = 0;
                            board.setValueAt(this, posX, posY + 1);
                            posY += 1;
                            board.repaint();

                        }


                    }

                }

            }
        }
    }
}
