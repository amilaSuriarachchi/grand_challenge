package edu.colostate.cs.gc.event;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.profit.TopProfitCellNode;
import edu.colostate.cs.gc.route.TopRouteCount;
import edu.colostate.cs.worker.comm.exception.MessageProcessingException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopProfitsEvent extends TopEvent {

    public TopProfitsEvent() {
    }

    public TopProfitsEvent(long startTime, String pickUpTime, long dropOffTime) {
        super(startTime, pickUpTime, dropOffTime);
    }

    @Override
    public Object getKey() {
        return new Long(this.dropOffTime);
    }

    @Override
    public void serialize(DataOutput dataOutput) throws MessageProcessingException {
        try {
            dataOutput.writeUTF(this.pickUpTime);
            dataOutput.writeLong(this.dropOffTime);
            dataOutput.writeLong(this.startTime);
            dataOutput.writeInt(this.processorID);
            dataOutput.writeInt(this.seqNo);

            dataOutput.writeInt(this.removedKeys.size());
            for (Object key : this.removedKeys) {
                Cell cell = (Cell) key;
                cell.serialize(dataOutput);
            }
            dataOutput.writeInt(this.newValueList.size());
            for (NodeValue nodeValue : this.newValueList) {
                TopProfitCellNode topProfitCellNode = (TopProfitCellNode) nodeValue;
                topProfitCellNode.serialize(dataOutput);
            }

        } catch (IOException e) {
            throw new MessageProcessingException("Can not write the message ");
        }
    }

    @Override
    public void parse(DataInput dataInput) throws MessageProcessingException {
        try {
            this.pickUpTime = dataInput.readUTF();
            this.dropOffTime = dataInput.readLong();
            this.startTime = dataInput.readLong();
            this.processorID = dataInput.readInt();
            this.seqNo = dataInput.readInt();

            int length = dataInput.readInt();
            this.removedKeys = new HashSet<Object>();
            for (int i = 0; i < length; i++) {
                Cell cell = new Cell();
                cell.parse(dataInput);
                this.removedKeys.add(cell);
            }

            length = dataInput.readInt();
            for (int i = 0; i < length; i++) {
                TopProfitCellNode topProfitCellNode = new TopProfitCellNode();
                topProfitCellNode.parse(dataInput);
                this.newValueList.add(topProfitCellNode);
            }
        } catch (IOException e) {
            throw new MessageProcessingException("Can not read the message ");
        }
    }

}
