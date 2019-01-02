package research.model.recommend;

import java.util.List;

import research.core.data.DataFrame;

/**
 * �Ƽ�ģ�ͽӿ�
 * 
 * @author liweigu714@163.com
 *
 */
public interface RecommendModelI {
	/**
	 * ѵ��
	 */
	void fit(List<List<Double>> features, List<List<Double>> labels);

	/**
	 * ѵ��
	 */
	void fit(DataFrame dataFrame);

	/**
	 * Ԥ����
	 */
	List<List<Double>> output(List<List<Double>> features);

	/**
	 * �������
	 */
	void evaluate(List<List<Double>> features, List<List<Double>> labels);
}
