package com.prgx.workbench.claim.controller;

public enum ObjectId {

	Comment("Comment"),	Claim("Claim"), Client("Client");

	String objectId;

	ObjectId(String objectId){
		this.objectId=objectId;
	}

	public String getObjectId(){
		return objectId;
	}
}
