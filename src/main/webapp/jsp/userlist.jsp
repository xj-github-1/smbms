<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/common/head.jsp"%>
        <div class="right">
            <div class="location">
                <strong>你现在所在的位置是:</strong>
                <span>用户管理页面</span>
            </div>
            <div class="search">
           		<form method="get" action="${pageContext.request.contextPath }/userlist">
					<input name="method" value="query" class="input-text" type="hidden">
					 <span>用户名：</span>										 <%--数据回显--%>
					 <input name="queryname" class="input-text"	type="text" value="${queryUserName }">
					 
					 <span>用户角色：</span>
					 <select name="queryUserRole">
						<c:if test="${roleList != null }">
						   <option value="0">--请选择--</option>
							<%--返回的就是List集合，不包含其它值,不用再加上list--%>
						   <c:forEach var="role" items="${roleList}">
						   		<option <c:if test="${role.id == queryUserRole }">selected="selected"</c:if>
						   		value="${role.id}">${role.rolename}</option>
						   </c:forEach>
						</c:if>
	        		</select>
					 
					 <input type="hidden" name="pageIndex" value="1"/>
					 <input	value="查 询" type="submit" id="searchbutton">
					 <a href="${pageContext.request.contextPath}/jsp/useradd.jsp" >添加用户</a>
				</form>
            </div>
            <!--用户-->
            <table class="providerTable" cellpadding="0" cellspacing="0">
                <tr class="firstTr">
                    <th width="10%">用户编码</th>
                    <th width="20%">用户名称</th>
                    <th width="10%">性别</th>
                    <th width="10%">年龄</th>
                    <th width="10%">电话</th>
                    <th width="10%">用户角色</th>
                    <th width="30%">操作</th>
                </tr>
							<%--由于返回的是PageInfo，里面还包含其它的值，所以userList后要加上list--%>
                   <c:forEach var="user" items="${userList.list }" varStatus="status">
					<tr>
						<td>
						<span>${user.usercode }</span>
						</td>
						<td>
						<span>${user.username }</span>
						</td>
						<td>
							<span>
								<c:if test="${user.gender==1}">男</c:if>
								<c:if test="${user.gender==2}">女</c:if>
							</span>
						</td>
						<td>
						<span>${user.age}</span>
						</td>
						<td>
						<span>${user.phone}</span>
						</td>
						<td>
							<span>${user.rolename}</span>
						</td>
						<td>
						<span><a class="viewUser" href="javascript:;" userid=${user.id } username=${user.username }><img src="${pageContext.request.contextPath }/images/read.png" alt="查看" title="查看"/></a></span>
						<span><a class="modifyUser" href="javascript:;" userid=${user.id } username=${user.username }><img src="${pageContext.request.contextPath }/images/xiugai.png" alt="修改" title="修改"/></a></span>
						<span><a class="deleteUser" href="javascript:;" userid=${user.id } username=${user.username }><img src="${pageContext.request.contextPath }/images/schu.png" alt="删除" title="删除"/></a></span>
						</td>
					</tr>
				</c:forEach>
			</table>
			<input type="hidden" id="totalPageCount" value="${userList.pages}"/>
		  	<c:import url="rollpage.jsp">
	          	<c:param name="totalCount" value="${userList.total}"/>
	          	<c:param name="currentPageNo" value="${userList.pageNum}"/>
	          	<c:param name="totalPageCount" value="${userList.pages}"/>
          	</c:import>
        </div>
    </section>

<!--点击删除按钮后弹出的页面-->
<div class="zhezhao"></div>
<div class="remove" id="removeUse">
    <div class="removerChid">
        <h2>提示</h2>
        <div class="removeMain">
            <p>你确定要删除该用户吗？</p>
            <a href="#" id="yes">确定</a>
            <a href="#" id="no">取消</a>
        </div>
    </div>
</div>

<%@include file="/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/userlist.js"></script>
