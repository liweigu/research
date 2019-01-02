package research.model.recommend;

import java.util.List;

import research.core.data.DataFrame;

public interface RecommendModelI {
	/**
	 * ÑµÁ·
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * ÑµÁ·
	 */
	void fit(DataFrame dataFrame);

	/**
	 * Ô¤²â½á¹û
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * ÆÀ¹À½á¹û
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
