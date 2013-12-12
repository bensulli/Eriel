package ca.sulli.eriel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ErielLauncher extends Activity {

	/* INST CONTENT OBJECTS */
	public int pageNum;
	public ImageView pageImage;
	public TextView content;
	public Button choice1;
	public Button choice2;
	public Button choice3;
	
	/* INST GAME OBJECTS */
	public TextView cash;
	public int hp; 
		
	/* BOOK TO USE */
	public static String book = "content.xml"; 
	
	/* ARRAY OF PAGES */
	public ArrayList<Page> pages = null;
	
	public static int readLimit = 5000; // Used to kill a loop if it clearly isn't advancing
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /* CUSTOM LAYOUT SETUP */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); //Remove title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Remove notification bar

        setContentView(R.layout.activity_eriel_launcher);


        
        /* LINK LAYOUT OBJECTS */
        pageImage = (ImageView)findViewById(R.id.pageImage);
        content = (TextView)findViewById(R.id.contentTxt);
        choice1 = (Button)findViewById(R.id.choice1Btn);
        choice2 = (Button)findViewById(R.id.choice2Btn);
        choice3 = (Button)findViewById(R.id.choice3Btn);
        
        /* LINK GAME OBJECTS */
        cash = (TextView)findViewById(R.id.cashTxt);
        
        /* READ XML BOOK */
        XmlPullParser parser;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();
		} catch (XmlPullParserException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
			parser = null;
		}
        
        Context context = getApplicationContext();
     
        Log.e("debug", "Starting inputstream");
		
		InputStream in_s;
		
		try {
			in_s = getApplicationContext().getAssets().open(book);
		} catch (IOException e2) {
			in_s = null;
			e2.printStackTrace();
		}

		/* Only used to confirm that the XML was actually readable by the inputstreamer
		try {
			String stringXML = IOUtils.toString(in_s, "UTF-8");
			Log.e(stringXML, stringXML);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		*/
        
        try {
        	parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        	parser.setInput(in_s, null);
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		}
        
        Log.e("debug", "Beginning XML parse");
        
        try {
			parseXML(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // ACTUALLY START THE THING!
        
        

                
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
    	
    		int eventType = parser.getEventType();
    		Page currentPage = null;
    		
    		int i = 0; // Need this to kill the loop if something goes wrong
    		
    	while (eventType != XmlPullParser.END_DOCUMENT && i < readLimit) // End loop at end of file or if readLimit reached (eg, no events found)
    	{
    		String name = null;
    		i++;
    		
    		Log.e("debug", "EventType is " + eventType);
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

    			    			
    			String pageString = "page";
    			
    			if(name.equals(pageString))
    			{
    				currentPage = new Page();
    				//AND THEN PARSE VALUES OF THIS CURRENTPAGE INTO PAGE OBJECT
    			} else if (name != pageString)
    			{
        			//if(name != null)
           			//Log.e(name, name.);
    			}
    			
    			else if (currentPage != null)
    				{
    					if(name == "id")
    					{
    						currentPage.id = Integer.parseInt(parser.nextText());
    					}
    					else if(name == "content")
    					{
    						currentPage.content = parser.nextText();
    					}
    					else if(name == "choice1")
    					{
    						currentPage.choice1 = parser.nextText();
    					}
    					else if(name == "choice2")
    					{
    						currentPage.choice2 = parser.nextText();
    					}
    					else if(name == "choice3")
    					{
    						currentPage.choice3 = parser.nextText();
    					}
    					else if(name == "choice1Result")
    					{
    						currentPage.choice1Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name == "choice2Result")
    					{
    						currentPage.choice2Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name == "choice3Result")
    					{
    						currentPage.choice3Result = Integer.parseInt(parser.nextText());
    					}
    					else if(name == "image")
    					{
    						currentPage.image = parser.nextText();
    					}
    						
    				}
    				break;
    		case XmlPullParser.END_TAG: // If end of file, add current page to list of pages
    			name = parser.getName();
    			if (name.equalsIgnoreCase("page") && currentPage != null)
    				pages.add(currentPage);
    		
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
