alter table userrole drop constraint if exists userrole_role_check;

alter table users add column if not exists blocked boolean not null default false;

alter table userrole add constraint userrole_role_check
  check (
    role::text = any (
      array[
        'ROOT',
        'ADMIN',
        'HELPDESK_PARTNER_MANAGER',
        'HELPDESK_CONTENT_MANAGER',
        'HELPDESK_SUPPORT',
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

create table if not exists helpdesk_user_allowed_partners (
  user_id bigint not null,
  partner_id bigint not null,
  primary key (user_id, partner_id),
  constraint helpdesk_user_allowed_partners_user_fk
    foreign key (user_id) references users(id),
  constraint helpdesk_user_allowed_partners_partner_fk
    foreign key (partner_id) references partner(id)
);

create table if not exists helpdesk_user_allowed_sights (
  user_id bigint not null,
  sight_id bigint not null,
  primary key (user_id, sight_id),
  constraint helpdesk_user_allowed_sights_user_fk
    foreign key (user_id) references users(id),
  constraint helpdesk_user_allowed_sights_sight_fk
    foreign key (sight_id) references sight(id)
);
