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

	/**
	 * 构造函数
	 * 
	 * @param values 数值
	 */
	public User(List<Double> values) {
		this.values = values;
	}

	/**
	 * 返回数值
	 * 
	 * @return 数值
	 */
	@Override
	public List<Double> doubleValue() {
		return values;
	}

}
