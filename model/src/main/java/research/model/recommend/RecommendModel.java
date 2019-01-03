package research.model.recommend;

import java.util.HashMap;
import java.util.Map;

import org.deeplearning4j.nn.graph.ComputationGraph;

/**
 * 推荐模型
 * 
 * @author liweigu714@163.com
 *
 */
public abstract class RecommendModel implements RecommendModelI {
	static ComputationGraph ComputationGraph;
	static Map<String, Object> InitProps = new HashMap<String, Object>();

	public Object getModel() {
		if (ComputationGraph == null) {
			throw new IllegalStateException("模型未初始化");
		}
		return ComputationGraph;
	}

	public Map<String, Object> getInitProps() {
		return InitProps;
	}

	public void save(String path) {
		throw new UnsupportedOperationException();
	}

	public void restore(String path) {
		throw new UnsupportedOperationException();
	}
}
