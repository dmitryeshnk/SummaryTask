package ua.nure.yeshenko.SummaryTask.web.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.entity.Product;
import ua.nure.yeshenko.SummaryTask.exception.DBException;

/**
 * 
 * @author D.Yeshenko
 *
 */
public class ContextListener implements ServletContextListener, HttpSessionListener {
	
	private static final Logger log = Logger.getLogger(ContextListener.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		log.debug("Servlet context destruction starts");
		// do nothing
		log.debug("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent sce) {
		log.debug("Servlet context initialization starts");
		ServletContext servletContext = sce.getServletContext();
		initLog4J(servletContext);
		initCommandContainer();
		initI18N(servletContext);
		log.debug("Servlet context initialization finished");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		ProductDAO manager;
		try {
			manager = ProductDAO.getInstance();
			for (Product pr : CartBean.get(se.getSession()).getCart()) {
				manager.updateProduct(pr, false);
			}
		} catch (DBException e1) {
			e1.printStackTrace();
		}
		se.getSession().setAttribute("inside", 0);
		System.out.println("Destroy session");
	}

	private void initCommandContainer() {
		try {
			Class.forName("ua.nure.yeshenko.SummaryTask.web.command.CommandContainer");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot init command container");
		}
	}

	/**
	 * Initializes i18n subsystem.
	 */
	private void initI18N(ServletContext servletContext) {
		log.debug("I18N subsystem initialization started");
		
		String localesValue = servletContext.getInitParameter("locales");
		if (localesValue == null || localesValue.isEmpty()) {
			log.warn("'locales' init parameter is empty, the default encoding will be used");
		} else {
			List<String> locales = new ArrayList<String>();
			StringTokenizer st = new StringTokenizer(localesValue);
			while (st.hasMoreTokens()) {
				String localeName = st.nextToken();
				locales.add(localeName);
			}							
			
			log.debug("Application attribute set: locales --> " + locales);
			servletContext.setAttribute("locales", locales);
		}		
		
		log.debug("I18N subsystem initialization finished");
	}
	
	private void initLog4J(ServletContext servletContext) {
		try {
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
