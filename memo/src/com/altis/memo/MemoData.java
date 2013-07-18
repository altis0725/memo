package com.altis.memo;

public class MemoData {
	  protected int id;
	  protected String title;
	  protected String body;
	  protected String lastupdate;
	  
	  public MemoData(int id, String title, String body, String lastupdate){
		  this.id = id;
		  this.title = title;
		  this.body = body;
		  this.lastupdate = lastupdate;
	  }
	  
	  public String getTitle(){
		  return title;
	  }
	  
	  public String getBody(){
		  return body;
	  }
	  
	  public String getLastupdate(){
		  return lastupdate;
	  }
	  
	  public int getId(){
		  return id;
	  }

}
