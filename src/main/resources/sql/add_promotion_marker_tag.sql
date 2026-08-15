alter table promotion_campaign
  add column if not exists marker_tag_id bigint;

do $$
begin
  if not exists (
    select 1 from pg_constraint
    where conname = 'promotion_campaign_marker_tag_fk'
  ) then
    alter table promotion_campaign
      add constraint promotion_campaign_marker_tag_fk
      foreign key (marker_tag_id) references tag(id);
  end if;
end $$;

create index if not exists promotion_campaign_marker_tag_idx
  on promotion_campaign(marker_tag_id);
