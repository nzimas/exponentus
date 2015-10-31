package task.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import task.dao.IssueDAO;
import task.model.Issue;
import task.validation.IssueValidator;
import task.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;


@Path("issues")
public class IssueService extends RestProvider {

	private IssueValidator validator = new IssueValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		IssueDAO dao = new IssueDAO(getSession());
		return Response.ok(new Issues(dao.findAll())).build();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		IssueDAO dao = new IssueDAO(getSession());
		Issue m = dao.findById(id);
		//
		if (m == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Issue m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Issue m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		IssueDAO dao = new IssueDAO(getSession());
		Issue pm = dao.findById(id);
		//
		if (pm == null) {
			return Response.noContent().status(Status.NOT_FOUND).build();
		}
		//
		pm.setNote(m.getNote());
		//
		return Response.ok(dao.update(pm)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		IssueDAO dao = new IssueDAO(getSession());
		Issue m = dao.findById(id);
		dao.delete(m);
		return Response.status(Status.NO_CONTENT).build();
	}

	@JsonRootName("issues")
	class Issues extends ArrayList <Issue> {

		private static final long serialVersionUID = 1L;

		public Issues(Collection <? extends Issue> m) {
			addAll(m);
		}
	}
}