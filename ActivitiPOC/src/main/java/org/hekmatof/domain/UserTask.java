/**
 * 
 */
package org.hekmatof.domain;

import java.util.Date;

import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * @author esa
 *
 */
public class UserTask {

	public UserTask(TaskEntity task) {
		this.id = task.getId();
		this.assignee = task.getAssignee();
		this.owner = task.getOwner();
		this.priority = task.getPriority();
		this.formKey = task.getFormKey();
		this.dueDate = task.getDueDate();
		this.category = task.getCategory();
	}

	protected String id;
	protected String assignee;
	protected String owner;
	protected int priority;
	protected String formKey;
	protected Date dueDate;
	protected String category;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}