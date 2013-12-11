package ca.sulli.eriel;

public class Page {

	public int id;
	public String content;
	public String choice1;
	public String choice2;
	public String choice3;
	public int choice1Result;
	public int choice2Result;
	public int choice3Result;
	public String image;	
	
	public Page()
	{
		
	}
	
	// GETTERS AND SETTERS
	public void SetID(int id)
	{
		//TODO: Ensure this is a number
		this.id = id;
	}
	
	public int GetID()
	{
		return this.id;
	}
	
	public void SetContent(String content)
	{
		this.content = content;
	}
	
	public String GetContent()
	{
		return this.content;
	}
	
	public void SetChoice1(String choice1)
	{
		this.choice1 = choice1;
	}
	
	public String GetChoice1()
	{
		return this.choice1;
	}
	
	public void SetChoice2(String choice2)
	{
		this.choice2 = choice2;
	}
	
	public String GetChoice2()
	{
		return this.choice2;
	}
	
	public void SetChoice3(String choice3)
	{
		this.choice3 = choice3;
	}
	
	public String GetChoice3()
	{
		return this.choice3;
	}

	public void SetChoice1Result(int choice1Result)
	{
		this.choice1Result = choice1Result;
	}
	
	public int GetChoice1Result()
	{
		return this.choice1Result;
	}
	
	public void SetChoice2Result(int choice2Result)
	{
		this.choice2Result = choice2Result;
	}
	
	public int GetChoice2Result()
	{
		return this.choice2Result;
	}
	
	public void SetChoice3Result(int choice3Result)
	{
		this.choice3Result = choice3Result;
	}
	
	public int GetChoice3Result()
	{
		return this.choice3Result;
	}

	public void SetImage(String image)
	{
		this.image = image;
	}
	
	public String GetImage()
	{
		return this.image;
	}




}