package research.core.vo;

import java.util.List;

/**
 * 用户
 * 
 * @author liweigu714@163.com
 *
 */
public class User extends BaseVo {
	private static final long serialVersionUID = -7635233846566806646L;

	private List<Double> values;

	public User(List<Double> values) {
		this.values = values;
	}

	@Override
	public List<Double> doubleValue() {
		return values;
	}

}
