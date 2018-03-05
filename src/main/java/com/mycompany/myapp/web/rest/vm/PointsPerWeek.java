package com.mycompany.myapp.web.rest.vm;

import java.time.LocalDate;

public class PointsPerWeek {
	private LocalDate week;
	private Integer points;
	private Integer pointsReceived;
	public PointsPerWeek(LocalDate week, Integer points){
		this.week = week;
		this.points = points;
	}
	public LocalDate getWeek() {
		return week;
	}
	public void setWeek(LocalDate week) {
		this.week = week;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	@Override
	public String toString() {
		return "PointsPerWeek [week=" + week + ", points=" + points + "]";
	}
	
	
	
}
