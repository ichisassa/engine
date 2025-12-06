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
CREATE TABLE temp_file (
    user_id      VARCHAR(50)  NOT NULL,      -- ユーザーID
    session_id   VARCHAR(50)  NOT NULL,      -- セッションID
    unique_id    VARCHAR(50)  NOT NULL,      -- ユニークID（UUID）
    image_type   SMALLINT     NOT NULL,      -- 画像種別（1:メイン、2:顔）
    file_name    VARCHAR(50)  NOT NULL,      -- 元ファイル名
    content_type VARCHAR(50)  NOT NULL,      -- MIMEタイプ
    file_base64  TEXT         NOT NULL,      -- Base64データ本体
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,    -- 登録日時
    CONSTRAINT pk_temp_file PRIMARY KEY (user_id, session_id, unique_id, image_type)
);
