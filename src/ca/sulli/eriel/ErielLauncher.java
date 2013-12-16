package ca.sulli.eriel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import ca.sulli.eriel.R.color;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ErielLauncher extends Activity {

	/* FOR DEBUGGING */
	public static final boolean DEV_MODE = true; // Determines whether debugging info is shown in app. Disable before release :)
	
	/* INST CONTENT OBJECTS */
	public int pageNum;
	public ImageView pageImage;
	public TextView content;
	public Button choice1;
	public Button choice2;
	public Button choice3;
	public ProgressBar hpBar;
	public TextView errorText;
	
	/* INST GAME OBJECTS */
	public TextView cashText;
	public int hp; 
	public int cash;
	public boolean alive = true;
	public static int startingHealth = 100;
	public static int startingCash = 15;
	
	/* XML ERROR LOG */
	public String errorLog = "";
	
	/* BOOK TO USE */
	public static String book = "content.xml";
	
	/* ARRAY OF PAGES */
	public ArrayList<Page> pages = null;
	public Page onPage;
	
	public static int readLimit = 5000; // Used to kill a loop if it clearly isn't advancing
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* CUSTOM LAYOUT SETUP */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_eriel_launcher);
        
        Initialize();
        
        
        // ACTUALLY START THE THING!
        
        
        // SET ONPAGE   
        for(int x = 0; x < pages.size(); x++)
        {
        	Log.e(null,"Page ID: " + pages.get(x).id);
        	if(pages.get(x).id == 1)
        	{
        		onPage = pages.get(x);
        		UpdatePage(onPage);
        		break;
        	}
        }
        
    }

    private void UpdatePage(Page onPage) { 
		hp = hp + onPage.hp; // This is "+" because damage is expressed as a negative number in the XML
		if (hp > 0)
		{
			hpBar.setProgress(hp);
			cash = cash + onPage.cash;
			cashText.setText(Integer.toString(cash));
			
	    	Log.e(null,"Updating layout...");
	    	String contentString = onPage.content;
	    	
	    	content.setText(contentString);

	    	if (CheckRequirements(onPage.choice1Result) == false) {
	    		choice1.setTextColor(color.disabled_text);
	    		choice1.setClickable(false);
	    	}
	    	
	    	choice1.setText(onPage.choice1);
			
	    	if (CheckRequirements(onPage.choice2Result) == false) {
    			choice2.setTextColor(color.disabled_text);
    			choice2.setClickable(false);
	    	}
	    				
	    	choice2.setText(onPage.choice2);
				
			choice2.setVisibility(View.VISIBLE);
			
			if (onPage.choice3 == null)
			{
				choice3.setVisibility(View.GONE);
			}
			else
			{	
				choice3.setVisibility(View.VISIBLE);
				
				if (CheckRequirements(onPage.choice3Result) == false) {
	    			choice3.setTextColor(color.disabled_text);
	    			choice3.setClickable(false);
				}
		    				
				choice3.setText(onPage.choice3);
			}
			
			Log.e(null,"Image name is: " + onPage.image);
			
			String newImage = onPage.image;
			int resID = getResources().getIdentifier(newImage, "drawable", getPackageName());
			pageImage.setImageResource(resID);
			
			
			
		}
		else if (hp <= 0)
		{
			Die();
		}
		
	}
    
    private boolean CheckRequirements(int choice) {
		
    	int requiredCash = 0;
    	
    	try
    	{
    		Page destPage = pages.get(choice - 1);
        	requiredCash = destPage.cash; 
    	}
    	catch(Exception e)
    	{
    		UpdateErrors("ERROR: Choice destination \'" + choice + "\' doesn't exist in XML!");
    		return false;
    	}
    	

    	
    	if (requiredCash < 0)
    	{
    		requiredCash = requiredCash * -1; // Inverted since negative numbers are used in the XML for cost
    	}
    	else
    		requiredCash = 0;
    	
    	if (requiredCash >= cash)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
	}

	public void Die()
    {
    	hpBar.setProgress(0);
    	cashText.setText("0");
    	String newImage = "epitaph";
		int resID = getResources().getIdentifier(newImage, "drawable", getPackageName());
		pageImage.setImageResource(resID);
		
		alive = false;
		
    	content.setText(onPage.deathMessage);
		
		choice1.setText("Start over!");		
		choice2.setVisibility(View.GONE);
		choice3.setVisibility(View.GONE);
		
		
    }

    public void Choose(View v)
    {	
    	
    	switch(v.getId()) {
    	case (R.id.choice1Btn):
    		
    		if(alive == true)
    		{
    			onPage = pages.get(onPage.choice1Result - 1);
    			UpdatePage(onPage);
    		}
    		else
    		{
    			alive = true;
    			hp = startingHealth;
    			onPage = pages.get(0);
    			UpdatePage(onPage);
    		}
    		break;
    	case (R.id.choice2Btn):
    		onPage = pages.get(onPage.choice2Result - 1);
    		UpdatePage(onPage);	
    		break;
    	case (R.id.choice3Btn):	
    		onPage = pages.get(onPage.choice3Result - 1);
    		UpdatePage(onPage);
    		break;
    	}
    }
    
	private void Initialize() {
        /* LINK LAYOUT OBJECTS */
        pageImage = (ImageView)findViewById(R.id.pageImage);
        content = (TextView)findViewById(R.id.contentTxt);
        choice1 = (Button)findViewById(R.id.choice1Btn);
        choice2 = (Button)findViewById(R.id.choice2Btn);
        choice3 = (Button)findViewById(R.id.choice3Btn);
        hpBar = (ProgressBar)findViewById(R.id.healthBar);
        errorText = (TextView)findViewById(R.id.errorText);
        errorText.setTextColor(color.error);
        
        /* LINK GAME OBJECTS */
        cashText = (TextView)findViewById(R.id.cashTxt);
        hp = startingHealth;
        cash = startingCash;
        cashText.setText(Integer.toString(cash));
        hpBar.setProgress(hp);
        hpBar.bringToFront();
        
        /* READ XML BOOK */
        XmlPullParser parser;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
		} catch (XmlPullParserException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			parser = null;
		}
		
		InputStream in_s;
		
		try {
			in_s = getApplicationContext().getAssets().open(book);
		} catch (IOException e2) {
			in_s = null;
			e2.printStackTrace();
		}
        
        try {
        	parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        	parser.setInput(in_s, null);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
        
 
        try {
			ParseXML(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void UpdateErrors(String newError)
	{
		errorLog = errorLog.concat("\n" + newError);
		if(DEV_MODE)
		{
			errorText.setText(errorLog);
		}
		
	}
	
	private void ParseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
    	
    		int eventType = parser.getEventType();
    		Page currentPage = null;
    		
    		int i = 0; // Need this to kill the loop if something goes wrong
    		
    	while (eventType != XmlPullParser.END_DOCUMENT && i < readLimit) // End loop at end of file or if readLimit reached (eg, no events found)
    	{
    		String name = null;
    		i++;
    		
    		//Log.e(null, "EventType is " + eventType);
    		
    		switch (eventType)
    		{
    		case XmlPullParser.START_DOCUMENT:
    			pages = new ArrayList();
    		case XmlPullParser.START_TAG:
    			name = parser.getName();
    			while(name == null)
    			{
    				parser.next();
    				name = parser.getName();
    			}

    			    			
 			
    			if(name.equals("page"))
    			{
    				Log.e(null,"Found a new page!");
    				currentPage = new Page();
    				//AND THEN PARSE VALUES OF THIS CURRENTPAGE INTO PAGE OBJECT
    			} 
    			
    			else if (currentPage != null)
    				{
    					Log.e(null, "Found something other than a page: " + name);
    					if(name.equals("id"))
    					{
    						currentPage.id = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("content"))
    					{
    						currentPage.content = parser.nextText();
    					}
    					else if(name.equals("choice1"))
    					{
    						currentPage.choice1 = parser.nextText();
    					}
    					else if(name.equals("choice2"))
    					{
    						currentPage.choice2 = parser.nextText();
    					}
    					else if(name.equals("choice3"))
    					{
    						currentPage.choice3 = parser.nextText();
    					}
    					else if(name.equals("choice1Result"))
    					{
    						currentPage.choice1Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("choice2Result"))
    					{
    						currentPage.choice2Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("choice3Result"))
    					{
    						currentPage.choice3Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("image"))
    					{
    						currentPage.image = parser.nextText();
    					}
    					else if(name.equals("hp"))
    					{
    						currentPage.hp = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("cash"))
    					{
    						currentPage.cash = Integer.parseInt(parser.nextText());
    					}
    					else if(name.equals("deathMessage"))
    					{
    						currentPage.deathMessage = parser.nextText();
    					}
    						
    				}
    				break;
    		case XmlPullParser.END_TAG: // If end of file, add current page to list of pages
    			name = parser.getName();
    			if (name.equalsIgnoreCase("page") && currentPage != null)
    			{
    				pages.add(currentPage);
    				Log.e(null,"Page " + pages.size() + " completed, adding to array.");
    			}
    			}
    		eventType = parser.next();
    		}
    		
    	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eriel_launcher, menu);
        return true;
    }
    
}
