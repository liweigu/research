package research.model.recommend;

import java.util.HashMap;
import java.util.Map;

/**
 * 推荐模型
 * 
 * @author liweigu714@163.com
 *
 */
public abstract class RecommendModel implements RecommendModelI {
	static Object Model;
	static Map<String, Object> InitProps = new HashMap<String, Object>();

	/**
	 * 初始化模型
	 * 
	 * @param initProps 初始化参数
	 */
	public void initModel(Map<String, Object> initProps) {
		throw new IllegalStateException("需调用RecommendModel子类的方法。");
	}

	/**
	 * 返回模型
	 * 
	 * @return
	 */
	public Object getModel() {
		if (Model == null) {
			throw new IllegalStateException("模型未初始化");
		}
		return Model;
	}

	/**
	 * 返回初始化参数
	 * 
	 * @return 初始化参数
	 */
	public Map<String, Object> getInitProps() {
		return InitProps;
	}

	/**
	 * 保存模型
	 * 
	 * @param path 模型路径
	 */
	public void save(String path) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 加载模型
	 * 
	 * @param path 模型路径
	 */
	public void restore(String path) {
		throw new UnsupportedOperationException();
	}
}
