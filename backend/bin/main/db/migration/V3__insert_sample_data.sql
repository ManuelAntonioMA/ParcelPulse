-- Insert sample products
INSERT INTO products (sku, name, price) VALUES 
('MAC-M3-PRO', 'MacBook Pro 14" M3', 1999.99),
('LOGI-MX3', 'Logitech MX Master 3S', 99.99),
('SONY-XM5', 'Sony WH-1000XM5 Headphones', 398.00);

-- Insert inventory for those products
INSERT INTO inventory (product_id, available_quantity, version) VALUES 
(1, 50, 0),
(2, 200, 0),
(3, 15, 0);