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

-- ============================================
-- 
-- ============================================
CREATE TABLE tmp_upload_file (
    user_id           VARCHAR(50)  NOT NULL,                              -- ユーザーID 
    session_id        VARCHAR(50)  NOT NULL,                              -- セッションID
    unique_id         VARCHAR(50)  NOT NULL,                              -- UUID
    file_type         INTEGER      NOT NULL,                              -- ファイル種別
    file_extension    VARCHAR(50)  NOT NULL,                              -- 拡張子
    file_content_type VARCHAR(50)  NOT NULL,                              -- Content-Type
    file_data         TEXT         NULL,                                  -- File本体
    file_base64       TEXT         NULL,                                  -- File本体(Base64)
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,    -- 登録日時
    CONSTRAINT pk_temp_file PRIMARY KEY (user_id, session_id, unique_id, file_type)
);
