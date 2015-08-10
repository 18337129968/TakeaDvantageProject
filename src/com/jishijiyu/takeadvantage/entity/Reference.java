package com.jishijiyu.takeadvantage.entity;

public class Reference {
private String directReference;//直接推荐人
private String inderectReference;//间接推荐人
private int points;//拍币
public String getDirectReference() {
	return directReference;
}
public void setDirectReference(String directReference) {
	this.directReference = directReference;
}
public String getInderectReference() {
	return inderectReference;
}
public void setInderectReference(String inderectReference) {
	this.inderectReference = inderectReference;
}
public int getPoints() {
	return points;
}
public void setPoints(int points) {
	this.points = points;
}

}
