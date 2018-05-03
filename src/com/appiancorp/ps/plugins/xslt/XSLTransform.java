package com.appiancorp.ps.plugins.xslt;

import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import com.appiancorp.suiteapi.common.exceptions.AppianException;
import com.appiancorp.suiteapi.common.exceptions.ErrorCode;
import com.appiancorp.suiteapi.content.ContentConstants;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Category;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

@Category("xsltCategory")
public class XSLTransform {

	private static final Logger log = Logger.getLogger(XSLTransform.class);
	
	@Function
	public static String xsltMap(ContentService cs, @Parameter @DocumentDataType Long inXslt, @Parameter String inXml) 
			throws AppianException {
		
		InputStream is;	
		StringWriter outWriter = new StringWriter();
		
		try {
			String filePath = cs.getInternalFilename(cs.getVersionId(inXslt, ContentConstants.VERSION_CURRENT));
			is = new FileInputStream(filePath);

			Source sourceXml = new StreamSource(new StringReader(inXml));
			Source xslt = new StreamSource(is);
			
			TransformerFactory factory = TransformerFactory.newInstance();
			Templates cachedXSLT = factory.newTemplates(xslt);
			Transformer transformer = cachedXSLT.newTransformer();					
			transformer.transform(sourceXml, new StreamResult(outWriter));
			
			is.close(); 
		} catch (IOException e) {
			log.error(e,e);
			throw new AppianException(ErrorCode.GENERIC_ERROR, e.getMessage());
		} catch (TransformerException e) {
			log.error(e,e);
			throw new AppianException(ErrorCode.GENERIC_ERROR, e.getMessage());
		} 		
		return outWriter.toString();
	}
	
}
