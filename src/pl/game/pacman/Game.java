package pl.game.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game extends JFrame {

    private Menu menu;
    private Board board;
    private Pacman pacman;
    private List<Monster> monsters;
    private List<Thread> monsterThread;
    private Set<Integer> pressedExit;
    private volatile JTable tBoard;
    private ScoreBoard scoreboard;
    private JPanel info;
    private int timer;
    protected Label time;
    protected Label points;
    protected Label health;


    public Game(int width, int height, Menu menu,ScoreBoard scoreboard) {
        setLayout(new BorderLayout());
//        setResizable(false);

        board = new Board(width, height);
        this.menu = menu;
        this.scoreboard = scoreboard;
        tBoard = new JTable(board);
        pacman = new Pacman(tBoard,this,board);
        monsters = new ArrayList<>();
        pressedExit = new HashSet<>();
        monsterThread = new ArrayList<>();
        createMonsters(width + height);

        tBoard.setDefaultRenderer(Object.class, new MyTableCellRenderer(pacman,this));
        tBoard.setFocusable(true);
        tBoard.setForeground(Color.BLACK);
        tBoard.setBackground(Color.BLACK);
        tBoard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tBoard.setAutoCreateRowSorter(false);
        tBoard.setShowGrid(false);
        for (int i = 0; i < tBoard.getColumnCount(); i++) {
            tBoard.getColumnModel().getColumn(i).setPreferredWidth(20);
        }


        timer = 0;
        info = new JPanel(new GridLayout(1,3));
        info.setSize(getWidth(),getHeight());
        info.setBackground(Color.BLACK);
        info.setForeground(Color.YELLOW);
        time=new Label("Time: "+timer);
        new Thread(() -> {
            while(!Thread.interrupted()){
                timer++;
                time.setText("Time: "+timer);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.getMessage();
                }
            }
        }).start();
        points=new Label("Points: "+pacman.getScore());
        health=new Label("Lives: "+pacman.getLives());
        info.add(time);
        info.add(points);
        info.add(health);

        ScrollPane sp = new ScrollPane();
        sp.add(tBoard);
        add(info,BorderLayout.NORTH);
        add(sp,BorderLayout.CENTER);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusableWindowState(true);
        setFocusTraversalKeysEnabled(false);
        setSize(width*10+200, height*10+200);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                menu.setVisible(true);
            }
        });
        gameStart(tBoard);
        board.setPoints(monsters.size());
        pacman.firstSetMaxPoints(board.getPointsCounter());
    }
    private void Exit(){
        dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }
    public void createMonsters(int temp){
        int tcounter=0;
        if(temp<50) tcounter=1;
        if(temp>=50 && temp<100) tcounter=2;
        if(temp>=100 && temp<150) tcounter=3;
        if(temp>=150) tcounter=4;
        for (int i = 0; i < tcounter; i++) new Monster(tBoard,pacman,this,board);
        monsters=Monster.ghosts;
    }
    public Monster createMosnter(){
        Monster temp=new Monster(tBoard,pacman,this,board);
        monsters=Monster.ghosts;
        return temp;
    }
    private void gameStart(JTable tBoard){
        tBoard.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_Q) && !pressedExit.contains(e.getKeyCode())) {
                    pressedExit.add(e.getKeyCode());
                }
                if (pressedExit.size() == 3)
                    Exit();

                if (e.getKeyCode() == KeyEvent.VK_A) {
                    if (!tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(50)) {
                        synchronized (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1)){
                    if (!(tBoard.getValueAt(pacman.getX(), pacman.getY() - 1) instanceof Monster) && !pacman.getMove()) {
                        pacman.setMoving(true);

                        if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(1)) {
                            pacman.addPoint();
                        } else if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(5)) {
                            Monster.upgrade1();
                        }else if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(6)) {
                            int temp=(int)(Math.random()*900)+100;
                            pacman.setPoints(temp);
                            pacman.setMaxPoints(temp);
                        }else if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(7)) {
                            Monster.upgrade3();
                        }else if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(8)) {
                            Monster temp = Monster.ghosts.get((int)(Math.random()*Monster.ghosts.size()));
                            tBoard.setValueAt(0, temp.getPosX(), temp.getPosY());
                            Monster.ghosts.remove(temp.getMonsterId());
                            monsterThread.remove(temp.getMonsterId());
                            temp.setExist();
                        }else if (tBoard.getValueAt(pacman.getX(), pacman.getY() - 1).equals(9)) {
                            Monster temp = new Monster(tBoard,pacman,Game.this, board);
                            tBoard.setValueAt(temp, temp.getPosX(), temp.getPosY());
                            Thread t = new Thread(temp);
                            monsterThread.add(t);
                            Monster.ghosts.remove(temp);
                            t.start();

                            monsterThread.get(temp.getMonsterId()).interrupt();
                        }
                        pacman.setMove(4);
                        tBoard.setValueAt(0, pacman.getX(), pacman.getY());
                        tBoard.setValueAt(pacman, pacman.getX(), pacman.getY() - 1);
                        pacman.setY(pacman.getY() - 1);
                        tBoard.repaint();
                        pacman.setMoving(false);
                    }
                    else {
                        pacman.currentPosRemove();
                        pacman.pacmanDead();
                        pacman.reSpawn();
                    }}
                    }
                    checkWin();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    if (!tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(50)) {
                        synchronized (tBoard.getValueAt(pacman.getX() + 1, pacman.getY())){
                    if (!(tBoard.getValueAt(pacman.getX() + 1, pacman.getY()) instanceof Monster) && !pacman.getMove()) {

                        pacman.setMoving(true);
                        if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(1)) {
                            pacman.addPoint();
                        } else if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(5)) {
                            Monster.upgrade1();
                        }else if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(6)) {
                            int temp=(int)(Math.random()*900)+100;
                            pacman.setPoints(temp);
                            pacman.setMaxPoints(temp);
                        }else if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(7)) {
                            Monster.upgrade3();
                        }else if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(8)) {
                            Monster temp = Monster.ghosts.get((int)(Math.random()*Monster.ghosts.size()));
                            tBoard.setValueAt(0, temp.getPosX(), temp.getPosY());
                            tBoard.repaint();
                            Monster.ghosts.remove(temp);
                            monsterThread.remove(temp.getMonsterId());
                            temp.setExist();
                        }else if (tBoard.getValueAt(pacman.getX() + 1, pacman.getY()).equals(9)) {
                            Monster temp = new Monster(tBoard,pacman,Game.this, board);
                            tBoard.setValueAt(temp, temp.getPosX(), temp.getPosY());
                            Thread t = new Thread(temp);
                            monsterThread.add(t);
                            Monster.ghosts.remove(temp);
                            t.start();

                            monsterThread.get(temp.getMonsterId()).interrupt();
                        }
                        pacman.setMove(3);
                        tBoard.setValueAt(0, pacman.getX(), pacman.getY());
                        tBoard.setValueAt(pacman, pacman.getX() + 1, pacman.getY());
                        pacman.setX(pacman.getX() + 1);
                        tBoard.repaint();
                    pacman.setMoving(false);
                    }
                    else {
                        pacman.currentPosRemove();
                        pacman.pacmanDead();
                        pacman.reSpawn();
                    }}
                }
                    checkWin();
            }

                if(e.getKeyCode() == KeyEvent.VK_W){
                    if(!tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(50)){
                        synchronized (tBoard.getValueAt(pacman.getX()-1,pacman.getY())){
                    if(!(tBoard.getValueAt(pacman.getX()-1,pacman.getY()) instanceof Monster) && !pacman.getMove()){
                        pacman.setMoving(true);

                        if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(1)) {
                            pacman.addPoint();
                        } else if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(5)) {
                            Monster.upgrade1();
                        }else if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(6)) {
                            int temp=(int)(Math.random()*900)+100;
                            pacman.setPoints(temp);
                            pacman.setMaxPoints(temp);
                        }else if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(7)) {
                            Monster.upgrade3();
                        }else if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(8)) {
                            Monster temp = Monster.ghosts.get((int)(Math.random()*Monster.ghosts.size()));
                            tBoard.setValueAt(0, temp.getPosX(), temp.getPosY());
                            tBoard.repaint();
                            Monster.ghosts.remove(temp);
                            monsterThread.remove(temp.getMonsterId());
                            temp.setExist();
                        }else if (tBoard.getValueAt(pacman.getX()-1,pacman.getY()).equals(9)) {
                            Monster temp = new Monster(tBoard,pacman,Game.this, board);
                            tBoard.setValueAt(temp, temp.getPosX(), temp.getPosY());
                            Thread t = new Thread(temp);
                            monsterThread.add(t);
                            Monster.ghosts.remove(temp);
                            t.start();

                            monsterThread.get(temp.getMonsterId()).interrupt();
                        }

                        pacman.setMove(1);
                        tBoard.setValueAt(0,pacman.getX(),pacman.getY());
                        tBoard.setValueAt(pacman,pacman.getX()-1,pacman.getY());
                        pacman.setX(pacman.getX()-1);
                        tBoard.repaint();

                        pacman.setMoving(false);}
                    else {
                        pacman.currentPosRemove();
                        pacman.pacmanDead();
                        pacman.reSpawn();
                    }}
                    }
                    checkWin();
                }
                if(e.getKeyCode() == KeyEvent.VK_D){
                    if(!tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(50)){
                        synchronized (tBoard.getValueAt(pacman.getX(),pacman.getY()+1)){
                    if(!(tBoard.getValueAt(pacman.getX(),pacman.getY()+1) instanceof Monster) && !pacman.getMove()){
                        pacman.setMoving(true);

                        if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(1)) {
                            pacman.addPoint();
                        } else if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(5)) {
                            Monster.upgrade1();
                        }else if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(6)) {
                            int temp=(int)(Math.random()*900)+100;
                            pacman.setPoints(temp);
                            pacman.setMaxPoints(temp);
                        }else if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(7)) {
                            Monster.upgrade3();
                        }else if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(8)) {
                            Monster temp = Monster.ghosts.get((int)(Math.random()*Monster.ghosts.size()));
                            tBoard.setValueAt(0, temp.getPosX(), temp.getPosY());
                            tBoard.repaint();
                            Monster.ghosts.remove(temp);
                            monsterThread.remove(temp.getMonsterId());
                            temp.setExist();
                        }else if (tBoard.getValueAt(pacman.getX(),pacman.getY()+1).equals(9)) {
                            Monster temp = new Monster(tBoard,pacman,Game.this, board);
                            tBoard.setValueAt(temp, temp.getPosX(), temp.getPosY());
                            Thread t = new Thread(temp);
                            monsterThread.add(t);
                            Monster.ghosts.remove(temp);
                            t.start();

                            monsterThread.get(temp.getMonsterId()).interrupt();
                        }

                        pacman.setMove(2);
                        tBoard.setValueAt(0,pacman.getX(),pacman.getY());
                        tBoard.setValueAt(pacman,pacman.getX(),pacman.getY()+1);
                        pacman.setY(pacman.getY()+1);
                        tBoard.repaint();

//                        }
                        pacman.setMoving(false);}
                    else {
                        pacman.currentPosRemove();
                        pacman.pacmanDead();
                        pacman.reSpawn();
                    }}
                    }
                    checkWin();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(pressedExit.contains(e.getKeyCode()))pressedExit.remove(e.getKeyCode());
            }
        });

        for (Runnable monster : monsters){
            monsterThread.add(new Thread(monster));
        }
        for (Thread monster : monsterThread){
            monster.start();
        }


        Thread t = new Thread(pacman);
        monsterThread.add(t);
        t.start();

    }
    public void checkWin(){

        if(pacman.getScore()>=pacman.getMaxPoints() ||pacman.getLives()==0)
        {
            for (Thread monster : monsterThread)
                monster.interrupt();

            String name = JOptionPane.showInputDialog("Enter nickname");
            if (name != null)scoreboard.addPlayer(name,pacman.getScore()-timer);
            Exit();
        }
    }


}
