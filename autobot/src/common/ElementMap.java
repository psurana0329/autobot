package common;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import common.Page;
import common.Uielement;
import common.Uimap;
import common.LoggerFactory;
import common.Logger;
import common.Constants;
public class ElementMap {
	private Page uiPage;
	public static Logger logger = LoggerFactory.getLogger();
	/**
	 * 
	 * @param pageName
	 */
	public ElementMap(String pageName) {
        try {
            JAXBContext jc = JAXBContext.newInstance("common");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Uimap uimap= (Uimap)unmarshaller.unmarshal(new File(Constants.elementMapFile));
            List<Page> pages = uimap.getPage();
			if(pages != null) {
				for( int i = 0; i < pages.size();i++ ) {
					Page page = pages.get(i);
					if (page.getName().equals(pageName)) {
						uiPage = page;
					}
				}
			}            
         }catch (Exception e ) {
            e.printStackTrace();
        }
	}
	/**
	 * 
	 * @param uiElementName
	 * @return
	 */
	public String getLocator(String uiElementName) {
		if(uiPage != null) {
			List<Uielement> uielements = uiPage.getUielement();
			if (uielements != null) {
				for( int j = 0; j < uielements.size(); j++ ) {
					Uielement uielement = uielements.get(j);
					if(uielement.getName().equals(uiElementName)) {
						logger.debug("Locator Key&Strategy -> "+uielement.getName()+" :: "+uielement.getLocator().getContent());
						return uielement.getLocator().getContent();
					}
				}
			}		
		}
		return null;
	}
}
