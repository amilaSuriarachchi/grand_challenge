package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.list.NodeValue;
import edu.colostate.cs.gc.process.EventWriter;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 2/28/15
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfitEventWriter extends EventWriter {

    public ProfitEventWriter(String fileName) {
        super(fileName);
    }

    @Override
    public void writeLine(long startTime, String pickUpTime, long dropOffTime, List<NodeValue> nodeValues) {
        long delay = System.currentTimeMillis() - startTime;
        try {
            this.eventWriter.write(pickUpTime + ",");
            this.eventWriter.write(this.simpleDateFormat.format(new Date(dropOffTime)) + ",");
            for (NodeValue nodeValue : nodeValues) {
                TopProfitCellNode profitCellNode = (TopProfitCellNode) nodeValue;
                this.eventWriter.write(profitCellNode.getCell().toString() + ",");
                this.eventWriter.write(profitCellNode.getNumOfEmptyTaxis() + ",");
                this.eventWriter.write(profitCellNode.getMidFare() + ",");
                this.eventWriter.write(profitCellNode.getProfitability() + ",");
            }
            this.eventWriter.write(delay + "");
            this.eventWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
