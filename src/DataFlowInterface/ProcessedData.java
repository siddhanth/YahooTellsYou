package DataFlowInterface;

import java.util.Map;

/**
 * This should include all data which is generated after the processing has been
 * done by zemanta etc..
 * 
 * 
 * The ranker class will make use of an array of processed Objects to decide the
 * final ranking
 * 
 * @author sjain7
 * 
 */
public class ProcessedData extends RawData {

	Map<String, Integer> entities;

	public Map<String, Integer> getEntities() {
		return entities;
	}

	public void setEntities(Map<String, Integer> entities) {
		this.entities = entities;
	}

}
