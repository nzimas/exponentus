package cashtracker.dao

import java.sql.Timestamp

import cashtracker.model.Account
import cashtracker.model.Category
import cashtracker.model.CostCenter
import cashtracker.model.Transaction

import com.flabser.dataengine.IDatabase
import com.flabser.script._Session
import com.flabser.users.User


public class TransactionDAO {

	private final static String selectAll = """
		SELECT
		  t.id                       AS "id",
		  t."USER"                   AS "t.USER",
		  t.transaction_type         AS "t.transaction_type",
		  t.transaction_state        AS "t.transaction_state",
		  t.date                     AS "t.date",
		  t.account_from             AS "t.account_from",
		  t.account_to               AS "t.account_to",
		  t.amount                   AS "t.amount",
		  t.exchange_rate            AS "t.exchange_rate",
		  t.category                 AS "t.category",
		  t.cost_center              AS "t.cost_center",
		  t.tags                     AS "t.tags",
		  t.repeat                   AS "t.repeat",
		  t.every                    AS "t.every",
		  t.repeat_step              AS "t.repeat_step",
		  t.start_date               AS "t.start_date",
		  t.end_date                 AS "t.end_date",
		  t.basis                    AS "t.basis",
		  t.note                     AS "t.note",
		  t.include_in_reports       AS "t.include_in_reports",

		  af.name                    AS "af.name",
		  af.type                    AS "af.type",
		  af.currency_code           AS "af.currency_code",
		  af.opening_balance         AS "af.opening_balance",
		  af.amount_control          AS "af.amount_control",
		  af.writers                 AS "af.writers",
		  af.readers                 AS "af.readers",
		  af.include_in_totals       AS "af.include_in_totals",
		  af.note                    AS "af.note",

		  at.name                    AS "at.name",
		  at.type                    AS "at.type",
		  at.currency_code           AS "at.currency_code",
		  at.opening_balance         AS "at.opening_balance",
		  at.amount_control          AS "at.amount_control",
		  at.writers                 AS "at.writers",
		  at.readers                 AS "at.readers",
		  at.include_in_totals       AS "at.include_in_totals",
		  at.note                    AS "at.note",

		  c.transaction_type         AS "c.transaction_type",
		  c.parent_id                AS "c.parent_id",
		  c.name                     AS "c.name",
		  c.note                     AS "c.note",
		  c.color                    AS "c.color",

		  cc.name                    AS "cc.name"
		FROM transactions         AS t
		  left join accounts      AS af  on af.id = t.account_from
		  left join accounts      AS at  on at.id = t.account_to
		  left join categories    AS c   on c.id = t.category
		  left join costcenters   AS cc  on cc.id = t.cost_center
		"""

	private IDatabase db
	private User user

	public TransactionDAO(_Session session) {
		this.db = session.getDatabase()
		this.user = session.getAppUser()
	}

	public String getSelectQuery() {
		return selectAll;
	}

	public String getCreateQuery() {
		return """INSERT INTO transactions
						("USER", transaction_type, transaction_state,
						date, account_from, account_to,
						amount, exchange_rate, category, cost_center,
						tags, repeat, every, repeat_step, start_date, end_date,
						note, include_in_reports, basis)
					VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"""
	}

	public String getUpdateQuery() {
		return """UPDATE transactions
					SET
						"USER" = ?,
						transaction_type = ?,
						transaction_state = ?,
						date = ?,
						account_from = ?,
						account_to = ?,
						amount = ?,
						exchange_rate = ?,
						category = ?,
						cost_center = ?,
						tags = ?,
						repeat = ?,
						every = ?,
						repeat_step = ?,
						start_date = ?,
						end_date = ?,
						note = ?,
						include_in_reports = ?,
						basis = ?
					WHERE id = ?"""
	}

	public String getDeleteQuery() {
		return "DELETE FROM transactions WHERE id = ";
	}

	public List <Transaction> findAll() {
		List <Transaction> result = db.select(getSelectQuery(), Transaction.class, user)
		return result
	}

	public Transaction findById(long id) {
		String sql = "${getSelectQuery()} WHERE id = $id"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByAccount(Account m) {
		String sql ="${getSelectQuery()} WHERE t.account = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByCostCenter(CostCenter m) {
		String sql ="${getSelectQuery()} WHERE t.cost_center = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public List <Transaction> findAllByCategory(Category m) {
		String sql ="${getSelectQuery()} WHERE t.category = ${m.id}"
		List <Transaction> list = db.select(sql, Transaction.class, user)
		Transaction result = list.size() ? list[0] : null
		return result
	}

	public int add(Transaction m) {
		m.setUser(user.id)
		String sql = """INSERT INTO transactions
							("USER", transaction_type, transaction_state,
							date, account_from, account_to,
							amount, exchange_rate, category, cost_center,
							tags, repeat, every, repeat_step, start_date, end_date,
							note, include_in_reports, basis)
						VALUES (${m.user}, ${m.transactionType.code}, ${m.transactionState.code},
								'${new java.sql.Date(m.date.getTime())}', ${m.accountFrom?.id}, ${m.accountTo?.id},
								${m.amount}, ${m.exchangeRate}, ${m.category?.id}, ${m.costCenter?.id},
								'{${m.tagsId?.join(",")}}', ${m.repeat}, ${m.every}, ${m.repeatStep}, '${new Timestamp(m.startDate.getTime())}', '${new Timestamp(m.endDate.getTime())}',
								'${m.note}', ${m.includeInReports}, '${m.basis}')"""
		return db.insert(sql, user)
	}

	public void update(Transaction m) {
		String sql = """UPDATE transactions
						SET
							transaction_type = ${m.transactionType.code},
							transaction_state = ${m.transactionState.code},
							date = ${m.date},
							account_from = ${m.accountFrom?.id},
							account_to = ${m.accountTo?.id},
							amount = ${m.amount},
							exchange_rate = ${m.exchangeRate},
							category = ${m.category?.id},
							cost_center = ${m.costCenter?.id},
							tags = '{${m.tags?.join(",")}}',
							repeat = ${m.repeat},
							every = ${m.every},
							repeat_step = ${m.repeatStep},
							start_date = ${m.startDate},
							end_date = ${m.endDate},
							note = '${m.note}',
							include_in_reports = ${m.includeInReports},
							basis = '${m.basis}'
						WHERE id = ${m.id}"""
		db.update(sql, user)
	}

	public void delete(Transaction m) {
		String sql = getDeleteQuery() + m.id
		db.delete(sql, user)
	}
}
