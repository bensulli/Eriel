package ca.sulli.eriel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Xml;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;

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
        XmlPullParser parser = Xml.newPullParser();
        
        InputStream in_s;
		try {
			
		in_s = getApplicationContext().getAssets().open(book);
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in_s, null);
		parseXML(parser);
		
		} catch (Exception e1) { // Don't judge me :p
			e1.printStackTrace();
		}

                
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eriel_launcher, menu);
        return true;
    }
    
    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
    	ArrayList<Page> pages = null;
    		int eventType = parser.getEventType();
    		Page currentPage = null;
    		
    	while (eventType != XmlPullParser.END_DOCUMENT)
    	{
    		String name = null;
    		
    		switch (eventType)
    		{
    		case XmlPullParser.START_DOCUMENT:
    			pages = new ArrayList();
    		case XmlPullParser.START_TAG:
    			name = parser.getName();
    			if (name == "page")
    			{
    				currentPage = new Page();
    				//AND THEN PARSE VALUES OF THIS CURRENTPAGE INTO PAGE OBJECT
    			} else if (currentPage != null)
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
    		case XmlPullParser.END_TAG:
    			name = parser.getName();
    			if (name.equalsIgnoreCase("page") && currentPage != null)
    				pages.add(currentPage);
    			
    			}
    			
    		}
    		eventType = parser.next();
    	}
    
}
