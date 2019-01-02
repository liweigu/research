package research.model.recommend;

import java.util.List;

import research.core.data.DataFrame;

/**
 * 推荐模型接口
 * 
 * @author liweigu714@163.com
 *
 */
public interface RecommendModelI {
	/**
	 * 训练
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * 训练
	 */
	void fit(DataFrame dataFrame);

	/**
	 * 预测
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * 结果评估
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
