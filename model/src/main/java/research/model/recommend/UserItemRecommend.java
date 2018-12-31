package research.model.recommend;

import java.util.List;

public class UserItemRecommend extends RecommendModel {

	@Override
	public void fit(List<List<Double>> features, List<List<Double>> labels) {

	}

	@Override
	public List<List<Double>> output(List<List<Double>> features) {
		return features;
	}

	@Override
	public void evaluate(List<List<Double>> features, List<List<Double>> labels) {

	}

}
