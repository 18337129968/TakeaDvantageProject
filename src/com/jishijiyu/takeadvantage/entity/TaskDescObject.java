package com.jishijiyu.takeadvantage.entity;

/**
 * 任务描述的对象，在详情页中使用到
 * 
 * @author youmi
 * 
 */
public class TaskDescObject {

	private String desc;

	/**
	 * 任务状态这里为了方便 直接使用了AdExtraTaskStatus的变量值
	 * <ul>
	 * <li>{@code AdExtraTaskStatus.NOT_START} ： 任务还没有开始</li>
	 * <li>{@code AdExtraTaskStatus.IN_PROGRESS} ： 任务可以进行</li>
	 * <li>{@code AdExtraTaskStatus.COMPLETE} ：任务已完成</li>
	 * <li>{@code AdExtraTaskStatus.OUT_OF_DATE} ： 任务已过时</li>
	 * </ul>
	 */
	private int status;

	private int points;

	public TaskDescObject(int status, String desc, int points) {
		super();

		this.desc = desc;
		this.status = status;
		this.points = points;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 任务状态这里为了方便 直接使用了AdExtraTaskStatus的变量值
	 * <ul>
	 * <li>{@code AdExtraTaskStatus.NOT_START} ： 任务还没有开始</li>
	 * <li>{@code AdExtraTaskStatus.IN_PROGRESS} ： 任务可以进行</li>
	 * <li>{@code AdExtraTaskStatus.COMPLETE} ：任务已完成</li>
	 * <li>{@code AdExtraTaskStatus.OUT_OF_DATE} ： 任务已过时</li>
	 * </ul>
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 任务状态这里为了方便 直接使用了AdExtraTaskStatus的变量值
	 * <ul>
	 * <li>{@code AdExtraTaskStatus.NOT_START} ： 任务还没有开始</li>
	 * <li>{@code AdExtraTaskStatus.IN_PROGRESS} ： 任务可以进行</li>
	 * <li>{@code AdExtraTaskStatus.COMPLETE} ：任务已完成</li>
	 * <li>{@code AdExtraTaskStatus.OUT_OF_DATE} ： 任务已过时</li>
	 * </ul>
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	@Override
	public String toString() {
		return "TaskDescObject [desc=" + desc + ", status=" + status + ", points=" + points + "]";
	}

}
