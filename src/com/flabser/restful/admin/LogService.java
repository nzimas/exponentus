package com.flabser.restful.admin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.servlets.sitefiles.AttachmentHandlerException;

@Path("/logs")
public class LogService extends RestProvider {

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LogsList get() {
		ArrayList<LogFile> fileList = new ArrayList<LogFile>();
		File logDir = new File("." + File.separator + "logs" + File.separator + "server");
		if (logDir.isDirectory()) {
			File[] list = logDir.listFiles();
			Arrays.sort(list, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
			for (int i = list.length; --i >= 0;) {
				fileList.add(new LogFile(list[i]));
			}
		}
		return new LogsList(fileList);
	}

	@GET
	@Path("/{logfile}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response get(@PathParam("logfile") String logFile) throws AttachmentHandlerException {
		File file = null;

		File logDir = new File("." + File.separator + "logs" + File.separator + "server");
		String fileName = logDir + File.separator + logFile;

		if (!fileName.equals("")) {
			file = new File(fileName);
		}

		if (file != null && file.exists()) {
			return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
					.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"").build();
		} else {
			return Response.status(HttpServletResponse.SC_NO_CONTENT).build();
		}

	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") int id) {
		File logDir = new File("." + File.separator + "logs");
		if (logDir.isDirectory()) {
			String filePath = logDir + File.separator + id;
			File file = new File(filePath);
			file.delete();
		}
		return Response.ok().build();
	}

	@JsonRootName("logs")
	class LogsList extends ArrayList<LogFile> {
		private static final long serialVersionUID = -7008854834343193674L;

		public LogsList(Collection<? extends LogFile> m) {
			addAll(m);
		}
	}

	class LogFile {
		String name;
		long length;
		String lastModified;

		public LogFile(File file) {
			this.name = file.getName();
			length = file.length();
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			lastModified = sdf.format(file.lastModified());
		}

		public String getName() {
			return name;
		}

		public long getLength() {
			return length / 1024 / 1024;
		}

		public String getLastModified() {
			return lastModified;
		}
	}
}
