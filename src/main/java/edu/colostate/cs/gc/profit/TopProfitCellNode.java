package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.event.Cell;
import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopProfitCellNode implements NodeValue {

    private double profitability;
    private double midFare;
    private int numOfEmptyTaxis;

    private int seqNo;
    private Cell cell;

    public TopProfitCellNode() {
    }

    public TopProfitCellNode(double profitability, double midFare, int numOfEmptyTaxis, int seqNo, Cell cell) {
        this.profitability = profitability;
        this.midFare = midFare;
        this.numOfEmptyTaxis = numOfEmptyTaxis;
        this.seqNo = seqNo;
        this.cell = cell;
    }

    public int compare(NodeValue value) {
        TopProfitCellNode newValue = (TopProfitCellNode) value;
        if (this.profitability > newValue.profitability){
            return 1;
        } else if (this.profitability < newValue.profitability){
            return -1;
        } else {
            if (this.seqNo > newValue.seqNo){
                return 1;
            } else if (this.seqNo < newValue.seqNo){
                return -1;
            } else {
                return 0;
            }
        }
    }

    public Object getKey() {
        return this.cell;
    }

    public void update(NodeValue value) {
        TopProfitCellNode newValue = (TopProfitCellNode) value;
        this.profitability = newValue.profitability;
        this.midFare = newValue.midFare;
        this.numOfEmptyTaxis = newValue.numOfEmptyTaxis;
        this.seqNo = newValue.seqNo;
    }

    public NodeValue getClone() {
        TopProfitCellNode topProfitCellNode = new TopProfitCellNode();
        topProfitCellNode.setProfitability(this.profitability);
        topProfitCellNode.setMidFare(this.midFare);
        topProfitCellNode.setNumOfEmptyTaxis(this.numOfEmptyTaxis);
        topProfitCellNode.setSeqNo(this.seqNo);
        topProfitCellNode.setCell(this.cell);
        return topProfitCellNode;
    }

    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeDouble(this.profitability);
            dataOutput.writeDouble(this.midFare);
            dataOutput.writeInt(this.numOfEmptyTaxis);
            dataOutput.writeInt(this.seqNo);
            this.cell.serialize(dataOutput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }

    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.profitability = dataInput.readDouble();
            this.midFare = dataInput.readDouble();
            this.numOfEmptyTaxis = dataInput.readInt();
            this.seqNo = dataInput.readInt();
            this.cell = new Cell();
            this.cell.parse(dataInput);
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopProfitCellNode that = (TopProfitCellNode) o;

        if (!cell.equals(that.cell)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cell.hashCode();
    }

    public double getProfitability() {
        return profitability;
    }

    public void setProfitability(double profitability) {
        this.profitability = profitability;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public double getMidFare() {
        return midFare;
    }

    public void setMidFare(double midFare) {
        this.midFare = midFare;
    }

    public int getNumOfEmptyTaxis() {
        return numOfEmptyTaxis;
    }

    public void setNumOfEmptyTaxis(int numOfEmptyTaxis) {
        this.numOfEmptyTaxis = numOfEmptyTaxis;
    }
}
