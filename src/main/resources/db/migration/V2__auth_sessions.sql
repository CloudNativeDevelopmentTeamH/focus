create table auth_sessions (
                               id uuid primary key,                 -- focus_sid (cookie value)
                               user_id integer not null,             -- numeric user id from auth
                               token text not null,                  -- auth token stored server-side
                               expires_at timestamptz not null,
                               created_at timestamptz not null default now(),
                               last_seen_at timestamptz not null default now(),
                               revoked_at timestamptz null
);

create index ix_auth_sessions_expires_at on auth_sessions(expires_at);
create index ix_auth_sessions_user_id on auth_sessions(user_id);
create index ix_auth_sessions_revoked_at on auth_sessions(revoked_at);
