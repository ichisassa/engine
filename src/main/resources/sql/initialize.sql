-- ============================================
-- 1. ユーザー作成（既に存在していたら無視）
-- ============================================
CREATE USER mingshiu_user WITH PASSWORD 'mingshiu_pass';

-- ============================================
-- 2. データベース作成（既に存在していたら無視）
-- ============================================
CREATE DATABASE mingshiu
    WITH OWNER = mingshiu_user
         ENCODING 'UTF8'
         LC_COLLATE 'C'
         LC_CTYPE 'C'
         TEMPLATE template0;

-- ============================================
-- 3. 権限付与（常に成功）
-- ============================================
GRANT ALL PRIVILEGES ON DATABASE mingshiu TO mingshiu_user;

