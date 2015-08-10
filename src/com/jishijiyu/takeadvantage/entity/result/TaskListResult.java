package com.jishijiyu.takeadvantage.entity.result;

import java.util.List;

/**
 * 任务列表返回
 * 
 * @author baohan
 * 
 */
public class TaskListResult {
	public int c;
	public Parameter p;

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public Parameter getP() {
		return p;
	}

	public void setP(Parameter p) {
		this.p = p;
	}

	public static class Parameter {
		public List<TaskList> taskList;
		private boolean isTrue;
		public List<ActivityList> activityList;

		public boolean isTrue() {
			return isTrue;
		}

		public void setTrue(boolean isTrue) {
			this.isTrue = isTrue;
		}

		public List<TaskList> getTaskList() {
			return taskList;
		}

		public void setTaskList(List<TaskList> taskList) {
			this.taskList = taskList;
		}

	}

	public static class ActivityList {
		public int completeState;
		public long endTime;
		public int id;
		public long startTime;
		public int taskId;
	}

	public static class TaskList {

		public int awardScore;
		public int id;
		public int lastFinishDay;
		public int state;
		public int targetDoingNum;
		public int targetNum;
		public int taskId;

		public String taskName;
		public int taskTarget;
		public int taskType;
		public int userId;

		public int getAwardScore() {
			return awardScore;
		}

		public void setAwardScore(int awardScore) {
			this.awardScore = awardScore;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getLastFinishDay() {
			return lastFinishDay;
		}

		public void setLastFinishDay(int lastFinishDay) {
			this.lastFinishDay = lastFinishDay;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public int getTargetDoingNum() {
			return targetDoingNum;
		}

		public void setTargetDoingNum(int targetDoingNum) {
			this.targetDoingNum = targetDoingNum;
		}

		public int getTargetNum() {
			return targetNum;
		}

		public void setTargetNum(int targetNum) {
			this.targetNum = targetNum;
		}

		public int getTaskId() {
			return taskId;
		}

		public void setTaskId(int taskId) {
			this.taskId = taskId;
		}

		public String getTaskName() {
			return taskName;
		}

		public void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		public int getTaskTarget() {
			return taskTarget;
		}

		public void setTaskTarget(int taskTarget) {
			this.taskTarget = taskTarget;
		}

		public int getTaskType() {
			return taskType;
		}

		public void setTaskType(int taskType) {
			this.taskType = taskType;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

	}
}
