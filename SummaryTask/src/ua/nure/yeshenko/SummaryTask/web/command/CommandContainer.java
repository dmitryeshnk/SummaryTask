package ua.nure.yeshenko.SummaryTask.web.command;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {
	private static Map<String, Command> commands = new TreeMap<String, Command>();
	
	static {
		commands.put("register", new RegisterCommand());
		commands.put("login", new LoginCommand());
		commands.put("unknownCommand", new UnknownCommand());
		commands.put("catalog", new CatalogCommand());
		commands.put("cart", new CartCommand());
		commands.put("orderCart", new OrderToCartCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("listClients", new ListClientsCommand());
		commands.put("deleteFromCart", new DeleteFromCartCommand());
		commands.put("checkout", new CheckoutCommand());
		commands.put("confirm", new ConfirmCommand());
		commands.put("allOrders", new ListOrderCommand());
		commands.put("orderStatus", new SetOrderStatusCommand());
		commands.put("insertProduct", new InsertProductCommand());
		commands.put("updateProduct", new UpdateCommand());
		commands.put("choice", new ChoiceCommand());
		commands.put("changeRole", new ChangeRoleUserCommand());
		commands.put("changeLocale", new ChangeLocaleCommand());
		commands.put("search", new SearchCommand());
	}
	
	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			return commands.get("unknownCommand"); 
		}
		
		return commands.get(commandName);
	}
}
