package edu.colostate.cs.gc.event;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/16/15
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Cell implements Comparable<Cell> {

    private int column;
    private int row;

    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;

        Cell cell = (Cell) o;

        if (column != cell.column) return false;
        if (row != cell.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = column;
        result = 31 * result + row;
        return result;
    }

    @Override
    public String toString() {
        return (this.column + 1) + "." + (this.row + 1);
    }

    public int compareTo(Cell o) {
        if (this.column == o.column) {
            if (this.row == o.row) {
                return 0;
            } else if (this.row > o.row) {
                return 1;
            } else {
                return -1;
            }
        } else if (this.column > o.column) {
            return 1;
        } else {
            return -1;
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
