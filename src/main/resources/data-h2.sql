-- =========================
-- RAW MATERIALS
-- =========================

INSERT INTO raw_materials (id, name, code, stock_quantity, unit)
VALUES (1, 'Iron', 'IRON01', 1050.00, 'kg');

INSERT INTO raw_materials (id, name, code, stock_quantity, unit)
VALUES (2, 'Steel', 'STL01', 1000.00, 'kg');

INSERT INTO raw_materials (id, name, code, stock_quantity, unit)
VALUES (3, 'Plastic', 'PST01', 500.00, 'kg');

INSERT INTO raw_materials (id, name, code, stock_quantity, unit)
VALUES (4, 'Rubber', 'RBR01', 2000.00, 'kg');


-- =========================
-- productsS
-- =========================

INSERT INTO products (id, code, name, price)
VALUES (1, 'CAR01', 'Car', 50000.00);

INSERT INTO products (id, code, name, price)
VALUES (2, 'BIKE01', 'Bike', 1000.00);

INSERT INTO products (id, code, name, price)
VALUES (3, 'TIRE01', 'Tire', 100.00);

INSERT INTO products (id, code, name, price)
VALUES (4, 'TRUCK01', 'Truck', 100000.00);


-- =========================
-- products MATERIAL RELATION
-- =========================

-- Car
INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (1, 1, 1, 100.0);

-- Bike
INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (2, 2, 1, 10.0);

INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (3, 2, 4, 2.0);

-- Tire
INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (4, 3, 2, 10.0);

INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (5, 3, 4, 2.0);

-- Truck
INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (6, 4, 1, 100.0);

INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (7, 4, 2, 200.0);

INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (8, 4, 3, 100.0);

INSERT INTO product_materials (id, products_id, raw_materials_id, quantity_required)
VALUES (9, 4, 4, 8.0);

ALTER TABLE raw_materials ALTER COLUMN id RESTART WITH 10;
ALTER TABLE products ALTER COLUMN id RESTART WITH 10;
ALTER TABLE product_materials ALTER COLUMN id RESTART WITH 20;