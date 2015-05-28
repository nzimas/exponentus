package com.flabser.scriptprocessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.flabser.env.Environment;
import com.flabser.localization.Vocabulary;
import com.flabser.runtimeobj.page.Element;
import com.flabser.script._Exception;
import com.flabser.script._Helper;
import com.flabser.script._IXMLContent;
import com.flabser.script._Session;
import com.flabser.script._URL;
import com.flabser.script.concurrency._AJAXHandler;
import com.flabser.script.constants._JSONTemplate;

public class ScriptEvent {
	protected Vocabulary vocabulary;
	protected String redirectURL = "";
	protected ArrayList<Element> toPublishElement = new ArrayList<Element>();
	protected _Session ses;
	
	public String getTmpDirPath(){
		return Environment.tmpDir;	
	}

	public String getWord(String word, Vocabulary vocabulary, String lang){
		try{
			return vocabulary.getSentenceCaption(word, lang).word;
		}catch(Exception e){
			return word.toString();
		}
	}

	public void publishElement(String entryName, String value){
		toPublishElement.add(new  Element(entryName, value));
	}
	
	public void publishElement(String entryName, Object value) throws _Exception {		
		if (value == null) {
			toPublishElement.add(new Element(entryName, ""));
		}else if (value instanceof String) {
			toPublishElement.add(new Element(entryName, (String)value));		
		}else if (value instanceof Date) {
			toPublishElement.add(new Element(entryName, _Helper.getDateAsString((Date)value)));
		}else if (value instanceof Enum) {
			toPublishElement.add(new Element(entryName, ((Enum)value).name()));
		}else if (value instanceof BigDecimal) {
			toPublishElement.add(new Element(entryName, value.toString()));
		}
	}
	
		
	public void publishElement(String spot, String launcher, _AJAXHandler value, boolean async, _JSONTemplate template){
/*		_JSONHandler jsHandler = new _JSONHandler(spot, launcher, value, template);
		UserSession userSession = ses.getUser().getSession();
		userSession.addDynmaicClass(jsHandler.id, jsHandler.getInstance());		
		toPublishElement.add(jsHandler);*/
	}
	
	public void publishElement(String id, String spot, String launcher, _AJAXHandler value, boolean async, _JSONTemplate template){
/*		_JSONHandler jsHandler = new _JSONHandler(id, spot, launcher, value,"", template);
		UserSession userSession = ses.getUser().getSession();
		userSession.addDynmaicClass(jsHandler.id, jsHandler.getInstance());		
		toPublishElement.add(jsHandler);*/
	}
	
	
	
	public  String getGroovyError(StackTraceElement stack[]){		
		for (int i=0; i<stack.length; i++){
			if (stack[i].getClassName().contains(this.getClass().getName())){
				return stack[i].getClassName() + " method=" + stack[i].getMethodName() + " > "+Integer.toString(stack[i].getLineNumber()) + "\n";	
			}
		}
		return "";
	}

	public String getLocalizedWord(String word, String lang){
		return getWord(word, vocabulary, lang);
	}

	public void setRedirectURL(_URL url){
		redirectURL = url.toString();
	}

	public static void log(Object logText){
		ScriptProcessor.logger.normalLogEntry(logText.toString());
	}
	
}
