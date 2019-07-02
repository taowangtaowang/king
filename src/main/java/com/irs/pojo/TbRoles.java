package com.irs.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_roles")
public class TbRoles {
    @Id
    @Column(name = "roleId")
    private Long roleId;
    @Column(name = "roleName")
    private String roleName;
    @Column(name = "roleRemark")
    private String roleRemark;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public String getRoleRemark() {
        return roleRemark;
    }

    public void setRoleRemark(String roleRemark) {
        this.roleRemark = roleRemark == null ? null : roleRemark.trim();
    }

	@Override
	public String toString() {
		return "TbRoles [roleId=" + roleId + ", roleName=" + roleName + ", roleRemark=" + roleRemark + "]";
	}
    
}