CREATE TABLE "users" (
  "id" serial PRIMARY KEY,
  "user_name" varchar(50) NOT NULL,
  "email" varchar(255) NOT NULL,
  "password" varchar(255) NOT NULL,
  "role" varchar(25) NOT NULL,
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "status" (
  "id" serial PRIMARY KEY,
  "name" varchar(25) NOT NULL UNIQUE
);

CREATE TABLE "projects" (
  "id" serial PRIMARY KEY,
  "creator_id" integer NOT NULL REFERENCES "users" ("id"),
  "project_name" varchar(50) NOT NULL,
  "description" varchar(255),
  "status_id" integer REFERENCES "status" ("id") DEFAULT 1,
  "deadline" timestamp without time zone NOT NULL,
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "project_members" (
  "id" serial PRIMARY KEY,
  "project_id" integer NOT NULL REFERENCES "projects" ("id"),
  "user_id" integer REFERENCES "users" ("id"),
  "invited_by" integer REFERENCES "users" ("id"),
  "project_role" varchar(25) NOT NULL,
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "tasks" (
  "id" serial PRIMARY KEY,
  "creator_id" integer REFERENCES "users" ("id"),
  "project_id" integer NOT NULL REFERENCES "projects" ("id"),
  "task_name" varchar(50) NOT NULL,
  "description" varchar(255),
  "status_id" integer REFERENCES "status" ("id") DEFAULT 1,
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "deadline" timestamp without time zone NOT NULL,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "task_users" (
  "id" serial PRIMARY KEY,
  "task_id" integer NOT NULL REFERENCES "tasks" ("id"),
  "user_id" integer REFERENCES "users" ("id"),
  "invited_by" integer REFERENCES "users" ("id"), -- User who invited the user to the task
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "tags" (
  "id" serial PRIMARY KEY,
  "project_id" integer NOT NULL REFERENCES "projects" ("id"),
  "name" varchar(25) NOT NULL,
  "user_id" integer NOT NULL REFERENCES "users" ("id"), -- who created the tag
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
 );

CREATE TABLE "task_tags" (
  "id" serial PRIMARY KEY,
  "task_id" integer NOT NULL REFERENCES "tasks" ("id"),
  "tag_id" integer NOT NULL REFERENCES "tags" ("id"),
  "user_id" integer NOT NULL REFERENCES "users" ("id"), -- who added the tag
  "created_at" timestamp without time zone,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "task_comments" (
  "id" serial PRIMARY KEY,
  "task_id" integer NOT NULL REFERENCES "tasks" ("id"),
  "user_id" integer NOT NULL REFERENCES "users" ("id"),
  "content" varchar(255) NOT NULL,
  "created_at" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);

CREATE TABLE "task_attachments" (
  "id" serial PRIMARY KEY,
  "task_id" integer NOT NULL REFERENCES "tasks" ("id"),
  "user_id" integer NOT NULL REFERENCES "users" ("id"),
  "content" bytea NOT NULL,
  "content_type" varchar(255),
  "orig_file_name" varchar(255) NOT NULL,
  "file_size" bigint NOT NULL,
  "created_at" timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
  "updated_at" timestamp without time zone,
  "is_deleted" boolean NOT NULL DEFAULT false
);
