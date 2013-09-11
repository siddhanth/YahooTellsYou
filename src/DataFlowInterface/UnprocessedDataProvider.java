package DataFlowInterface;

import java.util.ArrayList;

/**
 * Should be implemented by any of the data providers.. Yahoo/Google/Bing/Quora
 * 
 * @author sjain7
 * 
 */
public interface UnprocessedDataProvider {

	ArrayList<RawData> getRawData(String query);

}
