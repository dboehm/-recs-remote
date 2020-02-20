package com.example.parser;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class NodeInfo implements Cloneable  {
	 @NotNull
	    @NotEmpty
	private String id;
	 @NotNull
	    @NotEmpty
	private String health;
	 @NotNull
	    @NotEmpty
	private String baseBoardId;
	 @NotNull
	    @NotEmpty
	 private String baseBoardPosition;
	 @NotNull
	    @NotEmpty
	 private String state;

	public NodeInfo() {}
	
	public NodeInfo(String id, String health, String baseBoardId, String baseBoardPosition, String state) {
		this.id = id;
		this.health = health;
		this.baseBoardId = baseBoardId; 
		this.baseBoardPosition = baseBoardPosition;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public String getHealth() {
		return health;
	}

	public String getBaseBoardId() {
		return baseBoardId;
	}

	public String getBaseBoardPosition() {
		return baseBoardPosition;
	}

	public String getState() {
		return state;
	}
	
	

	public void setId(String id) {
		this.id = id;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public void setBaseBoardId(String baseBoardId) {
		this.baseBoardId = baseBoardId;
	}

	public void setBaseBoardPosition(String baseBoardPosition) {
		this.baseBoardPosition = baseBoardPosition;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "NodeInfo [id=" + id + ", health=" + health + ", baseBoardId=" + baseBoardId + ", baseBoardPosition="
				+ baseBoardPosition + ", state=" + state + "]";
	}


	

}
