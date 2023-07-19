CREATE TABLE IF NOT EXISTS products (
  id UUID PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(255),
  price_in_cents INTEGER NOT NULL,
  is_available BOOLEAN
);