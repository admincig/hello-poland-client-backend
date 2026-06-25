alter table category add column if not exists displayorder integer;

update category c
set displayorder = ordered_categories.position
from (
  select
    id,
    row_number() over (
      order by
        case when displayorder is null then 1 else 0 end,
        displayorder asc,
        id desc
    ) as position
  from category
) ordered_categories
where c.id = ordered_categories.id;
