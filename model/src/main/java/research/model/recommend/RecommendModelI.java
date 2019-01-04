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
	 * 
	 * @param features 特征值
	 * @param labels 标签值
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * 训练
	 * 
	 * @param dataFrame 数据框架
	 */
	void fit(DataFrame dataFrame);

	/**
	 * 预测
	 * 
	 * @param features 特征值
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * 结果评估
	 * 
	 * @param features 特征值
	 * @param labels 标签值
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
