package cn.zz.logistics.pojo;

public class Permission {
    private Long permissionId;

    private String name;

    private String type;

    private String url;

    private String expression;

    private Long parentId;

    private Integer sort;
     private String parentName;
	public Long getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Permission() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Permission(Long permissionId, String name, String type, String url, String expression, Long parentId,
			Integer sort, String parentName) {
		super();
		this.permissionId = permissionId;
		this.name = name;
		this.type = type;
		this.url = url;
		this.expression = expression;
		this.parentId = parentId;
		this.sort = sort;
		this.parentName = parentName;
	}
	@Override
	public String toString() {
		return "Permission [permissionId=" + permissionId + ", name=" + name + ", type=" + type + ", url=" + url
				+ ", expression=" + expression + ", parentId=" + parentId + ", sort=" + sort + ", parentName="
				+ parentName + "]";
	}
     
  
}