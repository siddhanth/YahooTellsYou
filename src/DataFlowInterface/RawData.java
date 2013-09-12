package DataFlowInterface;

/**
 * Raw data class which can include all the data before the data has been
 * processed
 * 
 * @author sjain7
 * 
 */
public class RawData {

	private String url;
	private String question;
	private String answer;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
