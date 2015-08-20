package cashtracker.rest;

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

import cashtracker.dao.AccountDAO;
import cashtracker.model.Account;
import cashtracker.validation.AccountValidator;
import cashtracker.validation.ValidationError;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.flabser.restful.RestProvider;
import com.flabser.restful.data.IAppEntity;


@Path("accounts")
public class AccountService extends RestProvider {

	private AccountValidator validator = new AccountValidator();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		AccountDAO dao = new AccountDAO(getSession());
		return Response.ok(new Accounts(dao.findAll())).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
		return Response.ok(m).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Account m) {
		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		AccountDAO dao = new AccountDAO(getSession());
		return Response.ok(dao.add(m)).build();
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") long id, Account m) {
		m.setId(id);

		ValidationError ve = validator.validate(m);
		if (ve.hasError()) {
			return Response.status(Status.BAD_REQUEST).entity(ve).build();
		}

		AccountDAO dao = new AccountDAO(getSession());
		return Response.ok(dao.update(m)).build();
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("id") long id) {
		AccountDAO dao = new AccountDAO(getSession());
		Account m = dao.findById(id);
		if (m != null) {
			dao.delete(m);
		}
		return Response.ok().build();
	}

	@JsonRootName("accounts")
	class Accounts extends ArrayList <IAppEntity> {

		private static final long serialVersionUID = 1L;

		public Accounts(Collection <? extends IAppEntity> m) {
			addAll(m);
		}
	}
}
