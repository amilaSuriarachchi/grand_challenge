package edu.colostate.cs.gc.profit;

import edu.colostate.cs.gc.process.EventWriter;
import edu.colostate.cs.gc.process.TopEventProcessor;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 3/9/15
 * Time: 4:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopProfitProcessor extends TopEventProcessor {

    public TopProfitProcessor() {
        super(new ProfitEventWriter("top_profit_cells.txt"));
    }

    public TopProfitProcessor(int numbOfProcessors) {
        super(numbOfProcessors, new ProfitEventWriter("top_profit_cells.txt"));
    }
}
