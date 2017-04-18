package cn.xglory.service.common.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据操作工具方法类
 * @author Bob
 */
@Repository
public class JdbcDao extends BaseJdbcDao {
	
	public <T> RowMapper<T> getGenericRowMapper(Class<T> clazz){ 
		return BeanPropertyRowMapper.newInstance(clazz);
	}
	/**
	 * 是否含有非法参数
	 * true 含有非法参数
	 * false 不含有非法参数
	 * @param params
	 * @return
	 */
	public boolean paramsUnSafe(String... params){
		for(String param : params){
			if(!CheckParms(param)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否含有非法参数
	 * @param parms
	 * @return
	 */
	protected boolean CheckParms(String parms){
        String[] word = new String[]{"exec","insert","select","delete","update","master","truncate","declare","join","drop"};
        if (StringUtils.isBlank(parms))
            return true;
        for (String i : word){
            if (parms.toLowerCase().contains(i)){
                return false;
            }
        }
        return true;
    }

    /**
     * SQL替换'特殊字符
     * @param iParam
     * @return
     */
	protected String SqlParamReplace(String iParam){
        return iParam.replace("'", "''");
    }

    /**
     * 模糊查询参数替换
     * @param iParam
     * @return
     */
	protected String SqlLikeReplace(String iParam){
        String rtn = "";
        for (int i = 0; i < iParam.length(); i++){
        	char[] param = iParam.toCharArray();
            if (param[i] == '[' || param[i] == ']')
                rtn = rtn + "[" + param[i] + "]";
            else
                rtn = rtn + param[i];
        }
        rtn = rtn.replace("'", "''").replace("%", "[%]").replace("_", "[_]");
        return " '%" + rtn + "%' ";
    }

    /**
     * 精确查询参数替换
     * @param iParam
     * @return
     */
	protected String SqlLikeReplaceForParam(String iParam){
        String rtn = "";
        for (int i = 0; i < iParam.length(); i++){
        	char[] param = iParam.toCharArray();
            if (param[i] == '[' || param[i] == ']')
                rtn = rtn + "[" + param[i] + "]";
            else
                rtn = rtn + param[i];
        }
        rtn = rtn.replace("'", "''").replace("%", "[%]").replace("_", "[_]");
        return rtn;
    }
	
	/**
	 * 分页参数对象
	 * @author Bob
	 *
	 */
	public static class PageParam{
		private int pageIndex;
		private int pageSize;
		private int limitStart;
		
		public int getPageIndex() {
			return pageIndex;
		}
		public void setPageIndex(int pageIndex) {
			this.pageIndex = pageIndex;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public int getLimitStart() {
			return limitStart;
		}
		public void setLimitStart(int limitStart) {
			this.limitStart = limitStart;
		}
	}
	
	/**
	 * 获取处理过的分页参数
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	protected PageParam getPageParam(int pageIndex, int pageSize){
		int limitStart = 0;

		if(pageIndex <= 0)
			pageIndex = 1;
		
        if(pageSize <=0)
        	pageSize = 20;
        
        if((pageIndex-1)*pageSize > Integer.MAX_VALUE){
        	limitStart = Integer.MAX_VALUE;
        }else{
        	limitStart = (pageIndex-1)*pageSize;
        }
        PageParam pp = new PageParam();
        pp.setPageIndex(pageIndex);
        pp.setPageSize(pageSize);
        pp.setLimitStart(limitStart);
        
        return pp;
	}
	
	/**
	 * 获取当前数据库会话最近插入的自增长id值
	 * @return
	 * @throws Exception 
	 */
	public Integer getLastInsertID() throws Exception{
		return queryValue_W("SELECT LAST_INSERT_ID();", Integer.class);
	}
}