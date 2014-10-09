package org.takinframework.core.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 简单显示3分页实现
 */
@SuppressWarnings("serial")
public class PageUtil<T> implements Serializable {

	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(ResourceUtil.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）
	
	private long count;// 总记录数，设置为“-1”表示不查询总数
	
	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引
	
	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 3;// 显示页面长度
	private int slider = 1;// 前后显示页面长度
	
	private List<T> list = new ArrayList<T>();
	
	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc
	
	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	
	private String message = ""; // 设置提示消息，显示在“共n条”之后
	
	public PageUtil() {
		this.pageSize = -1;
	}
	//modiy by twg
	public PageUtil(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public PageUtil(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}
	
	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param pageSize 分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public PageUtil(HttpServletRequest request, HttpServletResponse response, int pageSize){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		if (StringUtil.isNumeric(no)){
			CookieUtils.setCookie(response, "pageNo", no);
			this.setPageNo(Integer.parseInt(no));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtil.isNumeric(no)){
				this.setPageNo(Integer.parseInt(no));
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		if (StringUtil.isNumeric(size)){
			CookieUtils.setCookie(response, "pageSize", size);
			this.setPageSize(Integer.parseInt(size));
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageSize");
			if (StringUtil.isNumeric(size)){
				this.setPageSize(Integer.parseInt(size));
			}
		}
		if (pageSize != -2){
			this.pageSize = pageSize;
		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtil.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public PageUtil(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public PageUtil(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}
	
	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public PageUtil(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.setList(list);
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		
		this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
		
		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage=true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}
		
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {

		initialize();
		
		StringBuilder sb = new StringBuilder();
		
		if (pageNo == first) {// 如果是首页
			sb.append("<a href=\"javascript:\">&#171;</a>\n");
//			sb.append("<input type=\"button\" value=\"上一页\" class=\"disabled\" />\n");
		} else {
//			sb.append("<input type=\"button\" value=\"上一页\" onclick=\"javascript:"+funcName+"("+prev+","+pageSize+");\" />\n");
			sb.append("<a href=\"javascript:"+funcName+"("+prev+","+pageSize+");\">&#171;</a>\n");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
//				sb.append("<input type=\"button\" class=\"active\"  value="+ (i + 1 - first)+" />\n");
				sb.append("<a href=\"javascript:\" class=\"cur\">" + (i + 1 - first)
						+ "</a>\n");
			} else {
//				sb.append("<input type=\"button\" value="+(i + 1 - first)+" onclick=\"javascript:"+funcName+"("+i+","+pageSize+");\" />\n");
				sb.append("<a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a>\n");
			}
		}


		if (pageNo == last) {
//			sb.append("<input type=\"button\"  value=\"下一页 &#187;\" class=\"disabled\" />\n");
			sb.append("<a href=\"javascript:\">&#187;</a>\n");
		} else {
//			sb.append("<input type=\"button\" value=\"下一页 &#187;\" onclick=\"javascript:"+funcName+"("+next+","+pageSize+");\" />\n");
			sb.append("<a href=\"javascript:"+funcName+"("+next+","+pageSize+");\">"
					+ "&#187;</a>\n");
		}

		//js 分页
		sb.append("<script type=\"text/javascript\">function page(n,s){$(\"#pageNo\").val(n);$(\"#pageSize\").val(s);$(\"#searchForm\").submit();return false;}</script>");
		return sb.toString();
	}
	
//	public static void main(String[] args) {
//		Page<String> p = new Page<String>(3, 3);
//		System.out.println(p);
//		System.out.println("首页："+p.getFirst());
//		System.out.println("尾页："+p.getLast());
//		System.out.println("上页："+p.getPrev());
//		System.out.println("下页："+p.getNext());
//	}

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize > 500 ? 500 : pageSize;
	}

	/**
	 * 首页索引
	 * @return
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	public int getLast() {
		return last;
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 获取查询排序字符串
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	
	public int getLastResult(){
		int lastResult = getFirstResult()+getMaxResults();
		if(lastResult>getCount()) {
			lastResult =(int) getCount();
		}
		return lastResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}

	
}
