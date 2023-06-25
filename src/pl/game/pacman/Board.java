package pl.game.pacman;

import javax.swing.table.AbstractTableModel;

public class Board extends AbstractTableModel {

    private int row;
    private int column;
    private Object[][] board;
    private int pointsCounter;
    public Board(int row, int column) {
        this.column = column;
        this.row = row;
        board = new Object[row][column];
        createBoard();
    }

    private void createBoard(){

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++){
                board[i][j] = 50;
            }
        }
        int pointX;
        int pointY;
        do{
            pointX = (int)(Math.random()*column);
            pointY = (int)(Math.random()*row);
        }while ((pointX & 1) == 1 || (pointY & 1) == 1);
        board[pointY][pointX] = 0;
        int visited = 1;
        int total = (row * column) / 2;
        int countMoves;
        int[] posMove = new int [4];
        int[][] stack = new int[10][2];
        int stackIndex = 0;
        while (visited < total) {
            countMoves = 0;
            if(pointY - 2 >= 0 && (int)board[pointY - 2][pointX] == 50 && pointY - 2 != 0) posMove[countMoves++] = 0;
            if(pointY + 2 < board.length && (int)board[pointY + 2][pointX] == 50 && pointY + 2 != column - 1) posMove[countMoves++] = 1;
            if(pointX - 2 >= 0 && (int)board[pointY][pointX - 2] == 50 && pointX - 2 != 0) posMove[countMoves++] = 2;
            if(pointX + 2 < board[pointY].length && (int)board[pointY][pointX + 2] == 50 && pointX + 2 != row - 1) posMove[countMoves++] = 3;
            if(countMoves != 0) {
                switch (posMove[(int) (Math.random() * countMoves)]) {
                    case 0 -> {
                        board[--pointY][pointX] = 0;
                        board[--pointY][pointX] = 0;
                    }
                    case 1 -> {
                        board[++pointY][pointX] = 0;
                        board[++pointY][pointX] = 0;
                    }
                    case 2 -> {
                        board[pointY][--pointX] = 0;
                        board[pointY][--pointX] = 0;
                    }
                    case 3 -> {
                        board[pointY][++pointX] = 0;
                        board[pointY][++pointX] = 0;
                    }
                }
                stack[stackIndex][0] = pointY;
                stack[stackIndex++][1] = pointX;
                if(stackIndex == stack.length) stack = expandStack(stack);
                visited++;
            }
            else {
                if(stackIndex==0) {
                    break;
                }
                pointY = stack[--stackIndex][0];
                pointX = stack[stackIndex][1];
            }
        }


        removeRedundant();
        completeMaze();
        fillWithPoints();
    }
    private static int[][] expandStack(int[][] stack) {
        int lenght = stack.length;
        int[][] temp = stack.clone();
        stack = new int[++lenght][2];
        for (int i = 0; i < temp.length; i++) {
            stack[i][0] = temp[i][0];
            stack[i][1] = temp[i][1];
        }
        return stack;
    }
    private void removeRedundant(){
        for (int i = 1; i < row - 3; i++) {
            for (int j = 1; j < column-3; j++) {
                if(checkIfOccupied(i,j))remove(i,j);
            }
        }
    }
    private boolean checkIfOccupied(int x, int y) {
        return (int)board[x + 1][y] == 50 && (int)board[x][y + 1] == 50 && (int)board[x][y] == 50 && (int)board[x - 1][y] == 50 && (int)board[x][y - 1] == 50;
    }
    private void remove(int x, int y){
        board[x+1][y]=0;
        board[x][y+1]=0;
        board[x][y-1]=0;
        board[x-1][y]=0;
        board[x][y]=0;
    }
    private void completeMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i == 1 || i == row - 2 || j == 1 || j == column - 2) {
                    board[i][j] = 0;
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i == 0 || i == row - 1 || j == 0 || j == column - 1) {
                    board[i][j] = 50;
                }
            }
        }
    }
    private void fillWithPoints(){
        pointsCounter=0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if ((int)board[i][j] == 0)
                {board[i][j] = 1;pointsCounter+=20;
                }
            }
        }
    }


    public int getPointsCounter() {
        return pointsCounter;
    }
    public void setPoints(int temp){
        pointsCounter-=(temp*20+20);
        System.out.println(pointsCounter);
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return column;
    }

    @Override
    public synchronized Object getValueAt(int rowIndex, int columnIndex) {
        return board[rowIndex][columnIndex];
    }

    @Override
    public synchronized void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        board[rowIndex][columnIndex]=aValue;
        fireTableCellUpdated(rowIndex,columnIndex);
    }
}
