package ua.nure.yeshenko.SummaryTask.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import ua.nure.yeshenko.SummaryTask.exception.DBException;
import ua.nure.yeshenko.SummaryTask.exception.Messages;

/**
 * This class is created to convert images from a database and jsp page to
 * Base64
 * 
 * @author D.Yeshenko
 *
 */
public class FileConverter {
	/**
	 * 
	 * @param blob from database image
	 * @return String Base64 to display on the page
	 * @throws SQLException
	 * @throws  
	 */
	public static String convert(Blob blob) throws SQLException {
		InputStream inputStream = blob.getBinaryStream();
		String result = null;
		try {
			 result = convert(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String convert(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[(int) inputStream.available()];
		int bytesRead = -1;

		try {
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_PRODUCT, e);
		}

		byte[] imageBytes = outputStream.toByteArray();
		String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		return base64Image;
	}
	
	/**
	 * 
	 * @param base64Image base64 encoded image
	 * @return InputStream to work with the database
	 * @throws IOException
	 */
	public static InputStream convert(String base64Image) {
		byte[] image = Base64.getDecoder().decode(base64Image);
		InputStream inputStream = new ByteArrayInputStream(image);
		return inputStream;
	}
}
