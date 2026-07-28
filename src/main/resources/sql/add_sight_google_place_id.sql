alter table sight add column if not exists google_place_id text;
alter table sight alter column google_place_id type text;
