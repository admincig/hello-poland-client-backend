alter table promotion_campaign
  add column if not exists ticket_valid_to timestamp(6) null;

update promotion_campaign
set ticket_valid_to = date_trunc('year', valid_to)
    + interval '1 year' - interval '1 millisecond'
where promotion_type = 'TICKET'
  and ticket_valid_to is null;

do $$
begin
  if not exists (
    select 1 from pg_constraint
    where conname = 'promotion_campaign_ticket_valid_to_required_check'
  ) then
    alter table promotion_campaign
      add constraint promotion_campaign_ticket_valid_to_required_check
      check (promotion_type <> 'TICKET' or ticket_valid_to is not null);
  end if;
end $$;

do $$
begin
  if not exists (
    select 1 from pg_constraint
    where conname = 'promotion_campaign_ticket_valid_to_range_check'
  ) then
    alter table promotion_campaign
      add constraint promotion_campaign_ticket_valid_to_range_check
      check (ticket_valid_to is null or ticket_valid_to >= valid_to);
  end if;
end $$;
