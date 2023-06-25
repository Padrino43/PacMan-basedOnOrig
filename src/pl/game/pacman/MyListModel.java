package pl.game.pacman;

import javax.swing.*;
import java.util.Vector;

public class MyListModel extends AbstractListModel {
    Vector<String> model;

    MyListModel(Vector<String> model){
        this.model=model;
    }

    @Override
    public int getSize() {
        return model.size();
    }

    @Override
    public Object getElementAt(int index) {
        return model.get(index);
    }
}
