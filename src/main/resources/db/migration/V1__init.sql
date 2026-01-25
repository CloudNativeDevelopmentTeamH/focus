create table categories (
                            id uuid primary key,
                            owner_id integer not null,
                            name text not null,
                            color text null,
                            archived boolean not null default false,
                            created_at timestamptz not null default now(),
                            updated_at timestamptz not null default now()
);

create unique index ux_categories_owner_name on categories(owner_id, name);

create table focus_sessions (
                                id uuid primary key,
                                owner_id integer not null,
                                start_at timestamptz not null,
                                end_at timestamptz null,
                                category_id uuid null references categories(id) on delete set null,
                                note text null,
                                created_at timestamptz not null default now(),
                                updated_at timestamptz not null default now()
);

create index ix_sessions_owner_start on focus_sessions(owner_id, start_at desc);
create index ix_sessions_owner_end on focus_sessions(owner_id, end_at desc);

create unique index ux_sessions_owner_running
    on focus_sessions(owner_id)
    where end_at is null;
