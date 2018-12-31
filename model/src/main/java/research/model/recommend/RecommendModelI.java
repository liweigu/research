package research.model.recommend;

import java.util.List;

public interface RecommendModelI {
	/**
	 * 训练
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * 预测结果
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * 评估结果
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
