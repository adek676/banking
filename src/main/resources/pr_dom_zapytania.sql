-- ZAPYTANIE 1
select count(account_id), account_id from account a inner join operation o on a.id = o.account_id
group by account_id having count(account_id) > 1;

-- ZAPYTANIE 2
select cl.id, sum(ac.balance) as bogactwo from client cl join account ac on ac.client_id = cl.id
group by cl.id order by bogactwo desc limit 5;

-- ZAPYTANIE 3
with

	secondBest as(
		select client, accountId, total, rank from(
			select client_id client, id accountId, balance total, rank() over (partition by client_id order by balance desc, id asc) rank
			from account
			) as ranked
		where rank = 2
	),

	rest as (
		select client_id, id, balance
		from account
		where client_id not in (
			select client
			from secondBest
		)
	)

select client, accountId, total from secondBest
union all
select client_id, id, balance  from rest;