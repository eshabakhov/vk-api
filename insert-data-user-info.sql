DO $$
DECLARE
  n int := 92690186;
BEGIN
  INSERT INTO user_info (user_id) SELECT generate_series(n, n + 10000);
END $$;