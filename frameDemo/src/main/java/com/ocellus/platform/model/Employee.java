package com.ocellus.platform.model;

/**
 * 人员信息维护：类（对应表：TB_EMPLOYEE）
 *
 */
public class Employee extends AbstractModel{

	private static final long serialVersionUID = 1L;

	private String employeeId;	//主键
	private String employeeBh;	//人员编号
	private String employeeMc;	//人员名称
	private String employeeZzbm;//组织部门编码
	private String employeeZzbmMc;//所属单位名称
	private String employeeZzbmId;//所属单位ID
	private String employeeZw;	//职位
	private String employeeXb;	//性别
	private String employeeSjh;	//手机号
	private String employeeBz;	//备注

	@Override
	public void setDBId(String id) {
		this.setEmployeeId(id);
	}
	
	@Override
	public String getDBId() {
		
		return getEmployeeId();
	}


	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeBh() {
		return employeeBh;
	}

	public void setEmployeeBh(String employeeBh) {
		this.employeeBh = employeeBh;
	}

	public String getEmployeeMc() {
		return employeeMc;
	}

	public void setEmployeeMc(String employeeMc) {
		this.employeeMc = employeeMc;
	}

	public String getEmployeeZzbm() {
		return employeeZzbm;
	}

	public void setEmployeeZzbm(String employeeZzbm) {
		this.employeeZzbm = employeeZzbm;
	}

	public String getEmployeeZw() {
		return employeeZw;
	}

	public void setEmployeeZw(String employeeZw) {
		this.employeeZw = employeeZw;
	}

	public String getEmployeeXb() {
		return employeeXb;
	}
	public void setEmployeeXb(String employeeXb) {
		this.employeeXb = employeeXb;
	}
	public String getEmployeeSjh() {
		return employeeSjh;
	}
	public void setEmployeeSjh(String employeeSjh) {
		this.employeeSjh = employeeSjh;
	}
	public String getEmployeeBz() {
		return employeeBz;
	}
	public void setEmployeeBz(String employeeBz) {
		this.employeeBz = employeeBz;
	}

	public String getEmployeeZzbmMc() {
		return employeeZzbmMc;
	}

	public void setEmployeeZzbmMc(String employeeZzbmMc) {
		this.employeeZzbmMc = employeeZzbmMc;
	}

	public String getEmployeeZzbmId() {
		return employeeZzbmId;
	}

	public void setEmployeeZzbmId(String employeeZzbmId) {
		this.employeeZzbmId = employeeZzbmId;
	}
}
