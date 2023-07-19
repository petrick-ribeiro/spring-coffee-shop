ALTER TABLE products
ADD created_at TIMESTAMP,
ADD updated_at TIMESTAMP,
ADD deleted_at TIMESTAMP,
ADD is_deleted BOOLEAN NOT NULL;