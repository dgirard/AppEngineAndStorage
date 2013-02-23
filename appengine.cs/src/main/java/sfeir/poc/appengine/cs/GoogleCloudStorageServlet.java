package sfeir.poc.appengine.cs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.Channels;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

@SuppressWarnings("serial")
public class GoogleCloudStorageServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		// Get the file service
		FileService fileService = FileServiceFactory.getFileService();

		/**
		 * Set up properties of your new object
		 * After finalizing objects, they are accessible
		 * through Cloud Storage with the URL:
		 * http://storage.googleapis.com/my_bucket/my_object
		 */
		GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
		  .setBucket("premierbucket")
		  .setKey("my_object" + System.currentTimeMillis())
		  .setAcl("public-read")
		  .setMimeType("text/html");

		AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
		
		
		// Open a channel for writing
		boolean lockForWrite = true; // Do you want to exclusively lock this object?
		FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lockForWrite);
		
		
		// For this example, we write to the object using the PrintWriter
		PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
		out.println("The woods are lovely and deep.");
		out.println("But I have promises too keep.");

		// Close the object without finalizing.
		out.close();
		writeChannel.closeFinally();
		
		
		resp.getWriter().println("Hello, world");
	}
}
