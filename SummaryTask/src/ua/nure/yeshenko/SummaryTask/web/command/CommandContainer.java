package ua.nure.yeshenko.SummaryTask.web.command;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import ua.nure.yeshenko.SummaryTask.db.OrderDAO;
import ua.nure.yeshenko.SummaryTask.db.ProductDAO;
import ua.nure.yeshenko.SummaryTask.db.UserDAO;

public class CommandContainer {
	private Map<String, Command> commands = new TreeMap<String, Command>();
	
	public CommandContainer(ServletContext context) {
		init(context);
	}
	
	private void init(ServletContext context) {
		ProductDAO productDAO = (ProductDAO) context.getAttribute("ProductDAO");
		UserDAO userDAO = (UserDAO) context.getAttribute("UserDAO");
		OrderDAO orderDAO = (OrderDAO) context.getAttribute("OrderDAO");
		commands.put("register", new RegisterCommand(userDAO));
		commands.put("login", new LoginCommand(userDAO));
		commands.put("noCommand", new NoCommand());
		commands.put("catalog", new CatalogCommand(productDAO));
		commands.put("cart", new GetCartCommand());
		commands.put("orderCart", new OrderToCartCommand(productDAO));
		commands.put("logout", new LogoutCommand());
		commands.put("listClients", new ListClientsCommand(userDAO));
		commands.put("deleteFromCart", new DeleteFromCartCommand(productDAO));
		commands.put("checkout", new CheckoutCommand());
		commands.put("confirm", new ConfirmCommand(orderDAO, userDAO));
		commands.put("allOrders", new ListOrderCommand(orderDAO, productDAO));
		commands.put("orderStatus", new SetOrderStatusCommand(orderDAO));
		commands.put("insertProduct", new InsertProductCommand(productDAO));
		commands.put("updateProduct", new UpdateCommand(productDAO));
		commands.put("choice", new ChoiceCommand());
		commands.put("changeRole", new ChangeRoleUserCommand(userDAO));
		commands.put("changeLocale", new ChangeLocaleCommand());
		commands.put("search", new SearchCommand(productDAO));
		commands.put("changeQuantity", new ChangeQuantityCommand(productDAO));
		commands.put("sorter", new SortCommand(productDAO));
		commands.put("deleteProduct", new DeleteProductCommand(productDAO));
		commands.put("getImage", new GetImageCommand(productDAO));
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
}
