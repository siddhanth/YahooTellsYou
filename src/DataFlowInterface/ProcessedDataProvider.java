package DataFlowInterface;

import java.util.ArrayList;

public interface ProcessedDataProvider {

	ArrayList<ProcessedData> getProcessedData(ArrayList<RawData> arr);

}
