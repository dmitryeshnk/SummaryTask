package ua.nure.yeshenko.SummaryTask.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.nure.yeshenko.SummaryTask.bean.CartBean;
import ua.nure.yeshenko.SummaryTask.db.DBManager;
import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;
import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;
import ua.nure.yeshenko.SummaryTask.web.command.CommandContainer;

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
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DBManager.setDataSource((DataSource) envContext.lookup("jdbc/root"));
		} catch (NamingException e) {
			log.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE);
		}
		servletContext.setAttribute("UserDAO", new UserDAO());
		servletContext.setAttribute("ProductDAO", new ProductDAO());
		servletContext.setAttribute("OrderDAO", new OrderDAO());
		servletContext.setAttribute("CommandContainer", new CommandContainer(servletContext));
		log.debug("Servlet context initialization finished");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		ProductDAO manager;
		try {
			manager = (ProductDAO) se.getSession().getServletContext().getAttribute("ProductDAO");
			CartBean.get(se.getSession()).getCart().forEach((k, v) -> manager.updateProduct(k, v));
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

	private void initLog4J(ServletContext servletContext) {
		try {
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
