alter table userrole drop constraint if exists userrole_role_check;

alter table userrole add constraint userrole_role_check
  check (
    role::text = any (
      array[
        'ROOT',
        'ADMIN',
        'PARTNER',
        'PARTNER_ADMIN',
        'PARTNER_SALESMAN',
        'SALESMAN',
        'USHER',
        'USER',
        'partner'
      ]::text[]
    )
  );

create table if not exists partner_user_allowed_sights (
  user_id bigint not null,
  sight_id bigint not null,
  primary key (user_id, sight_id),
  constraint partner_user_allowed_sights_user_fk
    foreign key (user_id) references users(id),
  constraint partner_user_allowed_sights_sight_fk
    foreign key (sight_id) references sight(id)
);
