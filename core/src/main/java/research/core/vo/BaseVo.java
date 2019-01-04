package research.core.vo;

import research.core.data.DataFrame;

/**
 * VO基础类
 * 
 * @author liweigu714@163.com
 *
 */
public abstract class BaseVo implements BaseVoI {
	private static final long serialVersionUID = -3659395323547619813L;

	// TODO: BaseVo的子类各自实现此方法
	public DataFrame dataFrame() {
		return null;
	}
}
