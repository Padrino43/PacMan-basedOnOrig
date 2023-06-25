package pl.game.pacman;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class ScoreBoard extends JFrame implements Serializable {

    @Serial
    private static final long serialVersionUID = 13L;
    private JList listOfPlayers;
    private Map<String,Integer> playerPoints;
    private Vector<String> temp;
    private MyListModel mylistmodel;

    public ScoreBoard() {
        super("Score Board");

        playerPoints = new HashMap<>();
        temp = new Vector<>();
        mylistmodel = new MyListModel(temp);
        listOfPlayers = new JList(mylistmodel);
        listOfPlayers.setCellRenderer(new MyListCellRenderer());
        listOfPlayers.setBackground(Color.BLACK);

        ScrollPane temp = new ScrollPane();
        temp.add(listOfPlayers);
        add(temp);


        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setSize(500,600);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    public void addPlayer(String name, int score){
        playerPoints.put(name,score);
        scoreBoardSort();
        temp.clear();
        for (Map.Entry<String, Integer> map :
                playerPoints.entrySet()) {
            temp.add(map.getKey()+ " - "+map.getValue());
        }
        mylistmodel=new MyListModel(temp);
        listOfPlayers = new JList(mylistmodel);
    }

    private void scoreBoardSort(){
        ArrayList<Integer> points = new ArrayList<>();
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : playerPoints.entrySet()) {
            points.add(entry.getValue());
        }
        Collections.sort(points);
        Collections.reverse(points);
        for (int temp : points) {
            for (Map.Entry<String, Integer> entry : playerPoints.entrySet()) {
                if (entry.getValue().equals(temp)) {
                    map.put(entry.getKey(), temp);
                }
            }
        }
        playerPoints=map;
    }

}
