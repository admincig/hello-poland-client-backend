-- Repair redundant SightEvent.partner_id values so they match the owning Sight.partner_id.
-- Run before deploying backend code that treats Sight.partner as the owner of SightEvent.

update sightevent se
set partner_id = s.partner_id
from sight s
where s.id = se.sight_id
  and se.partner_id is distinct from s.partner_id;

-- Optional verification query. It should return no rows after the update above.
select se.id as sightevent_id,
       se.partner_id as sightevent_partner_id,
       s.id as sight_id,
       s.partner_id as sight_partner_id
from sightevent se
join sight s on s.id = se.sight_id
where se.partner_id is distinct from s.partner_id;
