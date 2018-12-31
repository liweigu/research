package research.model.recommend;

import java.util.List;

public interface RecommendModelI {
	/**
	 * ѵ��
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * Ԥ����
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * �������
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
